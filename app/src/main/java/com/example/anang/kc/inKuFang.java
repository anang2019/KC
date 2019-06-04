package com.example.anang.kc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;

public class inKuFang extends Activity {
    private dataOS[] posp;
    private SQLiteDatabase db;
    private Button dataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_in_ku_fang);
        Intent intent=getIntent();
        String data =intent.getStringExtra("extra_data");
        String data2 =intent.getStringExtra("extra_data2");

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        Cursor cursor1;
        String imagePathLS = Environment.getExternalStorageDirectory() + File.separator+"芝麻开花"+ File.separator;
        File file = new File(imagePathLS);
        if (!file.exists()) file.mkdirs();

        /**返回键*/
        ImageButton img=(ImageButton) findViewById(R.id.ic_back);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("2222");
                v.setVisibility(View.GONE);
                finish();
            }
        });
        gData app = (gData)getApplicationContext();
        if(app.getAdv().length()>1){
            TableRow tab=(TableRow) findViewById(R.id.TableRowgg);
            tab.setVisibility(View.VISIBLE);
            marqueeText img1=(marqueeText) findViewById(R.id.tv1);
            img1.setText(app.getAdv()+"                            ");
        }
        if (data2!=null){
            String[]nn=data2.split(",");
            cursor1= db.rawQuery("select * from company WHERE PID=?",new String[]{nn[0]});
        }else cursor1= db.rawQuery("select * from company WHERE PID=?",new String[]{data});

        if(cursor1.moveToFirst())
        {
            int PID=cursor1.getInt(cursor1.getColumnIndex("PID"));
            int line=cursor1.getInt(cursor1.getColumnIndex("line"));
            int list=cursor1.getInt(cursor1.getColumnIndex("list"));

            String data1 = cursor1.getString(cursor1.getColumnIndex("co"));
            String ware= cursor1.getString(cursor1.getColumnIndex("ware"));
            TextView text1=(TextView)findViewById(R.id.textView14);
            text1.setText("仓库 "+data1+"  "+ware+"   蓝色代表货物占用,黑色代表通道");
            dataOS dataos[][]=new dataOS[line][list];
            Cursor cursor = db.rawQuery("select * from house WHERE PID = ?",new String[]{Integer.toString(PID)});
            if(cursor.moveToFirst())
            {
                do{
                    //遍历Cursor对象，取出数据并打印
                    dataOS dataos1=new dataOS(cursor);
                    int x1=Integer.parseInt(dataos1.line);
                    int y1=Integer.parseInt(dataos1.list);
                    if (line>=x1&&list>=y1) {
                        dataos[x1-1][y1-1]=dataos1;
                    }
                }while(cursor.moveToNext());

            }

            posp=new dataOS[line*list];
            cursor.close();
            TableLayout layout=(TableLayout) this.findViewById(R.id.tableLayout2);
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3.0f);
            int ww=-8;
            lp1.setMargins(ww,ww,ww,ww);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
            lp.setMargins(0,0,0,0);
            int bid=0;
            Drawable drawable= getResources().getDrawable(R.drawable.ic_menu_gallery);
            /// 这一步必须要做,否则不会显示.
            drawable.setBounds(-10, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

            for (int x = 0; x < dataos.length; x++) { // 循环设置表格行
                TableRow row = new TableRow(this); // 定义表格行
                int n=0;
                for (int y = 0; y < dataos[x].length; y++,n++) {
                    if(dataos[x][y]==null)dataos[x][y]=new dataOS(PID,x+1,y+1);
                    if(y%2==1)
                    {
                        TextView text = new TextView(this);
                        text.setLayoutParams(lp);
                        String tt=dataos[x][y-1].pos;
                        if (tt.equals("")) tt=dataos[x][y].pos;
                        text.setText(tt);
                        text.setTextColor(Color.parseColor("#FF7BF2DE"));
                        row.addView(text, n);
                        n++;
                    }

                    Button btn = new Button(this);

                    //System.out.println("555555   "+x+"  "+y+"  "+dataos[x][y].line+" "+dataos[x][y].list);
                    String ttt=dataos[x][y].andnum.toString()+dataos[x][y].Country.toString();
                    if (dataos[x][y].pic.equals("1")){
                        //btn.setCompoundDrawables(drawable,null,null,null);
                        //btn.setCompoundDrawablePadding(-15);
                        ttt="<font color='#FF0000' size='12'><small>图</small></font>"+ttt;

                        btn.setText(Html.fromHtml(ttt));

                    }else btn.setText(ttt); // 设置文本内容

                    posp[bid]=dataos[x][y];

                    //System.out.println("St333ar   "+posp[bid].settoString());
                    btn.setMaxLines(50);

                    int posfull=0;
                    if (dataos[x][y].posfull!=null&&dataos[x][y].posfull.length()>0)
                        posfull=Integer.parseInt(dataos[x][y].posfull);

                    if (dataos[x][y].andnum.equals(""))posfull=0;
                    else if (y%2==1)posfull+=11;

                    posp[bid].Rposfull=posfull;
                    SetPicB(btn,posfull);

                    //btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.dd5));
                    //btn.onSetBmp(R.drawable.db5, R.drawable.btn_u);
                    btn.setLayoutParams(lp1);
                    btn.setGravity(-1);
                    btn.setSingleLine(true);
                    btn.setId(bid);
                    bid++;

                    //btn.setDrawingCacheBackgroundColor(564568);
                    //
                    btn.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            //添加事件处理逻辑
                            gData app = (gData)getApplicationContext();
                            String ls[]=app.getRootData();
                            //System.out.println("ls正确111  "+ls[4]);
                            int roota=Integer.parseInt(ls[4]);

                            Button b = (Button)v;
                            int dwid=b.getId();
                            if (!posp[dwid].andnum.equals("")) {
                                MyPopup morePopWindow = new MyPopup(inKuFang.this, posp[dwid], dwid, db);
                                //morePopWindow.setTouchable(true);
                                if (roota>8) {
                                    morePopWindow.setOnCallListener(new MyPopup.OnCallListener() {
                                        public void onCall(int sing) {
                                            //System.out.println("回成功 "+sing);
                                            Intent intent = new Intent(inKuFang.this, Second.class);

                                            //System.out.println(dataos.PID+","+dataos.line+","+dataos.list);
                                            intent.putExtra("extra_data", posp[sing].settoString());
                                            intent.putExtra("extra_data1", "" + sing);
                                            startActivityForResult(intent, 1);
                                            //startActivity(intent);
                                        }
                                    });
                                }
                                morePopWindow.showPopupWindow(b);
                            }else if (roota>8) {
                                Intent intent=new Intent(inKuFang.this, Second.class);
                                intent.putExtra("extra_data", posp[dwid].settoString());
                                intent.putExtra("extra_data1", "" + dwid);
                                startActivityForResult(intent, 1);
                            }
                        }
                    });

                    btn.setOnTouchListener(new View.OnTouchListener() {

                        @Override public boolean onTouch(View v, MotionEvent event) {
                            // TODO Auto-generated method stub
                            int eve=event.getAction();
                            if (eve == MotionEvent.ACTION_DOWN) {
                                v.setBackgroundResource(R.drawable.d0_1);
                            } else if (eve== MotionEvent.ACTION_UP||eve== MotionEvent.ACTION_MOVE||eve==3) {
                                SetPicB(v,posp[v.getId()].Rposfull);
                            }
                            return false;
                        }
                    });

                    btn.setOnLongClickListener(new View.OnLongClickListener(){
                        @Override
                        public boolean onLongClick(View v) {
                            gData app = (gData)getApplicationContext();
                            String ls[]=app.getRootData();
                            //System.out.println("ls正确111  "+ls[4]);
                            int roota=Integer.parseInt(ls[4]);
                            dataButton = (Button) v;
                            if (roota>8&&dataButton.getText().length()>0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(inKuFang.this);
                                builder.setMessage("是否清空票号 " + dataButton.getText().toString() + " ?")
                                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                int dwID=dataButton.getId();
                                                posp[dwID].Country="";
                                                posp[dwID].posfull="10";
                                                posp[dwID].num="";
                                                posp[dwID].Remarks="";
                                                posp[dwID].co="";
                                                posp[dwID].Star="";
                                                posp[dwID].andnum="";
                                                posp[dwID].people="";
                                                posp[dwID].Rposfull=0;
                                                posp[dwID].pic="";
                                                posp[dwID].date="";

                                                posp[dwID].savedata(db);
                                                bmobData bm=new bmobData(inKuFang.this);
                                                bm.setPicadd("1");

                                                bm.UpData(posp[dwID]);
                                                dataButton.setBackgroundResource(R.drawable.d0);
                                                dataButton.setText("");
                                            }
                                        })
                                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        }).show();
                                System.out.println("555   "+posp[dataButton.getId()].settoString());
                            }
                            return true;
                        }
                    });

                    row.addView(btn, n); // 加入一个编号
                }

                layout.addView(row); // 向表格之中增加若干个表格行

            }

            if (data2!=null) {
                for (int i = 0; i < posp.length; i++) {
                    if (data2.indexOf(posp[i].PID+","+ posp[i].line+","+posp[i].list) != -1) {
                        Button btn = (Button) findViewById(i);
                        btn.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        }

        cursor1.close();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ScrollView sv=(ScrollView)findViewById(R.id.scrollviev1);
                sv.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

    }

    public void SetPicB(View btn,int n) {
        switch (n) {
            case 1:
                btn.setBackgroundResource(R.drawable.d1);
                break;
            case 2:
                btn.setBackgroundResource(R.drawable.d2);
                break;
            case 3:
                btn.setBackgroundResource(R.drawable.d3);
                break;
            case 4:
                btn.setBackgroundResource(R.drawable.d4);
                break;
            case 5:
                btn.setBackgroundResource(R.drawable.d5);
                break;
            case 6:
                btn.setBackgroundResource(R.drawable.d6);
                break;
            case 7:
                btn.setBackgroundResource(R.drawable.d7);
                break;
            case 8:
                btn.setBackgroundResource(R.drawable.d8);
                break;
            case 9:
                btn.setBackgroundResource(R.drawable.d9);
                break;
            case 10:
                btn.setBackgroundResource(R.drawable.d10);
                break;
            case 12:
                btn.setBackgroundResource(R.drawable.d12);
                break;
            case 13:
                btn.setBackgroundResource(R.drawable.d13);
                break;
            case 14:
                btn.setBackgroundResource(R.drawable.d14);
                break;
            case 15:
                btn.setBackgroundResource(R.drawable.d15);
                break;
            case 16:
                btn.setBackgroundResource(R.drawable.d16);
                break;
            case 17:
                btn.setBackgroundResource(R.drawable.d17);
                break;
            case 18:
                btn.setBackgroundResource(R.drawable.d18);
                break;
            case 19:
                btn.setBackgroundResource(R.drawable.d19);
                break;
            case 20:
                btn.setBackgroundResource(R.drawable.d20);
                break;
            case 21:
                btn.setBackgroundResource(R.drawable.d10);
                break;
            default:
                btn.setBackgroundResource(R.drawable.d0);
        }
    }

    //重写onActivityResult方法获取返回的结果数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //System.out.println("4455555");
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    String strings=data.getStringExtra("data_return");
                    String strings1=data.getStringExtra("data_return1");
                    String strings2=data.getStringExtra("data_return2");
                    //System.out.println("4455555   2  "+strings1+"  "+strings2);
                    Button btn=(Button) findViewById(Integer.parseInt(strings));
                    posp[Integer.parseInt(strings)]=new dataOS(strings1);

                    btn.setText(posp[Integer.parseInt(strings)].andnum+posp[Integer.parseInt(strings)].Country);
                    if (strings2.length()>0)
                        posp[Integer.parseInt(strings)].Rposfull=Integer.parseInt(strings2);
                        SetPicB(btn,Integer.parseInt(strings2));
                }
                break;
            default:break;
        }

    }

}
