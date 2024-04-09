package com.wzj.mvvm_test.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wzj.mvvm_test.model.VideoResponse;
import com.wzj.mvvm_test.repository.VideoRepository;

public class VideoViewModel extends BaseViewModel {
    public LiveData<VideoResponse> video;

    public void getVideo() {
        VideoRepository videoRepository = new VideoRepository();
        failed = videoRepository.failed;
        video = videoRepository.getVideo();
    }
}
