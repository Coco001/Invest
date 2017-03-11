package cqupt.myinvest.adapter;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cqupt.myinvest.R;
import cqupt.myinvest.bean.Product;
import cqupt.myinvest.ui.RoundProgress;
import cqupt.myinvest.utils.UIUtils;

/**
 * baseadapter装配数据的holder.
 */
public class MyHolder extends BaseHolder<Product> {

    @Bind(R.id.p_name)
    TextView pName;
    @Bind(R.id.p_money)
    TextView pMoney;
    @Bind(R.id.p_yearlv)
    TextView pYearlv;
    @Bind(R.id.p_suodingdays)
    TextView pSuodingdays;
    @Bind(R.id.p_minzouzi)
    TextView pMinzouzi;
    @Bind(R.id.p_minnum)
    TextView pMinnum;
    /*@Bind(R.id.wavePro_home)
    WavaProgress mWaveProHome;*/
    @Bind(R.id.roundPro_home)
    RoundProgress mRoundProHome;


    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_product_list, null);
    }

    @Override
    protected void refreshData() {
        Product data = this.getData();

        //装数据
        pMinnum.setText(data.memberNum);
        pMinzouzi.setText(data.minTouMoney);
        pMoney.setText(data.money);
        pName.setText(data.name);
//        mWaveProHome.setPercent(Integer.parseInt(data.progress));
        mRoundProHome.setProgress(Integer.parseInt(data.progress));
        pSuodingdays.setText(data.suodingDays);
        pYearlv.setText(data.yearRate);

    }
}
