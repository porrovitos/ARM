package com.example.armmrk.ui.newdoc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewdocViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public NewdocViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is newdoc fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
