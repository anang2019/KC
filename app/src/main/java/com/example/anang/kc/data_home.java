package com.example.anang.kc;


import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/4/16.
 */

public class data_home extends BmobObject {

    private String pos,andnum,Country,num,co,people,Remarks,posfull,Star,PID,line,list,pic,date,editer;
    private String picadd="";
    //private BmobFile img;
    public void setData(dataOS address) {
        this.PID = address.PID;
        this.line = address.line;
        this.list = address.list;
        this.pos = address.pos;
        this.andnum = address.andnum;
        this.Country = address.Country;
        this.num = address.num;
        this.posfull = address.posfull;
        this.Star = address.Star;
        this.co = address.co;
        this.people = address.people;
        this.Remarks = address.Remarks;
        this.pic = address.pic;
        this.date = address.date;
    }

    public String getPic() {
        return this.picadd;
    }
    /*public BmobFile getImg() {
        return this.img;
    }
    public BmobFile setImg() {
        return this.img;
    }*/
    public void setName(String Pic) {
        this.editer=Pic;
    }

    public void setPic(String Pic) {
        this.picadd=Pic;
    }
    public dataOS getData() {
        dataOS dataos=new dataOS();

        dataos.PID = PID;
        dataos.line = line;
        dataos.list = list;
        dataos.pos = pos;
        dataos.andnum = andnum;
        dataos.Country = Country;
        dataos.num = num;
        dataos.posfull = posfull;
        dataos.Star = Star;
        dataos.co = co;
        dataos.people = people;
        dataos.Remarks = Remarks;
        dataos.pic=pic;
        dataos.date=date;
        return dataos;
    }

}
