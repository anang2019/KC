package com.example.anang.kc;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/5/10.
 */

public class SharedPre {

    public String callnum,mm,rname,add,root,date,code,state,band;
    private Context ContextM;
    private SharedPreferences sp;
    public SharedPre(Context context,String [] address) {
        ContextM=context;
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
    public SharedPre(Context context ) {
        ContextM=context;
        sp = ContextM.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        this.callnum = sp.getString("USER_NAME", "");
        this.mm = sp.getString("PASSWORD", "");
        this.rname =sp.getString("R_NAME", "");
        this.add = sp.getString("ADD", "");
        this.root = sp.getString("ROOT", "");
        this.date = sp.getString("DATE", "");
        this.code = sp.getString("USER_CODE", "");
        this.state = sp.getString("USER_STATE", "");
        this.band="";
    }
    public void saveData(String [] data3) {
        sp = ContextM.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", data3[0]);
        editor.putString("PASSWORD", data3[1]);
        editor.putString("R_NAME", data3[2]);
        editor.putString("ADD", data3[3]);
        editor.putString("ROOT",data3[4]);
        editor.putString("DATE",data3[5]);
        editor.putString("USER_CODE", data3[6]);
        editor.putString("USER_STATE", data3[7]);
        editor.commit();
    }
    public void saveData() {
        sp = ContextM.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", this.callnum);
        editor.putString("PASSWORD", this.mm);
        editor.putString("R_NAME", this.rname);
        editor.putString("ADD", this.add);
        editor.putString("ROOT",this.root);
        editor.putString("DATE",this.date);
        editor.putString("USER_CODE", this.code);
        editor.putString("USER_STATE", this.state);
        editor.commit();
    }
}
