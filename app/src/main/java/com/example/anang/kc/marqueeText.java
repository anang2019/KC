package com.example.anang.kc;

/**
 * Created by Administrator on 2017/5/19.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * Created by Administrator on 2016/3/17.
 * 实现跑马灯效果的TextView
 */
public class marqueeText extends TextView {
    public marqueeText(Context context) {
        super(context);
    }
    public marqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public marqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //返回textview是否处在选中的状态
    //而只有选中的textview才能够实现跑马灯效果
    @Override
    public boolean isFocused() {
        return true;
    }
}