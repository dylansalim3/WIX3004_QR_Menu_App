package com.dylansalim.qrmenuapp.ui.component;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dylansalim.qrmenuapp.R;

public class ConfirmDialog extends Dialog {

    View.OnClickListener listener;
    Button button;
    String dialogText = "Dialog Text";
    String buttonText = "OK";

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_confirm_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);

        button = findViewById(R.id.dialog_button);
        button.setText(buttonText);
        button.setOnClickListener(view -> {
            if (listener != null) {
                listener.onClick(view);
            }
            dismiss();
        });
        ((TextView) findViewById(R.id.dialog_text)).setText(dialogText);
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
