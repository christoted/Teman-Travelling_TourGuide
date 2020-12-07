package com.example.temantravellingtourguide.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.temantravellingtourguide.Activity.EditProfileActivity;
import com.example.temantravellingtourguide.Activity.TourGuideActivity;
import com.example.temantravellingtourguide.R;
import com.google.android.material.button.MaterialButton;

public class FillDataDialog extends DialogFragment {
    private MaterialButton btnFillData;

    public FillDataDialog() {
        // Constructor kosong diperlukan untuk DialogFragment.
        // Pastikan tidak memberikan argument/parameter apapun ke
        // constructor ini.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_fill_data, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnFillData = view.findViewById(R.id.btn_fill_data);

        btnFillData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                startActivity(new Intent(getContext(), EditProfileActivity.class));
            }
        });
    }
}
