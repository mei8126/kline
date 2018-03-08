package com.kline.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kline.activity.AddressListActivity;
import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.activity.LoginActivity;
import com.kline.activity.MyFavoriteActivity;
import com.kline.activity.MyOrderActivity;
import com.kline.R;
import com.kline.bean.User;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mei on 2016/3/1.
 */
public class MineFragment extends BaseFragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;
    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;
    @ViewInject(R.id.btn_logout)
    private Button mBtnLogout;

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void init() {
        User user = HtReaderApp.getInstance().getUser();
        showUser(user);
    }

    @OnClick(value = {R.id.img_head, R.id.txt_username})
    public void toLogin(View view) {
        if(HtReaderApp.getInstance().getUser() == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);
        }
    }
    @OnClick(R.id.btn_logout)
    public void logout(View view) {
        HtReaderApp.getInstance().clearUser();
        showUser(null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user = HtReaderApp.getInstance().getUser();
        showUser(user);
    }

    private void showUser(User user) {
        if(user != null) {
            if(!TextUtils.isEmpty(user.getLogo_url())) {
                showHeadImage(user.getLogo_url());
            }
            mTxtUserName.setText(user.getUserName());
            mBtnLogout.setVisibility(View.VISIBLE);
        } else {
            mTxtUserName.setText("点击登录");
            mBtnLogout.setVisibility(View.GONE);
        }
    }

    private void showHeadImage(String url) {
        Picasso.with(getContext()).load(url).into(mImageHead);
    }

    @OnClick(R.id.txt_my_orders)
    public void toMyOrderActivity(View view){

        startActivity(new Intent(getActivity(), MyOrderActivity.class), true);
    }


    @OnClick(R.id.txt_my_address)
    public void toAddressActivity(View view){

        startActivity(new Intent(getActivity(), AddressListActivity.class),true);
    }

    @OnClick(R.id.txt_my_favorite)
    public void toFavoriteActivity(View view){

        startActivity(new Intent(getActivity(), MyFavoriteActivity.class), true);
    }
}
