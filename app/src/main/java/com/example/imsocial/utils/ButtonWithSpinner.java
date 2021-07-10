package com.example.imsocial.utils;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imsocial.R;

public class ButtonWithSpinner {
    private TextView button_text_view;
    private ProgressBar button_spinner_view;
    private View parentView;

    public ButtonWithSpinner(String title, View view) {
        button_text_view = view.findViewById(R.id.button_spinner_text);
        button_spinner_view = view.findViewById(R.id.button_spinner);
        parentView = view;

        button_text_view.setText(title);
    }

    public void setLoading(boolean loading) {
        if(loading) {
            button_spinner_view.setVisibility(View.VISIBLE);
            parentView.setClickable(false);
        } else {
            button_spinner_view.setVisibility(View.GONE);
            parentView.setClickable(true);
        }
    }

    public void setTitle(String title) {
        button_text_view.setText(title);
    }
}

