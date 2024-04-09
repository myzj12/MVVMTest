package com.wzj.mvvm_test.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

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
            binding.setOnClick(new ClickBinding());
            binding.executePendingBindings();
        }
    }

    public static class ClickBinding{
        public void itemClick(VideoResponse.ResultBean resultBean, View view){
            if(resultBean.getShare_url()!=null){
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(resultBean.getShare_url())));
            }else {
                Toast.makeText(view.getContext(), "视频地址为空", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
