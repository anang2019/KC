package com.example.anang.kc;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/5/9.
 */

public class user extends BmobObject {
    private String callnum,mm,rname,add,root,date,code,state,band,kfmsg;


    public void setData(String [] address) {
        this.callnum = address[0];
        this.mm = address[1];
        this.rname = address[2];
        this.add = address[3];
        this.root = address[4];
        this.date = address[5];
        this.code = address[6];
        this.state = address[7];
        this.band = address[8];
    }

    public String[] getData() {
        return new String[]{callnum,mm,rname,add,root,date,code,state,band,kfmsg};
    }
}
