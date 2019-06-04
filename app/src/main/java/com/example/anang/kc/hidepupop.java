package com.example.anang.kc;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import android.widget.PopupWindow;


public class hidepupop extends PopupWindow {
    private View conentView;
    private ViewPager mViewPager1;
    //protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_hidepupop);
    //}

    public hidepupop(final Activity context,ViewPager mViewPager) {

        mViewPager1=mViewPager;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.activity_hidepupop, null);
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        float a=Math.round(screenWidth*0.45);
        this.setWidth((int)a);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        //System.out.println("555    "+screenWidth);
        //this.setTouchable(false);
        // 刷新状态
        this.update();
        this.setAnimationStyle(R.style.AnimationPreview);

        conentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager1.setCurrentItem(2);
                Intent intent = new Intent(context, rootwindow.class);
                context.startActivityForResult(intent, 2);
            }
        });
    }

    public void showPopupWindow(View parent) {
        final int anchorLoc[] = new int[2];
        parent.getLocationInWindow (anchorLoc);
        //获取系统版本，如4.0.2
        String sysVersion = android.os.Build.VERSION.RELEASE;
        int n=0;
        if (sysVersion.equals("7.0"))n=-240;
        //System.out.println("5558885    "+parent.getHeight() +"  "+sysVersion);
        if (!this.isShowing()) {
            this.showAsDropDown(parent, 0, n);
            //this.showAtLocation(parent, Gravity.NO_GRAVITY,0,-100+parent.getHeight());
            //this.showAtLocation(parent, Gravity.TOP , 100, 0);
        } else {
            //this.dismiss();
        }
    }


}
