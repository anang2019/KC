package com.example.anang.kc;


/**
 * Created by Administrator on 2017/3/27.
 *
 *
 * company  公司   库房  库房PID  行数   列数
 *          co  ware    PID line    list
 * house    库房PID    行   列   储位  提单号 产地  数量  数量比   星级  货商  联系人   是否有图片  入货日期 备注
 *          PID    line list    pos  andnum Country num  posfull Star    co  people pic date  Remarks
 * people   货商  联系人 PID
 *          co  p  PID
 */

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteDatabase.CursorFactory;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.provider.Settings;
        import android.widget.Toast;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_BOOK="create table company ("
            + "co text, "
            + "ware text, "
            + "PID integer primary key autoincrement,"
            + "line integer, "
            + "list integer)";
    public static final String CREATE_P="create table people ("
            + "co text, "
            + "p text, "
            + "PID integer primary key autoincrement)";

    public static final String WareHouse="create table house ("
            + "PID integer, "
            + "line integer, "
            + "list integer, "
            + "pos text, "
            + "andnum text, "
            + "Country text, "
            + "num text, "
            + "posfull integer, "
            + "Star text, "
            + "co text,"
            + "people text, "
            + "pic text, "
            + "date text, "
            + "Remarks text)";
    public static final String WareHouse1="create table input ("
            + "id  integer, "
            + "PID integer, "
            + "ware text, "
            + "type integer, "
            + "date text, "
            + "andnum text, "
            + "Country text, "
            + "color text, "
            + "num text, "
            + "co text, "
            + "people text, "
            + "Price text, "
            + "buy text, "
            + "address text, "
            + "buyp text, "
            + "pic text)";

    private Context context;

    public MyDatabaseHelper(Context context, String name,
                            CursorFactory factory, int version) {
        super(context, name, factory, version);
        // TODO Auto-generated constructor stub
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        //db.execSQL(CREATE_BOOK);
        //db.execSQL(WareHouse);
        //db.execSQL(CREATE_P);
        //copyFile("/data/data/com.example.anang.kc/databases/"+context.getString(R.string.dataku));

        Toast.makeText(context, "创建数据库成功!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        //db.execSQL("drop table if exists company");
        //db.execSQL("drop table if exists house");
        //db.execSQL("drop table if exists people");
        //onCreate(db);
        //System.out.println("数据库 "+oldVersion);
        switch(oldVersion){
            case 11:
                db.execSQL("alter table house add column pic text");
                db.execSQL("alter table house add column date text");
            case 12:
                //System.out.println(111111);
                db.execSQL(WareHouse1);

            default:break;
        }

        /**
        switch(oldVersion){
            case 10:
                //建临时表,复制数据
                db.execSQL("create table _temp as select PID,line,list,pos,andnum,Country,num,posfull,co,people,Remarks from house");
                //删除原表
                db.execSQL("drop table house");
                 //改名原表
                db.execSQL("alter table  _temp rename to house");
                //增加列
                db.execSQL("ALTER TABLE house add column Star text");

            default:break;
        }
        */
    }


}