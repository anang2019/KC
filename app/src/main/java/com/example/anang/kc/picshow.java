package com.example.anang.kc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;


import uk.co.senab.photoview.PhotoView;

public class picshow extends AppCompatActivity {
    //protected ImageView imageView;
    protected String data,data1;
    private SQLiteDatabase db;
    protected Bitmap bmp;
    protected int status;
    protected ProgressBar bar;
    protected bmobData bm;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    //upTimeText=msg.obj.toString();
                    Intent intent = new Intent();
                    intent.putExtra("data_return", msg.obj.toString());
                    setResult(RESULT_OK, intent);
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp=null;
                    Toast.makeText(picshow.this,"上传成功!",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 2:
                    Button btn=(Button) findViewById(R.id.buttonpicdo);
                    btn.setEnabled(true);
                    btn.setText("上传");
                    break;
                case 3:
                    nextSet();
                    break;
                case 4:
                    nextSet();
                    break;
                case 5:
                    nextSet();
                    break;
                case 6:

                    TextView text=(TextView) findViewById(R.id.hcts);
                    text.setText(msg.obj.toString());
                    break;
                case 111:
                    bar.setProgress(status);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picshow);
        Intent intent=getIntent();
        data =intent.getStringExtra("extra_data");
        data1 =intent.getStringExtra("extra_data1");
        String data2 =intent.getStringExtra("extra_data2");
        bar=(ProgressBar) findViewById(R.id.bar);
        bar.setVisibility(View.GONE);
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(picshow.this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        if (data2!=null&&data2.length()>10){
            LinearLayout yout1=(LinearLayout) findViewById(R.id.yout1);
            yout1.setVisibility(View.GONE);
            netImage(data2);

        }else init();

        /**返回键*/
        ImageButton img=(ImageButton) findViewById(R.id.ic_back);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                if (bmp != null && !bmp.isRecycled()) {
                    //System.out.println("333     ");
                    bmp.recycle();
                }
                bmp=null;
                finish();
            }
        });

        /**上传按钮*/
        Button btn=(Button) findViewById(R.id.buttonpicdo);
        //btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File file = new File(data);
                if (!file.exists()) {
                    Toast.makeText(picshow.this,"文件错误,请重新拍摄!",Toast.LENGTH_LONG).show();
                    return;
                }
                Button btn =(Button)v;
                bmobData bm=new bmobData(picshow.this,mHandler);
                bm.setPic(data);
                btn.setText("上传中");
                btn.setEnabled(false);
                bar.setVisibility(View.VISIBLE);
            }
        });
        /**缓存 按钮*/
        btn=(Button) findViewById(R.id.buttonpicdo1);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String ls[]=data1.split(",");

                dataOS dataos=new dataOS();
                dataos.PID=ls[0];
                dataos.line=ls[1];
                dataos.list=Integer.toString(Integer.parseInt(ls[2])+200);
                dataos.Remarks=ls[3];
                dataos.savedata(db);
                status=getNum();
                if (status>0){
                    TextView text=(TextView) findViewById(R.id.hcts);
                    text.setText("共有缓存:"+status+" 条");
                    Intent intent = new Intent();
                    intent.putExtra("data_return", "");
                    setResult(RESULT_OK, intent);
                    if (bmp != null && !bmp.isRecycled()) {
                        bmp.recycle();
                    }
                    bmp=null;
                    Toast.makeText(picshow.this,"共有缓存:"+status+" 条",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        /**批量 按钮*/
        btn=(Button) findViewById(R.id.buttonpicdo2);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                status=getNum();
                if (status>0){
                    bar.setVisibility(View.VISIBLE);
                    bar.setIndeterminate(false);
                    bm.setPic(db);
                }
            }
        });
    }

    private void init(){
        bm=new bmobData(this,mHandler);
        status=getNum();
        if (status>0){
            TextView text=(TextView) findViewById(R.id.hcts);
            text.setText("共有缓存:"+status+" 条");
        }
        if (!data.equals("")) {
            PhotoView imgControl1=(PhotoView)findViewById(R.id.imageView2);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds = false;

            //options.inSampleSize = 4;
            bmp=null;
            bmp = BitmapFactory.decodeFile(data,options);
            imgControl1.setImageBitmap(bmp);


            Button btn=(Button) findViewById(R.id.buttonpicdo2);
            btn.setEnabled(false);

        }else{
            Button btn=(Button) findViewById(R.id.buttonpicdo1);
            btn.setEnabled(false);

            btn=(Button) findViewById(R.id.buttonpicdo);
            btn.setEnabled(false);
        }


    }

    private void netImage(String ls) {
        ImageLoader loader = ImageLoader.getInstance();

        PhotoView imgControl1=(PhotoView)findViewById(R.id.imageView2);

        loader.displayImage(ls, imgControl1);
    }

    /**上传一个后*/
    private void nextSet(){
        int b=getNum();
        TextView text=(TextView) findViewById(R.id.hcts);
        bar.setProgress((status-b)*100/status);
        //System.out.println("666555  "+b*100/status);
        if (b>0) {
            text.setText("正在上传剩余: "+(status-b)+" 条");
            bm.setPic(db);
        }
        else {
            Button btn=(Button) findViewById(R.id.buttonpicdo2);
            btn.setEnabled(true);
            bm.DownData(db);
            text.setText("上传完成");
        }
    }

    /**返回缓存条数*/
    private int getNum(){
        Cursor cursor1 = db.rawQuery("select * from house WHERE list>?",new String[]{"200"});
        int a=cursor1.getCount();
        cursor1.close();
        return a;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (bmp != null && !bmp.isRecycled()) {
            //System.out.println("333     ");
            bmp.recycle();
        }
        //System.out.println("222     "+imgControl);

        bmp=null;

        finish();
        return super.onKeyDown(keyCode, event);
    }
}

