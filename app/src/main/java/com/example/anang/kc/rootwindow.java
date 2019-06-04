package com.example.anang.kc;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class rootwindow extends AppCompatActivity {
    private Button rootButton,toRegister;
    private TextView rootText;
    private CheckBox checkbox;
    private String userls[]={"","","","","1","","","1",""};
    private String[] permissions = {Manifest.permission.READ_PHONE_STATE};
    private AlertDialog dialog;

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 101:
                    Toast.makeText(rootwindow.this, getString(R.string.i3), Toast.LENGTH_LONG).show();
                    gotome();
                    break;
                case 102:
                    rootText.setText(getString(R.string.i5));
                    break;
                case 103:
                    rootText.setText(getString(R.string.i6));
                    checkbox.setChecked(true);
                    break;
                case 104:
                    rootText.setText(getString(R.string.i4));
                    break;
                case 105:
                    Toast.makeText(rootwindow.this, getString(R.string.i7), Toast.LENGTH_LONG).show();
                    gotome();
                    break;
                case 106:
                    rootText.setText(getString(R.string.i8));
                    break;
                case 107:
                    rootText.setText(getString(R.string.i9));
                    break;
                case 108:
                    rootText.setText(getString(R.string.i10));
                    break;
                case 109:
                    rootText.setText(getString(R.string.i11));
                    break;
                case 110:
                    rootText.setText(getString(R.string.i12));
                    break;
                default:
                    break;
            }
            if(msg.what>100){
                rootButton.setEnabled(true);
                toRegister.setEnabled(true);
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rootwindow);
        LinearLayout layout = (LinearLayout) findViewById(R.id.root_ZC_layout);
        layout.setVisibility(View.GONE);
        //键盘默认不弹出
        //getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        rootText= (TextView) findViewById(R.id.root_state);


        SharedPre sp =new SharedPre(this);
        if (sp.callnum!=null&&sp.callnum.length()==11){
            EditText text2 = (EditText) findViewById(R.id.callnum_edit);
            text2.setText(sp.callnum);
        }

        /**忘记密码*/
        TextView text1 = (TextView) findViewById(R.id.wjmm);
        text1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gData app = (gData)getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(rootwindow.this);
                builder.setMessage(getString(R.string.i13)+app.getWeiXin()+getString(R.string.i14))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();
            }
        });


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

        /**注册页面*/
        checkbox = (CheckBox) findViewById(R.id.checkBoxZC);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                LinearLayout layout = (LinearLayout) findViewById(R.id.root_ZC_layout);

                if(isChecked) {
                    layout.setVisibility(View.VISIBLE);
                    rootButton.setVisibility(View.GONE);
                    /**关闭软键盘*/
                    InputMethodManager imm = (InputMethodManager) getSystemService(rootwindow.this.INPUT_METHOD_SERVICE);
                    if (imm != null)
                        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }else{
                    layout.setVisibility(View.GONE);
                    rootButton.setVisibility(View.VISIBLE);
                }
            }
        });
        /**登录*/
        rootButton = (Button) findViewById(R.id.root_btn);
        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootText.setText("");
                EditText callnum = (EditText) findViewById(R.id.callnum_edit);
                EditText mm = (EditText) findViewById(R.id.mm_edit);
                String cn=callnum.getText().toString();
                String mm1=mm.getText().toString();

                if (cn.equals("")||cn.length()!=11||!cn.substring(0,1).equals("1")){
                    rootText.setText(getString(R.string.i22));
                    Toast.makeText(rootwindow.this, getString(R.string.i22) , Toast.LENGTH_LONG).show();
                    return;
                }
                if (mm1.equals("")||mm1.length()<6){
                    Toast.makeText(rootwindow.this, getString(R.string.i19), Toast.LENGTH_LONG).show();
                    rootText.setText(getString(R.string.i19));
                    return;
                }
                bmobData bm=new bmobData(rootwindow.this,mHandler);
                rootButton.setEnabled(false);
                toRegister.setEnabled(false);
                mm1=bm.stringToMD5(mm1);
                bm.getRootState(cn,mm1,bm.GetCode(getBaseContext(),getContentResolver()));
            }
        });

        /**注册*/
        toRegister = (Button) findViewById(R.id.register_btn);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootText.setText("");
                EditText callnum = (EditText) findViewById(R.id.callnum_edit);
                EditText mm = (EditText) findViewById(R.id.mm_edit);
                String cn=callnum.getText().toString();
                String mm1=mm.getText().toString();
                String text11=getString(R.string.i22);
                if (cn.equals("")||cn.length()!=11||!cn.substring(0,1).equals("1")){
                    rootText.setText(text11);
                    Toast.makeText(rootwindow.this, text11 , Toast.LENGTH_LONG).show();
                    return;
                }
                //mm = (EditText) findViewById(R.id.nextmm_edit);
                bmobData bm=new bmobData(rootwindow.this,mHandler);
                String jg=bm.isRmm(mm1,mm1);
                if (jg.length()>0){
                    Toast.makeText(rootwindow.this, jg , Toast.LENGTH_LONG).show();
                    rootText.setText(jg);
                    return;
                }
                userls[0]=cn;
                userls[1]=bm.stringToMD5(mm1);

                Intent intent = new Intent(rootwindow.this, root2.class);
                startActivityForResult(intent, 1);
            }
        });

        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                startRequestPermission();
            }
        }
    }


    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        showDialogTipUserGoToAppSettting();
                        //finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许芝麻开花使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //System.out.println("4455555");
        switch(requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    bmobData bm=new bmobData(rootwindow.this,mHandler);
                    userls[2]=data.getStringExtra("data_return");
                    userls[3]=data.getStringExtra("data_return1");
                    userls[6]=bm.GetCode(getBaseContext(),getContentResolver());
                    rootButton.setEnabled(false);
                    toRegister.setEnabled(false);
                    bm.toRegister(userls);
                }
                break;
            case 123:
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 检查该权限是否已经获取
                    int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                    // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:break;
        }

    }

    /**成功后跳转*/
    public void gotome() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
