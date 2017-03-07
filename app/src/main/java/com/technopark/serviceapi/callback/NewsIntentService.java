package com.technopark.serviceapi.callback;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class NewsIntentService extends IntentService {
    public final static String ACTION_NEWS = "action.NEWS";
    public final static String EXTRA_NEWS_RESULT_RECEIVER = "extra.EXTRA_NEWS_RESULT_RECEIVER";

    public final static int RESULT_SUCCESS = 1;
    public final static int RESULT_ERROR = 2;
    public final static String EXTRA_NEWS_RESULT = "extra.EXTRA_NEWS_RESULT";

    public NewsIntentService() {
        super("NewsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            System.out.println(action);
            if (ACTION_NEWS.equals(action)) {
                final ResultReceiver receiver = intent.getParcelableExtra(EXTRA_NEWS_RESULT_RECEIVER);
                handleActionYoda(receiver);
            }
        }
    }

    private void handleActionYoda(final ResultReceiver receiver) {
        final Bundle data = new Bundle();
        try {
            Context cont = this.getApplicationContext();
            Storage store = Storage.getInstance(cont);
            String topic = store.loadCurrentTopic();
            if (topic.equals("")) {
                topic = Topics.AUTO;
                store.saveCurrentTopic(topic);
            }
            NewsLoader loader = new NewsLoader();
            News test = loader.loadNews(topic);
            final String result = test.getTitle();
            if (result != null && !result.isEmpty()) {
                store.saveNews(test);
                data.putString(EXTRA_NEWS_RESULT, result);
                receiver.send(RESULT_SUCCESS, data);
            } else {
                data.putString(EXTRA_NEWS_RESULT, "result is null");
                receiver.send(RESULT_ERROR, data);
            }
        } catch (IOException ex) {
            data.putString(EXTRA_NEWS_RESULT, ex.getMessage());
            receiver.send(RESULT_ERROR, data);
        }
    }
}