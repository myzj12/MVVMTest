package com.wzj.mvvm_test.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.databinding.ItemVideoBinding;
import com.wzj.mvvm_test.model.VideoResponse;

import java.util.List;

public class VideoAdapter extends BaseQuickAdapter<VideoResponse.ResultBean, BaseDataBindingHolder<ItemVideoBinding>> {
    public VideoAdapter( @Nullable List<VideoResponse.ResultBean> data) {
        super(R.layout.item_video, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemVideoBinding> bindingHolder, VideoResponse.ResultBean resultBean) {
        ItemVideoBinding binding = bindingHolder.getDataBinding();
        if(binding!=null){
            binding.setVideo(resultBean);
            binding.executePendingBindings();
        }
    }
}
