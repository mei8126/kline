package com.kline.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kline.R;
import com.kline.Utils.AppConstants;
import com.kline.Utils.GetData;
import com.kline.Utils.Util;
import com.kline.activity.MainActivity;
import com.kline.adpter.DataAdapter;
import com.kline.bean.StockInfo;
import com.kline.widget.HVListView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 排名功能
 * Created by mei on 2018/3/8.
 */

public class RangeFragment extends BaseFragment {
    private Context context;
    private Handler handler;
    private LayoutInflater inflater;
    private Timer timer;

    private LinearLayout rootLayout;
    private TextView title;
    private TextView appName;
    private RelativeLayout bar;
    private Button refresh;
    private Button search;
    private HVListView hvListview;
    private TextView nameSort;
    private TextView newPriceSort;
    private TextView uprangeSort;
    private TextView updownSort;
    private TextView volumeSort;
    private TextView amountSort;
    private TextView openIntSort;
    private TextView vol2Sort;
    private TextView price2Sort;
    private TextView price3Sort;
    private TextView buy1Sort;
    private TextView sale1Sort;
    private TextView closeSort;
    private TextView openSort;
    private TextView highSort;
    private TextView lowSort;

    private PopupWindow popupWindow;
    private DataAdapter dataAdapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 引入布局文件
        View root = inflater.inflate(R.layout.fragment_range, container, false);
        // 全局化布局引入器
        this.inflater = inflater;
        // 全局化当前容器的上下文对象
        context = container.getContext();
        // handler用于处理消息提示界面更新操作等
        handler();
        // 初始化布局中的组件
        initView(root);
        // 为初始化了的组件绑定事件响应
        bindEvent();
        // 准备就绪标志
        isPrepared = true;
        // 延迟加载方法
        lazyLoad();
        return root;
    }

    @Override
    protected void lazyLoad() {
        // 判断当前的Fragment是否可见且就绪
        if (!isPrepared || !isVisible) {
            return;
        } else {
            if (timer != null)
                timer.cancel();
        }
        // 显示进度提示框
        Util.startProgressDialog(context);
        // 初始化当前界面上的一些数据显示（主题，字体等）
        AppConstants.lastPage = "range";
        initUI();
        // 数据刷新任务，专门用于定时刷新数据
        refreshTask();
    }

    //初始化布局中的组件
    private void initView(View view) {
        //根布局
        rootLayout = (LinearLayout) view.findViewById(R.id.root);
        //顶部标题
        title = (TextView) view.findViewById(R.id.title);
        //APP名称
        appName = (TextView) view.findViewById(R.id.app_name);
        //顶部菜单栏
        bar = (RelativeLayout) view.findViewById(R.id.bar);
        //刷新按钮
        refresh = (Button) view.findViewById(R.id.refresh);
        //搜索按钮
        search = (Button) view.findViewById(R.id.search);
        //listview数据显示列表
        hvListview = (HVListView) view.findViewById(R.id.hvlistview);
        //空视图:用于没有listview数据项时显示此空视图
        View empty = inflater.inflate(R.layout.emptyview, null);
        ((ViewGroup) hvListview.getParent()).addView(empty, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        //夜间模式空视图
        if (AppConstants.theme.contains("night")) {
            RelativeLayout emptyRoot = (RelativeLayout) empty
                    .findViewById(R.id.root);
            emptyRoot.setBackgroundColor(getResources().getColor(
                    R.color.listview_item));
        }
        //为listview设置空视图
        hvListview.setEmptyView(empty);
        //listview头部布局
        hvListview.mListHead = (LinearLayout) view.findViewById(R.id.head);
        //设置可点击
        hvListview.setClickable(true);
        //设置当前listview的滚动监听器，即将实现OnScrollListener后重写的事件响应绑定到此listview
        hvListview.setOnScrollListener(this);
        //初始化适配器
        dataAdapter = new DataAdapter();
        //为listview设置适配器
        hvListview.setAdapter(dataAdapter);
        //初始化列名布局文本
        nameSort = (TextView) view.findViewById(R.id.item01);
        newPriceSort = (TextView) view.findViewById(R.id.item2);
        uprangeSort = (TextView) view.findViewById(R.id.item3);
        updownSort = (TextView) view.findViewById(R.id.item4);
        volumeSort = (TextView) view.findViewById(R.id.item5);
        amountSort = (TextView) view.findViewById(R.id.item6);
        openIntSort = (TextView) view.findViewById(R.id.item7);
        vol2Sort = (TextView) view.findViewById(R.id.item8);
        price2Sort = (TextView) view.findViewById(R.id.item9);
        price3Sort = (TextView) view.findViewById(R.id.item10);
        buy1Sort = (TextView) view.findViewById(R.id.item11);
        sale1Sort = (TextView) view.findViewById(R.id.item12);
        closeSort = (TextView) view.findViewById(R.id.item13);
        openSort = (TextView) view.findViewById(R.id.item14);
        highSort = (TextView) view.findViewById(R.id.item15);
        lowSort = (TextView) view.findViewById(R.id.item16);
    }

    private void bindEvent() {
        //设置顶部市场名称
        title.setText(Configurations.nameMapMarket
                .get(Configurations.thisSelectedMarketPre) + "▼");
        //为市场名称文本添加点击事件
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示市场选择框
                showWindow(v);
            }
        });
        //刷新按钮点击事件
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动进度提示框
                Util.startProgressDialog(context);
                //新建子线程
                Timer timer = new Timer(false);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        //判断网络连接状态
                        if (!Util.isConnect(context)) {
                            handler.sendEmptyMessage(-1);
                            return;
                        } else {
                            try {
                                //获取数据
                                if (GetData.getRanges(context, sortIndex,
                                        AppConstants.thisMarket, false, false)) {
                                    //数据获取成功，向Handler发送消息,更新UI
                                    handler.sendEmptyMessage(1);
                                } else {
                                    //数据获取失败
                                    handler.sendEmptyMessage(-1);
                                }
                            } catch (Exception e) {
                                //产生异常
                                e.printStackTrace();
                                handler.sendEmptyMessage(-1);
                            }
                        }
                    }
                };
                //立即执行一次
                timer.schedule(task, 1);
            }
        });
        //搜索按钮点击事件
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置上一个页面
                AppConstants.lastPage = "range";
                //跳转到代码搜索页面
                startActivityForResult(new Intent(getActivity(), Search.class),
                        1);
            }
        });
        //名称列名点击事件
        nameSort.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //启动进度等待提示框
                Util.startProgressDialog(context);
                //修改界面显示
                sortIndex = 0;
                if ("名称&代码".equals(nameSort.getText())) {
                    nameSort.setText("↑名称&代码");
                } else if ("↑名称&代码".equals(nameSort.getText())) {
                    nameSort.setText("↓名称&代码");
                } else if ("↓名称&代码".equals(nameSort.getText())) {
                    nameSort.setText("名称&代码↑");
                } else if ("名称&代码↑".equals(nameSort.getText())) {
                    nameSort.setText("名称&代码↓");
                } else if ("名称&代码↓".equals(nameSort.getText())) {
                    nameSort.setText("↑名称&代码");
                }
                newPriceSort.setText("最新价");
                uprangeSort.setText("涨幅%");
                updownSort.setText("涨跌");
                volumeSort.setText("成交量");
                amountSort.setText("成交额");
                openIntSort.setText("持仓");
                vol2Sort.setText("现量");
                price2Sort.setText("结算价");
                price3Sort.setText("总笔或昨结算");
                buy1Sort.setText("买一");
                sale1Sort.setText("卖一");
                closeSort.setText("昨收");
                openSort.setText("开盘");
                highSort.setText("最高");
                lowSort.setText("最低");
                //按照当前列排序搜索代码实时数据信息
                refreshData();
            }
        });
    }

    private void initUI() {
        //设置夜间模式
        if (AppConstants.theme.equals("night")) {
            setNightTheme();
        }
        //设置字号
        if (AppConstants.fontsize.equals("big")) {
            setBigSize();
        } else if (AppConstants.fontsize.equals("small")) {
            setSmallSize();
        } else {
            setDefaultSize();
        }
    }

    private void refreshTask() {
        //新建全局的子线程，方便在外部使用timer.cancel停止该数据刷新任务
        timer = new Timer(false);
        //任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (Util.isConnect(context)) {
                    try {
                        // 股票排名数据请求
                        if (GetData.getRanges(context, sortIndex,
                                AppConstants.thisMarket, appendFlag, false))
                            //数据请求成功、发送获取成功的标志给handleMsg进行后续处理
                            handler.sendEmptyMessage(1);
                        else {
                            //数据获取失败
                            AppConstants.message = "获取排名数据失败！";
                            handler.sendEmptyMessage(-1);
                        }
                    } catch (Exception e) {
                        //获取数据过程中产生异常
                        e.printStackTrace();
                        handler.sendEmptyMessage(-1);
                    }
                } else {
                    //网络连接异常
                    AppConstants.message = "网络异常！";
                    handler.sendEmptyMessage(-1);
                }
            }
        };
        if (Util.isWeekend()) {
            //周末启动APP则不进行数据刷新
            timer.schedule(task, new Date());
        } else
            //其他时间启动APP,执行间断刷新
            timer.schedule(task, new Date(), AppConstants.listRefresh
                    + AppConstants.Interval);
    }


    private void handler() {
        //初始化并重写handleMessage方法
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //根据消息标志显示消息或者对界面进行数据更新
                if (msg.what == 1) {
                    //通知Adapter数据更新
                    dataAdapter.refreshDataAdapter();
                }
                //如果消息串中不为空则显示消息内容
                if (!"".equals(AppConstants.message)) {
                    if (AppConstants.message.contains("4014")) {
                        AppConstants.message = "所有数据加载完毕!";
                    }
                    Toast.makeText(context, AppConstants.message,
                            Toast.LENGTH_SHORT).show();
                }
                //清空消息内容并关闭进度提示框
                AppConstants.message = "";
                Util.stopProgressDialog();
            }
        };
    }


    // 弹出列表选择式的市场切换弹出框
    private void showWindow(View parent) {
        // 获取窗口管理器对象
        WindowManager windowManager = (WindowManager) getActivity()
                .getSystemService(Context.WINDOW_SERVICE);
        // 判断popWindow对象是否为空
        if (popupWindow == null) {
            // 引入弹出框的布局文件
            View view = inflater.inflate(R.layout.group_list, null);
            // 初始化布局中的listview组件
            listview = (ListView) view.findViewById(R.id.lvGroup);
            // 找到当前选中的数据的索引位置
            for (int i = 0; i < Configurations.marketNameList.size(); i++) {
                String temp = Configurations.marketNameList.get(i);
                if (Configurations.thisSelectedMarketPre.equals(Configurations
                        .getPreFromNameString(temp))) {
                    AppConstants.thisPopSelectedPosition = i;
                    break;
                }
            }
            // 弹出框中的listview的数据适配器初始化
            groupAdapter = new GroupAdapter(getActivity(),
                    Configurations.marketNameList);
            // 为listview设置数据适配器
            listview.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象，窗口的宽度和高度均设置为屏幕宽度和高度的1/3
            popupWindow = new PopupWindow(view, windowManager
                    .getDefaultDisplay().getWidth() / 3, windowManager
                    .getDefaultDisplay().getHeight() / 3);
        }
        // 使其可聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 显示
        popupWindow.showAsDropDown(parent, -50, 0);
        // 为窗口中的listview添加单行点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                // 获取点击的市场，修改市场
                tempText = Configurations.marketNameList.get(position);
                Configurations.lastSelectedMarketPre = Configurations.thisSelectedMarketPre;
                Configurations.thisSelectedMarketPre = Configurations
                        .getPreFromNameString(tempText);
                if (!Configurations.thisSelectedMarketPre
                        .equals(Configurations.lastSelectedMarketPre)) {
                    // 如果点击的市场与当前显示的市场不同，那么就提示当前数据的适配器更新数据显示
                    AppConstants.thisPopSelectedPosition = position;
                    groupAdapter.notifyDataSetChanged();
                    // 显示进度等待提示框
                    Util.startProgressDialog(context);
                    // 设置顶部市场的文本显示内容
                    title.setText(Configurations.nameMapMarket
                            .get(Configurations.thisSelectedMarketPre) + "▼");
                    noMoreDatas = false;
                }
                // 关闭popwindow
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                // 新建子线程获取最新选中的市场的排名数据信息
                Timer timer = new Timer(false);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            if (GetData.getRanges(context, sortIndex,
                                    Configurations.thisSelectedMarketPre,
                                    false, false)) {
                                // 向Handler发送消息,更新UI
                                handler.sendEmptyMessage(1);
                            } else {
                                handler.sendEmptyMessage(-1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(-1);
                        }
                    }
                };
                timer.schedule(task, 1);
            }
        });
    }

    class FlingListener implements GestureDetector.OnGestureListener {

        StockInfo item;
        ViewHolder holder;
        //手指点击并立即弹起的手势触发
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //修改默认的显示代码
            AppConstants.showSymbol = getItem().getSymbol();
            for (int index = 0; index < GetData.stockInfos.size(); index++) {
                //找到数据的索引位置
                if (GetData.stockInfos.get(index).getSymbol()
                        .equals(AppConstants.showSymbol)) {
                    AppConstants.thisNumberofIndex = index;
                    break;
                }
            }
            //设置数据源和MainActivity需要显示的页面
            AppConstants.srcPage = "range";
            AppConstants.showPage = "mintime";
            //取消定时刷新数据方法
            if (timer != null)
                timer.cancel();
            //发送消息标志2给MainActivity,重新设置显示的页面
            MainActivity.handler.sendEmptyMessage(2);
            return false;
        }
    }
}
