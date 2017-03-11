package cqupt.myinvest.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 重写textview,可以获取焦点.
 */
public class MyTextView extends TextView {

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;//获取焦点
    }
}
