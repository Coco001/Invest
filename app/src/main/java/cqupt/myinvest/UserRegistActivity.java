package cqupt.myinvest;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.utils.MD5Utils;
import cqupt.myinvest.utils.UIUtils;

/**
 * 用户注册的界面.
 */

public class UserRegistActivity extends BaseActivity {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.et_register_number)
    EditText mEtRegisterNumber;
    @Bind(R.id.et_register_name)
    EditText mEtRegisterName;
    @Bind(R.id.et_register_pwd)
    EditText mEtRegisterPwd;
    @Bind(R.id.et_register_pwdagain)
    EditText mEtRegisterPwdagain;
    @Bind(R.id.btn_register)
    Button mBtnRegister;

    @Override
    protected void initData() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取用户注册的信息
                String name = mEtRegisterName.getText().toString().trim();
                String number = mEtRegisterNumber.getText().toString().trim();
                String pwd = mEtRegisterPwd.getText().toString().trim();
                String pwdAgain = mEtRegisterPwdagain.getText().toString().trim();
                //2.所填写的信息不能为空
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
                    UIUtils.toast("填写的信息不能为空",false);
                }else if(!pwd.equals(pwdAgain)){//2.两次密码必须一致
                    UIUtils.toast("两次填写的密码不一致",false);
                    mEtRegisterPwd.setText("");
                    mEtRegisterPwdagain.setText("");
                }else{
                    //3.联网发送用户注册信息
                    String url = AppNetConfig.USERREGISTER;
                    RequestParams params = new RequestParams();
//                    name = new String(name.getBytes(),"UTF-8");
                    params.put("name",name);
                    params.put("password", MD5Utils.MD5(pwd));
                    params.put("phone",number);
                    mClient.post(url,params,new AsyncHttpResponseHandler(){
                        @Override
                        public void onSuccess(String content) {
                            JSONObject jsonObject = JSON.parseObject(content);
                            boolean isExist = jsonObject.getBoolean("isExist");
                            if(isExist){//已经注册过
                                UIUtils.toast("此用户已注册",false);
                            }else{
                                UIUtils.toast("注册成功",false);
                            }
                        }

                        @Override
                        public void onFailure(Throwable error, String content) {
                            UIUtils.toast("联网请求失败",false);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleName.setText("用户注册");
        mIvTitleRight.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_left)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_regist;
    }

}
