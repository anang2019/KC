package com.example.anang.kc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class root2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root2);
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
        /**注册*/
        Button toRegister = (Button) findViewById(R.id.register_btn1);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView rootText = (TextView) findViewById(R.id.name_edit00);
                String text11;
                EditText mm = (EditText) findViewById(R.id.name_edit);
                String rname=mm.getText().toString();
                if (!isGB2312(rname)||rname.length()<2||rname.length()>3){
                    text11="请填写真实姓名！";
                    Toast.makeText(root2.this, text11 , Toast.LENGTH_LONG).show();
                    rootText.setText(text11);
                    return;
                }
                mm = (EditText) findViewById(R.id.didian_edit);
                String add=mm.getText().toString();
                if (!isGB2312(add)||add.length()<2){
                    text11="请填写真实地点！";
                    Toast.makeText(root2.this, text11 , Toast.LENGTH_LONG).show();
                    rootText.setText(text11);
                    return;
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(root2.this.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                Intent intent = new Intent();
                intent.putExtra("data_return", rname);
                intent.putExtra("data_return1", add);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



    }

    /**判断是否为汉字*/
    public static Boolean isGB2312(String str) {
        for (int i = 0; i < str.length(); i++) {
            String bb = str.substring(i, i + 1);
            // 生成一个Pattern,同时编译一个正则表达式,其中的u4E00("一"的unicode编码)-\u9FA5("龥"的unicode编码)
            boolean cc = Pattern.matches("[\u4E00-\u9FA5]", bb);

            if (cc == false) {
                return cc;
            }
        }
        return true;
    }


}
