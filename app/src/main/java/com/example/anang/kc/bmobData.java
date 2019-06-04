package com.example.anang.kc;


import android.content.ContentResolver;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.net.ParseException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.widget.Toast;


import java.io.File;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by Administrator on 2017/4/16.
 */

public class bmobData {
    private dataOS data;
    private String[] data3, data2;
    private String rootdata, code, filename,editer;
    private String picadd = "";
    private Context ContextM;
    private SQLiteDatabase db;
    private Handler handler;
    private data_home gameScore1;
    private BmobFile bmobFile;
    private int inputID=13;
    private data_input gameinput;



    public bmobData(Context context, Handler handler1) {
        ContextM = context;
        handler = handler1;
        gData app = (gData) ContextM.getApplicationContext();
        Bmob.initialize(context, "2b826ee024c" + ContextM.getString(R.string.datak1u) + app.getFefd());
        editer=app.getName();
    }

    public bmobData(Context context) {
        ContextM = context;
        gData app = (gData) ContextM.getApplicationContext();
        Bmob.initialize(context, "2b826ee024c" + ContextM.getString(R.string.datak1u) + app.getFefd());
        editer=app.getName();
    }

    public void outpute(String text, BmobException e) {
        String showtext = e.getMessage() + "," + e.getErrorCode();
        if (e.getErrorCode() == 9016) showtext = ContextM.getString(R.string.i15);
        System.out.println(text+"    "+ showtext);
        Toast.makeText(ContextM, text + "\n" + showtext, Toast.LENGTH_LONG).show();
    }

    public void setPicadd(String add) {
        this.picadd = add;
    }

    public void UpData(dataOS data1) {
        data = data1;
        BmobQuery<data_home> query = new BmobQuery<data_home>();
        query.addWhereEqualTo("PID", data.PID);
        query.addWhereEqualTo("line", data.line);
        query.addWhereEqualTo("list", data.list);
        query.findObjects(new FindListener<data_home>() {
            @Override
            public void done(List<data_home> object, BmobException e) {
                if (e == null) {
                    String objectId = "";
                    for (data_home gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            System.out.println("得到的ID  " + objectId);

                            String pp = gameScore.getPic();
                            //System.out.println("44  "+pp);
                            if (!pp.equals("") && picadd.length() > 0) {
                                BmobFile file = new BmobFile();
                                file.setUrl(pp);
                                file.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            //System.out.println(5555);
                                        } else {
                                            //System.out.println(e.getErrorCode()+","+e.toString());
                                        }
                                    }
                                });
                            }
                            if (picadd.length() > 10) {
                                gameScore.setPic(picadd);
                            } else if (data.pic.equals("")) {
                                gameScore.setPic("");
                            }
                            gameScore.setData(data);
                            gameScore.setName(editer);
                            gameScore1 = gameScore;
                            gameScore.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //Log.i("bmob","更新成功");
                                    } else {
                                        outpute("第一次,服务器更新home数据失败：", e);
                                        gameScore1.setData(data);
                                        gameScore1.setName(editer);
                                        if (!picadd.equals("")) gameScore1.setPic(picadd);
                                        gameScore1.update(gameScore1.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    //Log.i("bmob","更新成功");
                                                } else {
                                                    outpute("第二次,服务器更新home数据失败：", e);
                                                }
                                            }
                                        });
                                        System.out.println(data.settoString() + "  " + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }

                            });
                        }
                        break;
                    }

                    if (objectId == "") {
                        System.out.println("创建  " + objectId);
                        data_home datahome = new data_home();
                        if (picadd.length() > 10) {
                            datahome.setPic(picadd);
                        } else if (data.pic.equals("")) {
                            datahome.setPic("");
                        }
                        datahome.setData(data);
                        datahome.setName(editer);
                        gameScore1 = datahome;
                        datahome.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //System.out.println("添加数据成功，返回objectId为："+objectId);
                                } else {
                                    //System.out.println("创建数据失败：" + e.getMessage());
                                    //Toast.makeText(ContextM, "第一次,服务器创建home数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                    outpute("第一次,服务器创建home数据失败：", e);
                                    gameScore1.setData(data);
                                    gameScore1.setName(editer);
                                    gameScore1.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId, BmobException e) {
                                            if (e == null) {
                                            } else {
                                                outpute("第二次,服务器创建home数据失败：", e);
                                            }
                                        }
                                    });
                                    System.out.println(data.settoString() + "  " + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    outpute("服务器查询home失败：", e);
                }
            }
        });
    }

    public void UpDataInput(String[] data1) {
        data2 = data1;
        BmobQuery<data_input> query = new BmobQuery<data_input>();
        query.addWhereEqualTo("id", data2[inputID]);
        query.findObjects(new FindListener<data_input>() {
            @Override
            public void done(List<data_input> object, BmobException e) {
                if (e == null) {
                    String objectId = "";
                    for (data_input gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            System.out.println("得到的ID  " + objectId);

                            gameScore.setData(data2);
                            gameinput = gameScore;
                            gameScore.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //Log.i("bmob","更新成功");
                                        Message msg = new Message();
                                        msg.what = 201;
                                        handler.sendMessage(msg);
                                    } else {
                                        outpute("第一次,服务器更新home数据失败：", e);
                                        gameinput.setData(data2);

                                        gameScore1.update(gameinput.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    //Log.i("bmob","更新成功");
                                                    Message msg = new Message();
                                                    msg.what = 201;
                                                    handler.sendMessage(msg);
                                                } else {
                                                    outpute("第二次,服务器更新home数据失败：", e);
                                                    Message msg = new Message();
                                                    msg.what = 202;
                                                    handler.sendMessage(msg);
                                                }
                                            }
                                        });
                                        System.out.println(data2[inputID] + "  " + e.getMessage() + "," + e.getErrorCode());
                                    }
                                }

                            });
                        }
                        break;
                    }

                    if (objectId == "") {
                        System.out.println("创建  " + objectId);
                        data_input datahome=new data_input();

                        datahome.setData(data2);
                        gameinput = datahome;
                        datahome.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    Message msg = new Message();
                                    msg.what = 201;
                                    handler.sendMessage(msg);
                                    //System.out.println("添加数据成功，返回objectId为："+objectId);
                                } else {
                                    //System.out.println("创建数据失败：" + e.getMessage());
                                    //Toast.makeText(ContextM, "第一次,服务器创建home数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                    outpute("第一次,服务器创建home数据失败：", e);
                                    gameinput.setData(data2);
                                    gameinput.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String objectId, BmobException e) {
                                            if (e == null) {
                                                Message msg = new Message();
                                                msg.what = 201;
                                                handler.sendMessage(msg);
                                            } else {
                                                outpute("第二次,服务器创建home数据失败：", e);
                                                Message msg = new Message();
                                                msg.what = 202;
                                                handler.sendMessage(msg);
                                            }
                                        }
                                    });
                                    System.out.println(data2[inputID] + "  " + e.getMessage() + "," + e.getErrorCode());
                                }
                            }
                        });
                    }
                } else {
                    outpute("服务器查询home失败：", e);
                    Message msg = new Message();
                    msg.what = 202;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void UpDataCo(String[] data1) {
        data2 = data1;
        BmobQuery<data_co> query = new BmobQuery<data_co>();
        query.addWhereEqualTo("PID", data1[0]);
        query.findObjects(new FindListener<data_co>() {
            @Override
            public void done(List<data_co> object, BmobException e) {
                if (e == null) {
                    String objectId = "";
                    for (data_co gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            //System.out.println("得到的ID  "+objectId);
                            gameScore.setData(data2);
                            gameScore.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //Log.i("bmob","更新成功");
                                    } else {
                                        //Toast.makeText(ContextM, "服务器更新co数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                        outpute("服务器更新co数据失败：", e);
                                    }
                                }

                            });
                        }
                        break;
                    }
                    if (objectId == "") {
                        data_co data = new data_co();
                        data.setData(data2);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //System.out.println("添加数据成功，返回objectId为："+objectId);
                                } else {
                                    //System.out.println("创建数co据失败：" + e.getMessage());
                                    //Toast.makeText(ContextM, "服务器创建数co据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                    outpute("服务器创建数co据失败：", e);
                                }
                            }
                        });
                    }
                } else {
                    //System.out.println("bmob失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器查询co数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器查询co数据失败：", e);
                }
            }
        });
    }

    public void UpDataP(String[] data1) {
        data3 = data1;
        //System.out.println(data3[0]+"  "+data3[1]+" 11111 "+data3[2]);
        BmobQuery<data_p> query = new BmobQuery<data_p>();
        query.addWhereEqualTo("PID", data1[0]);
        query.findObjects(new FindListener<data_p>() {
            @Override
            public void done(List<data_p> object, BmobException e) {
                if (e == null) {
                    String objectId = "";
                    for (data_p gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            //System.out.println("得到的ID  "+objectId);
                            gameScore.setData(data3);
                            gameScore.update(objectId, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        //Log.i("bmob","更新成功");
                                    } else {
                                        //Toast.makeText(ContextM, "服务器更新p数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                        outpute("服务器更新p数据失败：", e);
                                    }
                                }

                            });
                        }
                        break;
                    }
                    if (objectId == "") {
                        data_p data = new data_p();
                        data.setData(data3);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                if (e == null) {
                                    //System.out.println("添加数据成p功，返回objectId为："+objectId);
                                } else {
                                    //System.out.println("创建数co据失败：" + e.getMessage());
                                    //Toast.makeText(ContextM, "服务器创建p数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                                    outpute("服务器创建p数据失败：", e);
                                }
                            }
                        });
                    }
                } else {
                    //System.out.println("bmob失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器查询p数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器查询p数据失败：", e);
                }
            }
        });
    }


    public void DownData(SQLiteDatabase db1) {
        //MyDatabaseHelper dbHelper = new MyDatabaseHelper(ContextM, "data.db", null, 9);
        db = db1;
        String end = "";
        Cursor cursor1 = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?", new String[]{"1", "100", "100"});

        if (cursor1.moveToFirst()) {
            end = cursor1.getString(cursor1.getColumnIndex("andnum"));
        } else
            db.execSQL("insert into house(PID,line,list,andnum) values(?,?,?,?)", new String[]{"1", "100", "100", ""});
        cursor1.close();
        if (end.equals("")) end = "2017-04-17 22:20:17";
        //System.out.println("111111   "+end);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        try {
            date1 = sdf1.parse(end);
            long Time = (date1.getTime() / 1000) - 1;
            date1.setTime(Time * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //System.out.println("时间： " + str);
        //更新home
        BmobQuery<data_home> query = new BmobQuery<data_home>();
        query.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(date1));
        query.setLimit(1000);
        query.order("updatedAt");
        //执行查询方法
        query.findObjects(new FindListener<data_home>() {
            @Override
            public void done(List<data_home> object, BmobException e) {
                //MyDatabaseHelper dbHelper = new MyDatabaseHelper(ContextM, "data.db", null, 9);

                //db=dbHelper.getWritableDatabase();
                if (e == null) {
                    //System.out.println("查询成功：共" + object.size() + "条数据。");
                    int n = 0;
                    if (object.size() == 0) {
                        //Toast.makeText(ContextM, "数据已至最新版!", Toast.LENGTH_SHORT).show();
                    }
                    for (data_home gameScore : object) {
                        dataOS dataos = gameScore.getData();
                        dataos.savedata(db);

                        n++;
                        //System.out.println("查询 " + n + "条数据。"+object.size()+","+gameScore.getUpdatedAt());
                        if (n == object.size()) {
                            db.execSQL("update house set andnum =? WHERE PID = ? and line = ? and list = ?", new String[]{gameScore.getUpdatedAt(), "1", "100", "100"});
                            if (object.size() < 1000) {

                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = gameScore.getUpdatedAt();
                                handler.sendMessage(msg);
                                //Toast.makeText(ContextM, "服务器刷新成功：" + gameScore.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                            } else
                                outpute("服务器未全部刷新,请再次刷新!", e);
                        }
                        //获得数据的objectId信息
                    }
                } else {
                    //Toast.makeText(ContextM, "服务器下载home据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    System.out.println("服务器下载home据失败：" + e.getMessage() + "," + e.getErrorCode());
                    outpute("服务器下载home据失败：", e);
                }
            }
        });
        //更新co
        BmobQuery<data_co> query1 = new BmobQuery<data_co>();
        query1.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(date1));

        query1.setLimit(1000);
        //执行查询方法

        query1.findObjects(new FindListener<data_co>() {
            @Override
            public void done(List<data_co> object, BmobException e) {
                if (e == null) {
                    //System.out.println("查询成功：共"+object.size()+"条数据。");
                    for (data_co gameScore : object) {
                        String a[] = gameScore.getData();
                        Cursor cursor = db.rawQuery("select * from company WHERE PID=?", new String[]{a[0]});
                        if (cursor.getCount() == 0) {
                            db.execSQL("insert into company(PID,co,ware,line,list) values(?,?,?,?,?)", a);
                        } else
                            db.execSQL("update company set co =?,ware =?,line =?,list =? WHERE PID = ?", new String[]{a[1], a[2], a[3], a[4], a[0]});
                        //System.out.println(a[0]+"  "+a[1]+"    "+a[2]);
                        //获得数据的objectId信息
                        cursor.close();
                    }

                } else {
                    //System.out.println("bmob"+" 失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器下载co数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器下载co数据失败：", e);
                }
            }
        });
        //更新p
        BmobQuery<data_p> query2 = new BmobQuery<data_p>();
        query2.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(date1));

        query2.setLimit(1000);
        //执行查询方法

        query2.findObjects(new FindListener<data_p>() {
            @Override
            public void done(List<data_p> object, BmobException e) {
                if (e == null) {
                    //System.out.println("查询成功：共"+object.size()+"条数据。");
                    for (data_p gameScore : object) {
                        String a[] = gameScore.getData();
                        if (a[2].length() > 2) {
                            Cursor cursor1 = db.rawQuery("select * from people WHERE PID = ?", new String[]{a[0]});
                            if (cursor1.getCount() == 0) {
                                db.execSQL("insert into people(PID,co,p) values(?,?,?)", a);
                            } else
                                db.execSQL("update people set co =?,p =? WHERE PID = ?", new String[]{a[1], a[2], a[0]});
                            //System.out.println(a[0]+"  "+a[1]+"    "+a[2]);
                            //获得数据的objectId信息
                            cursor1.close();
                        }
                    }
                } else {
                    //System.out.println("bmob"+" 失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器下载co数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器下载co数据失败：", e);
                }
            }
        });


        end = "";
        cursor1 = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?", new String[]{"1", "100", "99"});

        if (cursor1.moveToFirst()) {
            end = cursor1.getString(cursor1.getColumnIndex("andnum"));
        } else
            db.execSQL("insert into house(PID,line,list,andnum) values(?,?,?,?)", new String[]{"1", "100", "99", ""});
        cursor1.close();
        if (end.equals("")) end = "2017-07-17 22:20:17";
        //System.out.println("111111   "+end);
        Date date2 = null;
        try {
            date2 = sdf1.parse(end);
            long Time = (date2.getTime() / 1000) - 1;
            date2.setTime(Time * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        //更新input
        BmobQuery<data_input> query3 = new BmobQuery<data_input>();
        query3.addWhereGreaterThanOrEqualTo("updatedAt", new BmobDate(date2));

        query3.setLimit(1000);
        //执行查询方法
        //System.out.println("更新input");
        query3.findObjects(new FindListener<data_input>() {
            @Override
            public void done(List<data_input> object, BmobException e) {
                if (e == null) {
                    int n=0;
                    //System.out.println("查询成功：共"+object.size()+"条数据。");
                    for (data_input gameScore : object) {
                        String a[] = gameScore.getData();
                        //if 票号>0
                        n++;
                        if (a[inputID].length() > 0) {
                            Cursor cursor1 = db.rawQuery("select * from input WHERE andnum = ? and date = ?", new String[]{a[inputID-1],a[inputID]});
                            if (cursor1.getCount() == 0) {
                                db.execSQL("insert into input(PID,ware,type,Country,color,num,co,people,Price,buy,address,buyp,andnum,date) " +
                                        "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",a);
                            } else
                                db.execSQL("update input set PID= ?,ware= ?,type= ?,Country= ?,color= ?," +
                                        "num= ?,co= ?,people= ?,Price= ?,buy= ?,address=?,buyp= ? WHERE andnum = ? and date = ?", a);
                            //System.out.println(a[0]+"  "+a[1]+"    "+a[2]);
                            //获得数据的objectId信息
                            cursor1.close();
                        }

                        if (n == object.size()) {
                            System.out.println("bmob时间更新 "+gameScore.getUpdatedAt());
                            db.execSQL("update house set andnum =? WHERE PID = ? and line = ? and list = ?", new String[]{gameScore.getUpdatedAt(), "1", "100", "99"});
                            if (object.size() < 1000) {

                                //Toast.makeText(ContextM, "服务器刷新成功：" + gameScore.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                            } else
                                outpute("服务器未全部刷新,请再次刷新!", e);
                        }
                    }
                } else {
                    //System.out.println("bmob"+" 失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器下载co数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器下载input数据失败：", e);
                }
            }
        });
        /*
        if (endTime.equals("")){
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            endTime=sdf1.format(curDate);
        }*/
    }

    /**
     * 取全局标志-----------------------
     */
    public void getGState() {
        BmobQuery<user> query = new BmobQuery<user>();
        query.getObject("1klLHHHu", new QueryListener<user>() {
            @Override
            public void done(user object, BmobException e) {
                if (e == null) {
                    Message msg = new Message();
                    String[] ls = object.getData();
                    gData app = (gData) ContextM.getApplicationContext();
                    if (ls[4] != null) {
                        //客服微信号
                        app.setWeiXin(ls[0]);
                        app.setGstate(Integer.parseInt(ls[4]));
                    }
                    app.setAdv(ls[3]);
                    msg.what = 1001;
                    handler.sendMessage(msg);
                } else {
                    BmobQuery<user> query = new BmobQuery<user>();
                    query.getObject("1klLHHHu", new QueryListener<user>() {
                        @Override
                        public void done(user object, BmobException e) {
                            if (e == null) {
                                Message msg = new Message();
                                String[] ls = object.getData();
                                gData app = (gData) ContextM.getApplicationContext();
                                if (ls[4] != null) {
                                    app.setWeiXin(ls[0]);
                                    app.setGstate(Integer.parseInt(ls[4]));
                                }
                                msg.what = 1001;
                                handler.sendMessage(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 更新APP-----------------------
     */
    public void UpApp() {
        BmobQuery<AppVersion> query = new BmobQuery<AppVersion>();
        query.getObject("Atkd111u", new QueryListener<AppVersion>() {
            public void done(AppVersion object, BmobException e) {
                if (e == null) {
                    Message msg = new Message();
                    msg.obj = object.getAll();
                    msg.what = 1002;
                    filename = object.getLs();
                    handler.sendMessage(msg);
                } else {

                }
            }
        });
    }

    /**
     * 登录-----------------------
     */
    public void getRootState(String callnum, String mm, String code1) {
        rootdata = mm;
        code = code1;
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("callnum", callnum);
        //System.out.println("得到的ID1  "+callnum);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> object, BmobException e) {
                Message msg = new Message();
                msg.what = 0;
                if (e == null) {
                    String objectId = "";

                    for (user gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            String[] ls = gameScore.getData();
                            //System.out.println("得到的ID11  "+ls[7]);
                            gData app = (gData) ContextM.getApplicationContext();
                            if (ls[1].equals(rootdata)) {
                                //密码正确

                                if (!code.equals(ls[6])) {
                                    //System.out.println("得到的22ID11  "+ls[6]);
                                    if (ls[8] != null ) {
                                        if (!ls[8].equals("1")){
                                        msg.what = 110;
                                        handler.sendMessage(msg);
                                        return;
                                        }
                                    } else if (!ls[7].equals("0")) {
                                        msg.what = 109;
                                        handler.sendMessage(msg);
                                        return;
                                    }
                                }
                                ls[6] = code;
                                ls[7] = "1";
                                app.setRootData(ls);
                                app.setObjectId(objectId);
                                SharedPre sp = new SharedPre(ContextM, ls);
                                sp.saveData();
                                //System.out.println("密码正确  ");
                                //登录后设置state为1
                                msg.what = 101;
                                gameScore.setData(ls);
                                gameScore.update(objectId, new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null) {
                                            //System.out.println("登录更新111");
                                        } else {
                                            //outpute("登录更新!：",e);
                                        }
                                    }

                                });

                            } else {
                                app.setRootData();
                                msg.what = 102;
                                //System.out.println("密码不正确  ");
                            }
                            //msg.obj = gameScore.getUpdatedAt();
                            handler.sendMessage(msg);
                        }
                        break;
                    }
                    if (objectId == "") {
                        msg.what = 103;
                        handler.sendMessage(msg);
                        System.out.println("没查找  ");
                    }
                } else {
                    //System.out.println("bmob失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(ContextM, "服务器查询p数据失败：" + e.getMessage() + "," + e.getErrorCode(), Toast.LENGTH_LONG).show();
                    outpute("服务器查询uset数据失败：", e);
                    msg.what = 104;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    /**
     * 注册---------------------
     */
    public void toRegister(String[] data1) {
        data3 = data1;
        //System.out.println(data3[0]+"  "+data3[1]+" 11111 "+data3[2]);
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("callnum", data1[0]);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> object, BmobException e) {
                Message msg = new Message();
                msg.what = 0;
                if (e == null) {
                    String objectId = "";
                    for (user gameScore : object) {
                        if (object.size() > 0) {
                            objectId = gameScore.getObjectId();
                            msg.what = 107;
                            handler.sendMessage(msg);
                        }
                        break;
                    }
                    if (objectId == "") {
                        user data = new user();
                        data.setData(data3);
                        data.save(new SaveListener<String>() {
                            @Override
                            public void done(String objectId, BmobException e) {
                                Message msg1 = new Message();
                                if (e == null) {
                                    gData app = (gData) ContextM.getApplicationContext();
                                    app.setRootData(data3);
                                    app.setObjectId(objectId);
                                    //System.out.println("objectId    "+objectId);
                                    msg1.what = 105;
                                    SharedPre sp = new SharedPre(ContextM, data3);
                                    sp.saveData();
                                } else {
                                    msg1.what = 106;
                                    //outpute("服务器创建user数据失败：",e);
                                }
                                handler.sendMessage(msg1);
                            }
                        });
                    }
                } else {
                    outpute("服务器查询创建user数据失败：", e);
                    msg.what = 108;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 修改密码---------------------
     */
    public void editmm(String[] data1) {
        gData app = (gData) ContextM.getApplicationContext();
        String obj = app.getObjectId();
        if (obj.equals("") || (data1[0].equals("") || data1[0].length() != 11)) {
            Toast.makeText(ContextM, ContextM.getString(R.string.i17), Toast.LENGTH_SHORT).show();
            return;
        }
        user gameScore = new user();
        gameScore.setValue("mm", data1[1]);
        gameScore.update(obj, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if (e == null) {

                    msg.what = 2;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 3;
                    handler.sendMessage(msg);
                    Toast.makeText(ContextM, ContextM.getString(R.string.i18), Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    /**
     * 退出登录---------------------
     */
    public void exitRoot() {
        gData app = (gData) ContextM.getApplicationContext();
        SharedPre sp = new SharedPre(ContextM);
        sp.mm = "";
        sp.saveData();
        data3 = app.getRootData();

        String obj = app.getObjectId();
        if (obj.equals("") || (data3[0].equals("") || data3[0].length() != 11)) {
            Toast.makeText(ContextM, ContextM.getString(R.string.i17), Toast.LENGTH_SHORT).show();
            return;
        }
        //System.out.println("退出了");

        user gameScore = new user();
        gameScore.setValue("state", "0");
        gameScore.update(obj, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if (e == null) {
                    gData app = (gData) ContextM.getApplicationContext();
                    app.setRootData();
                    app.setObjectId("");
                    msg.what = 5;
                    //System.out.println("1111555");
                    handler.sendMessage(msg);
                } else {
                    msg.what = 4;
                    handler.sendMessage(msg);
                    Toast.makeText(ContextM, ContextM.getString(R.string.i16), Toast.LENGTH_LONG).show();
                }
            }

        });

    }

    /**
     * 退出程序下线---------------------
     */
    public boolean exitThis() {

        gData app = (gData) ContextM.getApplicationContext();
        data3 = app.getRootData();
        if (data3[0].equals("") || data3[0].length() != 11) return false;
        String obj = app.getObjectId();
        if (obj.equals("")) {
            return false;
        }

        //System.out.println("下线了");
        app.setRootData();
        app.setObjectId("");

        user gameScore = new user();
        gameScore.setValue("state", "0");
        //gameScore.setData(data3);
        gameScore.update(obj, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //System.out.println("下线了111");
                } else {
                    outpute("下线失败!：", e);
                }
            }
        });
        return true;
    }

    /**
     * 检测密码-----------------------
     */
    public String isRmm(String mm, String mm1) {
        if (mm.equals("") || mm.length() < 6) {
            return ContextM.getString(R.string.i19);
        }
        if (!mm1.equals(mm)) {
            return ContextM.getString(R.string.i20);
        }
        if (!isSimplePassword(mm1)) {
            return ContextM.getString(R.string.i21);
        }
        return "";
    }

    /**
     * 判断是否为简单密码
     */
    public static Boolean isSimplePassword(String str) {
        String ls[] = new String[str.length()];
        for (int i = 0; i < str.length(); i++) {
            ls[i] = str.substring(i, i + 1);
        }
        boolean tt = false;
        for (int i = 0; i < ls.length - 1; i++) {
            if (!ls[i].equals(ls[i + 1])) {
                tt = true;
                break;
            }
        }

        if (!tt) return false;
        tt = false;
        for (int i = 0; i < ls.length - 1; i++) {
            if (isNumeric(ls[i]) && isNumeric(ls[i + 1])) {
                int a = Integer.parseInt(ls[i]);
                int b = Integer.parseInt(ls[i + 1]);
                if (Math.abs(a - b) != 1) tt = true;
                //System.out.println("33  "+a+"  "+b+"  "+tt+"   ");
            } else return true;
        }
        if (!tt) return false;
        return true;
    }

    /**
     * 判断是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 转md5-----------------------
     */
    public static String stringToMD5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    /**
     * 取机器码-----------------------
     */
    public static String GetCode(Context cot, ContentResolver cot1) {
        final TelephonyManager tm = (TelephonyManager) cot.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        String dv = "";

        dv = tm.getDeviceId();
        tmDevice = "" + dv;
        tmSerial = "" + tm.getSimSerialNumber();
        /*String sysVersion = android.os.Build.VERSION.RELEASE;
        if (Integer.parseInt(sysVersion.substring(0,1))<6) {
            dv=tm.getDeviceId();
            tmDevice = "" + dv;
            tmSerial = "" + tm.getSimSerialNumber();

        }else{
            tmDevice="";
            tmSerial="";
        }*/
        androidId = "" + android.provider.Settings.Secure.getString(cot1, android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        System.out.println("1111    " + deviceUuid.toString());
        return stringToMD5(deviceUuid.toString());
    }


    /**
     * 上传图片
     */
    public void setPic(String picPath) {
        //File file=new File(mm);
        bmobFile = new BmobFile(new File(picPath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //toast("上传文件成功:" + bmobFile.getFileUrl());
                    msg.what = 1;
                    msg.obj = bmobFile.getFileUrl();
                    //System.out.println("1111555");
                    handler.sendMessage(msg);

                    //System.out.println("2222  "+bmobFile.getFileUrl());
                } else {
                    //toast("上传文件失败：" + e.getMessage());
                    outpute("上传文件失败：", e);
                    msg.what = 2;
                    //System.out.println("1111555");
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                //System.out.println("333   "+value);
            }
        });
    }

    /**
     * 批量上传图片
     */
    public void setPic(SQLiteDatabase db1) {
        //File file=new File(mm);
        db = db1;
        Cursor cursor = db.rawQuery("select * from house WHERE list>?", new String[]{"200"});
        if (!cursor.moveToFirst()) {
            Message msg = new Message();
            msg.what = 5;
            handler.sendMessage(msg);
            return;
        }
        filename = cursor.getString(cursor.getColumnIndex("Remarks"));
        cursor.close();
        //System.out.println("一个文件上 " + filename);
        //System.out.println("222312  "+filename);
        bmobFile = new BmobFile(new File(filename));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                Message msg = new Message();
                if (e == null) {
                    Cursor cursor = db.rawQuery("select * from house WHERE Remarks=?", new String[]{filename});
                    if (!cursor.moveToFirst()) {
                        msg.what = 5;
                        handler.sendMessage(msg);
                        //System.out.println("666555  ");
                        return;
                    }
                    int PID = cursor.getInt(cursor.getColumnIndex("PID"));
                    int line = cursor.getInt(cursor.getColumnIndex("line"));
                    int list = cursor.getInt(cursor.getColumnIndex("list"));
                    list = list - 200;
                    //System.out.println("22212  "+filename);
                    /*cursor = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?",new String[]{PID+"",line+"",list+""});
                    dataOS dataos1=new dataOS(cursor);
                    cursor.close();
                    dataos1.pic="1";
                    picadd=bmobFile.getFileUrl();

                    UpData(dataos1);*/
                    picadd = bmobFile.getFileUrl();
                    BmobQuery<data_home> query = new BmobQuery<data_home>();
                    query.addWhereEqualTo("PID", PID + "");
                    query.addWhereEqualTo("line", line + "");
                    query.addWhereEqualTo("list", list + "");
                    query.findObjects(new FindListener<data_home>() {
                        @Override
                        public void done(List<data_home> object, BmobException e) {
                            if (e == null) {
                                String objectId = "";
                                for (data_home gameScore : object) {
                                    if (object.size() > 0) {
                                        objectId = gameScore.getObjectId();

                                        String pp = gameScore.getPic();
                                        //System.out.println("44  "+pp);
                                        if (!pp.equals("")) {
                                            BmobFile file = new BmobFile();
                                            file.setUrl(pp);
                                            file.delete(new UpdateListener() {
                                                @Override
                                                public void done(BmobException e) {

                                                }
                                            });
                                        }
                                        gameScore.setValue("pic", "1");
                                        gameScore.setValue("picadd", picadd);
                                        gameScore.update(objectId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    //Log.i("bmob","更新成功");
                                                    //System.out.println("更新成功 " );
                                                    db.execSQL("delete  from house WHERE list>? and Remarks=?", new String[]{"200", filename});
                                                    Message msg = new Message();
                                                    msg.what = 5;
                                                    handler.sendMessage(msg);

                                                } else {

                                                }
                                            }

                                        });
                                    }
                                    break;
                                }
                                if (objectId == "") {
                                    outpute("数据没有保存过!：", e);
                                    Message msg = new Message();
                                    msg.what = 6;
                                    msg.obj = filename.substring(filename.lastIndexOf("/") + 1, filename.length());
                                    handler.sendMessage(msg);
                                }
                            } else {
                                outpute("一个文件上传失败,可能已清票,请重新拍摄!", e);
                                Message msg = new Message();
                                msg.what = 3;
                                //System.out.println("一个文件上传失败,可能已清票,请重新拍摄! " + filename);
                                db.execSQL("delete  from house WHERE list>? and Remarks=?", new String[]{"200", filename});
                                handler.sendMessage(msg);
                            }
                        }
                    });

                    //System.out.println("2222  " + bmobFile.getFileUrl());
                } else {
                    //toast("上传文件失败：" + e.getMessage());
                    outpute("上传文件失败：", e);
                    msg.what = 4;
                    //db.execSQL("delete from house WHERE list>? ",new String[]{"200"});
                    //System.out.println("1111555");
                    handler.sendMessage(msg);
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                //System.out.println("333   "+value);
            }
        });
    }

    /**
     * 下载 图片
     */
    public void downloadPic(dataOS data1, String filename1) {
        if (!data1.pic.equals("1")) return;
        data = data1;
        filename = filename1;
        BmobQuery<data_home> query = new BmobQuery<data_home>();
        query.addWhereEqualTo("PID", data.PID);
        query.addWhereEqualTo("line", data.line);
        query.addWhereEqualTo("list", data.list);

        query.findObjects(new FindListener<data_home>() {
            @Override
            public void done(List<data_home> object, BmobException e) {
                Message msg = new Message();
                if (e == null) {
                    for (data_home gameScore : object) {
                        String pp = gameScore.getPic();
                        System.out.println("44  " + pp);
                        if (!pp.equals("")) {
                            //BmobFile bmobfile = gameScore.getImg();

                            BmobFile bmobfile = new BmobFile(filename, "", gameScore.getPic());

                            //System.out.println("开始下   "+bmobfile.getFilename()+"  "+gameScore.getPic());

                            File saveFile = new File(Environment.getExternalStorageDirectory() + File.separator + "芝麻开花" + File.separator, bmobfile.getFilename());

                            //File saveFile = new File(ContextM.getApplicationContext().getCacheDir()+"/bmob/", bmobfile.getFilename());

                            bmobfile.download(saveFile, new DownloadFileListener() {

                                @Override
                                public void onStart() {
                                    //System.out.println("开始下载");
                                }

                                @Override
                                public void done(String savePath, BmobException e) {
                                    Message msg = new Message();
                                    if (e == null) {
                                        msg.what = 1;
                                        msg.obj = savePath;
                                        handler.sendMessage(msg);
                                        //System.out.println("下载成功,保存路径:"+savePath);
                                    } else {
                                        outpute("下载失败 ,请重新下载!", e);
                                        msg.what = 2;
                                        handler.sendMessage(msg);

                                    }
                                }

                                @Override
                                public void onProgress(Integer value, long newworkSpeed) {
                                    //Log.i("bmob","下载进度："+value+","+newworkSpeed);

                                    Message msg = new Message();
                                    msg.what = 111;
                                    msg.obj = Integer.toString(value);
                                    //System.out.println("下载进111度 "+value+","+newworkSpeed);
                                    handler.sendMessage(msg);
                                    //System.out.println("下载进度 "+value+","+newworkSpeed);
                                }

                            });
                        } else {
                            outpute("文件已删除!", e);
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                    }
                } else {
                    outpute("下载 查询失败,请重新下载!", e);
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 下载 APP
     */
    public void downloadApp() {
        if (filename != null && filename.length() > 10) {
            String name="芝麻开花.apk";
            BmobFile bmobfile = new BmobFile(name, "", filename);

            File saveFile = new File(Environment.getExternalStorageDirectory() + File.separator , name);
            System.out.println("开始下载 "+bmobfile.getFilename()+"  "+saveFile.exists());
            //File saveFile = new File(ContextM.getApplicationContext().getCacheDir()+"/bmob/", bmobfile.getFilename());

            bmobfile.download(saveFile, new DownloadFileListener() {

                @Override
                public void onStart() {
                    //System.out.println("开始下载");
                }

                @Override
                public void done(String savePath, BmobException e) {
                    Message msg = new Message();
                    if (e == null) {
                        msg.what = 1003;
                        msg.obj = savePath;
                        handler.sendMessage(msg);
                        System.out.println("下载成功,保存路径:"+savePath);

                    }
                }

                @Override
                public void onProgress(Integer value, long newworkSpeed) {
                    //Message msg = new Message();
                    //msg.what = 111;
                    //msg.obj = Integer.toString(value);
                    //handler.sendMessage(msg);

                }

            });
        }
    }

    /**下载 图片*/
    public void downloadPic(dataOS data1) {
        if (!data1.pic.equals("1"))return ;
        data = data1;
        BmobQuery<data_home> query = new BmobQuery<data_home>();
        query.addWhereEqualTo("PID", data.PID);
        query.addWhereEqualTo("line", data.line);
        query.addWhereEqualTo("list", data.list);

        query.findObjects(new FindListener<data_home>() {
            @Override
            public void done(List<data_home> object, BmobException e) {
                Message msg = new Message();
                if (e == null) {
                    for (data_home gameScore : object) {
                        String pp=gameScore.getPic();
                        System.out.println("44  "+pp);
                        if (!pp.equals("")) {
                            Intent intent = new Intent(ContextM, picshow.class);
                            intent.putExtra("extra_data2", gameScore.getPic());
                            ContextM.startActivity(intent);
                        }else{
                            outpute("文件已删除!",e);
                            msg.what=2;
                            handler.sendMessage(msg);
                        }
                    }
                }else{
                    outpute("下载 查询失败,请重新下载!",e);
                    msg.what=2;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}