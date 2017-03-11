package cqupt.myinvest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.common.BaseActivity;

/**
 * 关于界面.
 */

public class AboutActivity extends BaseActivity {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleName.setText("关于硅谷理财");
        mIvTitleRight.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_invest;
    }

    @OnClick(R.id.iv_title_left)
    public void back(View view){
        removeCurrentActivity();
    }
}
