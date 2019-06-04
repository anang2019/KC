package com.example.anang.kc;


/**
 * Created by Administrator on 2017/4/18.
 */
import cn.bmob.v3.BmobObject;

public class data_co extends BmobObject {
    private String PID,line,list,ware,co;

    public void setData(String []address) {
        this.PID = address[0];
        this.co = address[1];
        this.ware = address[2];
        this.line = address[3];
        this.list = address[4];

    }


    public String[] getData() {
        String address[]={this.PID,this.co,this.ware,this.line,this.list};
        return address;
    }

}