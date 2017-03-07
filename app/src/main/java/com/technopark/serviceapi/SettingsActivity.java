package com.technopark.serviceapi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class SettingsActivity extends AppCompatActivity {

    public void onRadioButtonClicked(View view) {
        Context test = this.getApplicationContext();
        Storage store = Storage.getInstance(test);
        switch(view.getId()) {
            case R.id.radio_auto:
                store.saveCurrentTopic(Topics.AUTO);
                break;
            case R.id.radio_it:
                store.saveCurrentTopic(Topics.IT);
                break;
            case R.id.radio_health:
                store.saveCurrentTopic(Topics.HEALTH);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        RadioButton radio;
        Context test = this.getApplicationContext();
        Storage store = Storage.getInstance(test);
        String state = store.loadCurrentTopic();
        if (state.equals(Topics.AUTO)) {
            radio = (RadioButton) findViewById(R.id.radio_auto);
            radio.setChecked(true);
        }
        else if (state.equals(Topics.IT)) {
            radio = (RadioButton) findViewById(R.id.radio_it);
            radio.setChecked(true);
        }
        else if (state.equals(Topics.HEALTH)) {
            radio = (RadioButton) findViewById(R.id.radio_health);
            radio.setChecked(true);
        }
    }
}
