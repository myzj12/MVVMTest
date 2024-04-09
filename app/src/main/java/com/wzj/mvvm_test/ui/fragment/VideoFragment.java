package com.wzj.mvvm_test.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzj.mvvm_test.R;
import com.wzj.mvvm_test.databinding.VideoFragmentBinding;
import com.wzj.mvvm_test.viewmodels.VideoViewModel;


public class VideoFragment extends BaseFragment {

    private VideoFragmentBinding binding;

    public static VideoFragment newInstance() {
        return new VideoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         binding = DataBindingUtil.inflate(inflater, R.layout.video_fragment, container, false);
         return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        VideoViewModel mViewModel = new ViewModelProvider(this).get(VideoViewModel.class);
    }
}