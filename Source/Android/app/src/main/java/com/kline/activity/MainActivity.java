package com.kline.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kline.R;
import com.kline.adpter.TestPagerAdapter;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener ,
        RadioGroup.OnCheckedChangeListener{

    private TestPagerAdapter adapter;
    private ViewPager pager;
    Context context;
    RadioGroup radioGroup;
    RadioButton self, range, mintime, kline, market, community;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 引入布局文件
        setContentView(R.layout.test_main);
        context = this;
        //初始化布局中的组件
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        self = (RadioButton) findViewById(R.id.self);
        range = (RadioButton) findViewById(R.id.range);
        mintime = (RadioButton) findViewById(R.id.mintime);
        kline = (RadioButton) findViewById(R.id.kline);
        market = (RadioButton) findViewById(R.id.market);
        community = (RadioButton) findViewById(R.id.community);
        // 查找初始化布局文件中的pager
        pager = (ViewPager) findViewById(R.id.pager);
        // Test必须要继承自FragmentActivity才能通过getSupportFragmentManager()对其初始化
        adapter = new TestPagerAdapter(getSupportFragmentManager());
        // 为paegr设置适配器
        pager.setAdapter(adapter);
        // 为Pager的页面改变添加事件监听
        pager.addOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        pager.setCurrentItem(0);
        self.setChecked(true);
    }

    // 实现OnPageChangeListener需要重写的一些方法
    @Override
    public void onPageScrollStateChanged(int state) {
        // 主要这里编写页面翻动之后需要执行的事件
        /** state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕 */
        if (state == 2) {
            switch (pager.getCurrentItem()) {
                case 0:
                    self.setChecked(true);
                    break;
                case 1:
                    range.setChecked(true);
                    break;
                case 2:
                    mintime.setChecked(true);
                    break;
                case 3:
                    kline.setChecked(true);
                    break;
                case 4:
                    market.setChecked(true);
                    break;
                case 5:
                    community.setChecked(true);
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // 页面滚动时执行
    }

    @Override
    public void onPageSelected(int arg0) {
        // 页面被选中之后执行
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // 判断当前选中的按钮的资源ID值，单选按钮触发页面翻页
        switch (checkedId) {
            case R.id.self:
                pager.setCurrentItem(0);
                break;
            case R.id.range:
                pager.setCurrentItem(1);
                break;
            case R.id.mintime:
                pager.setCurrentItem(2);
                break;
            case R.id.kline:
                pager.setCurrentItem(3);
                break;
            case R.id.market:
                pager.setCurrentItem(4);
                break;
            case R.id.community:
                pager.setCurrentItem(5);
                break;
        }
    }
}
