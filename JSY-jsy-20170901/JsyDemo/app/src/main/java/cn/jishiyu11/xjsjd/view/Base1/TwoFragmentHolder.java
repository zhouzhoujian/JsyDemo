package cn.jishiyu11.xjsjd.view.Base1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import cn.jishiyu11.xjsjd.EntityClass.Fragment2ListData;
import cn.jishiyu11.xjsjd.EntityClass.Fragment2LoanDetails;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData2;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData3;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData4;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalDataCertificatesActivity;
import cn.jishiyu11.xjsjd.activity.LogoActivity;
import cn.jishiyu11.xjsjd.activity.MainActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.CustomDialogUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.BaseViewHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jishiyu11.xjsjd.view.CustomDialog;
import okhttp3.Request;

@SuppressWarnings({"LoopStatementThatDoesntLoop", "UnusedAssignment"})
public class TwoFragmentHolder extends BaseViewHolder<Fragment2ListData> implements DataCallBack {

    private int PERSONALDATASTATUS = 0;
    private ViewGroup parent;

    private Context context;

    private ImageView two_item_icon;

    private LinearLayout two_item_je_layout, two_item_qx_layout;

    private TextView two_item_title, two_item_je, two_item_qx;

    private Button two_item_bt;

    private CustomDialog customDialog;

    private Fragment2LoanDetails fragment2LoanDetails;
    public TwoFragmentHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.second_item);
        this.parent = parent;
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setData(final Fragment2ListData object) {
        super.setData(object);
        if (object != null) {
            Glide.with(context)
                    .load(object.getIcon())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                            two_item_icon.setImageResource(R.mipmap.ic_path_in_load);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                            return false;
                        }
                    })
                    .into(two_item_icon);

            two_item_title.setText(object.getPlatform() + "");//设置产品名称
            two_item_je.setText(object.getAmount());//产品金额
            two_item_qx.setText(object.getLoanDate());//产品时间
            two_item_qx_layout.setOnClickListener(new View.OnClickListener() {//期限选择弹窗
                @Override
                public void onClick(View v) {
                    if (object.getIsEditLoanDate().equals("1")) {//1  允许修改  2  不允许

                    }
                }
            });
            two_item_je_layout.setOnClickListener(new View.OnClickListener() {//金额选择弹窗
                @Override
                public void onClick(View v) {
                    if (object.getIsEditAmount().equals("1")) {//1  允许修改  2  不允许

                    }
                }
            });
            two_item_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //进行数据校验工作
                    //进行申请判断操作
                    if (StringUtil.isNullOrEmpty( SharedPreferencesUtils.get( context, "uid", "" ).toString() )) {//判断是否登录    否
                        context.startActivity( new Intent( context, LogoActivity.class ) );
                    }else {
                        getFragment2DialogHttp(object.getId() + "");
                    }
                }
            });
        }
    }

    @Override
    public void onInitializeView() {
        super.onInitializeView();
        two_item_je_layout = findViewById(R.id.two_item_je_layout);
        two_item_qx_layout = findViewById(R.id.two_item_qx_layout);
        two_item_icon = findViewById(R.id.two_item_icon);
        two_item_title = findViewById(R.id.two_item_title);
        two_item_je = findViewById(R.id.two_item_je);
        two_item_qx = findViewById(R.id.two_item_qx);
        two_item_bt = findViewById(R.id.two_item_bt);
    }

    @Override
    public void onItemViewClick(Fragment2ListData object) {
    }

    private void getFragment2DialogHttp(String id) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(context, "uid", "").toString());
        map.put("id", id);
        OkHttpManager.postAsync(HttpURL.getInstance().FRAGMENT2_DATAS_DIALOG, "getFragment2DialogHttp", map, this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        ToatUtils.showShort1(context, context.getString(R.string.network_timed));
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name){
            case "postSureToGetMoney":

                break;
            case "getFragment2DialogHttp":
                JSONObject jsonObject = new JSONObject(result);
                String code = jsonObject.getString("code")+"";
                fragment2LoanDetails = new Fragment2LoanDetails();
            if (code.equals("0000")) {
                JSONObject object = jsonObject.getJSONObject("data");
                fragment2LoanDetails.setId(object.getString("id")+"");
                fragment2LoanDetails.setUid(object.getString("uid")+"");
                fragment2LoanDetails.setOrderCode(object.getString("orderCode")+"");
                fragment2LoanDetails.setPlatformId(object.getString("platformId")+"");
//                fragment2LoanDetails.setPlatform(object.getString("platform")+"");
                fragment2LoanDetails.setAmount(object.getString("amount")+"");
                fragment2LoanDetails.setPoundage(object.getString("poundage")+"");
                fragment2LoanDetails.setRate(object.getString("rate")+"");
                fragment2LoanDetails.setOverdueFine(object.getString("overdueFine")+"");
                fragment2LoanDetails.setPoundageType(object.getString("poundageType")+"");
                fragment2LoanDetails.setIsEditAmount(object.getString("isEditAmount")+"");
                fragment2LoanDetails.setIsEditLoanDate(object.getString("isEditLoanDate")+"");
                fragment2LoanDetails.setLoanDate(object.getString("loanDate")+"");
                fragment2LoanDetails.setRepaymentDate(object.getString("repaymentDate")+"");
                fragment2LoanDetails.setStatus(object.getString("status")+"");
                fragment2LoanDetails.setCreated_at(object.getString("created_at")+"");
                fragment2LoanDetails.setUpdated_at(object.getString("updated_at")+"");
                fragment2LoanDetails.setAccount(object.getString("account")+"");
                fragment2LoanDetails.setReal_name(object.getString("real_name")+"");
                fragment2LoanDetails.setBank_card(object.getString("bank_card")+"");
//                setFragment2DetailDialog(fragment2LoanDetails);
            }
            customDialog = setFragment2Dialog(context,fragment2LoanDetails);
            customDialog.show();

                break;
            case "personalDataStatus":

                JSONObject jsonObject2 =new JSONObject(result);
                JSONObject data = jsonObject2.getJSONObject("data");
                String status = data.get("status")+"";
                PERSONALDATASTATUS = Integer.parseInt(status);

                IsPersonalDataStatusSucces(fragment2LoanDetails.getAccount()+"",fragment2LoanDetails.getLoanDate()+"");

                break;

        }
    }

//    private void setFragment2DetailDialog(Fragment2LoanDetails fragment2DetailDialog){
//        MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_layout).setVisibility(View.VISIBLE);
//        TextView textViewName =MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_name);
//        textViewName.setText(SharedPreferencesUtils.get(context,"realname","")+"");
//        TextView textViewQx = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_qx);
//        textViewQx.setText(fragment2DetailDialog.getLoanDate()+"");
//        TextView textViewLiLv = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_lilv);
//        textViewLiLv.setText(fragment2DetailDialog.getRate()+"");
//        TextView textViewSxf = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_sxf);
//        textViewSxf.setText(fragment2DetailDialog.getPoundage()+"");
//        TextView textViewSjdz = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_dzje);
//        textViewSjdz.setText(fragment2DetailDialog.getAccount()+"");
//        TextView textViewGetName = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_getmoney_name);
//        textViewGetName.setText(fragment2DetailDialog.getReal_name()+"");
//        TextView textViewIdCardNum = MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_get_address);
//        textViewIdCardNum.setText(fragment2DetailDialog.getBank_card()+"");
//
//        MainActivity.mainActivity.findViewById(R.id.fragment2_dialog_details_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getPersonalDataStatus();
//            }
//        });
//    }

    private void postSureToGetMoney(String amount,String date){
        Map<String,Object> map = new HashMap<>();
        map.put("uid",SharedPreferencesUtils.get(context,"uid","")+"");
        map.put("amount",amount);
        map.put("loanDate",date);
        OkHttpManager.postAsync(HttpURL.getInstance().FRAGMENT2_DATAS_DIALOG_SURE,"postSureToGetMoney"
        ,map,this);
    }


    /**
     * 获取资料填写进度
     */
    private void getPersonalDataStatus(){
        Map<String,Object> map =new HashMap<>();
        map.put("uid",SharedPreferencesUtils.get( context, "uid", "" ));
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL_DATA_STATUS,"personalDataStatus"
                ,map,this);
    }

    /**
     * 根据资料填写进度跳转界面
     * */

    private void IsPersonalDataStatusSucces(String sjdz,String qx){
        Intent intent;
        if (PERSONALDATASTATUS ==0){
            intent = new Intent(context, PersonalDataCertificatesActivity.class);
            context.startActivity(intent);
        }else if (PERSONALDATASTATUS ==1){
            intent = new Intent(context, PersonalData2.class);
            context.startActivity(intent);
        }else if (PERSONALDATASTATUS ==2){
            intent = new Intent(context, PersonalData3.class);
            context. startActivity(intent);
        }else if (PERSONALDATASTATUS == 3){
            intent = new Intent(context, PersonalData4.class);
            context.startActivity(intent);
        }else if (PERSONALDATASTATUS == 4){
            postSureToGetMoney(sjdz,qx);
            ToatUtils.showShort1(context,"已提交申请");
        }
    }

    private CustomDialog setFragment2Dialog(Context context, Fragment2LoanDetails details) {//
        final CustomDialog dialog = new CustomDialog(context, R.style.add_dialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.fragmnent2_item_dialog, null);
        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        TextView textViewName =contentView.findViewById(R.id.fragment2_dialog_details_name);
        textViewName.setText(SharedPreferencesUtils.get(context,"realname","")+"");
        TextView textViewQx = contentView.findViewById(R.id.fragment2_dialog_details_qx);
        textViewQx.setText(details.getLoanDate()+"");
        TextView textViewLiLv = contentView.findViewById(R.id.fragment2_dialog_details_lilv);
        textViewLiLv.setText(details.getRate()+"");
        TextView textViewSxf = contentView.findViewById(R.id.fragment2_dialog_details_sxf);
        textViewSxf.setText(details.getPoundage()+"");
        TextView textViewSjdz = contentView.findViewById(R.id.fragment2_dialog_details_dzje);
        textViewSjdz.setText(details.getAccount()+"");
        TextView textViewGetName = contentView.findViewById(R.id.fragment2_dialog_details_getmoney_name);
        textViewGetName.setText(details.getReal_name()+"");
        TextView textViewIdCardNum = contentView.findViewById(R.id.fragment2_dialog_details_get_address);
        textViewIdCardNum.setText(details.getBank_card()+"");

        contentView.findViewById(R.id.fragment2_dialog_details_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        contentView.findViewById(R.id.fragment2_dialog_details_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPersonalDataStatus();
                dialog.dismiss();
            }
        });

        return dialog;
    }



}