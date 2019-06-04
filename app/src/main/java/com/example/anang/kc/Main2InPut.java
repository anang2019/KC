package com.example.anang.kc;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main2InPut extends AppCompatActivity {
    private String qTime="",zTime="";
    private SQLiteDatabase db;
    private int roota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_in_put);
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        /**返回键*/
        ImageButton img=(ImageButton) findViewById(R.id.ic_back);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //System.out.println("2222");
                v.setVisibility(View.GONE);
                finish();
            }
        });

        RadioButton textbtn1 = (RadioButton) findViewById(R.id.radioButto3);
        textbtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                RadioButton b =(RadioButton) v;

                Button btn=(Button) findViewById(R.id.register_btn1Q);
                Button btn1=(Button) findViewById(R.id.register_btn1Z);

                if (b.isChecked()){
                    btn.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                } else{
                    btn.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.INVISIBLE);
                }
            }
        });

        Button btn=(Button) findViewById(R.id.register_btn1Q);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                new DatePickerDialog(Main2InPut.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        qTime=String.format("%d-%02d-%02d",i,i1+1,i2);
                    }
                },2017,6,1).show();
            }
        });

        Button btn1=(Button) findViewById(R.id.register_btn1Z);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String date1=sdf1.format(curDate);

                new DatePickerDialog(Main2InPut.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        zTime=String.format("%d-%02d-%02d",i,i1+1,i2);
                    }
                },2017,Integer.parseInt(date1)-1,1).show();
            }
        });



        gData app = (gData)getApplicationContext();
        String ls[]=app.getRootData();
        //System.out.println("ls正确111  "+ls[4]);
        roota=Integer.parseInt(ls[4]);
        if (roota>8) {
            btn1=(Button) findViewById(R.id.register_btnedit);
            btn1.setVisibility(View.VISIBLE);
            btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(Main2InPut.this, Main2EditInPut.class);
                    startActivity(intent);

            }
        });
        }
        btn1=(Button) findViewById(R.id.register_btncx);
        btn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setList();
            }
        });



        Spinner spinnerView = (Spinner) findViewById(R.id.s2pinner1);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0 &&position!=2 && roota<5) {
                            Spinner b= (Spinner) findViewById(R.id.s2pinner1);
                            b.setSelection(2);
                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2InPut.this);
                            builder.setMessage(getString(R.string.i1121))
                                    .setCancelable(false)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    }).show();

                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //System.out.println("111111");
                    }
                });

        setSX();
    }

    //筛选初始化
    public void setSX(){
        //公司
        Cursor cursor1 = db.rawQuery("select DISTINCT  co from people WHERE  co != ?",new String[]{""});
        int n=0;
        String ls[];
        ls=new String[cursor1.getCount()+1];
        ls[0]="全部公司";
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

        Spinner spinner1 = (Spinner) findViewById(R.id.s1pinner2);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Main2InPut.this,android.R.layout.simple_spinner_item, ls);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        Resources res = getResources();
        gData app = (gData)getApplicationContext();
        String ls1[]=app.getRootData();
        String[] titleData;
        System.out.println(" eeee"+ls1[9]);
        if (ls1[9].length()>1){
            //如果是库房管理员
            titleData=new String[1];
            titleData[0]=ls1[9];
        }else
            titleData= res.getStringArray (R.array.CangKu);
        String[] Data=new String[titleData.length+1];
        Data[0]="全部库房";
        for(int i=0;i<titleData.length;i++){
            Data[i+1]=titleData[i];
        }

        /**按库房*/
        spinner1 = (Spinner) findViewById(R.id.spinner41);
        adapter1=new ArrayAdapter<String>(Main2InPut.this,android.R.layout.simple_spinner_item, Data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        Spinner spinnerView = (Spinner) findViewById(R.id.spinner41);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0) {
                            Spinner spinner1 = (Spinner) findViewById(R.id.spinner41);
                            String num1 = spinner1.getSelectedItem().toString();
                            Cursor cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{num1});

                            int n=1;
                            String ls[];
                            ls=new String[cursor.getCount()+1];
                            ls[0]="全部";
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

                            spinner1 = (Spinner) findViewById(R.id.spinner51);
                            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Main2InPut.this,android.R.layout.simple_spinner_item, ls);
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

    public static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() + (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }

    //查看
    public void setList(){
        Cursor cursor;
        String where="";
        String wLs="";

        String andnum1[]={"","","",""};
        String wLs1[]={"","","",""};

        //交易类型


        Spinner spinnerView = (Spinner) findViewById(R.id.s2pinner1);
        int n=spinnerView.getSelectedItemPosition();
        if (roota<5)n=2;
        if(n>1){
            andnum1[0]="type=? ";
            wLs1[0]=Integer.toString(n-2);
        }
        //产地
         spinnerView = (Spinner) findViewById(R.id.s1pinner);
         n=spinnerView.getSelectedItemPosition();
        if(n>1){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[1]="Country=? ";
            wLs1[1]=num2;
        }
        //公司
        spinnerView = (Spinner) findViewById(R.id.s1pinner2);
        n=spinnerView.getSelectedItemPosition();
        if(n>0){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[2]="co=? ";
            wLs1[2]=num2;
        }
        //花色
        spinnerView = (Spinner) findViewById(R.id.s2pinner22);
        n=spinnerView.getSelectedItemPosition();
        if(n>1){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[3]="color=? ";
            wLs1[3]=num2;
        }

        boolean bn=false;
        for (int i=0;i<3;i++){
            if(andnum1[i] != ""){
                if (bn){
                    bn=false;
                    where+="and ";
                    wLs+=";";
                }
                where+=andnum1[i];
                wLs+=wLs1[i];
                if (andnum1[i+1] != "") {
                    where+="and ";
                    wLs+=";";
                }
                else bn=true;
            }
        }
        if (bn&&andnum1[3]!= ""){
            bn=false;
            where+="and ";
            wLs+=";";
        }
        where+=andnum1[3];
        wLs+=wLs1[3];

        //仓库
        String PID="";
        spinnerView = (Spinner) findViewById(R.id.spinner41);
        n=spinnerView.getSelectedItemPosition();
        String kfname=spinnerView.getSelectedItem().toString();
        if(n>0){
            if (where.length()>0){
                where+="and ";
                wLs+=";";
            }
            Spinner spinnerView1 = (Spinner) findViewById(R.id.spinner51);
            int n1=spinnerView1.getSelectedItemPosition();
            if (n1>0){
                cursor = db.rawQuery("select * from company WHERE co=? and ware = ?",new String[]{kfname,spinnerView1.getSelectedItem().toString()});
                if (cursor.getCount()>0){
                    cursor.moveToFirst();
                    PID=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
                    where+="PID=? ";
                    wLs+=PID;
                }
                cursor.close();
                //System.out.println("   222    "+spinnerView1.getSelectedItem().toString());
            }
            else{
                cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{kfname});
                if (cursor.getCount()>0){
                    where+="(PID in(";
                    int a=0;
                    if (cursor.moveToFirst()){
                        do{
                            if (a>0){
                                where+=",";
                                wLs+=";";
                            }
                            wLs+=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));

                            where+="?";
                            a++;
                        }while(cursor.moveToNext());
                    }
                    where+=") or ware=?)";
                    wLs+=";"+kfname;
                }
                cursor.close();
                //System.out.println("   333    "+spinnerView.getSelectedItem().toString());
            }
        }

        if (where.length()>0){
            where+=" and ";
            wLs+=";";
        }

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime=sdf1.format(curDate);

        RadioButton RadioButton1 = (RadioButton) findViewById(R.id.radioButto2);
        if(RadioButton1.isChecked()){
            where+="date>=? ";
            wLs+=nowTime;
        }else {
            RadioButton1 = (RadioButton) findViewById(R.id.radioButto3);
            if(RadioButton1.isChecked()){
                if (qTime.length()==0){
                    Toast.makeText(this,"请选择起始日期!",Toast.LENGTH_LONG).show();
                    return;
                }
                if (zTime.length()>0){
                    where+="(date>=? and date<=?)";
                    wLs+=qTime+";"+zTime;
                }else{
                    where+="date>=? ";
                    wLs+=qTime;
                }
            }else{
                wLs+=getDateStr(nowTime,-30);
                where+="date>=? ";
            }
        }

        //System.out.println(where+"\n"+wLs);
        String wLs2[]=wLs.split(";");
        cursor = db.rawQuery("select * from input WHERE "+where+" ORDER BY date DESC",wLs2);
        ListView mListView = (ListView)findViewById(R.id.l1istView);
        mListView.setAdapter(null);
        ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();
        int nn=0;

        if(cursor.moveToFirst()) {
            int in=0,put=0;

            do {

                int PID1 =cursor.getInt(cursor.getColumnIndex("PID"));
                String num=cursor.getString(cursor.getColumnIndex("num"));
                String ntype="入货";
                int type=cursor.getInt(cursor.getColumnIndex("type"));
                if (type==1){
                    ntype="出售";
                    put+=Integer.parseInt(num);
                }else if(type==0){
                    in+=Integer.parseInt(num);
                }else continue;

                String date=cursor.getString(cursor.getColumnIndex("date"));
                String andnum=cursor.getString(cursor.getColumnIndex("andnum"));
                if (andnum.equals(""))continue;
                nn++;
                String Country=cursor.getString(cursor.getColumnIndex("Country"));

                String color=cursor.getString(cursor.getColumnIndex("color"));

                String Price=cursor.getString(cursor.getColumnIndex("Price"));
                String buy=cursor.getString(cursor.getColumnIndex("buy"));
                String add=cursor.getString(cursor.getColumnIndex("address"));
                String buyp=cursor.getString(cursor.getColumnIndex("buyp"));

                String co=cursor.getString(cursor.getColumnIndex("co"));
                String people=cursor.getString(cursor.getColumnIndex("people"));
                String pp="";
                String[] strings1 = people.split(",");
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

                String co2="";
                String ware="";
                if (PID1==0)ware=cursor.getString(cursor.getColumnIndex("ware"));
                else{
                    Cursor cursor1 = db.rawQuery("select * from company WHERE PID = ?",new String[]{Integer.toString(PID1)});
                    if(cursor1.moveToFirst()){
                        co2=cursor1.getString(cursor1.getColumnIndex("co"));
                        ware=cursor1.getString(cursor1.getColumnIndex("ware"));
                    }

                    cursor1.close();
                }

                String dated[]=date.split(" ");

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("PiaoHao", ntype);
                map.put("ChanDi", dated[0]);
                map.put("CangKu", co2+ware);
                map.put("ChuWei", andnum);
                map.put("GongSi", Country);
                map.put("BeiZhu", color);
                map.put("PiaoHao1", num);
                map.put("ChanDi1", co);
                map.put("CangKu1", pp);
                map.put("ChuWei1", Price);
                map.put("GongSi1", buy);
                map.put("GongSi12", add);
                map.put("BeiZhu1", buyp);
                item.add(map);
            }while(cursor.moveToNext());

            //创建简单适配器SimpleAdapter
            SimpleAdapter simpleAdapter = new SimpleAdapter(Main2InPut.this, item, R.layout.item2,
                    new String[] {"PiaoHao","ChanDi", "CangKu", "ChuWei", "GongSi", "BeiZhu",
                            "PiaoHao1","ChanDi1", "CangKu1", "ChuWei1", "GongSi1","GongSi12", "BeiZhu1"},
                    new int[] {R.id.PiaoHao, R.id.ChanDi, R.id.CangKu, R.id.ChuWei, R.id.GongSi, R.id.BeiZhu,
                            R.id.PiaoHao1, R.id.ChanDi1, R.id.CangKu1, R.id.ChuWei1, R.id.GongSi1, R.id.GongSi12, R.id.BeiZhu1});

            //加载SimpleAdapter到ListView中
            mListView.setAdapter(simpleAdapter);
            TextView text = (TextView) findViewById(R.id.textin);
            text.setText(Integer.toString(in)+"吨");
            text = (TextView) findViewById(R.id.textput);
            text.setText(Integer.toString(put)+"吨");
        }
        Toast.makeText(this,"共查询到 "+nn+" 个结果",Toast.LENGTH_SHORT).show();
        cursor.close();


    }

}
