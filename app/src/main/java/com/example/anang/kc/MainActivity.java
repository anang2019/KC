package com.example.anang.kc;



import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.listener.BmobDialogButtonListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateStatus;


public class MainActivity extends FragmentActivity implements
        android.view.View.OnClickListener {
    //private String titleData[]
    private SQLiteDatabase db;
    private ViewPager mViewPager;// 用来放置界面切换
    private PagerAdapter mPagerAdapter;// 初始化View适配器
    private List<View> mViews = new ArrayList<View>();// 用来存放Tab01-04
    private boolean upKF,upFind,upME,setED;
    // 四个按钮
    private ImageButton mWeiXinImg;
    private ImageButton mFrdImg;
    private ImageButton mSettingImg;

    private TextView mWeiXinText;
    private TextView mFrdText;
    private TextView mSettingText;

    private String upTimeText,endTimeText;

    private int isLockscreen=0;
    private String[] itemdata={};
    private bmobData bm;
    private hidepupop pupop;
    private showroot showw;
    private long mExitTime;

    public Handler mHandler=new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case 1:
                    upTimeText=msg.obj.toString();
                    break;
                case 2:
                    TextView rootText = (TextView) findViewById(R.id.gaimimatext);
                    Button btn = (Button) findViewById(R.id.exitto_xgmm);
                    btn.setEnabled(true);
                    rootText.setText(getString(R.string.i1));
                    break;
                case 3:
                    TextView rootText1 = (TextView) findViewById(R.id.gaimimatext);
                    Button btn1 = (Button) findViewById(R.id.exitto_xgmm);
                    btn1.setEnabled(true);
                    rootText1.setText(getString(R.string.i2));
                    break;
                case 4:
                    Button btn2 = (Button) findViewById(R.id.exitto_mm);
                    btn2.setEnabled(true);
                    isLockscreen=2;
                    break;
                case 5:
                    Button btn3 = (Button) findViewById(R.id.exitto_mm);
                    btn3.setEnabled(true);
                    isLockscreen=2;
                    Button FindButton=(Button) findViewById(R.id.button11);
                    FindButton.setEnabled(false);
                    FindButton.setTextColor(getResources().getColor(R.color.TS));
                    updataMe();
                    break;
                case 101:
                    if(showw!=null)showw.dismiss();
                    Toast.makeText(MainActivity.this, getString(R.string.i3), Toast.LENGTH_LONG).show();
                    gData app = (gData)getApplicationContext();
                    String ls[]=app.getRootData();
                    //System.out.println(app.getGstate()+"  "+ls[4]+"  "+ls[5]);
                    if(app.getGstate()==1||Integer.parseInt(ls[4])>=app.getGstate()) {
                        endTimeText=ls[5];

                        if (isEndTime(app.getWeiXin())){
                            //Toast.makeText(MainActivity.this,"您的使用期限已到期!",Toast.LENGTH_LONG).show();
                            isLockscreen=2;
                            Lockscreen(true);
                        }else{
                            if (mViewPager.getCurrentItem()==0)Lockscreen(false);
                            else isLockscreen=1;
                        }
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(getString(R.string.i111)+"\n"+getString(R.string.i13)+"\n"+app.getWeiXin())
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).show();
                    }
                    if (pupop!=null)pupop.dismiss();
                    break;
                case 102:
                    if(showw!=null)showw.dismiss();
                    pupop=new hidepupop(MainActivity.this,mViewPager);
                    pupop.showPopupWindow(mSettingImg);
                    break;
                case 103:
                    if(showw!=null)showw.dismiss();
                    pupop=new hidepupop(MainActivity.this,mViewPager);
                    pupop.showPopupWindow(mSettingImg);
                    break;
                case 104:
                    if(showw!=null)showw.dismiss();
                    Toast.makeText(MainActivity.this,getString(R.string.i4),Toast.LENGTH_SHORT).show();
                    pupop=new hidepupop(MainActivity.this,mViewPager);
                    pupop.showPopupWindow(mSettingImg);
                    break;
                case 109:
                    if(showw!=null)showw.dismiss();
                    pupop=new hidepupop(MainActivity.this,mViewPager);
                    pupop.showPopupWindow(mSettingImg);
                    break;
                case 110:
                    if(showw!=null)showw.dismiss();
                    pupop=new hidepupop(MainActivity.this,mViewPager);
                    pupop.showPopupWindow(mSettingImg);
                    break;
                case 1001:
                    SetGState();
                    break;
                case 1002:
                    upappput(msg.obj.toString());
                    break;
                case 1003:
                    installApkFile();
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
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        initData();
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        //db.execSQL("alter table house add column date text");
        //db.execSQL("insert into house(PID,line,list,andnum) values(?,?,?,?)",new String[]{"1","100","101","ade2bed49"});
        Cursor cursor1 = db.rawQuery("select * from house WHERE PID = ? and line=? and list=?",new String[]{"1",getString(R.string.i24),getString(R.string.i25)});
        cursor1.moveToFirst();
        gData app = (gData)getApplicationContext();
        app.setAdv1(cursor1.getString(cursor1.getColumnIndex("andnum")));
        bm = new bmobData(MainActivity.this,mHandler);
        cursor1.close();
        initView();
        initViewPage();
        initEvent();
        bm.DownData(db);
        bm.getGState();

        //System.out.println("果没有    "+Build.VERSION_CODES.N+"   "+Build.VERSION_CODES.M+"   "+Build.VERSION.SDK_INT);
        //Build.VERSION_CODES.N==24   M==23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //设置对对话框按钮的点击事件的监听
            BmobUpdateAgent.setDialogListener(new BmobDialogButtonListener() {

                @Override
                public void onClick(int status) {
                    // TODO Auto-generated method stub
                    switch (status) {
                        case UpdateStatus.Update:
                            //Toast.makeText(MainActivity.this, "点击了立即更新按钮" , Toast.LENGTH_SHORT).show();
                            break;
                        case UpdateStatus.NotNow:
                            //Toast.makeText(MainActivity.this, "点击了以后再说按钮" , Toast.LENGTH_SHORT).show();
                            break;
                        case UpdateStatus.Close://只有在强制更新状态下才会在更新对话框的右上方出现close按钮,如果用户不点击”立即更新“按钮，这时候开发者可做些操作，比如直接退出应用等
                            //Toast.makeText(MainActivity.this, "点击了对话框关闭按钮" , Toast.LENGTH_SHORT).show();
                            MainActivity.this.finish();
                            break;
                    }
                }
            });
            BmobUpdateAgent.setUpdateOnlyWifi(false);
            BmobUpdateAgent.update(this);
        }else{
            bm.UpApp();
        }


    }


    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "权限获取成功!", Toast.LENGTH_SHORT).show();
                    bm.UpApp();
                }
            }
        }
    }
    /**设置更新窗口*/
    private void upappput(String aa){
        String[]ls=aa.split("\\|");
        System.out.println(getVersion()+"    5465656656    "+ls[1]+"   "+ls[1].compareTo(getVersion()));
        if (ls.length==4&&ls[1].compareTo(getVersion())>0){
            parD(ls);
        }
    }

    private void installApkFile(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //String filePath=Environment.getExternalStorageDirectory() + File.separator+"芝麻开花.apk";
        File f=new File(Environment.getExternalStorageDirectory() + File.separator,"芝麻开花.apk");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            //System.out.println("5465656656    "+f.exists());
            Uri contentUri = FileProvider.getUriForFile(MainActivity.this, "com.example.anang.kc.fileprovider", f );
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }else{
            System.out.println("5465656656    23");
            intent.setDataAndType(Uri.fromFile(f), getMIMEType(f));
        }
        startActivity(intent);
        if (pupop!=null){
            System.out.println("5465656656   pupop ");
            pupop.dismiss();
        }
        MainActivity.this.finish();
    }
    public String getMIMEType(File var0) {
        String var1 = "";
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }


    private void parD(String[]ls){
        int xh_count = 0;
        // 声明进度条对话框
        ProgressDialog xh_pDialog;
        // 创建ProgressDialog对象
        xh_pDialog = new ProgressDialog(MainActivity.this);

        // 设置进度条风格，风格为圆形，旋转的
        xh_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        // 设置ProgressDialog 标题
        xh_pDialog.setTitle("版本更新!最新版 "+ls[1]);

        String tt="";
        tt+=ls[2].replace("；","\n");
        // 设置ProgressDialog提示信息
        xh_pDialog.setMessage(tt);

        // 设置ProgressDialog标题图标
        //xh_pDialog.setIcon(R.drawable.img2);

        // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
        xh_pDialog.setIndeterminate(false);

        // 设置ProgressDialog 进度条进度
        xh_pDialog.setProgress(1);

        // 设置ProgressDialog 是否可以按退回键取消
        if (ls[0].equals("1")) xh_pDialog.setCancelable(false);
        else xh_pDialog.setCancelable(true);
        // 设置ProgressDialog 的一个Button
        xh_pDialog.setButton("确定", new Bt1DialogListener());
        // 让ProgressDialog显示
        xh_pDialog.show();
    }

    // xhButton01的监听器类

    class Bt1DialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // 点击“确定”按钮取消对话框
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                System.out.println("果没有    ");
                ActivityCompat.requestPermissions(MainActivity.this, permissions, 321);
                return;
            }
            bm.downloadApp();
            dialog.cancel();
        }
    }

    private void initEvent() {

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             *ViewPage左右滑动时
             */
            @Override
            public void onPageSelected(int arg0) {
                resetImg(mViewPager.getCurrentItem());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * 初始化设置
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpage);
        // 初始化四个LinearLayout
        LinearLayout mTabWeiXin = (LinearLayout) findViewById(R.id.id_tab_weixin);
        LinearLayout mTabFrd = (LinearLayout) findViewById(R.id.id_tab_frd);
        LinearLayout mTabSetting = (LinearLayout) findViewById(R.id.id_tab_settings);

        mTabWeiXin.setOnClickListener(this);
        mTabFrd.setOnClickListener(this);
        mTabSetting.setOnClickListener(this);
        // 初始化四个按钮
        mWeiXinImg = (ImageButton) findViewById(R.id.id_tab_weixin_img);
        mFrdImg = (ImageButton) findViewById(R.id.id_tab_frd_img);
        mSettingImg = (ImageButton) findViewById(R.id.id_tab_settings_img);

        mWeiXinText= (TextView) findViewById(R.id.id_text_home);
        mFrdText= (TextView) findViewById(R.id.id_text_search);
        mSettingText= (TextView) findViewById(R.id.id_text_friendslist);
        upTimeText= "";

    }

    /**
     * 初始化ViewPage
     */

    private void initViewPage() {

        // 初妈化四个布局
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        View tab01 = mLayoutInflater.inflate(R.layout.activity_kufanglist, null);
        View tab02 = mLayoutInflater.inflate(R.layout.activity_find_call, null);
        View tab03 = mLayoutInflater.inflate(R.layout.activity_main2me, null);

        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);

        // 适配器初始化并设置
        mPagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(mViews.get(position));

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                if (position==0&&!upKF){
                    updataListView();
                    upKF=true;
                }
                else if (position==1&&!upFind){
                    updataFindcall();
                    upFind=true;
                }
                else if (position==2&&!upME){
                    updataMe();
                    upME=true;
                }

                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {

                return arg0 == arg1;
            }

            @Override
            public int getCount() {

                return mViews.size();
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(0);
        resetImg(0);
    }

    /**
     * 全局信息:用户名 callnum(0)=微信,地点 add(3)=广告,是否在线 state(7)=状态\n(1注册用户能用,2以上账号权限大于2能用,5无法使用)
     *
     * 个人权限 root >2是会员 >3查看照片 >4查看出货明细 >8可编辑 >9可设置库房,联系人
     *
     * <5看不到出货
     *
     *  4查看照片 5查看出货明细 9可编辑\n 10可设置库房,联系人 小于5看不到出货
     *
     * house 100,100 更新时间
     * 100,101码
     *
     * 100,99进出货明细更新时间
     *
     *
     * http://www.ijiami.cn/tlogin
     *19915277@qq.com  加密
     *
     */
    private void SetGState() {
        gData app = (gData)getApplicationContext();

        String ls[]=app.getRootData();
        System.out.println("目hhh  "+app.getGstate()+"  "+ls[4]);
        //全局root
        switch(app.getGstate()) {
            case 1:
                //无限制
                break;
            case 2:
                //试用一个月
                break;
            case 3:
                //收费
                if (Integer.parseInt(ls[4])<3){
                    isLockscreen=2;
                    Lockscreen(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getString(R.string.i111)+"\n"+getString(R.string.i13)+"\n"+app.getWeiXin())
                            .setCancelable(false)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            }).show();

                }
                break;
            case 4:
                break;
            case 5:
                //无法使用

                exitDialog(getString(R.string.datava1r)+app.getWeiXin());
                break;
            case 6:
                break;
            default:

                isLockscreen=2;
                Lockscreen(true);
        }

        if(app.getAdv().length()>1){
            TableRow tab=(TableRow) findViewById(R.id.TableRowgg);
            tab.setVisibility(View.VISIBLE);
            marqueeText img=(marqueeText) findViewById(R.id.tv1);
            img.setText(app.getAdv()+"                            ");
        }
    }

    /**是否过期 */
    public boolean isEndTime(String WeiXin){
        //System.out.println("ddddd   "+endTimeText);
        if (endTimeText!=null&&endTimeText.length()>5&&likeTime(endTimeText)) {
            //Toast.makeText(MainActivity.this, "您的使用期限已到期!", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您的使用期限已到期"+"\n"+getString(R.string.i13)+"\n"+WeiXin)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    }).show();
            return true;

        }
        return false;
    }

    /**比较时间 */
    public boolean likeTime(String day){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间

        if (nowDate.getTime()<curDate.getTime())return true;
        return false;
        //System.out.println("4455555   "+nowDate.getTime()+"  "+curDate.getTime());

    }

    /**让程序禁止使用  退出对话框*/
    public void exitDialog(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                }).show();
    }

    /**回到桌面*/
    public void BleakHome() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }
    /**
     * 判断哪个要显示，及设置按钮图片
     */
    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.id_tab_weixin:
                mViewPager.setCurrentItem(0);
                resetImg(0);
                break;
            case R.id.id_tab_frd:
                mViewPager.setCurrentItem(1);
                resetImg(1);
                break;
            case R.id.id_tab_settings:
                mViewPager.setCurrentItem(2);
                resetImg(2);
                break;
            default:
                break;
        }
    }

    /**
     * 把所有图片变暗
     */
    private void resetImg(int currentItem) {
        if (currentItem==0){
            if (isLockscreen==1){
                if (Lockscreen(false)) isLockscreen=0;
            }
            else if (isLockscreen==2) {
                if (Lockscreen(true)) isLockscreen=0;
            }
        }
        mWeiXinImg.setBackgroundResource(R.drawable.ic_menu_home2);
        mFrdImg.setBackgroundResource(R.drawable.ic_menu_search2);
        mSettingImg.setBackgroundResource(R.drawable.ic_menu_friendslist2);

        int color=getResources().getColor(R.color.bottom_layout_text_b);
        mWeiXinText.setTextColor(color);
        mFrdText.setTextColor(color);
        mSettingText.setTextColor(color);
        color=getResources().getColor(R.color.bottom_layout_text_w);
        switch (currentItem) {
            case 0:
                mWeiXinImg.setBackgroundResource(R.drawable.ic_menu_home1);
                mWeiXinText.setTextColor(color);
                break;
            case 1:
                mFrdImg.setBackgroundResource(R.drawable.ic_menu_search1);
                mFrdText.setTextColor(color);
                break;
            case 2:
                mSettingImg.setBackgroundResource(R.drawable.ic_menu_friendslist1);
                mSettingText.setTextColor(color);
                break;
            default:
                break;
        }
    }


    //刷新仓库
    public void updataListView(){

        /**是否有记住密码*/
        SharedPre sp =new SharedPre(this);
        if (sp.callnum!=null&&sp.callnum.length()==11&&sp.mm!=null&&sp.mm.length()>5){
            /**刷新登录进度条*/
            showw=new showroot(MainActivity.this,mViewPager);
            showw.showPopupWindow(mFrdImg);
            bm.getRootState(sp.callnum,sp.mm,bm.GetCode(getBaseContext(),getContentResolver()));
        }else{
            pupop=new hidepupop(MainActivity.this,mViewPager);
            pupop.showPopupWindow(mSettingImg);
        }

    }


    /**刷新库房*/
    public void showKf(){
        if (setED)return;
        setED=true;
        Resources res = getResources();
        gData app = (gData)getApplicationContext();
        String ls1[]=app.getRootData();
        String[] titleData;
        //System.out.println(" d1ddd"+ls1[9]);
        if (ls1[9].length()>1){
            //如果是库房管理员
            titleData=new String[1];
            titleData[0]=ls1[9];
        }else
            titleData = res.getStringArray(R.array.CangKu);
        //System.out.println(" d2ddd"+ls1[9]);
        //Intent intent=getIntent();
        //data =intent.getStringExtra("extra_data");
        TableLayout layout=(TableLayout) this.findViewById(R.id.tableLayout);
        TableRow.LayoutParams lp1 = new TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT, 1.0f);
        int ww=24;
        lp1.setMargins(ww,ww,ww,ww);
        TableRow.LayoutParams lp2 = new TableRow.LayoutParams(0,80 , 1.0f);


        for(int j=0;j<titleData.length;j++) {
            Cursor cursor = db.rawQuery("select * from company WHERE co = ?", new String[]{titleData[j]});
            TableRow row1 = new TableRow(this); // 定义表格行
            row1.setWeightSum(1);
            TextView textbtn = new TextView(this);
            textbtn.setText(titleData[j]+" ("+cursor.getCount()+")");
            textbtn.setLayoutParams(lp2);
            textbtn.setTextColor(getResources().getColor(R.color.KFtext));
            textbtn.setGravity(Gravity.CENTER_VERTICAL);
            textbtn.setBackgroundColor(getResources().getColor(R.color.KFbg));
            row1.addView(textbtn);
            layout.addView(row1);
            int n1 = 0;
            String ls[];
            int dwid[];
            ls = new String[cursor.getCount()];
            dwid = new int[cursor.getCount()];
            if (cursor.moveToFirst()) {
                do {
                    String people1 = cursor.getString(cursor.getColumnIndex("ware"));
                    if (people1.length() > 0) {
                        dwid[n1]=cursor.getInt(cursor.getColumnIndex("PID"));

                        ls[n1] = people1;
                        n1++;
                    }
                } while (cursor.moveToNext());
            }
            int n = 0;
            cursor.close();
            if(cursor.getCount()>0) {
                //LinearLayout btn2 = (LinearLayout) this.findViewById(dwid[0]);
                //if (btn2 != null) return;
            }


            for (int x = 0; x < n1; x += 3) { // 循环设置表格行
                TableRow row = new TableRow(this); // 定义表格行
                row.setWeightSum(3);
                for (int y = 0; y < 3; y++) {
                    if (n >= n1) {
                        TextView text=new TextView(this);
                        text.setLayoutParams(lp1);
                        text.setWidth(0);
                        row.addView(text);
                    }else {
                        LinearLayout btn = (LinearLayout) getLayoutInflater().inflate(R.layout.imgbutton, null).findViewById(R.id.ck_ly);
                        TextView tex=(TextView)btn.findViewById(R.id.id_text_home1);
                        ImageButton img=(ImageButton)btn.findViewById(R.id.id_tab_weixin_img1);
                        tex.setText(ls[n]); // 设置文本内容
                        btn.setLayoutParams(lp1);
                        btn.setId(dwid[n]);
                        /**所有库房都为正常显示
                        //if (j!=1) {
                            btn.setEnabled(false);
                            img.setBackgroundResource(R.drawable.ddd1);
                            tex.setTextColor(getResources().getColor(R.color.TS));
                        //}else
                         */
                            tex.setTextColor(getResources().getColor(R.color.KFtextcolor));
                        tex.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
                        //btn.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
                        //btn.setGravity(Gravity.CENTER);

                        //btn.setBackgroundResource(R.drawable.btn_default_small_normal);

                        btn.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                //添加事件处理逻辑

                                Intent intent = new Intent(MainActivity.this, inKuFang.class);
                                LinearLayout b = (LinearLayout) v;
                                //UpData1(b.getText().toString());
                                //System.out.println("data  "+b.getId());
                                intent.putExtra("extra_data", Integer.toString(b.getId()));
                                //intent.putExtra("extra_data1", data);
                                startActivity(intent);
                            }
                        });

                        row.addView(btn); // 加入一个编号
                        n++;
                    }
                }
                layout.addView(row); // 向表格之中增加若干个表格行
            }
        }
    }

    //刷新查询
    public void updataFindcall(){
        EditText editText = (EditText) findViewById(R.id.editText9);
        String digists = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        editText.setKeyListener(DigitsKeyListener.getInstance(digists));
        Cursor cursor1 = db.rawQuery("select DISTINCT  co from people WHERE  co != ?",new String[]{""});
        int n=0;
        String ls[];
        ls=new String[cursor1.getCount()+1];
        ls[0]="公司";
        if(cursor1.moveToFirst()){
            do{
                String people1 = cursor1.getString(cursor1.getColumnIndex("co"));
                if (people1.length()>0)
                {
                    ls[n+1]=people1;
                    n++;
                }
            }while(cursor1.moveToNext());
        }
        cursor1.close();
        int spinnerstyles=android.R.layout.simple_spinner_item;
        /**按公司*/
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this,spinnerstyles, ls);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        Resources res = getResources();
        /**按产地*/
        spinner1 = (Spinner) findViewById(R.id.spinner);
        adapter1=new ArrayAdapter<String>(MainActivity.this,spinnerstyles, res.getStringArray (R.array.colorCD));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        /**按颜色*/
        spinner1 = (Spinner) findViewById(R.id.spinner3);
        adapter1=new ArrayAdapter<String>(MainActivity.this,spinnerstyles, res.getStringArray (R.array.colorYW));
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        gData app = (gData)getApplicationContext();
        String ls1[]=app.getRootData();
        String[] titleData;
        System.out.println(" dddd"+ls1[9]);
        if (ls1[9].length()>1) {
            //如果是库房管理员
            titleData=new String[1];
            titleData[0]=ls1[9];
        }else
            titleData= res.getStringArray (R.array.CangKu);
        String[] Data=new String[titleData.length+1];
        Data[0]="库房";
        for(int i=0;i<titleData.length;i++){
            Data[i+1]=titleData[i];
        }

        /**按库房*/
        spinner1 = (Spinner) findViewById(R.id.spinner4);
        adapter1=new ArrayAdapter<String>(MainActivity.this,spinnerstyles, Data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        Spinner spinnerView = (Spinner) findViewById(R.id.spinner4);
        spinnerView.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        if (position>0) {
                            Spinner spinner1 = (Spinner) findViewById(R.id.spinner4);
                            String num1 = spinner1.getSelectedItem().toString();
                            Cursor cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{num1});

                            int n=1;
                            String ls[];
                            ls=new String[cursor.getCount()+1];
                            ls[0]="所有";
                            if(cursor.moveToFirst()){
                                do{
                                    String people1 = cursor.getString(cursor.getColumnIndex("ware"));
                                    if (people1.length()>0)
                                    {
                                        ls[n]=people1;
                                        n++;
                                    }
                                }while(cursor.moveToNext());
                            }
                            cursor.close();

                            spinner1 = (Spinner) findViewById(R.id.spinner5);
                            ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, ls);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //绑定 Adapter到控件
                            spinner1 .setAdapter(adapter1);

                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //System.out.println("111111");
                    }
                });
        //如果是库房管理员
        if (titleData.length==1){
            spinnerView.setSelection(1);
            spinnerView.setEnabled(false);
        }
        //统计库存 按钮
        Button FindButton=(Button) findViewById(R.id.button12);
        FindButton.setEnabled(false);
        FindButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                subKC();
            }
        });

        //查询按钮
        FindButton=(Button) findViewById(R.id.button11);
        FindButton.setEnabled(false);
        FindButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Cursor cursor;
                String where="";
                String wLs="";

                String andnum1[]={"","","",""};
                String wLs1[]={"","","",""};
                /**关闭软键盘*/
                InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                //System.out.println("find  up");
                //提单号
                EditText text=(EditText)findViewById(R.id.editText9);
                String tt=text.getText().toString();
                if (tt.length()>0) {
                    andnum1[0]="andnum LIKE ? ";
                    wLs1[0]="%"+tt+"%";
                }
                //产地
                Spinner spinnerView = (Spinner) findViewById(R.id.spinner);
                int n=spinnerView.getSelectedItemPosition();
                if(n>0){
                    String num2 = spinnerView.getSelectedItem().toString();
                    andnum1[1]="Country=? ";
                    wLs1[1]=num2;
                }
                //公司
                spinnerView = (Spinner) findViewById(R.id.spinner2);
                n=spinnerView.getSelectedItemPosition();
                if(n>0){
                    String num2 = spinnerView.getSelectedItem().toString();
                    andnum1[2]="co=? ";
                    wLs1[2]=num2;
                }
                //花色
                spinnerView = (Spinner) findViewById(R.id.spinner3);
                n=spinnerView.getSelectedItemPosition();
                if(n>0){
                    String num2 = spinnerView.getSelectedItem().toString();
                    andnum1[3]="Remarks LIKE ? ";
                    wLs1[3]="%"+num2+"%";
                }
                boolean bn=false;
                for (int i=0;i<3;i++){
                    if(andnum1[i] != ""){
                        if (bn){
                            bn=false;
                            where+="and ";
                            wLs+=";";
                        }
                        where+=andnum1[i];
                        wLs+=wLs1[i];
                        if (andnum1[i+1] != "") {
                            where+="and ";
                            wLs+=";";
                        }
                        else bn=true;
                    }
                }
                if (bn&&andnum1[3]!= ""){
                    bn=false;
                    where+="and ";
                    wLs+=";";
                }
                where+=andnum1[3];
                wLs+=wLs1[3];
                TextView text11= (TextView) findViewById(R.id.textView15);
                if (where.equals("")) {
                    text11.setText("没有查询条件!");
                    return;
                }
                //仓库
                String PID="";
                spinnerView = (Spinner) findViewById(R.id.spinner4);
                n=spinnerView.getSelectedItemPosition();
                String kf=spinnerView.getSelectedItem().toString();
                if(n>0){
                    Spinner spinnerView1 = (Spinner) findViewById(R.id.spinner5);
                    int n1=spinnerView1.getSelectedItemPosition();
                    if (n1>0){
                        cursor = db.rawQuery("select * from company WHERE co = ? and ware = ?",new String[]{kf,spinnerView1.getSelectedItem().toString()});
                        if (cursor.getCount()>0){
                            cursor.moveToFirst();
                            PID=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
                            where+="and PID=? ";
                            wLs+=";"+PID;
                        }
                        cursor.close();
                        //System.out.println("   222    "+spinnerView1.getSelectedItem().toString());
                    }
                    else{
                        cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{kf});
                        if (cursor.getCount()>0){
                            where+="and PID in(";
                            int a=0;
                            if (cursor.moveToFirst()){
                                do{
                                    wLs+=";"+Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
                                    if (a>0)where+=",";
                                    where+="?";
                                    a++;
                                }while(cursor.moveToNext());
                            }
                            where+=")";
                        }
                        cursor.close();
                        //System.out.println("   333    "+spinnerView.getSelectedItem().toString());
                    }
                }
                String wLs2[]=wLs.split(";");
                cursor = db.rawQuery("select * from house WHERE "+where,wLs2);

                n=0;

                text11.setText("共查到 "+cursor.getCount()+" 条");

                if (cursor.getCount()>0){
                    itemdata=new String[cursor.getCount()];

                    ListView mListView = (ListView)findViewById(R.id.listView);

                    mListView.setAdapter(null);

                    ArrayList<HashMap<String, Object>> item = new ArrayList<HashMap<String, Object>>();

                    if(cursor.moveToFirst()){
                        do{
                            String pos=cursor.getString(cursor.getColumnIndex("pos"));
                            String andnum=cursor.getString(cursor.getColumnIndex("andnum"));
                            String Country=cursor.getString(cursor.getColumnIndex("Country"));
                            String co=cursor.getString(cursor.getColumnIndex("co"));
                            String Remarks=cursor.getString(cursor.getColumnIndex("Remarks"));
                            String PID1 =Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
                            String line =Integer.toString(cursor.getInt(cursor.getColumnIndex("line")));
                            String list =Integer.toString(cursor.getInt(cursor.getColumnIndex("list")));
                            //if(co.length()>3)co=co.substring(0,2);
                            Cursor cursor1 = db.rawQuery("select * from company WHERE PID = ?",new String[]{PID1});
                            cursor1.moveToFirst();
                            String co2=cursor1.getString(cursor1.getColumnIndex("co"));
                            String ware=cursor1.getString(cursor1.getColumnIndex("ware"));
                            cursor1.close();
                            itemdata[n]=PID1+","+line+","+list;
                            n++;

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("PiaoHao", andnum);
                            map.put("ChanDi", Country);
                            map.put("CangKu", co2+" "+ware);
                            map.put("ChuWei", pos);
                            map.put("GongSi", co);
                            map.put("BeiZhu", Remarks);
                            item.add(map);
                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                    System.out.println(666555666);
                    //创建简单适配器SimpleAdapter
                    SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, item, R.layout.item,
                            new String[] {"PiaoHao","ChanDi", "CangKu", "ChuWei", "GongSi", "BeiZhu"},
                            new int[] {R.id.PiaoHao, R.id.ChanDi, R.id.CangKu, R.id.ChuWei, R.id.GongSi, R.id.BeiZhu});

                    //加载SimpleAdapter到ListView中
                    mListView.setAdapter(simpleAdapter);

                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {
                            // TODO Auto-generated method stub
                            Intent intent=new Intent(MainActivity.this, inKuFang.class);
                            String[] nn1=itemdata[position].split(",");
                            String fh=itemdata[position];
                            for (int i=0;i<itemdata.length;i++){
                                String[]nn=itemdata[i].split(",");
                                if (nn[0].equals(nn1[0])){
                                    fh+=";"+itemdata[i];
                                }
                            }
                            intent.putExtra("extra_data2", fh);
                            startActivity(intent);

                        }

                    });
                }

            }

        });

    }
    //刷新我
    public void updataMe(){
        /**更新时间*/
        TextView ut = (TextView) findViewById(R.id.textView_me);
        ut.setText(upTimeText);
        if (endTimeText!=null&&endTimeText.length()>5){
            ut = (TextView) findViewById(R.id.textView_me1);
            ut.setText(endTimeText);
        }
        LinearLayout layout = (LinearLayout) findViewById(R.id.xgkf_layout);
        layout.setVisibility(View.GONE);
        TextView text = (TextView) findViewById(R.id.rootnow);
        text.setOnTouchListener(new View.OnTouchListener() {

            @Override public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int eve=event.getAction();
                if (eve == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundColor(getResources().getColor(R.color.text_btn_tl_on));
                } else if (eve== MotionEvent.ACTION_UP||eve== MotionEvent.ACTION_MOVE||eve==3) {
                    v.setBackgroundColor(getResources().getColor(R.color.white));
                }
                return false;
            }
        });
        helpwindow();
        updataMeRoot();
    }

    /**设置行情出入货*/
    public void setHQ(){

        TextView textbtn1 = (TextView) findViewById(R.id.textView11);
        textbtn1.setVisibility(View.VISIBLE);
        textbtn1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        //textbtn1.setTextColor(getResources().getColor(R.color.dxk_bg));
        String ttt="今日芝麻行情"+"<font color='#FF0000' ><small>new</small></font>";
        textbtn1.setText(Html.fromHtml(ttt));
        textbtn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                Intent intent = new Intent(MainActivity.this, Quotation.class);
                startActivity(intent);
            }
        });

        textbtn1 = (TextView) findViewById(R.id.textView16);
        textbtn1.setVisibility(View.VISIBLE);
        textbtn1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        //textbtn1.setTextColor(getResources().getColor(R.color.dxk_bg));

        Cursor cursor = db.rawQuery("select * from input ",null);
        String nn="new";
        if (cursor.getCount()<10)nn="测试中";
        ttt="出入货明细"+"<font color='#FF0000' ><small>"+nn+"</small></font>";
        textbtn1.setText(Html.fromHtml(ttt));
        textbtn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //添加事件处理逻辑
                Cursor cursor = db.rawQuery("select * from input ",null);
                gData app = (gData)getApplicationContext();
                String ls[]=app.getRootData();

                if (cursor.getCount()<10&& Integer.parseInt(ls[4])<9){
                    Toast.makeText(MainActivity.this,"测试中!即将开启!",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, Main2InPut.class);
                    startActivity(intent);
                }

            }
        });
    }

    /**帮助窗口*/
    public void helpwindow(){
        /**版本号*/
        TextView text1 = (TextView) findViewById(R.id.thisbb);
        text1.setText("V"+getVersion());
        /**登录帮助*/
        TextView text = (TextView) findViewById(R.id.button9ddl);
        //text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, helpwindow.class);
                intent.putExtra("extra_data", "1");
                startActivity(intent);
            }
        });

        text = (TextView) findViewById(R.id.button8JS);
        //text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, helpwindow.class);
                intent.putExtra("extra_data", "2");
                startActivity(intent);
            }
        });

    }
    /**锁屏*/
    public boolean Lockscreen(boolean bn){
        /**  bn==true 锁定
        if (!bn){
            gData app = (gData)getApplicationContext();
            System.out.println("4455555  "+app.getGstate());
            if (!app.getGstate().equals("1")&&!app.getGstate().equals("0")) return;
        }
         */
        System.out.println("4455555   "+bn);
        showKf();

        Button FindButton=(Button) findViewById(R.id.button11);
        if (FindButton==null)return false;

        int findcolor=getResources().getColor(R.color.buttonupcolor);
        int btncolor=getResources().getColor(R.color.KFtextcolor);
        int tubiao=R.drawable.button_item;
        if (bn){
            findcolor=getResources().getColor(R.color.TS);
            btncolor=getResources().getColor(R.color.TS);
            tubiao=R.drawable.ddd1;
        }else {
            updataFindcall();
            setHQ();

        }

        TextView textbtn1 = (TextView) findViewById(R.id.textView16);
        textbtn1.setEnabled(!bn);

        FindButton.setEnabled(!bn);
        FindButton.setTextColor(findcolor);

        FindButton=(Button) findViewById(R.id.button12);
        FindButton.setEnabled(!bn);
        FindButton.setTextColor(findcolor);

        Resources res = getResources();
        String[] titleData = res.getStringArray(R.array.CangKu);
        for(int j=0;j<titleData.length;j++) {
            if (j != 1) {
                Cursor cursor = db.rawQuery("select * from company WHERE co = ?", new String[]{titleData[j]});

                if (cursor.moveToFirst()) {
                    do {
                        String people1 = cursor.getString(cursor.getColumnIndex("ware"));
                        if (people1.length() > 0) {
                            int pid = cursor.getInt(cursor.getColumnIndex("PID"));
                            LinearLayout btn = (LinearLayout) this.findViewById(pid);

                            //System.out.println("4455555111   "+pid);
                            if (btn != null) {
                                ImageButton img=(ImageButton)btn.findViewById(R.id.id_tab_weixin_img1);
                                TextView tex=(TextView)btn.findViewById(R.id.id_text_home1);
                                btn.setEnabled(!bn);
                                img.setBackgroundResource(tubiao);
                                tex.setTextColor(btncolor);
                            }
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        return true;
    }

    public void subKC(){
        Cursor cursor;
        String where="";
        String wLs="";
        /**关闭软键盘*/
        InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        //System.out.println("find  up");

        String andnum1[]={"","","",""};
        String wLs1[]={"","","",""};
        String text1="";
        //提单号
        EditText text=(EditText)findViewById(R.id.editText9);
        String tt=text.getText().toString();
        if (tt.length()>0) {
            andnum1[0]="andnum = ? ";
            wLs1[0]=tt;
        }
        //产地
        Spinner spinnerView = (Spinner) findViewById(R.id.spinner);
        int n=spinnerView.getSelectedItemPosition();
        if(n>0){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[1]="Country=? ";
            wLs1[1]=num2;
            text1=spinnerView.getSelectedItem().toString();
        }
        //公司
        spinnerView = (Spinner) findViewById(R.id.spinner2);
        n=spinnerView.getSelectedItemPosition();
        if(n>0){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[2]="co=? ";
            wLs1[2]=num2;
            text1+=" "+spinnerView.getSelectedItem().toString();
        }
        //花色
        spinnerView = (Spinner) findViewById(R.id.spinner3);
        n=spinnerView.getSelectedItemPosition();
        if(n>0){
            String num2 = spinnerView.getSelectedItem().toString();
            andnum1[3]="Remarks LIKE ? ";
            wLs1[3]="%"+num2+"%";
            text1+=" "+spinnerView.getSelectedItem().toString();
        }
        boolean bn=false;
        for (int i=0;i<3;i++){
            if(andnum1[i] != ""){
                if (bn){
                    bn=false;
                    where+="and ";
                    wLs+=";";
                }
                where+=andnum1[i];
                wLs+=wLs1[i];
                if (andnum1[i+1] != "") {
                    where+="and ";
                    wLs+=";";
                }
                else bn=true;
            }
        }
        if (bn&&andnum1[3]!= ""){
            bn=false;
            where+="and ";
            wLs+=";";
        }
        where+=andnum1[3];
        wLs+=wLs1[3];
        TextView text11= (TextView) findViewById(R.id.textView15);

        //仓库
        String PID="";
        spinnerView = (Spinner) findViewById(R.id.spinner4);
        n=spinnerView.getSelectedItemPosition();

        Spinner spinnerView1 = (Spinner) findViewById(R.id.spinner5);
        int n1=spinnerView1.getSelectedItemPosition();
        /**目前只支持巨惠达
        if (n>1){
            Toast.makeText(this,"目前只支持巨惠达",Toast.LENGTH_SHORT).show();
            spinnerView1.setSelection(0);
            n1=0;
        }
         spinnerView.setSelection(1);
         n=1;
         */


        text1+=" "+spinnerView.getSelectedItem().toString();

        if (where.length()>0){
            where+="and ";
            wLs+=";";
        }
        if(n>0){
            if (n1>0){
                text1+=" "+spinnerView1.getSelectedItem().toString();
                cursor = db.rawQuery("select * from company WHERE ware = ?",new String[]{spinnerView1.getSelectedItem().toString()});
                if (cursor.getCount()>0){
                    cursor.moveToFirst();
                    PID=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));
                    where+="PID=? ";
                    wLs+=PID;
                }
                cursor.close();
                //System.out.println("   222    "+spinnerView1.getSelectedItem().toString());
            }
            else{
                cursor = db.rawQuery("select * from company WHERE co = ?",new String[]{spinnerView.getSelectedItem().toString()});
                if (cursor.getCount()>0){
                    where+="PID in(";
                    int a=0;
                    if (cursor.moveToFirst()){
                        do{

                            if (a>0){
                                where+=",";
                                wLs+=";";
                            }
                            where+="?";

                            wLs+=Integer.toString(cursor.getInt(cursor.getColumnIndex("PID")));

                            a++;
                        }while(cursor.moveToNext());
                    }
                    where+=")";
                }
                cursor.close();
                //System.out.println("   333    "+spinnerView.getSelectedItem().toString());
            }
        }
        String wLs2[]=wLs.split(";");

        //cursor = db.rawQuery("select * from house WHERE "+where,wLs2);
        cursor = db.rawQuery("select SUM(num) AS num1 from house WHERE "+where,wLs2);
        cursor.moveToFirst();


        System.out.println(where+"   "+wLs2+"  "+cursor.getCount());

        int num=cursor.getInt(cursor.getColumnIndex("num1"));
        cursor.close();
        TextView text12= (TextView) findViewById(R.id.textView13);
        text12.setTextColor(getResources().getColor(R.color.buttonupcolor));

        if (!tt.equals("")){
            text1="票号:"+tt+" 共查询到 "+num+" 吨";

        }else{
            text1+=" 共查询到 "+num+" 吨";
        }
        text12.setText(text1);

        cursor = db.rawQuery("select COUNT(*) AS cou from house WHERE "+where,wLs2);
        cursor.moveToFirst();
        int cou=cursor.getInt(cursor.getColumnIndex("cou"));
        cursor.close();
        text11.setText("共查到 "+cou+" 条");


    }
    /**编辑库房名的选项*/

    public void updataWare() {
        /**编辑库房名的选项*/
        Resources res = getResources();
        String[] titleData= res.getStringArray (R.array.CangKu);
        String[] Data=new String[titleData.length+1];
        Data[0]="选择库房";
        for(int i=0;i<titleData.length;i++){
            Data[i+1]=titleData[i];
        }
        Spinner spinner1 = (Spinner) findViewById(R.id.me_spinner4);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, Data);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1 .setAdapter(adapter1);

        /**刷新按钮*/
        TextView text = (TextView) findViewById(R.id.button9);
        text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main2me, null).findViewById(R.id.textView_me);
                //System.out.println("3333    "+text.getText().toString());
                //bmobData bm=new bmobData(MainActivity.this,mHandler);
                //MyDatabaseHelper dbHelper=new MyDatabaseHelper(MainActivity.this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
                bm.DownData(db);
            }
        });

        /**备份*/
        text = (TextView) findViewById(R.id.button7);
        text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPath = "data/data/com.example.anang.kc/databases/" + getString(R.string.dataku);
                System.out.println("3333    "+ Environment.getExternalStorageDirectory() + File.separator);
                String newPath = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.dataku);
                copyFile(oldPath, newPath);
            }
        });

        /**恢复*/
        text = (TextView) findViewById(R.id.button8);
        text.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPath = "data/data/com.example.anang.kc/databases/" + getString(R.string.dataku);
                String newPath = Environment.getExternalStorageDirectory() + File.separator + getString(R.string.dataku);
                copyFile(newPath,oldPath );
            }
        });

        /**联系人*/
        TextView FindButton = (TextView) findViewById(R.id.button10);
        FindButton.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        FindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, chaxun.class);
                startActivity(intent);
            }
        });
        /**修改库房名*/
        Button addKFDate=(Button) findViewById(R.id.button3);
        addKFDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Spinner spinner1 = (Spinner) findViewById(R.id.me_spinner4);
                if (spinner1.getSelectedItemPosition()<1){
                    Toast.makeText(MainActivity.this, "请先选择库房!" , Toast.LENGTH_SHORT).show();
                    return;
                }
                String data=spinner1.getSelectedItem().toString();
                // TODO Auto-generated method stub
                EditText Text=(EditText) findViewById(R.id.editText);
                String s1=Text.getText().toString();
                EditText Text1=(EditText) findViewById(R.id.editText1);
                String s2=Text1.getText().toString();
                EditText Text2=(EditText) findViewById(R.id.editText2);
                String s3=Text2.getText().toString();
                EditText Text3=(EditText) findViewById(R.id.editText12);
                String s4=Text3.getText().toString();
                if (s1.length()>0&&((s2.length()>0&&s3.length()>0)||s4.length()>0)) {
                    //bmobData bm=new bmobData(MainActivity.this,mHandler);
                    Cursor cursor = db.rawQuery("select * from company WHERE ware = ? and co=?", new String[]{s1,data});
                    if (cursor.getCount() == 0) {
                        if (s2.length()>0&&s3.length()>0){
                            Toast.makeText(MainActivity.this, "创建仓库 "+s1+" 成功", Toast.LENGTH_SHORT).show();
                            db.execSQL("insert into company(co,ware,line,list) values(?,?,?,?)", new String[]{data, s1, s2, s3});
                            Cursor cursor1 = db.rawQuery("select * from company WHERE co=? and ware = ?", new String[]{data,s1});
                            if (cursor1.moveToFirst()) {
                                int PID=cursor1.getInt(cursor1.getColumnIndex("PID"));
                                bm.UpDataCo(new String[]{""+PID,data, s1, s2, s3});
                            }
                            cursor1.close();
                        }else Toast.makeText(MainActivity.this, "请输入行数和列数", Toast.LENGTH_SHORT).show();

                    } else if (cursor.moveToFirst()) {
                        int PID=cursor.getInt(cursor.getColumnIndex("PID"));

                        if (s2.length()==0||s3.length()==0){
                            s2=Integer.toString(cursor.getInt(cursor.getColumnIndex("line")));
                            s3=Integer.toString(cursor.getInt(cursor.getColumnIndex("list")));
                        }
                        if(s4.length()>0) s1=Text3.getText().toString();
                        //System.out.println("复制单个文件操   "+ s1);
                        bm.UpDataCo(new String[]{""+PID,data, s1, s2, s3});

                        db.execSQL("update company set co =?,ware =?,line =?,list =? WHERE PID = ?", new String[]{data, s1, s2, s3,""+PID});
                        Toast.makeText(MainActivity.this, "修改 "+s1+" 成功", Toast.LENGTH_SHORT).show();
                    }
                    cursor.close();


                }else Toast.makeText(MainActivity.this, "请输入行数和列数", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**刷新登录*/

    public void updataMeRoot() {
        gData app = (gData)getApplicationContext();
        String ls[]=app.getRootData();
        //System.out.println("ls正确  "+ls.length+"  "+ls[0]);
        if (ls[0].length()==11){
            TextView text = (TextView) findViewById(R.id.text1Videw2);
            text.setText("已登录：");
            text = (TextView) findViewById(R.id.rootnow);
            text.getPaint().setFlags(Paint.HINTING_ON );
            text.setText(ls[0]);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, usersetup.class);
                    startActivityForResult(intent, 3);
                }
            });

            ImageButton image=(ImageButton)findViewById(R.id.id_tab_me);
            image.setBackgroundResource(R.drawable.me);
            /**设置按钮*/
            TextView checkbox = (TextView) findViewById(R.id.checkBox_sz);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout layout1 = (LinearLayout) findViewById(R.id.xgkf_layout);
                    //System.out.println("11111   "+layout1.getVisibility());
                    if(layout1.getVisibility()==View.GONE){
                        gData app = (gData)getApplicationContext();
                        String ls[]=app.getRootData();
                        //System.out.println("ls正确111  "+ls[4]);
                        if (Integer.parseInt(ls[4])>9){
                            //System.out.println("ls正确  "+ls.length+"  "+ls[0]);
                            layout1.setVisibility(View.VISIBLE);
                            updataWare();
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ScrollView sv=(ScrollView)findViewById(R.id.scrollviev_me);
                                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }
                    }else layout1.setVisibility(View.GONE);
                }
            });
        }else{
            TextView text1 = (TextView) findViewById(R.id.text1Videw2);
            text1.setText("未登录：");
            /**立即登录*/
            text1 = (TextView) findViewById(R.id.rootnow);
            text1.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
            text1.setText("立即登录");
            text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity.this, rootwindow.class);
                    startActivityForResult(intent, 2);
                }
            });
            ImageButton image=(ImageButton)findViewById(R.id.id_tab_me);
            image.setBackgroundResource(R.drawable.me1);
        }
    }

    /**复制数据库*/
    public void copyFile(String oldPath, String newPath) {
        //System.out.println("ssssss" + newPath);
        try {
            int bytesum = 0;
            int byteread = 0;

            File oldfile = new File(oldPath);
            File newfile = new File(newPath);

            if (!newfile.exists()) {
                newfile.createNewFile();
                System.out.println("创建");
            }

            if (oldfile.exists()) { // 文件存在时
                //System.out.println("zzzzzzzzzzz");
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                Toast.makeText(MainActivity.this, "操作成功!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            //System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }
    /**初始化数据库*/
    public boolean initData( ) {
        try {
            int bytesum = 0;
            int byteread = 0;
            String dbName="data.db";//数据库的名字
            String DATABASE_PATH="/data/data/com.example.anang.kc/databases/";//数据库在手机里的路径
            String databaseFilenames = DATABASE_PATH + dbName;
            File dir = new File(DATABASE_PATH);
            if (!dir.exists())// 判断文件夹是否存在，不存在就新建一个
                dir.mkdir();

            //File oldfile = new File(oldPath);
            File newfile = new File(databaseFilenames);
            if (!newfile.exists()) {
                //System.out.println("创建");
                newfile.createNewFile();

                if (newfile.exists()) { // 文件存在时
                    //System.out.println("zzzzzzzzzzz");
                    InputStream inStream =this.getResources().openRawResource(R.raw.data);

                    //InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                    FileOutputStream fs = new FileOutputStream(databaseFilenames);
                    byte[] buffer = new byte[1444];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; // 字节数 文件大小
                        fs.write(buffer, 0, byteread);
                    }
                    //System.out.println("333333333333333333333 "+buffer);
                    inStream.close();
                    Toast.makeText(this, "数据库创建成功!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        } catch (Exception e) {
            //System.out.println("复制单个文件操作出错");
            e.printStackTrace();
            return false;
        }

    }
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            //version+="  Code  "+info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "未知";
        }
    }

    //重写onActivityResult方法获取返回的结果数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //System.out.println("4455555");
        switch(requestCode){
            case 2:
                //登录/注册 返回
                if(resultCode==RESULT_OK){
                    gData app = (gData)getApplicationContext();
                    String ls[]=app.getRootData();
                    updataMeRoot();
                    if(app.getGstate()==1||Integer.parseInt(ls[4])>=app.getGstate()) {
                        endTimeText=ls[5];
                        if (isEndTime(app.getWeiXin())){
                            //Toast.makeText(MainActivity.this,"您的使用期限已到期!",Toast.LENGTH_LONG).show();
                            isLockscreen=2;
                            //Lockscreen(true);
                        }else{
                            isLockscreen=1;
                            Button FindButton=(Button) findViewById(R.id.button11);
                            FindButton.setEnabled(true);
                            FindButton.setTextColor(getResources().getColor(R.color.buttonupcolor));
                            if (pupop!=null)pupop.dismiss();
                        }


                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(getString(R.string.i111)+"\n"+getString(R.string.i13)+"\n"+app.getWeiXin())
                                .setCancelable(false)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).show();
                    }

                }
                break;
            case 3:
                //设置用户界面返回
                if(resultCode==RESULT_OK){
                    isLockscreen=2;
                    Button FindButton=(Button) findViewById(R.id.button11);
                    FindButton.setEnabled(false);
                    FindButton.setTextColor(getResources().getColor(R.color.TS));
                    updataMe();
                }
                break;
            default:break;
        }

    }

    /**按两两次退出*/
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出！", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {

            if(bm.exitThis()) {
                Toast.makeText(MainActivity.this, "正在退出！", Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            finish();
            System.exit(0);
        }
    }

    /**程序被杀死时调用
    @Override
    protected void onDestroy() {
        if(bm.exitThis()) {
            try {
                Thread.sleep(2000);
                System.out.println("下线了188811");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }
    */

}


