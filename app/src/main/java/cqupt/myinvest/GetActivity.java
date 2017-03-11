package cqupt.myinvest;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.utils.UIUtils;

/**
 * 提现界面.
 */

public class GetActivity extends BaseActivity {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.view2)
    TextView mView2;
    @Bind(R.id.view1)
    View mView1;
    @Bind(R.id.account_zhifubao)
    EditText mAccountZhifubao;
    @Bind(R.id.select_bank)
    RelativeLayout mSelectBank;
    @Bind(R.id.chongzhi_text)
    TextView mChongzhiText;
    @Bind(R.id.view)
    View mView;
    @Bind(R.id.et_input_money)
    EditText mEtInputMoney;
    @Bind(R.id.chongzhi_text2)
    TextView mChongzhiText2;
    @Bind(R.id.textView5)
    TextView mTextView5;
    @Bind(R.id.btn_tixian)
    Button mBtnTixian;

    @Override
    protected void initData() {
        mBtnTixian.setClickable(false);
        mEtInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = mEtInputMoney.getText().toString().trim();
                if(TextUtils.isEmpty(money)){
                    //设置button不可操作的
                    mBtnTixian.setClickable(false);
                    //修改背景颜色
                    mBtnTixian.setBackgroundResource(R.drawable.btn_02);
                }else{
                    //设置button可操作的
                    mBtnTixian.setClickable(true);
                    //修改背景颜色
                    mBtnTixian.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.VISIBLE);
        mTvTitleName.setText("提现");
        mIvTitleRight.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_left)
    public void back(View view) {
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get;
    }

    @OnClick(R.id.btn_tixian)
    public void get(View view) {
        UIUtils.toast("您的提现申请已被成功受理。审核通过后，24小时内，你的钱自然会到账", false);
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeCurrentActivity();
            }
        },2000);
    }
}
