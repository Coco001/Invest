package cqupt.myinvest.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cqupt.myinvest.AboutActivity;
import cqupt.myinvest.GestureEditActivity;
import cqupt.myinvest.R;
import cqupt.myinvest.UserRegistActivity;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseActivity;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.utils.UIUtils;

/**
 * MoreFragment界面显示的fragment
 */

public class MoreFragment extends BaseFragment {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.tv_more_regist)
    TextView mTvMoreRegist;
    @Bind(R.id.toggle_more)
    ToggleButton mToggleMore;
    @Bind(R.id.tv_more_reset)
    TextView mTvMoreReset;
    @Bind(R.id.tv_more_phone)
    TextView mTvMorePhone;
    @Bind(R.id.rl_more_contact)
    RelativeLayout mRlMoreContact;
    @Bind(R.id.tv_more_fankui)
    TextView mTvMoreFankui;
    @Bind(R.id.tv_more_share)
    TextView mTvMoreShare;
    @Bind(R.id.tv_more_about)
    TextView mTvMoreAbout;
    private String department = "不明确";
    private SharedPreferences sp;

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected void initData(String content) {
        //初始化SharedPreferences
        sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);
        //用户注册
        userResgist();
        //获取当前设置手势密码的ToggleButton的状态
        getGestureStatus();
        //设置手势密码
        setGesturePassword();
        //重置手势密码
        resetGesture();
        //联系客服
        contactService();
        //提交反馈意见
        commitFanKui();
        //分享
        share();
        //关于硅谷理财
        aboutInvest();
    }

    private void aboutInvest() {
        mTvMoreAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).setupActivity(AboutActivity.class, null);
            }
        });
    }

    private void share() {
        mTvMoreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("世界上最遥远的距离，是我在if里你在else里，似乎一直相伴又永远分离；\\\\n\\\" +\\n\" +\n" +
                "                \"        \\\"     世界上最痴心的等待，是我当case你是switch，或许永远都选不上自己；\\\\n\\\" +\\n\" +\n" +
                "                \"        \\\"     世界上最真情的相依，是你在try我在catch。无论你发神马脾气，我都默默承受，静静处理。到那时，再来期待我们的finally。");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("https://github.com/Coco001");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this.getActivity());
    }
    private void commitFanKui() {
        mTvMoreFankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提供一个View
                View view = View.inflate(MoreFragment.this.getActivity(), R.layout.view_fankui, null);
                final RadioGroup rg = (RadioGroup) view.findViewById(R.id.rg_fankui);
                final EditText et_fankui_content = (EditText) view.findViewById(R.id.et_fankui_content);

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb = (RadioButton) rg.findViewById(checkedId);
                        department = rb.getText().toString();
                    }
                });

                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setView(view)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取反馈的信息
                                String content = et_fankui_content.getText().toString();
                                //联网发送反馈信息
                                AsyncHttpClient client = new AsyncHttpClient();
                                String url = AppNetConfig.FEEDBACK;
                                RequestParams params = new RequestParams();
                                params.put("department", department);
                                params.put("content", content);
                                client.post(url, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(String content) {
                                        UIUtils.toast("发送反馈信息成功", false);
                                    }

                                    @Override
                                    public void onFailure(Throwable error, String content) {
                                        UIUtils.toast("发送反馈信息失败", false);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void contactService() {
        mRlMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setTitle("联系客服")
                        .setMessage("是否现在联系客服：15670338365")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取手机号码
                                String phone = mTvMorePhone.getText().toString().trim();
                                //使用隐式意图，启动系统拨号应用界面
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phone));
                                if (ActivityCompat.checkSelfPermission(MoreFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                MoreFragment.this.getActivity().startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void resetGesture() {
        mTvMoreReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取按钮的状态，判断事都开启
                boolean checked = mToggleMore.isChecked();
                if (checked) {
                    ((BaseActivity) MoreFragment.this.getActivity()).setupActivity(GestureEditActivity.class, null);
                } else {
                    UIUtils.toast("手势密码操作已关闭，请开启后再设置", false);
                }
            }
        });
    }

    private void setGesturePassword() {
        mToggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String inputCode = sp.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {//之前没有设置过
                        new AlertDialog.Builder(MoreFragment.this.getActivity())
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIUtils.toast("现在设置手势密码", false);
                                        sp.edit().putBoolean("isOpen", true).commit();
                                        //开启新的activity:
                                        ((BaseActivity) MoreFragment.this.getActivity()).setupActivity(GestureEditActivity.class, null);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIUtils.toast("取消了现在设置手势密码", false);
                                        sp.edit().putBoolean("isOpen", false).commit();
                                        mToggleMore.setChecked(false);
                                    }
                                }).show();
                    } else {
                        UIUtils.toast("开启手势密码", false);
                        sp.edit().putBoolean("isOpen", true).commit();
                    }
                } else {
                    UIUtils.toast("关闭了手势密码", false);
                    sp.edit().putBoolean("isOpen", false).commit();
                }
            }
        });
    }

    //用户注册
    private void userResgist() {

        mTvMoreRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).setupActivity(UserRegistActivity.class, null);
            }
        });
    }

    private void getGestureStatus() {
        boolean isOpen = sp.getBoolean("isOpen", false);
        mToggleMore.setChecked(isOpen);

    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.GONE);
        mIvTitleRight.setVisibility(View.GONE);
        mTvTitleName.setText("更多");
    }

    @Override
    public int getLayoutId() {
        return R.layout.morefragment;
    }

}
