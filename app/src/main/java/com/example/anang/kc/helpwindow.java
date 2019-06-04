package com.example.anang.kc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class helpwindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpwindow);
        /**返回键*/
        ImageButton img=(ImageButton) findViewById(R.id.ic_back);
        img.setVisibility(View.VISIBLE);
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                System.out.println("2222");
                v.setVisibility(View.GONE);
                finish();
            }
        });
        Intent intent=getIntent();
        String data =intent.getStringExtra("extra_data");
        if (data.equals("1")) {
            LinearLayout layout=(LinearLayout) this.findViewById(R.id.help_layout1);
            layout.setVisibility(View.GONE);
            layout=(LinearLayout) this.findViewById(R.id.help_layout2);
            layout.setVisibility(View.VISIBLE);

            gData app = (gData) getApplicationContext();
            if (data.equals("1")) {
                String tt = "<font color='red' size='20'>" + "提示的信息: <br /><br />" + "</font>" +
                        "<font color='red' size='20'>" + "您还没有注册! " + "&nbsp&nbsp&nbsp&nbsp</font>" + "请先免费注册!</font><br /><br />" +
                        "<font color='red' size='20'>" + "本软件需要联网使用! " + "&nbsp&nbsp&nbsp&nbsp</font>" + "提示请打开数据连接!</font><br /><br />" +
                        "<font color='red' size='20'>" + "您的号码已在其它手机登录,请先退出! " + "</font>" +
                        "<br />该账号在其它手机登录过,请在原手机上点击 " +
                        "<br />我=>我的手机号码=>退出登录 (如未登录请先登录) </font><br /><br />" +
                        "</font>" + "<font color='red' size='20'>" + "忘记密码? " + "&nbsp&nbsp&nbsp&nbsp</font>" +
                        "请联系客服微信号&nbsp&nbsp </font>" + "<font color='red' size='20'>" + app.getWeiXin() + "</font>";
                TextView text = (TextView) findViewById(R.id.textView2nr);
                text.setText(Html.fromHtml(tt));
            }
        }else if (data.equals("2")){
            LinearLayout layout=(LinearLayout) this.findViewById(R.id.help_layout2);
            layout.setVisibility(View.GONE);
            layout=(LinearLayout) this.findViewById(R.id.help_layout1);
            layout.setVisibility(View.VISIBLE);
            String tt = "是不是很简单，最后祝各位老板<font color='red' size='30'>" + "生意兴隆！ </font>" ;
            TextView text = (TextView) findViewById(R.id.end_tl);
            text.setText(Html.fromHtml(tt));
        }
    }
}
