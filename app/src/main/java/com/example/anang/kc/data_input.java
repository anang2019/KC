package com.example.anang.kc;

/**
 * Created by Administrator on 2017/7/26.
 */


import cn.bmob.v3.BmobObject;



public class data_input extends BmobObject {

    private String id,PID,ware,type,date,andnum,Country,color,num,co,people,Price,buy,address,buyp;

    public void setData(String address[]) {
        this.PID = address[0];
        this.ware = address[1];
        this.type = address[2];
        this.Country = address[3];
        this.color = address[4];
        this.num = address[5];
        this.co = address[6];
        this.people = address[7];
        this.Price = address[8];
        this.buy = address[9];
        this.address = address[10];
        this.buyp = address[11];
        this.andnum = address[12];
        this.date = address[13];
    }

    public String[] getData() {
        //System.out.println("bmob新 "+ware);
        String[] dataos={
                PID,ware,type,Country,color,num,co,people,Price,buy,address,buyp,andnum,date
        };
        return dataos;
    }


}