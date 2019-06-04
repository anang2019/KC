package com.example.anang.kc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chaxun extends AppCompatActivity {
    private SQLiteDatabase db;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("222222222  1");
        setContentView(R.layout.activity_chaxun);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this, getString(R.string.dataku), null, Integer.parseInt(getString(R.string.datavar)));
        db=dbHelper.getWritableDatabase();
        //Cursor cursor = db.rawQuery("select * from people",null);
        //System.out.println("222222222  3");
        mListView = (ListView)findViewById(R.id.listView_cx);


        //加载SimpleAdapter到ListView中
        List<Map<String, Object>> mData = getData();
        MyAdapter adapter = new MyAdapter(this,mData,db);
        mListView.setAdapter(adapter);
    }
    private List<Map<String, Object>> getData() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.rawQuery("select * from people order by PID desc",null);
        if(cursor.moveToFirst()){
            do{
                String co = cursor.getString(cursor.getColumnIndex("co"));
                String people1 = cursor.getString(cursor.getColumnIndex("p"));
                int pid=cursor.getInt(cursor.getColumnIndex("PID"));

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("pid", Integer.toString(pid));
                map.put("co", co);
                map.put("people", people1);
                list.add(map);
            }while(cursor.moveToNext());

        }
        cursor.close();

        return list;
    }
}
