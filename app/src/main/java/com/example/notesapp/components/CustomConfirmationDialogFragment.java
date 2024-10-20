package com.example.notesapp.components;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.notesapp.R;
import com.example.notesapp.databinding.CustomConfirmationDialogBinding;
import com.example.notesapp.enums.DialogTypeEnum;
import com.example.notesapp.utils.GlobalVariables;

public class CustomConfirmationDialogFragment extends DialogFragment implements View.OnClickListener {

    private CustomConfirmationDialogBinding binding;
    private OnConfirmedListener onConfirmedListener;
    private String confirmationMessage;
    private DialogTypeEnum dialogTypeEnum;

    public CustomConfirmationDialogFragment(OnConfirmedListener onConfirmedListener, String confirmationMessage, DialogTypeEnum dialogTypeEnum) {
        this.onConfirmedListener = onConfirmedListener;
        this.confirmationMessage = confirmationMessage;
        this.dialogTypeEnum = dialogTypeEnum;
    }

    public interface OnConfirmedListener {
        void onPressConfirmed();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        binding = CustomConfirmationDialogBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        binding.btnNegative.setOnClickListener(this);
        binding.btnPositive.setOnClickListener(this);

        updateDialogUI();


        return dialog;
    }

    private void updateDialogUI() {
        // Confirmation Message
        binding.tvConfirmationMessage.setText(confirmationMessage);
        switch (dialogTypeEnum) {
            case SUCCESS: {
                binding.ivDialogIcon.setImageResource(R.drawable.ic_done);
                binding.btnNegative.setVisibility(View.GONE);
                binding.btnPositive.setText(getResources().getString(R.string.STR_OK));
                break;
            }
            case ALERT: {
                binding.ivDialogIcon.setImageResource(R.drawable.ic_alert);
                binding.btnNegative.setVisibility(View.VISIBLE);
                binding.btnPositive.setText(getResources().getString(R.string.STR_YES));
                break;
            }
            default: {
                binding.ivDialogIcon.setImageResource(R.drawable.ic_info);
                binding.btnPositive.setVisibility(View.GONE);
                binding.btnNegative.setText(getResources().getString(R.string.STR_OK));
                binding.btnNegative.setBackground(ContextCompat.getDrawable(GlobalVariables.getInstance().getMainActivity().getApplicationContext(), R.drawable.rounded_corners_button_default));
                break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_negative) {
            dismiss();
            return;
        }
        if (v.getId() == R.id.btn_positive) {
            if (onConfirmedListener != null)
                onConfirmedListener.onPressConfirmed();
            dismiss();
        }
    }
}
