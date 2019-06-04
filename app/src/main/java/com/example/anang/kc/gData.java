package com.example.anang.kc;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/5/10.
 */

public class gData extends Application {

    private String callnum,mm,rname,add,root,date,code,state,objectId,band,Adv,Adv1;
    private String WeiXin="";
    private String kfmsg="";
    private int Gstate=0;
    private static final String VALUE = "kc";

    public void setGstate(int address) {
        this.Gstate=address;
    }
    public int getGstate() {
        return this.Gstate;
    }

    public void setAdv(String address) {
        this.Adv=address;
    }
    public String getAdv() {
        return this.Adv;
    }

    public String getName() {
        return this.rname;
    }

    public void setAdv1(String address) {
        this.Adv1=address;
    }
    public String getFefd() {
        return this.Adv1;
    }

    public void setWeiXin(String address) {
        this.WeiXin=address;
    }
    public String getWeiXin() {
        return this.WeiXin;
    }

    public void setObjectId(String address) {
        this.objectId=address;
    }
    public String getObjectId() {
        return this.objectId;
    }

    public void setRootData(String [] address) {
        this.callnum = address[0];
        this.mm = address[1];
        this.rname = address[2];
        this.add = address[3];
        this.root = address[4];
        this.date = address[5];
        this.code = address[6];
        this.state = address[7];
        this.band = address[8];
        if (address[9]!=null) this.kfmsg=address[9];
        else this.kfmsg="";
    }
    public void setRootData() {
        callnum="";
        mm="";
        rname="";
        add="";
        root="0";
        date="";
        code="";
        state="";
        objectId="";
        band="";
        kfmsg="";
    }
    public String[] getRootData() {
        return new String[]{callnum,mm,rname,add,root,date,code,state,band,kfmsg};
    }
    public void onCreate(){
        setRootData();
        initImagloader(getApplicationContext());
        super.onCreate();
    }

    private void initImagloader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "azm");// 获取到缓存的目录地址
        System.out.println(cacheDir.getPath());
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
}
