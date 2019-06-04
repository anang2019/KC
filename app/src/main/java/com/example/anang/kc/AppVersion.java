package com.example.anang.kc;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
/**
 * Created by Administrator on 2017/6/5.
 */

public class AppVersion extends BmobObject  {
    private String version="";
    private String update_log="";
    private BmobFile path;
    private boolean isforce;

    public String getAll(){
        String tt="0";
        if (isforce)tt="1";
        tt+="|"+version+"|"+update_log;
        if (path!=null)tt+="|"+path.getFileUrl();
        return tt;
    }
    public float getVersion(){
        return Float.parseFloat(version);
    }
    public String getLs(){
        return path.getFileUrl();
    }
    public boolean getIsforce(){
        return isforce;
    }
    public BmobFile getPath(){
        return path;
    }
}
