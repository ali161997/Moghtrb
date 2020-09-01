package com.moghtrb.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moghtrb.models.MyLatLong;
import com.moghtrb.models.ServiceInfoModel;

import java.util.ArrayList;
import java.util.List;

public class InfoViewModel extends ViewModel {
    private MutableLiveData<List<ServiceInfoModel>> listInfo;
    private MutableLiveData<String> typeData;

    public InfoViewModel() {
        listInfo = new MutableLiveData<>(new ArrayList<>());
        typeData = new MutableLiveData<>();
    }

    public MutableLiveData<String> getTypeData() {
        return typeData;
    }

    public void setTypeData(MutableLiveData<String> typeData) {
        this.typeData = typeData;
    }

    public MutableLiveData<List<ServiceInfoModel>> getListInfo() {
        PrepareList();
        return listInfo;
    }

    public void setListInfo(MutableLiveData<List<ServiceInfoModel>> listInfo) {
        this.listInfo = listInfo;
    }

    private void PrepareList() {
        ServiceInfoModel model = new ServiceInfoModel("Homsany Koshry", "01153063449", new MyLatLong(), 5, 10, "https://www.e7kky.com/uploads/article/internal/images/%D8%A3%D8%B4%D9%87%D8%B1-%D9%85%D8%AD%D9%84%D8%A7%D8%AA-%D9%83%D8%B4%D8%B1%D9%8A-%D9%84%D8%AA%D8%B3%D8%AA%D9%85%D8%AA%D8%B9%D9%88%D8%A7-%D8%A8%D8%A3%D9%83%D9%84%D8%A9-%D9%85%D8%B5%D8%B1%D9%8A%D8%A9-%D8%B4%D8%B9%D8%A8%D9%8A%D8%A9.jpeg");
        listInfo.getValue().add(model);
    }

}
