package cqupt.myinvest;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.bean.User;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.utils.MD5Utils;
import cqupt.myinvest.utils.UIUtils;

/**
 * 用户登录界面的activity.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.tv_login_number)
    TextView mTvLoginNumber;
    @Bind(R.id.et_login_number)
    EditText mEtLoginNumber;
    @Bind(R.id.rl_login)
    RelativeLayout mRlLogin;
    @Bind(R.id.tv_login_pwd)
    TextView mTvLoginPwd;
    @Bind(R.id.et_login_pwd)
    EditText mEtLoginPwd;
    @Bind(R.id.btn_login)
    Button mBtnLogin;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleName.setText("用户登录");
        mIvTitleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }


    @OnClick(R.id.iv_title_left)
    public void back() {
        removeAllActivity();
        setupActivity(MainActivity.class, null);
    }

    @OnClick(R.id.btn_login)
    public void login(View view) {
        String id = mEtLoginNumber.getText().toString().trim();
        String password = mEtLoginPwd.getText().toString().trim();
        AsyncHttpClient client = new AsyncHttpClient();
        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(password)) {
            String url = AppNetConfig.LOGIN;
            RequestParams params = new RequestParams();
            params.put("phone", id);
            params.put("password", MD5Utils.MD5(password));
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onFailure(Throwable error, String content) {
                    UIUtils.toast("联网失败",false);
                }

                @Override
                public void onSuccess(String content) {
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(LoginActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    }
                    //解析json数据
                    JSONObject object = JSON.parseObject(content);
                    boolean success = object.getBoolean("success");
                    if (success) {
                        //得到用户数据
                        String data = object.getString("data");
                        User user = JSON.parseObject(data, User.class);
                        //保存用户信息
                        saveUser(user);
                        //重新加载界面
                        removeAllActivity();
                        setupActivity(MainActivity.class, null);
                    } else {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            UIUtils.toast("用户名或密码不能为空",false);
        }
    }
}
