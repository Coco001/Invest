package cqupt.myinvest.adapter;

import java.util.List;

/**
 * 理财产品的adapter.
 */

public class ProductAdapter extends MyBaseAdapter {

    public ProductAdapter(List list) {
        super(list);
    }

    @Override
    protected BaseHolder getHolder() {
        return new MyHolder();
    }
}
