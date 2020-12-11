package com.dylansalim.qrmenuapp.ui.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.dylansalim.qrmenuapp.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RatingDialog extends AppCompatDialogFragment {
    private EditText mEditText;
    private RatingDialogListener mRatingDialogListener;
    private RatingBar mRatingBar;
    private final String NEGATIVE_BTN_TEXT = "Cancel";
    private final String POSITIVE_BTN_TEXT = "Submit";

    public RatingDialog(RatingDialogListener mRatingDialogListener) {
        this.mRatingDialogListener = mRatingDialogListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_rating_dialog, null);
        builder.setView(view)
                .setNegativeButton(NEGATIVE_BTN_TEXT, (dialogInterface, i) -> {
                })
                .setPositiveButton(POSITIVE_BTN_TEXT, (dialogInterface, i) -> {
                    String text = mEditText.getText().toString();
                    float rating = mRatingBar.getRating();
                    mRatingDialogListener.submitReviewDialog(text, rating);
                });
        mEditText = view.findViewById(R.id.et_rating_dialog);
        mRatingBar = view.findViewById(R.id.rating_bar_rating_dialog);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mRatingDialogListener = (RatingDialog.RatingDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface RatingDialogListener {
        void submitReviewDialog(String text, float rating);
    }
}
