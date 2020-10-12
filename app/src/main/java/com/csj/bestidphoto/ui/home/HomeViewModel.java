package com.csj.bestidphoto.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csj.bestidphoto.ui.home.bean.NearHotBean;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<NearHotBean> photoModel;

    public HomeViewModel() {
        photoModel = new MutableLiveData<>();
    }

    public LiveData<NearHotBean> getPhotoModel() {
        return photoModel;
    }

    public void setPhotoModel(NearHotBean photoModel) {
        if(this.photoModel != null){
            this.photoModel.setValue(photoModel);
        }
    }
}