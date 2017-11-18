package cn.jishiyu11.xjsjd.adapter;

import android.content.Context;
import android.view.ViewGroup;

import cn.jishiyu11.xjsjd.EntityClass.Fragment2ListData;
import cn.jishiyu11.xjsjd.EntityClass.HomeProduct;
import cn.jishiyu11.xjsjd.view.Base1.TwoFragmentHolder;
import cn.jishiyu11.xjsjd.view.BaseViewHolder;
import cn.jishiyu11.xjsjd.view.RecyclerAdapter;

/**
 * Created by jsy_zj on 2017/11/6.
 */

public class TwoFragmengDataAdapter extends RecyclerAdapter<Fragment2ListData> {
    public TwoFragmengDataAdapter(Context context) {
        super(context);
    }
    @Override
    public BaseViewHolder<Fragment2ListData> onCreateBaseViewHolder(Context context, ViewGroup parent, int viewType) {
        return new TwoFragmentHolder(context,parent);
    }


}
