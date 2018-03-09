package com.kline.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kline.activity.CreateOrderActivity;
import com.kline.R;
import com.kline.adapter.CartAdapter;
import com.kline.adapter.decoration.DividerItemDecoration;
import com.kline.bean.ShoppingCart;
import com.kline.http.OkHttpHelper;
import com.kline.log.HtLog;
import com.kline.utils.CartProvider;
import com.kline.widget.HtToolBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by mei on 2016/3/1.
 */
@ContentView(R.layout.fragment_cart)
public class CartFragment extends BaseFragment implements View.OnClickListener {

    private final String TAG = CartFragment.class.getSimpleName();
    private CartAdapter cartAdapter;
    private CartProvider cartProvider;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBoxAll;

    @ViewInject(R.id.text_total)
    private TextView mTotalText;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    @ViewInject(R.id.toolbar)
    protected HtToolBar mToolbar;

    private OkHttpHelper mOkHttpHelper;


    @Nullable
    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_cart, container, false);
        View view = x.view().inject(this, inflater, container);
        return view;
    }

    @Override
    public void init() {
        mOkHttpHelper = OkHttpHelper.getInstance();
        cartProvider = new CartProvider(getContext());
        showData();
    }

    @Override
    public void initToolBar() {
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mToolbar.getRightButton().setOnClickListener(this);
    }

    private void showData() {
        List<ShoppingCart> carts = cartProvider.getAll();

        if (carts != null && carts.size() > 0) {
            cartAdapter = new CartAdapter(getContext(), carts, mCheckBoxAll, mTotalText);

            HtLog.i("cartAdapter: " + cartAdapter + ", mRecyclerView:" + mRecyclerView);

            mRecyclerView.setAdapter(cartAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }
    }

    public void refreshData() {
        List<ShoppingCart> carts = cartProvider.getAll();
        if(cartAdapter == null) {
            cartAdapter = new CartAdapter(getContext(), carts, mCheckBoxAll, mTotalText);
        } else {
            cartAdapter.clearData();
        }
        cartAdapter.addData(carts);
        showToolbarEdit();
        cartAdapter.showTotalPrice();
    }

    // onAttach 当fragment添加到activity中时调用
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }



    public static final int ACTION_EDIT = 1;
    public static final int ACTION_DONE = 2;

    private void showToolbarEdit() {
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mTotalText.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        cartAdapter.checkAllOrNone(true);
        mCheckBoxAll.setChecked(true);
    }

    private void showToolbarDone() {
        mToolbar.setRightButtonText("完成");
        mToolbar.getRightButton().setTag(ACTION_DONE);
        mTotalText.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);

        cartAdapter.checkAllOrNone(false);
        mCheckBoxAll.setChecked(false);

    }

    @Override
    public void onClick(View view) {
        int action = (int) view.getTag();
        if (action == ACTION_EDIT) {
            showToolbarDone();
        } else if (action == ACTION_DONE) {
            showToolbarEdit();
            cartAdapter.showTotalPrice();
        }

    }

    @Event(type = View.OnClickListener.class, value = R.id.btn_del)
    public void deleteBtnOnClick(View view) {
        cartAdapter.deleteSelectedCart();
    }

    @Event(type = View.OnClickListener.class, value = R.id.btn_order)
    public void toOrder(View view) {
//        mOkHttpHelper.get(Contants.API.USER_DETAIL, new SpotsCallback<User>(getContext()) {
//            @Override
//            public void onSuccess(Response response, User user) {
//                Log.i(TAG, "onSuccess response.code():" + response.code());
//            }
//
//            @Override
//            public void onError(Response response, int code, Exception e) {
//                Log.i(TAG, "onError response.code():" + response.code());
//            }
//        });

        Intent intent = new Intent(getContext(), CreateOrderActivity.class);
        startActivity(intent, true);
    }

}
