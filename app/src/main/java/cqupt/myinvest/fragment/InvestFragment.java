package cqupt.myinvest.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cqupt.myinvest.R;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.utils.UIUtils;

/**
 * InvestFragment界面显示的fragment
 */

public class InvestFragment extends BaseFragment {
    @Bind(R.id.iv_title_left)
    ImageView mIvTitleLeft;
    @Bind(R.id.tv_title_name)
    TextView mTvTitleName;
    @Bind(R.id.iv_title_right)
    ImageView mIvTitleRight;
    @Bind(R.id.vp_invest)
    ViewPager mVpInvest;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    private List<Fragment> mFragmentList = new ArrayList<>();

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
        //1.加载三个不同的Fragment：ProductListFragment,ProductRecommondFragment,ProductHotFragment.
        initFragments();
        //2.ViewPager设置三个Fragment的显示
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        mVpInvest.setAdapter(adapter);
        //3.将TabPagerIndicator与ViewPager关联
        mTabLayout.setupWithViewPager(mVpInvest);
    }

    private void initFragments() {
        ProductListFragment productListFragment = new ProductListFragment();
        ProductRecommondFragment productRecommondFragment = new ProductRecommondFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();
        //添加到集合中
        mFragmentList.add(productListFragment);
        mFragmentList.add(productRecommondFragment);
        mFragmentList.add(productHotFragment);
    }

    @Override
    protected void initTitle() {
        mIvTitleLeft.setVisibility(View.GONE);
        mIvTitleRight.setVisibility(View.GONE);
        mTvTitleName.setText("投资");
    }

    @Override
    public int getLayoutId() {
        return R.layout.investfragment;
    }

    /**
     * 提供PagerAdapter的实现
     * 如果ViewPager中加载的是Fragment,则提供的Adpater可以继承于具体的：FragmentStatePagerAdapter或FragmentPagerAdapter
     * FragmentStatePagerAdapter:适用于ViewPager中加载的Fragment过多，会根据最近最少使用算法，实现内存中Fragment的清理，避免溢出
     * FragmentPagerAdapter:适用于ViewPager中加载的Fragment不多时，系统不会清理已经加载的Fragment。
     */
    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return UIUtils.getStringArr(R.array.invest_tab)[position];
        }
    }
}
