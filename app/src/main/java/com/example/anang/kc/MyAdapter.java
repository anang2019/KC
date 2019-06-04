package com.example.anang.kc;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/12.
 */

public class MyAdapter extends BaseAdapter {
    private LinearLayout holderls[];
    private LayoutInflater mInflater;
    private List<Map<String, Object>> mData;
    private SQLiteDatabase db;
    private Context contextM;
    private bmobData bm;

    public MyAdapter(Context context, List<Map<String, Object>> mData1, SQLiteDatabase db1){
        this.mInflater = LayoutInflater.from(context);
        mData=mData1;
        db=db1;
        contextM=context;
        holderls=new LinearLayout[mData1.size()];
        bm=new bmobData(contextM);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {

            holder=new ViewHolder();
            convertView = mInflater.inflate(R.layout.chaxun_item, null);

            holder.pid = (TextView)convertView.findViewById(R.id.textchaxun_1);
            holder.co = (EditText)convertView.findViewById(R.id.editchaxun_1);
            holder.people = (EditText)convertView.findViewById(R.id.editchaxun_2);
            holder.save = (ImageButton)convertView.findViewById(R.id.chaxun_1);
            holder.call = (ImageButton)convertView.findViewById(R.id.chaxun_2);

            convertView.setTag(holder);

        }else {

            holder = (ViewHolder)convertView.getTag();
        }

        holder.pid.setText((String)mData.get(position).get("pid"));
        holder.co.setText((String)mData.get(position).get("co"));
        holder.people.setText((String)mData.get(position).get("people"));

        holder.pid.setTag(position);
        holder.co.setTag(position);
        holder.people.setTag(position);
        holder.save.setTag(position);
        holder.call.setTag(position);
        holderls[position]=(LinearLayout)convertView;
        //System.out.println("1    "+position+"  "+holder.count);

        holder.save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //System.out.println("保存");
                int position = (Integer) v.getTag();
                LinearLayout vv=holderls[position];

                EditText cotext = (EditText)vv.findViewById(R.id.editchaxun_1);
                EditText peopletext = (EditText)vv.findViewById(R.id.editchaxun_2);
                // (R.id.editchaxun_2);
                //System.out.println(cotext.getText().toString()+"    "+peopletext.getText().toString() );

                String pid1=(String)mData.get(position).get("pid");
                setsave(cotext,peopletext,pid1);
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //System.out.println("打");
                int position = (Integer) v.getTag();
                LinearLayout vv=holderls[position];
                EditText peopletext = (EditText)vv.findViewById(R.id.editchaxun_2);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String nump="";
                String num=peopletext.getText().toString();
                if (num.length()>0){
                    nump=num.substring(num.indexOf("1"));
                }
                intent.setData(Uri.parse("tel:"+nump));
                contextM.startActivity(intent);
            }
        });


        return convertView;
    }

    public void setsave(EditText co,EditText people,String pid){
        String people1=people.getText().toString();
        //System.out.println("保存1  "+people1+"   "+pid);
        if (!people1.equals("")&&people1.length()>2){
            db.execSQL("update people set co =?,p =? WHERE PID = ?", new String[]{co.getText().toString(), people1,pid});

            bm.UpDataP(new String[]{pid, co.getText().toString(), people1});

            Toast.makeText(contextM,"保存 "+people1+" 成功!",Toast.LENGTH_SHORT).show();
        }else{
            db.execSQL("delete from people WHERE PID = ?", new String[]{pid});
            bm.UpDataP(new String[]{pid, "", ""});
            Toast.makeText(contextM,"删除联系人成功!",Toast.LENGTH_SHORT).show();
        }
    }
}

