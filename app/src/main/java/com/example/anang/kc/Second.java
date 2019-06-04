package com.example.anang.kc;


import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Second extends Activity {
    private String data,dataid;
    private SQLiteDatabase db;
    private MultiAutoCompleteTextView mc;
    private String[] res;
    private String[] nameP;
    private String imagePathTP,imagePathLS,imageAdd,imageDel;
    private int hc1=0;
    private ArrayAdapter<String> adapter1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_second);
        imageAdd="";
        imageDel="";
        //键盘默认不弹出
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EditText editText = (EditText) findViewById(R.id.editText6);
        String digists = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        editText = (EditText) findViewById(R.id.editText71);
        digists = "0123456789+-*/";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        editText = (EditText) findViewById(R.id.editText7);
        digists = "0123456789.";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        editText = (EditText) findViewById(R.id.editText2date);
        digists = "0123456789-";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        //db.rawQuery("delete from people WHERE PID = ?", new String[]{"155"});
        Intent intent=getIntent();
        data =intent.getStringExtra("extra_data");
        dataid =intent.getStringExtra("extra_data1");
        imagePathLS = Environment.getExternalStorageDirectory() + File.separator+"芝麻开花"+ File.separator;
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        SetPP();
        TableLayout layout=(TableLayout) this.findViewById(R.id.tableLayout11);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        int ww=0;
        lp1.setMargins(ww,ww,ww,ww);



        /**储位比
        Spinner spinner1 = (Spinner) findViewById(R.id.noberbi);
        adapter1=new ArrayAdapter<String>(Second.this,R.layout.spinner_item, res.getStringArray (R.array.posfull));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);
        */

        Cursor cursor1 = db.rawQuery("select DISTINCT  co from people WHERE  co != ?",new String[]{""});

        int n=0;
        String ls[];
        ls=new String[cursor1.getCount()+1];
        ls[0]="无";
        if(cursor1.moveToFirst()){
            do{
                String people1 = cursor1.getString(cursor1.getColumnIndex("co"));
                if (people1.length()>0)
                {
                    ls[n+1]=people1;
                    n++;
                }
            }while(cursor1.moveToNext());
        }
        cursor1.close();

        n=0;
        for (int x = 0; x < ls.length; x+=4) { // 循环设置表格行
            TableRow row = new TableRow(this); // 定义表格行
            row.setWeightSum(4);
            for (int y = 0; y < 4; y++) {
                //Toast.makeText(MainActivity.this, this.titleData[n], 5000).show();
                Button btn = new Button(this) ;
                btn.setText(ls[n]); // 设置文本内容
                btn.setWidth(0);
                btn.setLayoutParams(lp1);
                btn.setMaxLines(50);
                btn.setGravity(-1);
                btn.setSingleLine(true);

                //btn.setBackgroundColor(Color.parseColor("#FF4CCE6D"));
                btn.setBackgroundColor(getResources().getColor(R.color.fybg));

                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //添加事件处理逻辑
                        Button b=(Button)v;
                        EditText text=(EditText)findViewById(R.id.editText2);
                        String coname=b.getText().toString();
                        if (coname=="无")coname="";
                        text.setText(coname);
                        int n=0;

                        Cursor cursor1 = db.rawQuery("select * from people WHERE  co = ?",new String[]{coname});
                        String tt="";

                        String[] mItems1=new String[cursor1.getCount()+1];
                        mItems1[0]="选择联系人";
                        if(cursor1.moveToFirst()){
                            do{
                                String people1 = cursor1.getString(cursor1.getColumnIndex("p"));
                                if (people1.length()>0)
                                {
                                    if (n<2&&coname.length()>0){
                                        if (n>0)tt+=",";
                                        tt+=people1;
                                    }
                                    mItems1[n+1]=people1;
                                    n++;
                                }

                                //System.out.println(n+" id="  + people1);

                            }while(cursor1.moveToNext());
                        }
                        cursor1.close();
                        Spinner spinner1 = (Spinner) findViewById(R.id.noberbi1);
                        adapter1=new ArrayAdapter<String>(Second.this,android.R.layout.simple_spinner_item, mItems1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //绑定 Adapter到控件
                        spinner1 .setAdapter(adapter1);

                        text=(EditText)findViewById(R.id.editText4);
                        text.setText(tt);

                    }
                });

                row.addView(btn); // 加入一个编号
                n++;
                if(n>=ls.length){
                    break;
                }
            }
            layout.addView(row); // 向表格之中增加若干个表格行
        }

        upUi();

        Spinner spinnerView = (Spinner) findViewById(R.id.noberbi1);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0) {
                            EditText text = (EditText) findViewById(R.id.editText4);
                            String num2 = text.getText().toString();
                            if (num2.length() > 0) num2 += ",";
                            text.setText(num2 + adapter1.getItem(position));
                            //System.out.println(position+" id="  + adapter1.getItem(position));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //System.out.println("111111");
                    }
                });

        //保存按钮
        Button SButton=(Button) findViewById(R.id.button2);
        SButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveButtonC();
            }
        });

        /**计算器*/
        TextView evlClick=(TextView) findViewById(R.id.textView10);
        evlClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText text=(EditText)findViewById(R.id.editText71);
                String strContent=text.getText().toString();
                if (strContent.length()>2) {
                    try {
                        Symbols s = new Symbols();
                        double res = s.eval(strContent);
                        EditText result = (EditText) findViewById(R.id.editText7);
                        res=res/20;
                        result.setText(Integer.toString((int)Math.floor(res)));

                    } catch (SyntaxException e) {
                        Toast.makeText(Second.this, "错误！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            });
        /**票号按钮事件*/
        evlClick =(TextView) findViewById(R.id.textView8);
        evlClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                EditText text=(EditText)findViewById(R.id.editText6);
                if(!text.getText().toString().equals("")) saveButtonC();
            }
        });

        /**选择照片 按钮事件*/
        Button btnpic =(Button) findViewById(R.id.buttonpic);
        btnpic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //plsc("11");
                intent.setType("image/*");

                startActivityForResult(intent, 56);
            }
        });

        /**删除 照片 按钮事件*/
        btnpic =(Button) findViewById(R.id.buttonpsc);
        btnpic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                TextView tt=(TextView) findViewById(R.id.sccg);
                tt.setText("删除成功!");
                imageDel="1";
            }
        });
        /**批量上传 照片 按钮事件*/
        btnpic =(Button) findViewById(R.id.buttondopic2);
        btnpic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                plsc("");
            }
        });

        /**拍照  按钮事件*/
        btnpic =(Button) findViewById(R.id.buttondopic);
        btnpic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (shoot())return;
                //String imagePath = getCacheDir().getAbsolutePath() + "/new.jpg";
                Intent intent  = new Intent("android.media.action.IMAGE_CAPTURE");
                //imagePathTP = Environment.getExternalStorageDirectory() + File.separator+"芝麻开花"+ File.separator ;
                imagePathTP=getPicName();
                File f = new File(imagePathLS+imagePathTP);
                try {
                    f.createNewFile();
                    // 这行代码很重要，没有的话会因为写入权限不够出一些问题
                    f.setWritable(true, false);
                } catch (IOException e) {
                }
                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                if (currentapiVersion<24){
                    intent.putExtra("return-data", false);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true);
                    startActivityForResult(intent, 55);
                }else {
                    Uri uri = FileProvider.getUriForFile(Second.this, "com.example.anang.kc.fileprovider", f);
                    //通过FileProvider创建一个content类型的Uri
                    //Uri uri = Second.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true);
                    startActivityForResult(intent, 55);
                }



            }
        });

    }
    /**拍照  事件*/
    public boolean shoot(){
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                System.out.println("果1没有    ");
                ActivityCompat.requestPermissions(this, permissions, 321);
                return true;
            }else return false;
        }else return false;
    }

    /**保存按钮事件*/
    public void saveButtonC(){
        EditText text=(EditText)findViewById(R.id.editText6);
        String andnum1=text.getText().toString();
        andnum1=andnum1.toUpperCase();
        text=(EditText)findViewById(R.id.editText5);
        String Country1=text.getText().toString();

        //text=(EditText)findViewById(R.id.editText10);
        //String posfull1=text.getText().toString();

        Spinner spinner1 = (Spinner) findViewById(R.id.noberbi);
        String posfull1=spinner1.getSelectedItem().toString();
        //Log.d("Second","rrrrrrrr  "+spinner1.getSelectedItem());

        String star="";
        Spinner spinner=(Spinner)findViewById(R.id.star1);
        star+=Integer.toString(Integer.parseInt(spinner.getSelectedItem().toString())-1);

        spinner=(Spinner)findViewById(R.id.star2);
        star+=Integer.toString(Integer.parseInt(spinner.getSelectedItem().toString())-1);
        spinner=(Spinner)findViewById(R.id.star3);
        star+=Integer.toString(Integer.parseInt(spinner.getSelectedItem().toString())-1);

        text=(EditText)findViewById(R.id.editText7);
        String num1=text.getText().toString();

        text=(EditText)findViewById(R.id.editText2date);
        String date1=text.getText().toString();

        text=(EditText)findViewById(R.id.editText71);
        String strContent=text.getText().toString();
        if (strContent.length()>2&&num1.length()==0) {
            try {
                Symbols s = new Symbols();
                double res = s.eval(strContent);
                res=res/20;
                num1=Integer.toString((int)Math.floor(res));

            } catch (SyntaxException e) {
                Toast.makeText(Second.this, "计算错误！", Toast.LENGTH_SHORT).show();
            }
        }

        text=(EditText)findViewById(R.id.editText8);
        String pos1=text.getText().toString();

        text=(EditText)findViewById(R.id.editText3);
        String Remarks1=text.getText().toString();

        text=(EditText)findViewById(R.id.editText2);
        String co1=text.getText().toString();

        text=(EditText)findViewById(R.id.editText4);
        String pp1=text.getText().toString();
        String[] strings2 = data.split("\\|");
        //System.out.println(strings2.length);
        String strings[] ={strings2[0],strings2[1],strings2[2]};
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?",strings);
        String ppp="";

        bmobData bm=new bmobData(Second.this);
        String pic="";

        if (strings2.length>12)pic=strings2[12];

        System.out.println(strings2.length+"  "+pic);

        if (!imageAdd.equals("")){
            pic="1";
            bm.setPicadd(imageAdd);
        }else if(imageDel.equals("1")){
            pic="";
            bm.setPicadd("1");
        }

        if (andnum1.length()==0) {
            Country1="";
            posfull1="10";
            num1="";
            Remarks1="";
            co1="";
            star="";
            pic="";
        }else if (pp1.length()>0){
            pp1=pp1.replace("\n","");
            String[] strings1 = pp1.split(",|，");
            //System.out.println("444   "+strings1.length);
            for(int j=0;j<strings1.length;j++) {
                if (strings1[j].length()>1) {
                    strings1[j]=strings1[j].replace(" ","");
                    Cursor cursor1 = db.rawQuery("select * from people WHERE p = ?", new String[]{strings1[j]});
                    while (cursor1.getCount() == 0) {
                        db.execSQL("insert into people(co,p) values(?,?)", new String[]{co1, strings1[j]});
                        cursor1 = db.rawQuery("select * from people WHERE p = ?", new String[]{strings1[j]});
                        cursor1.moveToFirst();
                        int PID = cursor1.getInt(cursor1.getColumnIndex("PID"));

                        bm.UpDataP(new String[]{Integer.toString(PID), co1, strings1[j]});

                        //Log.d("MainActivity", "新加一个"+strings1[j]);
                    }
                    if (cursor1.getCount() > 0) {
                        cursor1.moveToFirst();
                        int PID = cursor1.getInt(cursor1.getColumnIndex("PID"));
                        if (j > 0) {
                            ppp += ",";
                        }
                        ppp += PID;
                    }
                    cursor1.close();
                }
            }
        }
        String upS[]={strings[0],strings[1],strings[2],pos1,andnum1,Country1,num1,posfull1,star,co1,ppp,Remarks1,pic,date1};

        dataOS dos1=new dataOS(upS);
        dos1.savedata(db);


        //String upS1[]={strings[0],strings[1],strings[2],posfull1,andnum1,Country1,pos1,num1,co1,ppp,Remarks1,star};
        bm.UpData(dos1);

        Intent intent = new Intent();
        if(andnum1.equals("")){
            posfull1="0";
        }else {
            if (Integer.parseInt(strings[2]) % 2 == 0) {
                int a = Integer.parseInt(posfull1);
                a += 11;
                posfull1 = Integer.toString(a);
            }
        }

        intent.putExtra("data_return", dataid);
        intent.putExtra("data_return1", dos1.settoString());
        intent.putExtra("data_return2", posfull1);
        setResult(RESULT_OK, intent);
        //System.out.println("保存成功");

        finish();

    }

    /**自动匹配*/
    public void SetPP(){
        int n=0;

        Cursor cursor = db.rawQuery("select * from people WHERE p !=?",new String[]{""});
        res=new String[cursor.getCount()];
        nameP=new String[cursor.getCount()];
        //System.out.println("789    1   "+cursor.getCount());
        if(cursor.moveToFirst()){
            do{
                String people1 = cursor.getString(cursor.getColumnIndex("p"));
                //int pid = cursor.getInt(cursor.getColumnIndex("PID"));
                if (people1.length()>2) {

                    if (!people1.substring(0,1).equals("1")){
                        String ls[]=people1.split("1",2);
                        if(ls.length>1) {
                            nameP[n] = ls[0];
                            res[n] = "1" + ls[1];
                        }
                    }else {
                        res[n] = people1;
                        nameP[n]="";
                    }
                    //System.out.println("8888    "+people1+" "+nameP[n]+" "+ res[n]+" "+n);

                }else{
                    res[n] = "";
                    nameP[n]="";
                }
                n++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,res);
        /*
        AutoCompleteTextView acTextView=(AutoCompleteTextView) findViewById(R.id.editText4);
        acTextView.setAdapter(adapter);
        */
        //设置逗号分割

        mc=(MultiAutoCompleteTextView)findViewById(R.id.editText4);
        mc.setAdapter(adapter);
        mc.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tt=(TextView)view;
                String str=tt.getText().toString();

                for (int i=0;i<res.length;i++){
                    if (str.equals(res[i])){

                        String text1=mc.getText().toString();
                        text1=text1.replace(res[i],nameP[i] +res[i]);
                        //System.out.println("999    "+nameP[i]+"   "+res[i]+"   "+mc.getText().toString());
                        //if (!text1.equals(""))text1+=",";
                        mc.setText(text1);
                        mc.setSelection(mc.getText().length());
                        return;
                    }
                }

            }

        });
    }
    public void CountryClick(View v) {
        RadioButton b =(RadioButton) v;
        EditText text=(EditText)findViewById(R.id.editText5);
        text.setText(b.getText());
    }

    public void RemarksClick(View v) {
        RadioButton b =(RadioButton) v;
        EditText text=(EditText)findViewById(R.id.editText3);
        text.setText(b.getText());
    }
    public void LikeOld(View v) {
        Button b =(Button) v;
        String[] strings = data.split("\\|");
        int id=b.getId();
        if (id==R.id.button5){
            strings[1]=Integer.toString(Integer.parseInt(strings[1])+1);
        }
        else if (id==R.id.button6){
            strings[1]=Integer.toString(Integer.parseInt(strings[1])-1);
        }
        else if (id==R.id.button4) {
            strings[2] = Integer.toString(Integer.parseInt(strings[2]) - 1);
        }
        else if (id==R.id.button) {
            strings[2] = Integer.toString(Integer.parseInt(strings[2]) + 1);
        }
        upUi(strings);
    }

    public void upUi(String []strings) {
        Cursor cursor = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?",new String[]{strings[0],strings[1],strings[2]});
        if(cursor.moveToFirst()) {
            dataOS dataos=new dataOS(cursor);
            String num="";
            String pos=dataos.pos;
            if (strings.length>3&&strings[3].length()>0) pos=strings[3];

            EditText text = (EditText) findViewById(R.id.editText7);
            text.setText(num);
            text = (EditText) findViewById(R.id.editText8);
            text.setText(pos);
            dataos.pic="";
            setupUi(dataos);

        }
        cursor.close();
    }

    public void upUi() {
        dataOS dataos=new dataOS(data);

        EditText text = (EditText) findViewById(R.id.editText7);
        text.setText(dataos.num);
        text = (EditText) findViewById(R.id.editText8);
        text.setText(dataos.pos);
        setupUi(dataos);
    }

    public void setupUi(dataOS dataos) {

        int ss[]={6,6,6};
        if (dataos.Star!=null&&dataos.Star.length()>2){
            for (int i=0;i<3;i++){
                String tt=dataos.Star.substring(i,i+1);
                if (tt!=null&&!tt.equals(" "))ss[i]=Integer.parseInt(tt);
            }

        }
        if (dataos.pic.equals("1")){
            TextView text=(TextView)findViewById(R.id.sccg);
            text.setText("有图片! ");
        }


        Spinner spinner=(Spinner)findViewById(R.id.star1);
        spinner.setSelection(10-(ss[0]+1));
        spinner=(Spinner)findViewById(R.id.star2);
        spinner.setSelection(10-(ss[1]+1));
        spinner=(Spinner)findViewById(R.id.star3);
        spinner.setSelection(10-(ss[2]+1));

        EditText text=(EditText)findViewById(R.id.editText6);
        text.setText(dataos.andnum);
        text=(EditText)findViewById(R.id.editText5);
        text.setText(dataos.Country);

        text=(EditText)findViewById(R.id.editText2date);
        String date1=dataos.date;
        if (date1.length()==0){
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            date1=sdf1.format(curDate);
        }
        text.setText(date1);

        Spinner spinner1 = (Spinner) findViewById(R.id.noberbi);
        int posf=10;
        if(dataos.posfull!=null&&dataos.posfull.length()>0) posf=Integer.parseInt(dataos.posfull);
        spinner1.setSelection(10-posf);

        text=(EditText)findViewById(R.id.editText3);
        text.setText(dataos.Remarks);
        String pp="";
        if (dataos.people!=null&&!dataos.people.equals(" ")) {
            String[] strings1 = dataos.people.split(",");
            for (int j = 0; j < strings1.length; j++) {
                if (strings1[j].length() > 0) {
                    Cursor cursor1 = db.rawQuery("select * from people WHERE PID = ?", new String[]{strings1[j]});
                    if (cursor1.moveToFirst()) {
                        String people1 = cursor1.getString(cursor1.getColumnIndex("p"));
                        if (j > 0) pp += ",\n";
                        pp += people1;
                    }
                    cursor1.close();
                }
            }
        }

        text=(EditText)findViewById(R.id.editText4);
        text.setText(pp);
        text=(EditText)findViewById(R.id.editText2);
        text.setText(dataos.co);

    }

    public void plsc(String newpath){

        Intent intent = new Intent(Second.this, picshow.class);
        intent.putExtra("extra_data", newpath);
        String[] strings2 = data.split("\\|");
        intent.putExtra("extra_data1",strings2[0]+","+strings2[1]+","+strings2[2]+","+newpath);
        startActivityForResult(intent,50);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1) {
        super.onActivityResult(requestCode, resultCode, data1);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case 50:
                        imageAdd=data1.getStringExtra("data_return");
                        if (imageAdd.equals("")){
                            hc1=1;
                        }else {
                            TextView text = (TextView) findViewById(R.id.sccg);
                            text.setText("上传成功! " + imagePathTP);
                            Button btnpic = (Button) findViewById(R.id.buttonpsc);
                            btnpic.setEnabled(true);
                        }
                        break;
                    case 55:
                        try {
                            String newpath=saveBitmap("");
                            plsc(newpath);
                            // 获取文件进行操作
                            // ...
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 56:
                        Uri originalUri = data1.getData();        //获得图片的uri


                        String[] proj = {MediaStore.Images.Media.DATA};

                        //System.out.println(originalUri+"   222     "+proj[0]);

                        //好像是android多媒体数据库的封装接口，具体的看Android文档

                        Cursor cursor = this.getContentResolver().query(originalUri, proj, null, null, null);

                        //按我个人理解 这个是获得用户选择的图片的索引值

                        int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

                        //将光标移至开头 ，这个很重要，不小心很容易引起越界

                        cursor.moveToFirst();

                        //最后根据索引值获取图片路径

                        String newpath=saveBitmap(cursor.getString(column_index));

                        //System.out.println(cursor.getColumnName(column_index)+"  "+path+"   222     "+cursor+"   "+column_index);
                        cursor.close();


                        plsc(newpath);
                        break;
                }

                break;
            default:
                break;
        }
    }


    public String getPicName() {
        dataOS dataos=new dataOS(data);
        Cursor cursor = db.rawQuery("select * from company WHERE PID = ?", new String[]{dataos.PID});
        cursor.moveToFirst();
        String co = cursor.getString(cursor.getColumnIndex("co"));
        String ware = cursor.getString(cursor.getColumnIndex("ware"));
        cursor.close();
        EditText text=(EditText)findViewById(R.id.editText6);
        String andnum1=text.getText().toString();
        andnum1=andnum1.toUpperCase();
        //andnum1 = URLDecoder.decode(andnum1,"UTF-8");
        return co+"-"+ware+"-"+andnum1+"-"+dataos.line+dataos.list+".jpg";

    }
    /**保存图片*/
    public String saveBitmap(String fileName) {
        imagePathTP=getPicName();

        if (fileName.equals(""))fileName=imagePathLS+imagePathTP;

        else if(fileName.indexOf("芝麻开花")>0) {
            imagePathTP=fileName.substring(fileName.lastIndexOf("/")+1,fileName.length());
            imagePathLS=fileName.substring(0,fileName.lastIndexOf("/")+1);

            //System.out.println("555    "+imagePathLS+"   "+imagePathTP);
            return imagePathLS+imagePathTP;
        }


        //System.out.println("1111    "+imagePathTP);
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        options.inSampleSize = 2;
        Bitmap image=null;
        image = BitmapFactory.decodeFile(fileName,options);


        File f = new File(imagePathLS,imagePathTP);
        if (f.exists()) {
            f.delete();
        }
        //System.out.println(f.delete()+"  "+imagePathLS+imagePathTP);
        try {
            FileOutputStream out = new FileOutputStream(f);
            image.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        image.recycle();


        // 最后通知图库更新
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + f.getAbsolutePath())));

        return imagePathLS+imagePathTP;
    }


    /**按两两次退出*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (hc1==1){
                Toast.makeText(Second.this,"缓存图片必须点保存! ",Toast.LENGTH_LONG).show();
            }else finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
