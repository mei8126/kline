package com.kline.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.R;
import com.kline.adapter.FullyLinearLayoutManager;
import com.kline.adapter.WareOrderAdapter;
import com.kline.bean.Charge;
import com.kline.bean.ShoppingCart;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;
import com.kline.msg.BaseRespMsg;
import com.kline.msg.CreateOrderRespMsg;
import com.kline.utils.CartProvider;
import com.kline.utils.GsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pingplusplus.android.PaymentActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import okhttp3.Response;

@ContentView(R.layout.activity_create_order)
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 百度支付渠道
     */
    private static final String CHANNEL_BFB = "bfb";
    /**
     * 京东支付渠道
     */
    private static final String CHANNEL_JDPAY_WAP = "jdpay_wap";

    @ViewInject(R.id.txt_order)
    private TextView txtOrder;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;


    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;


    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;

    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBd;

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;


    private CartProvider cartProvider;

    private WareOrderAdapter mAdapter;



    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private String orderNum;
    private String payChannel = CHANNEL_ALIPAY;
    private float amount;
    private HashMap<String,RadioButton> channels = new HashMap<>(3);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_create_order);
        x.view().inject(this);
        showData();
        init();
    }

    private void init(){
        channels.put(CHANNEL_ALIPAY,mRbAlipay);
        channels.put(CHANNEL_WECHAT,mRbWechat);
        channels.put(CHANNEL_BFB,mRbBd);
        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);
        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥" + amount);
    }

    public void showData(){

        cartProvider = new CartProvider(this);
        mAdapter = new WareOrderAdapter(this,cartProvider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        if(cartProvider.getAll().size() > 0) {
            mBtnCreateOrder.setEnabled(true);
        } else {
            mBtnCreateOrder.setEnabled(false);
        }

    }

    @Override
    public void onClick(View v) {
        selectPayChannle(v.getTag().toString());
    }

    public void selectPayChannle(String paychannel){

        for (Map.Entry<String,RadioButton> entry:channels.entrySet()){
            payChannel = paychannel;
            RadioButton rb = entry.getValue();
            if(entry.getKey().equals(paychannel)){
                boolean isCheck = rb.isChecked();
                rb.setChecked(!isCheck);
            }
            else
                rb.setChecked(false);
        }
    }

    @Event(type = View.OnClickListener.class, value = R.id.btn_createOrder)
    public void createNewOrder(View view){
        postNewOrder();
    }

    private void postNewOrder(){
        final List<ShoppingCart> carts = mAdapter.getDatas();
        List<WareItem> items = new ArrayList<>(carts.size());
        for (ShoppingCart c:carts ) {
            WareItem item = new WareItem(c.getId(),(int)c.getPrice());
            items.add(item);
        }
        String item_json = GsonUtils.toJsonStr(items);
        Map<String,Object> params = new HashMap<>(5);
        params.put("user_id", HtReaderApp.getInstance().getUser().getId()+"");
        params.put("item_json",item_json);
        params.put("pay_channel",payChannel);
        params.put("amount",(int)amount+"");
        params.put("addr_id", 1 + "");
        mBtnCreateOrder.setEnabled(false);

        okHttpHelper.post(Contants.API.ORDER_CREATE, params, new SpotsCallback<CreateOrderRespMsg>(this) {
            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {
//                cartProvider.
                mBtnCreateOrder.setEnabled(true);
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();
                openPaymentActivity(GsonUtils.toJsonStr(charge));
            }
            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);
            }
        });
    }



    class WareItem {
        private  Long ware_id;
        private  int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    private void openPaymentActivity(String charge){

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, Contants.REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Contants.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");

                if (result.equals("success"))
                    changeOrderStatus(1);
                else if (result.equals("fail"))
                    changeOrderStatus(-1);
                else if (result.equals("cancel"))
                    changeOrderStatus(-2);
                else
                    changeOrderStatus(0);

            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
//                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
//                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

            }
        }
    }

    private void changeOrderStatus(final int status){

        Map<String,Object> params = new HashMap<>(5);
        params.put("order_num",orderNum);
        params.put("status",status+"");

        okHttpHelper.post(Contants.API.ORDER_COMPLEPE, params, new SpotsCallback<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {

                toPayResultActivity(status);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);
            }
        });

    }

    private void toPayResultActivity(int status){

        Intent intent = new Intent(this,PayResultActivity.class);
        intent.putExtra("status", status);

        startActivity(intent);
        this.finish();

    }
}
