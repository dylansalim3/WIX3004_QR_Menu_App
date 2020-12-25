package com.dylansalim.qrmenuapp.ui.component;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.dylansalim.qrmenuapp.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SingleEditTextDialog extends AppCompatDialogFragment {
    private EditText mEditText;
    private EditTextDialogListener listener;
    private String title;
    private String hint;
    private String negativeBtnText;
    private String positiveBtnText;

    public static boolean openStatus = false;

    public SingleEditTextDialog(String title, String hint, String negativeBtnText, String positiveBtnText) {
        this.title = title;
        this.hint = hint;
        this.negativeBtnText = negativeBtnText;
        this.positiveBtnText = positiveBtnText;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_single_edit_text_dialog, null);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openStatus = false;
                    }
                })
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String text = mEditText.getText().toString();
                        listener.applyTexts(text);
                        openStatus = false;
                    }
                });
        mEditText = view.findViewById(R.id.et_single_edit_text_dialog);
        mEditText.setHint(hint);
        openStatus = true;
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (EditTextDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface EditTextDialogListener {
        void applyTexts(String text);
    }
}
