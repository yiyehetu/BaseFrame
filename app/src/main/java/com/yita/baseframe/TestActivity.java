package com.yita.baseframe;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class TestActivity extends BaseActivity {
    private TextView textView;
    private Button button;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initContentViews(View contentView) {
        textView = (TextView) contentView.findViewById(R.id.textView);
        button = (Button) contentView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData();
            }
        });
    }

    @Override
    protected void initData() {
        requestData();
    }

    @Override
    protected void requestData() {
        showLoadingView();
        new Task().execute();
    }

    class Task extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return new Random().nextInt(2) + "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (TextUtils.equals(s, "1")) {
                showContentView();
                textView.setText(s);
            } else {
                showOtherView();
            }
        }
    }
}
