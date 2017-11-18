package cn.jishiyu11.xjsjd.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.interfaces.Action;

/**
 * vvguolaing 2017-6-23
 * <p>
 * RefreshRecyclerView view 视图
 */
public class RefreshRecyclerView extends FrameLayout {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private boolean loadMoreAble;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.view_refresh_recycler, this);
        mRecyclerView = view.findViewById(R.id.$_recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.$_refresh_layout);
        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
        boolean refreshAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_refresh_able, true);
        loadMoreAble = typedArray.getBoolean(R.styleable.RefreshRecyclerView_load_more_able, true);
        if (!refreshAble) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    public void setAdapter(RecyclerAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        mAdapter = adapter;
        mAdapter.loadMoreAble = loadMoreAble;
    }

    public void setLayoutManager(final RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int type = mAdapter.getItemViewType(position);
                    if (type == RecyclerAdapter.HEADER_TYPE
                            || type == RecyclerAdapter.FOOTER_TYPE
                            || type == RecyclerAdapter.STATUS_TYPE) {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public void setRefreshAction(final Action action) {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                action.onAction();
            }
        });
    }

    public void setLoadMoreAction(final Action action) {
        if (mAdapter.isShowNoMore || !loadMoreAble) {
            return;
        }
        mAdapter.loadMoreAble = true;
        mAdapter.setLoadMoreAction(action);
    }

    public void showNoMore() {
        mAdapter.showNoMore();
    }

    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public TextView getNoMoreView() {
        return mAdapter.mNoMoreView;
    }

    public void setSwipeRefreshColorsFromRes(@ColorRes int... colors) {
        mSwipeRefreshLayout.setColorSchemeResources(colors);
    }

    /**
     * 8位16进制数 ARGB
     */
    public void setSwipeRefreshColors(@ColorInt int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void showSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    public void dismissSwipeRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
