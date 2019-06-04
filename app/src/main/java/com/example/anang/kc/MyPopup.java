package com.example.anang.kc;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


/**
 * Created by Administrator on 2017/5/6.
 */

public class MyPopup extends PopupWindow {
    private View conentView;
    private dataOS dataos;
    private SQLiteDatabase db;
    private Context ContextM;
    private int btnid;
    public interface OnCallListener {
        public void onCall(int sing);
    }
    protected ProgressBar bar;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    Toast.makeText(ContextM,"图片下载完成!",Toast.LENGTH_SHORT).show();
                    bar.setProgress(100);
                    // 最后通知图库更新
                    File file=new File(msg.obj.toString());
                    ContextM.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
                    /*
                    File file=new File(msg.obj.toString());
                    gData app = (gData)ContextM.getApplicationContext();
                    String ls[]=app.getRootData();
                    if(Integer.parseInt(ls[4])>7){
                    }
                    Intent intent =new Intent(Intent.ACTION_VIEW);
                    Uri mUri = Uri.parse("file://"+file.getPath());
                    intent.setDataAndType(mUri, "image/*");
                    ContextM.startActivity(intent);
                    */
                    break;
                case 2:
                    TextView v=(TextView) conentView.findViewById(R.id.bar1t);
                    v.setText("查看图片");
                    v.setEnabled(true);
                    bar.setProgress(0);
                    break;
                case 111:
                    bar.setProgress(Integer.parseInt(msg.obj.toString()));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void setOnCallListener(final OnCallListener listener) {
        LinearLayout tab=(LinearLayout)conentView.findViewById(R.id.to_second);
        tab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                //System.out.println("回调中");
                MyPopup.this.dismiss();
                listener.onCall(btnid);
            }
        });
    }
    public MyPopup(final Activity context,dataOS dataos1,int btnid1,SQLiteDatabase db1) {
        db=db1;
        dataos=dataos1;
        btnid=btnid1;
        ContextM=(Context)context;


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.activity_my_popup, null);
        upUI(inflater);
        //int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w/2);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        System.out.println(w/2);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

    }

    public void upUI(LayoutInflater inflater) {
        TextView text=(TextView)conentView.findViewById(R.id.textView20);
        text.setText(dataos.andnum);

        text=(TextView)conentView.findViewById(R.id.textView17);
        text.setText(dataos.Country);

        text=(TextView)conentView.findViewById(R.id.textViewC);
        text.setText(dataos.Remarks);

        text=(TextView)conentView.findViewById(R.id.textView23);
        text.setText(dataos.pos);

        text=(TextView)conentView.findViewById(R.id.textView21);
        text.setText(dataos.co);

        text=(TextView)conentView.findViewById(R.id.textView171);
        text.setText(dataos.num);
        text=(TextView)conentView.findViewById(R.id.rhdate);
        String rq=dataos.date;
        if (rq.startsWith("20")){
            rq=rq.substring(2,rq.length());
        }
        text.setText(rq);

        bar=(ProgressBar) conentView.findViewById(R.id.bar1);

        if (dataos.pic.equals("1")){
            TableRow row=(TableRow)conentView.findViewById(R.id.bar1row);
            row.setVisibility(View.VISIBLE);
            TextView SButton=(TextView) conentView.findViewById(R.id.bar1t);
            SButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    downloadPIC((TextView) v);
                }
            });
            gData app = (gData)ContextM.getApplicationContext();
            String ls[]=app.getRootData();
            //System.out.println("ls正确111  "+ls[4]);
            if(Integer.parseInt(ls[4])>7){
                bar.setVisibility(View.VISIBLE);
                bar.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        String name=getPicName(dataos);
                        bmobData bm= new bmobData(ContextM,mHandler);
                        bm.downloadPic(dataos,name);
                    }
                });
            }

        }

        if (dataos.Star!=null&&dataos.Star.length()>2){
            float ss[]={3.5f,3.5f,3.5f};
            for (int i=0;i<3;i++) {
                String tt = dataos.Star.substring(i, i+1);
                if (tt!=null&&!tt.equals(" "))ss[i]=(Integer.parseInt(tt)+1)/2f;
            }
            RatingBar ratingbar=(RatingBar)conentView.findViewById(R.id.small_ratingbar);
            ratingbar.setRating(ss[0]);
            ratingbar=(RatingBar)conentView.findViewById(R.id.small_ratingbar1);
            ratingbar.setRating(ss[1]);
            ratingbar=(RatingBar)conentView.findViewById(R.id.small_ratingbar2);
            ratingbar.setRating(ss[2]);
        }
        //System.out.println("5555  "+dataos.people);
        if (dataos.people!=null&&!dataos.people.equals(" ")) {
            String[] strings1 = dataos.people.split(",");
            TableLayout layout=(TableLayout) conentView.findViewById(R.id.popuplist);
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0, 100, 5.0f);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 2.0f);
            //dataos.peoplelist=new String[strings1.length];
            int n=0;
            for (int j = 0; j < strings1.length; j++) {

                Cursor cursor1 = db.rawQuery("select * from people WHERE PID = ?", new String[]{strings1[j]});
                //Log.d("MainActivity", strings1[j] + " 联系人1   "+cursor1.getCount());

                if (cursor1.moveToFirst())
                {
                    String people1 = cursor1.getString(cursor1.getColumnIndex("p"));
                    if (people1.length()>0){
                        //dataos.peoplelist[j]=people1;
                        TableRow row = new TableRow(ContextM);
                        TextView text1 = new TextView(ContextM);
                        text1.setLayoutParams(lp1);
                        String tt[]=people1.split("1",2);
                        if (tt.length>0)people1=tt[0]+"\n1"+tt[1];
                        text1.setText(people1);
                        text1.setTextColor(Color.parseColor("#FF9340FF"));
                        text1.setId(100+n);
                        row.addView(text1);
                        ImageButton btn = (ImageButton) inflater.inflate(R.layout.call, null).findViewById(R.id.button_call);
                        btn.setLayoutParams(lp);
                        btn.setId(200+n);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView text=(TextView)conentView.findViewById(v.getId()-100);
                                String num=text.getText().toString();
                                String nump="";
                                if (num.length()>0){
                                    nump=num.substring(num.indexOf("1"));
                                }
                                if (nump.length()>0) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:"+nump));
                                    ContextM.startActivity(intent);
                                }
                            }

                        });
                        row.addView(btn);
                        n++;
                        layout.addView(row);
                    }

                }
                cursor1.close();
            }
        }

    }


    /**查看图片*/
    public void downloadPIC(TextView v){
        gData app = (gData)ContextM.getApplicationContext();
        String ls[]=app.getRootData();
        //System.out.println("ls正确111  "+ls[4]);
        if(Integer.parseInt(ls[4])<4){
            AlertDialog.Builder builder = new AlertDialog.Builder(ContextM);
            builder.setMessage("您还不是VVIP用户,不能查看!\n请联系客服微信号："+app.getWeiXin())
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();
            return;
        }

        bmobData bm= new bmobData(ContextM,mHandler);
        bm.downloadPic(dataos);
        /*
        String name=getPicName(dataos);
        bar=(ProgressBar) conentView.findViewById(R.id.bar1);
        v.setText("下载中..");
        v.setEnabled(false);
        bar.setVisibility(View.VISIBLE);
        bm.downloadPic(dataos,name);
        */
    }

    public String getPicName(dataOS dataos) {
        Cursor cursor = db.rawQuery("select * from company WHERE PID = ?", new String[]{dataos.PID});
        cursor.moveToFirst();
        String co = cursor.getString(cursor.getColumnIndex("co"));
        String ware = cursor.getString(cursor.getColumnIndex("ware"));
        cursor.close();
        return co+"-"+ware+"-"+dataos.andnum+"-"+dataos.line+dataos.list+".jpg";

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            int h=getHpos(parent, conentView);
            //System.out.println(h);
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, h);
            //int windowPos[] = calculatePopWindowPos(parent, conentView);
            //this.showAtLocation(parent, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);
        } else {
            this.dismiss();
        }
    }
    private static int getHpos(final View anchorView, final View contentView) {
        final int windowPos;
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight =anchorView.getContext().getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = anchorView.getContext().getResources().getDisplayMetrics().widthPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        //final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            windowPos = - windowHeight-anchorHeight-18;
        }
        else{
            final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
            //System.out.println(anchorLoc[0]+" "+screenWidth);

            if (isNeedShowUp&&(anchorLoc[0]<screenWidth/2+1)) {
                windowPos = - windowHeight-anchorHeight-18;
            } else {

                windowPos = 18;
            }
        }

        //System.out.println(windowPos);

        return windowPos;
    }

}

