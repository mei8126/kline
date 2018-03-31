package com.kline.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kline.common.Contants;
import com.kline.common.HtReaderApp;
import com.kline.R;
import com.kline.city.CityModel;
import com.kline.city.DistrictModel;
import com.kline.city.ProvinceModel;
import com.kline.city.XmlParserHandler;
import com.kline.http.OkHttpHelper;
import com.kline.http.SpotsCallback;
import com.kline.msg.BaseRespMsg;
import com.kline.widget.ClearEditText;
import com.kline.widget.HtToolBar;


import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

@ContentView(R.layout.activity_address_add)
public class AddressAddActivity extends BaseActivity {

    private OptionsPickerView mCityPickerView;

    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.edittxt_consignee)
    private ClearEditText mEditConsignee;

    @ViewInject(R.id.edittxt_phone)
    private ClearEditText mEditPhone;

    @ViewInject(R.id.edittxt_add)
    private ClearEditText mEditAddr;

    @ViewInject(R.id.toolbar)
    private HtToolBar mToolBar;



    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_address_add);
        x.view().inject(this);
        initToolbar();
        init();
    }

    @Event(type = View.OnClickListener.class,value = R.id.ll_city_picker)
    public void showCityPickerViwe(View view) {
        mCityPickerView.show();
    }


    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddress();
            }
        });

    }

    private void init() {
        initProvinceDatas();
        mCityPickerView = new OptionsPickerView(this);
        mCityPickerView.setPicker((ArrayList) mProvinces, mCities, mDistricts, true);
        mCityPickerView.setTitle("选择城市");
        mCityPickerView.setCyclic(false, false, false);
        mCityPickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String address = mProvinces.get(options1).getName() + " "
                        + mCities.get(options1).get(option2) + " "
                        + mDistricts.get(options1).get(option2).get(options3);
                mTxtAddress.setText(address);
            }
        });
    }

    private void initProvinceDatas() {
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析sml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            mProvinces = handler.getDataList();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {

        }
        if(mProvinces != null) {
            for(ProvinceModel province:mProvinces) {
                List<CityModel> cities = province.getCityList();
                // 城市list
                ArrayList<String> cityStrs = new ArrayList<>(cities.size());
                for(CityModel city:cities) {
                    // 把城市名称放入cityStrs
                    cityStrs.add(city.getName());

                    // 一个城市对应的区县
                    List<DistrictModel> districtList = city.getDistrictList();
                    ArrayList<String> districtStrs = new ArrayList<>(districtList.size());
                    for(DistrictModel d:districtList) {
                        districtStrs.add(d.getName());
                    }

                    // 地区
                    ArrayList<ArrayList<String>> district = new ArrayList<>();
                    district.add(districtStrs);
                    mDistricts.add(district);
                }
                mCities.add(cityStrs);
            }

        }
    }

    public void createAddress() {
        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mTxtAddress.getText().toString() + mEditAddr.getText().toString();

        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", HtReaderApp.getInstance().getUser().getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", address);
        params.put("zip_code", "000000");
        mHttpHelper.post(Contants.API.ADDRESS_CREATE, params, new SpotsCallback<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == baseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


}
