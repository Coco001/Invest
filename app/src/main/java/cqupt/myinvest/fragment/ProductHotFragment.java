package cqupt.myinvest.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cqupt.myinvest.R;
import cqupt.myinvest.bean.HotItem;
import cqupt.myinvest.bean.Name;
import cqupt.myinvest.bean.Pic;
import cqupt.myinvest.common.AppNetConfig;
import cqupt.myinvest.common.BaseFragment;
import cqupt.myinvest.utils.UIUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 投资界面的热门理财界面.
 */

public class ProductHotFragment extends BaseFragment {
    @Bind(R.id.flow_hot)
    RecyclerView mFlowHot;
    private List<Name> mNames;
    private List<Pic> mPics;
    private List<HotItem> mHotItems;
    private Bitmap mBitmap;

    @Override
    protected RequestParams getParams() {
        return new RequestParams();
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    protected void initData(String content){
        JSONObject object = JSON.parseObject(content);
        Boolean success = object.getBoolean("success");
        if (success) {
            mHotItems = new ArrayList<>();
            //LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.context);
            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
            mFlowHot.setLayoutManager(layoutManager);
            String dataArr = object.getString("dataArr");
            String picArr = object.getString("picArr");
            mNames = JSON.parseArray(dataArr, Name.class);
            mPics = JSON.parseArray(picArr, Pic.class);
            for (int i = 0; i < mNames.size(); i++) {
                HotItem item = new HotItem();
                try {
                    String path = mPics.get(i).IMAURL;
                    /*RequestCreator load = Picasso.with(this.getContext()).load(path);
                    Bitmap bitmap = load.get();*/
                    Bitmap bitmap = getBitmap(path);
                    item.setTp(bitmap);
                    item.setXm(mNames.get(i).name);
                    mHotItems.add(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FlowAdapter adapter = new FlowAdapter(mHotItems);
            mFlowHot.setAdapter(adapter);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_producthot;
    }

    class FlowAdapter extends RecyclerView.Adapter<FlowAdapter.ViewHolder> {
        List<HotItem> mHotLists;
        public FlowAdapter(List<HotItem> hotLists) {
            mHotLists = hotLists;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View mView;
            TextView mTextView;
            ImageView mImageView;
            public ViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                mTextView = (TextView) itemView.findViewById(R.id.item_hot_tv);
                mImageView = (ImageView) itemView.findViewById(R.id.item_hot_iv);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.toast(mHotLists.get(viewType).getXm(), false);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            HotItem item = mHotLists.get(position);
            holder.mTextView.setText(item.getXm());
            holder.mImageView.setImageBitmap(item.getTp());
        }

        @Override
        public int getItemCount() {
            return mHotLists.size();
        }
    }

    private Bitmap getBitmap(final String path) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(path).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException(" Unexpect code" + response);
                }
                //得到从网上获取资源，转换成我们想要的类型
                byte[] picture = response.body().bytes();
                mBitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            }
        });
        return mBitmap;
    }

}
