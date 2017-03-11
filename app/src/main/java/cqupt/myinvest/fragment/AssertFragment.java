package cqupt.myinvest.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.BarChartActivity;
import cqupt.myinvest.GestureVerifyActivity;
import cqupt.myinvest.GetActivity;
import cqupt.myinvest.LineChartActivity;
import cqupt.myinvest.LoginActivity;
import cqupt.myinvest.PayActivity;
import cqupt.myinvest.PieChartActivity;
import cqupt.myinvest.R;
import cqupt.myinvest.UserInfoActivity;
import cqupt.myinvest.bean.User;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.utils.BitmapUtils;
import cqupt.myinvest.utils.UIUtils;

/**
 * AssertFragment界面显示的fragment
 */

public class AssertFragment extends BaseFragment {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.iv_me_icon)
    ImageView mIvMeIcon;
    @Bind(R.id.rl_me_icon)
    RelativeLayout mRlMeIcon;
    @Bind(R.id.tv_me_name)
    TextView mTvMeName;
    @Bind(R.id.rl_me)
    RelativeLayout mRlMe;
    @Bind(R.id.recharge)
    ImageView mRecharge;
    @Bind(R.id.withdraw)
    ImageView mWithdraw;
    @Bind(R.id.ll_touzi)
    TextView mLlTouzi;
    @Bind(R.id.ll_touzi_zhiguan)
    TextView mLlTouziZhiguan;
    @Bind(R.id.ll_zichan)
    TextView mLlZichan;

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected void initData(String content) {
        //判断用户是否已经登陆
        isLogin();
    }

    private void isLogin() {
        SharedPreferences use_name = getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = use_name.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();
        } else {
            //已经登录过，则直接加载用户的信息并显示
            doUser();
        }
    }

    private void doUser() {
        User user = ((BaseActivity) AssertFragment.this.getActivity()).readUser();
        mTvMeName.setText(user.getName());
        if (isLocalPicExist()) {
            return;
        }
        Picasso.with(this.getActivity()).load(user.getImageurl())
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {//通过URL下载的图片
                        Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                        bitmap = BitmapUtils.circleBitmap(source);
                        //回收bitmap资源
                        source.recycle();
                        return bitmap;
                    }

                    @Override
                    public String key() {
                        return "";//需要保证返回值不能为null，否则报错
                    }
                }).into(mIvMeIcon);
        //判断一下，是否开启了手势密码。如果开启：先输入手势密码
        SharedPreferences sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        boolean isOpen = sp.getBoolean("isOpen", false);
        if(isOpen){
            ((BaseActivity)this.getActivity()).setupActivity(GestureVerifyActivity.class,null);
            return;
        }

    }

    private void doLogin() {
        new AlertDialog.Builder(this.getActivity()).setTitle("提示")
                .setMessage("用户未登录")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进入登录界面
                        FragmentActivity activity = AssertFragment.this.getActivity();
                        Intent intent = new Intent(activity, LoginActivity.class);
                        startActivity(intent);
                        //((BaseActivity) AssertFragment.this.getActivity()).setupActivity(LoginActivity.class, null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        readImage();
    }

    private void readImage() {
        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileDir = this.getActivity().getExternalFilesDir("");
        } else {
            fileDir = this.getActivity().getFilesDir();
        }
        File file = new File(fileDir, "icon.png");
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mIvMeIcon.setImageBitmap(bitmap);
        }
    }

    //判断本地是否存在缓存图片
    private boolean isLocalPicExist() {
        File fileDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            fileDir = this.getActivity().getExternalFilesDir("");
        } else {
            fileDir = this.getActivity().getFilesDir();
        }
        File file = new File(fileDir, "icon.png");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.INVISIBLE);
        mTvTitleName.setText("我的资产");
        mIvTitleRight.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_title_right)
    public void setting(View view) {
        //启动用户信息界面的Activity
        ((BaseActivity)this.getActivity()).setupActivity((UserInfoActivity.class), null);
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    //充值操作的监听
    @OnClick(R.id.recharge)
    public void recharge(View view) {
        ((BaseActivity) this.getActivity()).setupActivity(PayActivity.class, null);
    }

    //提现操作的监听
    @OnClick(R.id.withdraw)
    public void withdraw(View view) {
        ((BaseActivity) this.getActivity()).setupActivity(GetActivity.class, null);
    }

    //启动折线图
    @OnClick(R.id.ll_touzi)
    public void startLineChart(View view){
        ((BaseActivity)this.getActivity()).setupActivity(LineChartActivity.class,null);
    }
    //启动折线图
    @OnClick(R.id.ll_touzi_zhiguan)
    public void startBarChart(View view){
        ((BaseActivity)this.getActivity()).setupActivity(BarChartActivity.class,null);
    }
    //启动折线图
    @OnClick(R.id.ll_zichan)
    public void startPieChart(View view){
        ((BaseActivity)this.getActivity()).setupActivity(PieChartActivity.class,null);
    }

}
