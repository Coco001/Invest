package cqupt.myinvest;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.fragment.AssertFragment;
import cqupt.myinvest.fragment.HomeFragment;
import cqupt.myinvest.fragment.InvestFragment;
import cqupt.myinvest.fragment.MoreFragment;
import cqupt.myinvest.utils.UIUtils;

import static cqupt.myinvest.R.id.iv_main_home;
import static cqupt.myinvest.R.id.ll_main_home;

public class MainActivity extends BaseActivity {

    @Bind(R.id.fl_main)
    FrameLayout mFlMain;
    @Bind(iv_main_home)
    ImageView mIvMainHome;
    @Bind(R.id.tv_main_home)
    TextView mTvMainHome;
    @Bind(ll_main_home)
    LinearLayout mLlMainHome;
    @Bind(R.id.iv_main_invest)
    ImageView mIvMainInvest;
    @Bind(R.id.tv_main_invest)
    TextView mTvMainInvest;
    @Bind(R.id.ll_main_invest)
    LinearLayout mLlMainInvest;
    @Bind(R.id.iv_main_me)
    ImageView mIvMainMe;
    @Bind(R.id.tv_main_me)
    TextView mTvMainMe;
    @Bind(R.id.ll_main_me)
    LinearLayout mLlMainMe;
    @Bind(R.id.iv_main_more)
    ImageView mIvMainMore;
    @Bind(R.id.tv_main_more)
    TextView mTvMainMore;
    @Bind(R.id.ll_main_more)
    LinearLayout mLlMainMore;
    @Bind(R.id.activity_main)
    LinearLayout mActivityMain;
    private HomeFragment mHomeFragment;
    private InvestFragment mInvestFragment;
    private AssertFragment mAssertFragment;
    private MoreFragment mMoreFragment;
    private FragmentTransaction mTransaction;
    private static boolean flag = true;


    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    flag = true;
                    break;
            }
        }
    };

    @Override
    protected void initData() {
        select(0);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 选择显示的fragment
     */
    @OnClick({ll_main_home, R.id.ll_main_invest, R.id.ll_main_me, R.id.ll_main_more})
    public void showTab(View view) {
        switch (view.getId()) {
            case ll_main_home:
                select(0);
                break;
            case R.id.ll_main_invest:
                select(1);
                break;
            case R.id.ll_main_me:
                select(2);
                break;
            case R.id.ll_main_more:
                select(3);
                break;
        }
    }

    /**
     * 使用FragmentManager管理显示的fragment
     */
    private void select(int i) {
        FragmentManager manager = this.getSupportFragmentManager();
        mTransaction = manager.beginTransaction();
        hintOther();
        switch (i) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    mTransaction.add(R.id.fl_main, mHomeFragment);
                }
                mTransaction.show(mHomeFragment);
                mIvMainHome.setImageResource(R.drawable.bottom02);
                mTvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
            case 1:
                if (mInvestFragment == null) {
                    mInvestFragment = new InvestFragment();
                    mTransaction.add(R.id.fl_main, mInvestFragment);
                }
                mTransaction.show(mInvestFragment);
                mIvMainInvest.setImageResource(R.drawable.bottom04);
                mTvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
            case 2:
                if (mAssertFragment == null) {
                    mAssertFragment = new AssertFragment();
                    mTransaction.add(R.id.fl_main, mAssertFragment);
                }
                mTransaction.show(mAssertFragment);
                mIvMainMe.setImageResource(R.drawable.bottom06);
                mTvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_selected01));
                break;
            case 3:
                if (mMoreFragment == null) {
                    mMoreFragment = new MoreFragment();
                    mTransaction.add(R.id.fl_main, mMoreFragment);
                }
                mTransaction.show(mMoreFragment);
                mIvMainMore.setImageResource(R.drawable.bottom08);
                mTvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
        }
        mTransaction.commit();
    }

    /**
     * 显示选择的fragment之前需要隐藏其他的fragment
     */
    private void hintOther() {
        if (mHomeFragment != null) {
            mTransaction.hide(mHomeFragment);
            mIvMainHome.setImageResource(R.drawable.bottom01);
            mTvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        }
        if (mInvestFragment != null) {
            mTransaction.hide(mInvestFragment);
            mIvMainInvest.setImageResource(R.drawable.bottom03);
            mTvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        }
        if (mAssertFragment != null) {
            mTransaction.hide(mAssertFragment);
            mIvMainMe.setImageResource(R.drawable.bottom05);
            mTvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        }
        if (mMoreFragment != null) {
            mTransaction.hide(mMoreFragment);
            mIvMainMore.setImageResource(R.drawable.bottom07);
            mTvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        }
    }

    /**
     * 重写返回键点击事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                Toast.makeText(MainActivity.this, "再点击一次，退出硅谷金融", Toast.LENGTH_SHORT).show();
                flag = false;
                //发送一个延迟消息
                handler.sendEmptyMessageDelayed(1, 2000);
                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

