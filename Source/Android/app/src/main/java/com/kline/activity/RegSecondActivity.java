package com.kline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.R;
import com.kline.bean.User;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;
import com.kline.msg.LoginRespMsg;
import com.kline.utils.CountTimerView;
import com.kline.utils.DESUtil;
import com.kline.utils.ManifestUtil;
import com.kline.utils.ToastUtils;
import com.kline.widget.ClearEditText;
import com.kline.widget.HtToolBar;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

@ContentView(R.layout.activity_reg_second)
public class RegSecondActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private HtToolBar mToolBar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.btn_reSend)
    private Button mBtnResend;

    @ViewInject(R.id.edittxt_code)
    private ClearEditText mEtCode;

    private String phone;
    private String pwd;
    private String countryCode;

    private CountTimerView countTimerView;
    private SpotsDialog dialog;
    private OkHttpHelper okHttpHelper  = OkHttpHelper.getInstance();
    private  SMSEventHanlder evenHanlder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_reg_second);
        x.view().inject(this);

        initToolbar();
        checkCode();
    }

    private void checkCode(){
        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);
        String text = getString(R.string.smssdk_send_mobile_detail)+formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));
        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();
        SMSSDK.initSDK(this, ManifestUtil.getMetaDataValue(this, "mob_sms_appKey"),
                ManifestUtil.getMetaDataValue(this, "mob_sms_appSecrect"));
        evenHanlder = new SMSEventHanlder();
        SMSSDK.registerEventHandler(evenHanlder);
        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this,"正在校验验证码");

    }


    private String splitPhoneNum(String phoneNum) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for(int i = 4, len = builder.length(); i < len; i+= 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }



    private void initToolbar() {
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCode();
            }
        });
    }

    private void submitCode(){
        String vCode = mEtCode.getText().toString().trim();

        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_write_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode, phone, vCode);
        dialog.show();
    }


    @Event(type = View.OnClickListener.class, value = R.id.btn_reSend)
    public void reSendCode(View view) {
        SMSSDK.getVerificationCode("+"+countryCode, phone);
        countTimerView = new CountTimerView(mBtnResend,R.string.smssdk_resend_identify_code);
        countTimerView.start();
        dialog.setMessage("正在重新获取验证码");
        dialog.show();
    }

    private void doReg() {
        Map<String,Object> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        okHttpHelper.post(Contants.API.REG, params, new SpotsCallback<LoginRespMsg<User>>(this) {


            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();

                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    ToastUtils.show(RegSecondActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }
                HtReaderApp application = HtReaderApp.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());

                startActivity(new Intent(RegSecondActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }

            @Override
            public void onTokenError(Response response, int code) {
                super.onTokenError(response, code);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

    class SMSEventHanlder extends EventHandler {


        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {



            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(dialog !=null && dialog.isShowing())
                        dialog.dismiss();

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {


//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);


                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();;
                        }
                    } else {

                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;

                            JSONObject object = new JSONObject(
                                    throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });
        }
    }

}
