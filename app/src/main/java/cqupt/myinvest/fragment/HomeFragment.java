package cqupt.myinvest.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import cqupt.myinvest.R;
import cqupt.myinvest.bean.Image;
import cqupt.myinvest.bean.Index;
import cqupt.myinvest.bean.Product;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.ui.WavaProgress;

/**
 * homeFragment界面显示的fragment
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.banner)
    Banner mBanner;
    @Bind(R.id.tv_home_product)
    TextView mTvHomeProduct;
    @Bind(R.id.wavePro_home)
    WavaProgress mWaveProHome;
    @Bind(R.id.tv_home_yearrate)
    TextView mTvHomeYearrate;
    private Index mIndex;
    private int currentProress;

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    protected void initData(String content) {
        if (!TextUtils.isEmpty(content)) {
            mIndex = new Index();
            //使用fastJson解析得到json数据，将其封装到Java对象中
            JSONObject object = JSON.parseObject(content);
            String proinfo = object.getString("proInfo");
            Product product = JSON.parseObject(proinfo, Product.class);
            String imageArr = object.getString("imageArr");
            List<Image> images = JSON.parseArray(imageArr, Image.class);
            mIndex.product = product;
            mIndex.images = images;
            //更新页面数据
            mTvHomeProduct.setText(product.name);
            mTvHomeYearrate.setText(product.yearRate + "%");
            //获取数据中的进度值
            currentProress = Integer.parseInt(mIndex.product.progress);
            new Thread(runnable).start();
            //设置banner样式
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器
            mBanner.setImageLoader(new GlideImageLoader());
            //设置图片地址构成的集合
            ArrayList<String> imagesUrl = new ArrayList<>(mIndex.images.size());
            for (int i = 0; i < mIndex.images.size(); i++) {
                imagesUrl.add(mIndex.images.get(i).IMAURL);
            }
            mBanner.setImages(imagesUrl);
            //设置banner动画效果
            mBanner.setBannerAnimation(Transformer.Accordion);
            //设置标题集合（当banner样式有显示title时）
            String[] titles = new String[]{"砍价我最行", "人脉总动员", "想不到你是这样的app", "疯狂购物节"};
            mBanner.setBannerTitles(Arrays.asList(titles));
            //设置自动轮播，默认为true
            mBanner.isAutoPlay(true);
            //设置轮播时间
            mBanner.setDelayTime(1500);
            //设置指示器位置（当banner模式中有指示器时）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            //banner设置方法全部调用完毕时最后调用
            mBanner.start();
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < currentProress; i++) {
                mWaveProHome.setPercent(i + 1);
                SystemClock.sleep(20);
                //强制重绘
//                roundProHome.invalidate();//只有主线程才可以如此调用
                mWaveProHome.postInvalidate();//主线程、分线程都可以如此调用
            }
        }
    };

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.GONE);
        mIvTitleRight.setVisibility(View.GONE);
        mTvTitleName.setText("首页");
    }

    @Override
    public int getLayoutId() {
        return R.layout.homefragment;
    }

}
