package cn.jishiyu11.xjsjd.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.FaceDetector;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jishiyu11.xjsjd.EntityClass.Fragment2ListData;
import cn.jishiyu11.xjsjd.EntityClass.HomeProduct;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData4;
import cn.jishiyu11.xjsjd.adapter.TwoFragmengDataAdapter;
import cn.jishiyu11.xjsjd.base.BaseFragment;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.interfaces.Action;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import cn.jishiyu11.xjsjd.view.RefreshRecyclerView;
import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/23.
 * <p>
 * 我的贷款
 */
@SuppressWarnings({"deprecation", "ConstantConditions", "ResultOfMethodCallIgnored"})
@SuppressLint({"ValidFragment", "InflateParams"})
public class TwoFragment extends BaseFragment implements View.OnClickListener,DataCallBack {

    private  int PERSONALDATASTATUS = 0;
    private Activity mActivity;
    private RefreshRecyclerView mRecyclerView;
    private List<Map<String,Object>> mineLoadLists;
    private Handler mHandler;
    private TwoFragmengDataAdapter mAdapter;
    private Fragment2ListData[] VirtualData;
    private List<Fragment2ListData> ListData;

    private int page = 1;




    public TwoFragment() {
        super();
    }

    public TwoFragment(Activity activity) {
        super( activity );
        this.mActivity = activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        this.mActivity = (Activity) context;
    }

    @Override
    protected int getLayout() {
        return R.layout.fra_twofragment;
    }

    @Override
    protected void initView() {
        mHandler = new Handler();
        mRecyclerView = getmRootView().findViewById(R.id.two_recycler_view);
        mRecyclerView.setSwipeRefreshColors(0xFF437845, 0xFFE44F98, 0xFF2FAC21);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mAdapter = new TwoFragmengDataAdapter(mActivity);
        mRecyclerView.setAdapter(mAdapter);


        mRecyclerView.setRefreshAction(new Action() {
            @Override
            public void onAction() {
                getData(true);
            }
        });

        mRecyclerView.post(new Runnable() {//下拉刷新
            @Override
            public void run() {
                mRecyclerView.showSwipeRefresh();
                getHttpPRODUCTCATELIST();
                getData(true);
            }
        });
    }


    public void getData(final boolean isRefresh) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                mAdapter.clear();
                mAdapter.addAll(VirtualData);
                mRecyclerView.dismissSwipeRefresh();
                mRecyclerView.getRecyclerView().scrollToPosition(0);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        getHttpPRODUCTCATELIST();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged( hidden );
        if (!hidden) {
            onResume();
        }

        if (!StringUtil.isNullOrEmpty( SharedPreferencesUtils.get( mActivity, "uid", "" ).toString() )) {//判断是否登录    否
            getPersonalDataStatus();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd( "TwoFragment" );
    }


    private void getHttpPRODUCTCATELIST() {
        Map<String,Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(mActivity,"uid",""));
        OkHttpManager.postAsync(HttpURL.getInstance().FRAGMENT2_DATAS, "getHttpPRODUCTCATELIST", map, this);
    }


    @Override
    public void requestFailure(Request request, String name, IOException e) {
        if (name.equals("getHttpPRODUCTCATELIST")){
            ToatUtils.showShort1(mActivity,"获取数据失败！");
        }
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name){
            case "getHttpPRODUCTCATELIST":
                JSONObject jsonObject = new JSONObject(result);
                String msg = jsonObject.getString("msg");
                if (msg.equals("成功")) {
                    JSONArray data = jsonObject.getJSONArray("data");
                    getListBean(data);

                }
                break;

            case "personalDataStatus":

                JSONObject jsonObject1 =new JSONObject(result);
                JSONObject data = jsonObject1.getJSONObject("data");
                String status = data.get("status")+"";
                PERSONALDATASTATUS = Integer.parseInt(status);
                break;
        }
    }

    private void getListBean(JSONArray array){
        VirtualData = new Fragment2ListData[array.length()];
        Fragment2ListData fragment2ListData;
        JSONObject object;
        for (int i =0; i< array.length();i++){
            try {
                object = array.getJSONObject(i);
                fragment2ListData = new Fragment2ListData();
                fragment2ListData.setId(object.getString("id"));
                fragment2ListData.setUid(object.getString("uid"));
                fragment2ListData.setPlatform(object.getString("platform"));
                fragment2ListData.setAmount(object.getString("amount"));
                fragment2ListData.setLoanDate(object.getString("loanDate"));
                fragment2ListData.setIsEditAmount(object.getString("isEditAmount"));
                fragment2ListData.setIsEditLoanDate(object.getString("isEditLoanDate"));
                fragment2ListData.setIcon(HttpURL.getInstance().HTTP_URL_PATH+object.getString("icon"));

                VirtualData[i] = fragment2ListData;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.clear();
            mAdapter.addAll(VirtualData);
            mAdapter.notifyDataSetChanged();
        }
    }
    /**
     * 获取资料填写进度
     */
    private void getPersonalDataStatus(){
        Map<String,Object> map =new HashMap<>();
        map.put("uid",SharedPreferencesUtils.get( mActivity, "uid", "" ));
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL_DATA_STATUS,"personalDataStatus"
                ,map,this);
    }


}
