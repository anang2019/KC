package com.example.anang.kc;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Quotation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        /**返回键*/
        ImageButton img=(ImageButton) findViewById(R.id.ic_back);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //System.out.println("2222");
                v.setVisibility(View.GONE);
                finish();
            }
        });

        try{
            String dod=testGetHtml("http://www.woaizhima.com/zx/type/hqfx");
            //String dod="青岛港进口芝麻价格行情hhhhtarget";
            //System.out.println("2222   "+dod);


            //dod=subAB(dod,"青岛港进口","\">");

            dod=subAB(dod,"青岛港","\">");

            //System.out.println("333   "+dod);
            dod=subAB(dod,"\"","");
            //System.out.println("333   "+dod);
            dod=testGetHtml("http://www.woaizhima.com"+dod);

            //dod=subAB(dod,"青岛港进口芝麻价格行情","<a href=");


            TextView btn=(TextView) findViewById(R.id.qt);
            //System.out.println("111   "+dod);
            String ntime=subAB(dod,"h3","青");
            //System.out.println("222   "+ntime);
            if(ntime.length()>0){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String date1=sdf1.format(curDate);
                ntime=subAB(ntime,".","");
                btn.setText(date1+ntime+"日");
            }

            dod=subAB(dod,"Description\"","/");
            dod=subAB(dod,"\"","\"");
            //System.out.println("333   "+dod);
            if(dod.length()>0){
                dod=dod.replace("，","\n");
                btn=(TextView) findViewById(R.id.qt1);
                btn.setText(dod);
            }

            /**
            String ls[]=dod.split("\">");
            String tt="";
            if (ls.length>0){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy年MM月");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String date1=sdf1.format(curDate);

                btn.setText(date1+subAB(ls[1],"","<"));
                for (int i=2;i<ls.length-1;i++){
                    //System.out.println(subAB(ls[i],"","<"));
                    tt+=subAB(ls[i],"","<")+"\n";
                }
                btn=(TextView) findViewById(R.id.qt1);
                btn.setText(tt);
            }else btn.setText("信息刷新失败!");
             */


        }catch(Exception e){
            System.out.println("2222"+e.toString());

        }
    }

    public static byte[] readStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public String testGetHtml(String urlpath) throws Exception {
        URL url = new URL(urlpath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(6 * 1000);
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            byte[] data = readStream(inputStream);
            String html = new String(data);
            return html;
        }
        return null;
    }

    private String subAB(String text,String aa,String bb){
        int a=text.indexOf(aa);
        if (a<0)a=0;
        else a+=aa.length();
        //System.out.println(a);
        int b;
        if (bb.equals(""))b=text.length();
        else b=text.indexOf(bb,a);

        if (b<0)b=text.length();
        //System.out.println(b+"  "+text.length());
        return text.substring(a,b);
    }
}
