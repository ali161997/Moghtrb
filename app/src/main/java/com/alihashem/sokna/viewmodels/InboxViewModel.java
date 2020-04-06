package com.alihashem.sokna.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alihashem.sokna.models.Notification;

import java.util.ArrayList;
import java.util.List;

public class InboxViewModel extends ViewModel {
    private MutableLiveData<List<Notification>> notyList;
    private boolean notySeen;

    public InboxViewModel() {
        notyList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Notification>> getNotyList() {
        preparedList();
        return notyList;
    }

    public void setNotyList(MutableLiveData<List<Notification>> notyList) {
        this.notyList = notyList;
    }

    private void preparedList() {
        List<Notification> list = new ArrayList<>();
        list.add(new Notification("العنوان الاول ", "التفاصيل1", "title1", "body1"));
        list.add(new Notification("العنوان الثانى ", "التفاصل2", "title2", "body2"));
        list.add(new Notification("العنوان الثالث ", "التفاصل2", "title3", "body3"));
        notyList.setValue(list);

    }

}
