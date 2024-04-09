package com.wzj.mvvm_test.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.ui.adapter.WallPaperAdapter;
import com.wzj.mvvm_test.viewmodels.MainViewModel;

public class MainActivity extends BaseActivity {

    private MainViewModel mainViewModel;
    private com.wzj.mvvm_test.databinding.ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //创建ViewModel对象
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //网络请求
        mainViewModel.getBiying();
        //返回数据是更新ViewModel,ViewModel更新则xml更新
        mainViewModel.biying.observe(this, biYingResponse -> {
            dataBinding.setViewModel(mainViewModel);
        });
        initView();
        mainViewModel.getWallPaper();
        //一旦LiveData中的数据发生改变,就会执行这里
        //也就是说,当我们调用了   mainViewModel.getWallPaper(); 这行代码时,就回去网络上获取数据
        //然后我们通过observe添加了数据变化监听器,当拿到数据之后,这里就会执行
        //然后我们设置了适配器并且将获取到的数据传进去了
        mainViewModel.wallPaper.observe(this, wallPaperResponse -> dataBinding.rv.setAdapter(new WallPaperAdapter(wallPaperResponse.getRes().getVertical())));
    }

    /**
     * 初始化
     */
    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        dataBinding.rv.setLayoutManager(manager);

        //伸缩偏移量监听
        dataBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    dataBinding.toolbarLayout.setTitle("MVVM-Demo");
                    isShow = true;
                } else if (isShow) {//展开时
                    dataBinding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });

        //设置滚动监听器,
        dataBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scroolY, oldScrollX, oldScrollY) -> {
            if(scroolY>oldScrollY){
                //上滑
                dataBinding.fabHome.hide();
            }else{
                //下滑
                dataBinding.fabHome.show();
            }
        });


    }
    public void toHome(View view){
        jumpActivity(HomeActivity.class);
    }
}