package com.example.anang.kc;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;

import java.util.Date;

public class Main2EditInPut extends AppCompatActivity {
    private SQLiteDatabase db;
    private ArrayAdapter<String> adapter1;
    private MultiAutoCompleteTextView mc;
    private String[] res;
    private String[] nameP;
    private String zTime;
    private String upS[];
    private bmobData bm;
    private Button saveBtn;
    private boolean isS;
    public Handler mHandler=new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 201:
                    finish();
                    break;
                case 202:
                    saveBtn.setEnabled(true);
                    saveBtn.setText("保存");
                    Toast.makeText(Main2EditInPut.this,"未保存成功,请重新保存!",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_edit_in_put);

        zTime="";
        //键盘默认不弹出
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        EditText editText = (EditText) findViewById(R.id.e1ditText6);
        String digists = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        editText = (EditText) findViewById(R.id.e1ditText7);
        digists = "0123456789.";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        bm = new bmobData(Main2EditInPut.this,mHandler);
        setLXR();
        SetPP();

        Spinner spinnerView = (Spinner) findViewById(R.id.n1oberbi1);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0) {
                            EditText text = (EditText) findViewById(R.id.e1ditText4);
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

        Button btn1=(Button) findViewById(R.id.register_btQ);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                int m=Integer.parseInt(sdf1.format(curDate))-1;
                //System.out.println("日期 "+m);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");

                int y=Integer.parseInt(sdf2.format(curDate));
                //System.out.println("日期 "+y+" "+m);
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd");
                int d=Integer.parseInt(sdf3.format(curDate));
                //System.out.println("日期 "+y+" "+m+" "+d);
                new DatePickerDialog(Main2EditInPut.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        zTime=String.format("%d-%02d-%02d",i,i1+1,i2);
                        Button b = (Button) findViewById(R.id.register_btQ);
                        b.setText(zTime);

                    }
                },y,m,d).show();
            }
        });
        RadioButton textbtn1 = (RadioButton) findViewById(R.id.radioButto1);
        textbtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                LinearLayout textbtn1 = (LinearLayout) findViewById(R.id.LinearLayoutw);

                textbtn1.setVisibility(View.INVISIBLE);
            }
        });
        textbtn1 = (RadioButton) findViewById(R.id.radioButto2);
        textbtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                LinearLayout textbtn1 = (LinearLayout) findViewById(R.id.LinearLayoutw);

                textbtn1.setVisibility(View.VISIBLE);
            }
        });
        setKF();
        //保存按钮
        saveBtn=(Button) findViewById(R.id.b1utton2);
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveButtonC();
            }
        });
    }

    /**按库房*/
    protected void setKF()
    {
        Resources res1 = getResources();

        gData app = (gData)getApplicationContext();
        String ls1[]=app.getRootData();
        String[] titleData;
        System.out.println(" eeee"+ls1[9]);
        if (ls1[9].length()>1){
            //如果是库房管理员
            titleData=new String[1];
            titleData[0]=ls1[9];
        }else
            titleData= res1.getStringArray (R.array.CangKu);
        String[] Data=new String[titleData.length+1];
        Data[0]="库房";
        for(int i=0;i<titleData.length;i++){
            Data[i+1]=titleData[i];
        }

        Spinner spinner1 = (Spinner) findViewById(R.id.s1pinner4);
        adapter1=new ArrayAdapter<String>(Main2EditInPut.this,android.R.layout.simple_spinner_item, Data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);


        Spinner spinnerView = (Spinner) findViewById(R.id.s1pinner4);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0) {
                            Spinner spinner1 = (Spinner) findViewById(R.id.s1pinner4);
                            String num1 = spinner1.getSelectedItem().toString();
                            Cursor cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{num1});

                            int n=1;
                            String ls[];
                            ls=new String[cursor.getCount()+1];
                            ls[0]="";
                            if(cursor.moveToFirst()){
                                do{
                                    String people1 = cursor.getString(cursor.getColumnIndex("ware"));
                                    if (people1.length()>0)
                                    {
                                        ls[n]=people1;
                                        n++;
                                    }
                                }while(cursor.moveToNext());
                            }
                            cursor.close();

                            spinner1 = (Spinner) findViewById(R.id.s1pinner5);
                            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Main2EditInPut.this,android.R.layout.simple_spinner_item, ls);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //绑定 Adapter到控件
                            spinner1 .setAdapter(adapter1);

                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //System.out.println("111111");
                    }
                });

        //如果是库房管理员
        if (titleData.length==1){
            spinnerView.setSelection(1);
            spinnerView.setEnabled(false);
        }
    }
    protected void setLXR()
    {
        TableLayout layout=(TableLayout) this.findViewById(R.id.t1ableLayout11);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        int ww=0;
        lp1.setMargins(ww,ww,ww,ww);

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
                        EditText text=(EditText)findViewById(R.id.e1ditText2);
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
                        Spinner spinner1 = (Spinner) findViewById(R.id.n1oberbi1);
                        adapter1=new ArrayAdapter<String>(Main2EditInPut.this,android.R.layout.simple_spinner_item, mItems1);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //绑定 Adapter到控件
                        spinner1 .setAdapter(adapter1);

                        text=(EditText)findViewById(R.id.e1ditText4);
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
    }

    public void CountryClick1(View v) {
        RadioButton b =(RadioButton) v;
        EditText text=(EditText)findViewById(R.id.e1ditText5);
        text.setText(b.getText());
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
                        nameP[n]=ls[0];
                        res[n]="1"+ls[1];
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

        mc=(MultiAutoCompleteTextView)findViewById(R.id.e1ditText4);
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

    /**保存按钮事件*/
    public void saveButtonC(){
        if (zTime.length()<5){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2EditInPut.this);
            builder.setMessage("请选择交易日期!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();
            return;
        }
        //交易类型
        RadioButton btt=(RadioButton)findViewById(R.id.radioButto1);
        int ntype=1;
        if (btt.isChecked())ntype=0;
        String color="";
        btt=(RadioButton)findViewById(R.id.r1adioButton10);
        if (btt.isChecked())color="花";
        btt=(RadioButton)findViewById(R.id.r1adioButton11);
        if (btt.isChecked())color="白";


        //票号
        EditText text=(EditText)findViewById(R.id.e1ditText6);
        String andnum1=text.getText().toString();
        andnum1=andnum1.toUpperCase();
        if (andnum1.length()<=0){
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2EditInPut.this);
            builder.setMessage("票号为必添项!")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();
            return;
        }

        if(isS && upS!=null&&upS.length>10){
            saveBtn.setEnabled(true);
            bm.UpDataInput(upS);

            return;
        }
        //产地
        text=(EditText)findViewById(R.id.e1ditText5);
        String Country1=text.getText().toString();

        //库房
        Spinner spinner1 = (Spinner) findViewById(R.id.s1pinner4);
        String posfull1="";
        int pid=0;
        if(spinner1.getSelectedItemPosition()>0) {
            posfull1 = spinner1.getSelectedItem().toString();
            //Log.d("Second","rrrrrrrr  "+spinner1.getSelectedItem());
            Spinner spinner=(Spinner)findViewById(R.id.s1pinner5);
            if(spinner.getSelectedItemPosition()>0) {
                String posfull2 = spinner.getSelectedItem().toString();
                Cursor cursor1 = db.rawQuery("select * from company WHERE co=? and ware = ?", new String[]{posfull1,posfull2});
                if (cursor1.moveToFirst()){
                    pid=cursor1.getInt(cursor1.getColumnIndex("PID"));
                }
                cursor1.close();
                if (pid>0)posfull1="";
            }
        }

        //数量
        text=(EditText)findViewById(R.id.e1ditText7);
        String num1=text.getText().toString();

        //单价
        text=(EditText)findViewById(R.id.e1ditText3);
        String price=text.getText().toString();

        //买方联系人
        text=(EditText)findViewById(R.id.editText33);
        String buy=text.getText().toString();

        //买方电话
        text=(EditText)findViewById(R.id.editText41);
        String buyp=text.getText().toString();

        //买方地点
        text=(EditText)findViewById(R.id.editText331);
        String add=text.getText().toString();
        //公司
        text=(EditText)findViewById(R.id.e1ditText2);
        String co1=text.getText().toString();

        //联系人
        text=(EditText)findViewById(R.id.e1ditText4);
        String pp1=text.getText().toString();

        String ppp="";


        if (pp1.length()>0){
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
        String msg="";
        if (Country1.length()==0)msg+="品种、 ";
        if (color.length()==0)msg+="花色、 ";
        if (num1.length()==0)msg+="数量、 ";
        if (co1.length()==0)msg+="公司、 ";
        if (posfull1.length()==0 && pid==0)msg+="库房、 ";
        if (ntype==1){
            if (price.length()==0)msg+="\n单价、 ";
            if (buy.length()==0)msg+="买家、 ";
            if (add.length()==0)msg+="买家地点、 ";
            if (buyp.length()==0)msg+="买家电话、 ";
        }

        //Cursor cursor = db.rawQuery("select * from input",null);
        //int id=cursor.getCount()+1;
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime=sdf1.format(curDate);

        upS=new String []{Integer.toString(pid),posfull1,Integer.toString(ntype),
                Country1,color,num1,co1,ppp,price,buy,add,buyp,andnum1,zTime+" "+nowTime};
        if (msg.length()>0){
            msg+="\n以上未添加,是否继续保存?\n选否重新编辑!";
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2EditInPut.this);
            builder.setTitle("以下信息未填写").setMessage(msg).setCancelable(false)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.execSQL("insert into input(PID,ware,type,Country,color,num,co,people,Price,buy,address,buyp,andnum,date) " +
                                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",upS);
                            //finish();
                            isS=true;
                            saveBtn.setEnabled(false);
                            saveBtn.setText("上传中");
                            bm.UpDataInput(upS);

                        }
                    }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
            }).show();
            //return;
        }else{
            db.execSQL("insert into input(PID,ware,type,Country,color,num,co,people,Price,buy,address,buyp,andnum,date) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",upS);
            isS=true;
            saveBtn.setEnabled(false);
            saveBtn.setText("上传中");
            bm.UpDataInput(upS);
        }
        //String upS1[]={strings[0],strings[1],strings[2],posfull1,andnum1,Country1,pos1,num1,co1,ppp,Remarks1,star};
    }


}
