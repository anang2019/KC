package com.example.anang.kc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/5/7.
 */

public class dataOS {
    public String PID,line,list,pos,andnum,Country,num,posfull,Star,co,people,Remarks,pic,date;
    public int Rposfull=0;
    public dataOS(Cursor cursor){
        PID=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
        line=Integer.toString(cursor.getInt(cursor.getColumnIndex("line")));
        list=Integer.toString(cursor.getInt(cursor.getColumnIndex("list")));
        pos=cursor.getString(cursor.getColumnIndex("pos"));
        andnum=cursor.getString(cursor.getColumnIndex("andnum"));
        Country=cursor.getString(cursor.getColumnIndex("Country"));
        num=cursor.getString(cursor.getColumnIndex("num"));
        posfull=Integer.toString(cursor.getInt(cursor.getColumnIndex("posfull")));
        Star=cursor.getString(cursor.getColumnIndex("Star"));
        co=cursor.getString(cursor.getColumnIndex("co"));
        people=cursor.getString(cursor.getColumnIndex("people"));
        Remarks=cursor.getString(cursor.getColumnIndex("Remarks"));
        if (Star==null)Star="";
        pic=cursor.getString(cursor.getColumnIndex("pic"));
        if (pic==null)pic="";
        date=cursor.getString(cursor.getColumnIndex("date"));
        if (date==null)date="";
    }
    public dataOS(int pid,int x,int y){
        PID=Integer.toString(pid);
        line=Integer.toString(x);
        list=Integer.toString(y);
        pos="";
        andnum="";
        Country="";
        num="";
        posfull="";
        Star="";
        co="";
        people="";
        Remarks="";
        pic="";
        date="";
    }
    public void setforarray(String [] ls){
        if (ls.length<14) {
            System.out.println("dataOS数组初始化失败!");
            return;
        }
        PID =ls[0];
        line = ls[1];
        list = ls[2];
        pos = ls[3];
        andnum = ls[4];
        Country = ls[5];
        num = ls[6];
        posfull = ls[7];
        Star = ls[8];
        co = ls[9];
        people = ls[10];
        Remarks = ls[11];
        pic=ls[12];
        date=ls[13];
    }
    public dataOS(String str){
        String ls1[]=str.split("\\|");
        String ls[]=new String[14];

        for (int i=0;i<ls.length;i++){
            if (i<ls1.length)ls[i]=ls1[i];
            else ls[i]="";
        }
        setforarray(ls);
    }
    public dataOS(String [] ls){
        setforarray(ls);
    }
    public dataOS(){

    }
    public void savedata(SQLiteDatabase db){
        String [] in={PID,line,list} ;

        Cursor cursor1 = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?",in);
        if (cursor1.getCount()>0){
            String[]data1={pos,andnum,Country,num,posfull,Star,co,people,Remarks,pic,date,in[0],in[1],in[2]};

            db.execSQL("update house set pos =?,andnum =?, Country =?,num =?,posfull =?,Star =?,co =?,people =?,Remarks =? " +
                    ",pic=?,date =? WHERE PID = ? and line=? and list=?", data1);
        }else {
            String[]data1={in[0],in[1],in[2],pos,andnum,Country,num,posfull,Star,co,people,Remarks,pic,date};
            db.execSQL("insert into house(PID,line,list,pos,andnum,Country,num,posfull,Star,co,people,Remarks,pic,date) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",data1);
        }
        cursor1.close();
    }
    public String settoString(){
        /**加入联系人
        String peoplestring="";
        if (peoplelist!=null&&peoplelist.length>0){
            for (int i=0;i<peoplelist.length;i++) {
                if(peoplelist[i].length()>0) {
                    if (i > 0) peoplestring += ",";
                    peoplestring += peoplelist[i];
                }
            }
            peoplestring+="@";
        } else peoplestring=people;
        //System.out.println(peoplestring);
         */
        String tt=PID+"|"+line+"|"+list+"|"+pos+"|"+andnum+"|"+Country+"|"+num+"|"+posfull +"|"
                +Star +"|"+co+"|"+people+"|"+Remarks+"|"+pic+"|"+date;

        return tt;
    }
}
