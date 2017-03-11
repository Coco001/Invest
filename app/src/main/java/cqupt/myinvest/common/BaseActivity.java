package cqupt.myinvest.common;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import cqupt.myinvest.bean.User;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        ActivityManager.getInstance().add(this);
        initTitle();
        initData();
    }

    protected abstract void initData();

    protected abstract void initTitle();

    protected abstract int getLayoutId();

    public AsyncHttpClient mClient = new AsyncHttpClient();

    //启动新的activity
    public void setupActivity(Class Activity, Bundle bundle) {
        Intent intent = new Intent(this, Activity);
        if (bundle != null && bundle.size() != 0) {
            intent.putExtra("data", bundle);
        }
        startActivity(intent);
    }

    //销毁当前的activity
    public void removeCurrentActivity() {
        ActivityManager.getInstance().removeCurrent();
    }

    //销毁对所有
    public void removeAllActivity() {
        ActivityManager.getInstance().removeAll();
    }

    //保存用户信息
    protected void saveUser(User user) {
        SharedPreferences info = this.getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor edit = info.edit();
        String name;
        try {
            name = new String(user.getName().getBytes(), "UTF-8");
            edit.putString("name", name);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        edit.putString("imageurl", user.getImageurl());
        edit.putString("phone", user.getPhone());
        edit.putBoolean("iscredit", user.isCredit());
        edit.commit();
    }

    //读取用户信息
    public User readUser() {
        SharedPreferences info = this.getSharedPreferences("user_info", MODE_PRIVATE);
        User user = new User();
        user.setName(info.getString("name", ""));
        user.setImageurl(info.getString("imageurl", ""));
        user.setPhone(info.getString("phone", ""));
        user.setCredit(info.getBoolean("iscredit", false));

        return user;
    }


}
