package com.wzj.mvvm_test.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.databinding.ItemImageBinding;
import com.wzj.mvvm_test.db.bean.WallPaper;

import java.util.List;

public class ImageAdapter extends BaseQuickAdapter<WallPaper, BaseDataBindingHolder<ItemImageBinding>> {
    public ImageAdapter(@Nullable List<WallPaper> data) {
        super(R.layout.item_image, data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemImageBinding> bindingHolder, WallPaper wallPaper) {
        if (wallPaper == null) {
            return;
        }
        ItemImageBinding binding = bindingHolder.getDataBinding();
        if(binding!=null){
            binding.setWallPaper(wallPaper);
            binding.executePendingBindings();
        }
    }
}
