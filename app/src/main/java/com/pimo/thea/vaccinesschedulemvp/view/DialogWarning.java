package com.pimo.thea.vaccinesschedulemvp.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pimo.thea.vaccinesschedulemvp.R;

/**
 * Created by thea on 8/2/2017.
 */

public class DialogWarning extends Dialog {

    private OnButtonClickListener onButtonClickListener;
    private Button btnNotYes;
    private Button btnYes;

    public DialogWarning(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_warning);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btnYes = (Button) findViewById(R.id.dialog_warning_yes);
        btnNotYes = (Button) findViewById(R.id.dialog_warning_not_yes);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.onBtnYesClickListener();
            }
        });

        btnNotYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonClickListener.onBtnNotYesClickListener();
            }
        });
    }


    public interface OnButtonClickListener {
        void onBtnYesClickListener();
        void onBtnNotYesClickListener();
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
