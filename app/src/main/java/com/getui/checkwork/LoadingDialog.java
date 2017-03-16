package com.getui.checkwork;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ProgressBar;


/**
 * Created by wang on 16/6/25.
 */
public class LoadingDialog extends Dialog {
    ProgressBar progressView;

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        progressView = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
