package com.example.temantravellingtourguide.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.temantravellingtourguide.Activity.TourGuideActivity;
import com.example.temantravellingtourguide.R;
import com.google.android.material.button.MaterialButton;

public class PaymentConfirmationDialog extends DialogFragment {
    private MaterialButton btnSelesai;

    public PaymentConfirmationDialog() {
        // Constructor kosong diperlukan untuk DialogFragment.
        // Pastikan tidak memberikan argument/parameter apapun ke
        // constructor ini.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_confirmation, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSelesai = view.findViewById(R.id.btn_selesai);

        btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TourGuideActivity) getActivity()).closeThisOrder();
                getDialog().dismiss();
            }
        });

        setCancelable(false);
    }
}
