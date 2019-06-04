package com.example.anang.kc;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class usersetup extends AppCompatActivity {
    private bmobData bm;
    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 2:
                    TextView rootText = (TextView) findViewById(R.id.gaimimatext);
                    Button btn = (Button) findViewById(R.id.exitto_xgmm);
                    btn.setEnabled(true);
                    rootText.setText("修改成功!");
                    break;
                case 3:
                    TextView rootText1 = (TextView) findViewById(R.id.gaimimatext);
                    Button btn1 = (Button) findViewById(R.id.exitto_xgmm);
                    btn1.setEnabled(true);
                    rootText1.setText("修改失败,请稍候重试!");
                    break;
                case 4:

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case 5:

                    Intent intent1 = new Intent();
                    setResult(RESULT_OK, intent1);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersetup);
        bm = new bmobData(usersetup.this,mHandler);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

        /**修改密码按钮*/
        Button btn = (Button) findViewById(R.id.exitto_xgmm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editMM(v);
            }
        });
        /**退出按钮*/
        btn = (Button) findViewById(R.id.exitto_mm);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                bm.exitRoot();
            }
        });
        gData app = (gData)this.getApplicationContext();
        String []ls=app.getRootData();
        TextView text = (TextView) findViewById(R.id.exitto_name);
        text.setText(ls[2]);
        text = (TextView) findViewById(R.id.exitto_add);
        text.setText(ls[3]);
    }

    public void editMM(View v) {
        EditText text = (EditText) findViewById(R.id.mm_edit_y);
        TextView rootText = (TextView) findViewById(R.id.gaimimatext);
        String mm=text.getText().toString();
        String text11;
        if (mm.equals("")||mm.length()<6){
            text11="请输入6位或以上的密码！";
            Toast.makeText(usersetup.this, text11 , Toast.LENGTH_LONG).show();
            rootText.setText(text11);
            return;
        }
        gData app = (gData)getApplicationContext();
        String ls[]=app.getRootData();
        //bmobData bm=new bmobData(MainActivity.this,mHandler);
        if (!bm.stringToMD5(mm).equals(ls[1])){
            text11="原密码不正确！";
            Toast.makeText(usersetup.this, text11 , Toast.LENGTH_LONG).show();
            rootText.setText(text11);
            return;
        }
        EditText text1 = (EditText) findViewById(R.id.mm_edit_c);
        String mm1=text1.getText().toString();

        EditText text2 = (EditText) findViewById(R.id.mm_edit_n);

        String jg=bm.isRmm(mm1,text2.getText().toString());
        if (jg.length()>0){
            Toast.makeText(usersetup.this, jg , Toast.LENGTH_LONG).show();
            rootText.setText(jg);
            return;
        }
        v.setEnabled(false);
        text.setText("");
        text1.setText("");
        text2.setText("");
        ls[1]=bm.stringToMD5(mm1);
        bm.editmm(ls);
    }

}
