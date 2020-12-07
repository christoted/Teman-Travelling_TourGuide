package com.example.temantravellingtourguide.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.temantravellingtourguide.Activity.TourGuideActivity;
import com.example.temantravellingtourguide.R;
import com.google.android.material.button.MaterialButton;

public class ConfirmCancelationDialog extends DialogFragment {
    private MaterialButton btnTidak;
    private TextView tvYa;

    public ConfirmCancelationDialog() {
        // Constructor kosong diperlukan untuk DialogFragment.
        // Pastikan tidak memberikan argument/parameter apapun ke
        // constructor ini.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_confirm_cancelation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnTidak = view.findViewById(R.id.btn_tidak);
        tvYa = view.findViewById(R.id.tv_ya);

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        tvYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //remove the progress from onProgress Collection
                ((TourGuideActivity) getActivity()).cancelThisOrder();
                getDialog().dismiss();
            }
        });
    }
}
