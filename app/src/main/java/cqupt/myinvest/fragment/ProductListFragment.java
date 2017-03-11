package cqupt.myinvest.fragment;

import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.Bind;
import cqupt.myinvest.R;
import cqupt.myinvest.adapter.ProductAdapter;
import cqupt.myinvest.bean.Product;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.ui.MyTextView;

/**
 * 投资界面的全部理财产品界面.
 */

public class ProductListFragment extends BaseFragment {

    @Bind(R.id.tv_product_title)
    MyTextView mTvProductTitle;
    @Bind(R.id.lv_product_list)
    ListView mLvProductList;
    private List<Product> mProductList;

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    protected void initData(String content) {
        JSONObject object = JSON.parseObject(content);
        Boolean success = object.getBoolean("success");
        if (success) {
            String data = object.getString("data");
            mProductList = JSON.parseArray(data, Product.class);
            ProductAdapter productAdapter = new ProductAdapter(mProductList);
            mLvProductList.setAdapter(productAdapter);//显示列表
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_productlist;
    }

}
