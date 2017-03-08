package com.technopark.serviceapi.callback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.technopark.serviceapi.R;
import com.technopark.serviceapi.SettingsActivity;

import java.util.Date;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Scheduler;
import ru.mail.weather.lib.Storage;

public class CallbackActivity extends AppCompatActivity implements ServiceHelper.NewsResultListener {
    private int mRequestId;
    private TextView mHeader;
    private TextView mPostData;
    private TextView mDate;
    private Button mButton;
    private CallbackActivity activity;
    private Intent mIntent;

    private final View.OnClickListener onButtonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_settings:
                    final Intent intent = new Intent(CallbackActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_send:
                    if (mRequestId == 0) {
                        mRequestId = ServiceHelper.getInstance().downloadNewsProcess(activity, activity);
                    } else {
                        Snackbar.make(activity.findViewById(R.id.news_content),
                                "Too much pending requests", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callback);
        activity = this;
        findViewById(R.id.btn_settings).setOnClickListener(onButtonClick);
        mHeader = (TextView) findViewById(R.id.head);
        mPostData = (TextView) findViewById(R.id.all_text);
        mDate = (TextView) findViewById(R.id.date);
        mButton = (Button) this.findViewById(R.id.btn_send);
        mButton.setOnClickListener(onButtonClick);
        Context test = this.getApplicationContext();
        Storage store = Storage.getInstance(test);
        News data = store.getLastSavedNews();
        if (data == null) {
            mRequestId = ServiceHelper.getInstance().downloadNewsProcess(activity, activity);
        }
        else {
            mHeader.setText(data.getTitle());
            mPostData.setText(data.getBody());
            Date time = new Date(data.getDate());
            mDate.setText("Created at: " + time);
        }
    }

    @Override
    protected void onStop() {
        ServiceHelper.getInstance().removeListener(mRequestId);
        super.onStop();
    }

    @Override
    public void onNewsResult(final boolean success, final String result) {
        System.out.println("worked");
        mRequestId = 0;
        Context test = this.getApplicationContext();
        Storage store = Storage.getInstance(test);
        News data = store.getLastSavedNews();
        if (data == null)
        {
            mHeader.setText("Error :(");
            mPostData.setText("We have no cached data to show you" +
                              " and your connection appears to be offline :(");
            mDate.setText("");
        }
        else {
            mHeader.setText(data.getTitle());
            mPostData.setText(data.getBody());
            Date time = new Date(data.getDate());
            mDate.setText("Created at: " + time);
        }
        if (!success) {
            Snackbar.make(this.findViewById(R.id.news_content), "Can't come online", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
    }

    public void onCheckboxClicked(View view) {
        CheckBox check = (CheckBox) this.findViewById(R.id.chbx_update);
        if (check.isChecked()) {
            System.out.println("CHECKED");
            mIntent = ServiceHelper.getInstance().getIntent(activity, activity);
            Scheduler.getInstance().schedule(this, mIntent, 60000);

        }
        else {
            Scheduler.getInstance().unschedule(this, mIntent);
            ServiceHelper.getInstance().removeScheduledListener();
        }
    }
}
