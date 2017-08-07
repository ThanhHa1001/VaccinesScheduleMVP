package com.pimo.thea.vaccinesschedulemvp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 7/23/2017.
 */

public class DialogLoading extends Dialog {
    private TextView txtNotification;

    public DialogLoading(Context context, String textNotification) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        txtNotification = (TextView) findViewById(R.id.txt_dialog_loading);
        txtNotification.setText(textNotification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setTitle(String title) {
        txtNotification.setText(title);
    }
}
