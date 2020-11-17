package com.example.armmrk.ui.newdoc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.armmrk.R;


public class NewdocFragment extends androidx.fragment.app.Fragment {
    private NewdocViewModel newdocViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newdocViewModel =
                ViewModelProviders.of(this).get(NewdocViewModel.class);
        View root = inflater.inflate(R.layout.fragment_new_doc, container, false);
        final TextView textView = root.findViewById(R.id.text_newdoc);
        newdocViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

