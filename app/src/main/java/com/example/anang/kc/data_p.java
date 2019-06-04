package com.example.anang.kc;

/**
 * Created by Administrator on 2017/4/18.
 */
import cn.bmob.v3.BmobObject;

public class data_p extends BmobObject {
    private String p,co,PID;

    public void setData(String []address) {
        this.PID=address[0];
        this.co = address[1];
        this.p = address[2];

    }

    public String[] getData() {
        String address[]={this.PID,this.co,this.p};
        return address;
    }

}
