package com.technopark.serviceapi.callback;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.Hashtable;
import java.util.Map;

class ServiceHelper {
    private int mIdCounter = 2;
    private int mScheduledListener = 1;
    private final Map<Integer, NewsResultReceiver> mResultReceivers = new Hashtable<>();

    private static ServiceHelper instance;

    private ServiceHelper() {
    }

    synchronized static ServiceHelper getInstance() {
        if (instance == null) {
            instance = new ServiceHelper();
        }
        return instance;
    }

    int getCountListeners()
    {
        return mResultReceivers.size();
    }

    int downloadNewsProcess(final Context context, final NewsResultListener listener) {
        final NewsResultReceiver receiver = new NewsResultReceiver(mIdCounter, new Handler());
        receiver.setListener(listener);
        mResultReceivers.put(mIdCounter, receiver);

        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction(NewsIntentService.ACTION_NEWS);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_RESULT_RECEIVER, receiver);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_TEXT, "test");
        context.startService(intent);

        return mIdCounter++;
    }

    Intent getIntent(final Context context, final NewsResultListener listener) {
        final NewsResultReceiver receiver = new NewsResultReceiver(mIdCounter, new Handler());
        receiver.setListener(listener);
        mResultReceivers.put(mScheduledListener, receiver);

        Intent intent = new Intent(context, NewsIntentService.class);
        intent.setAction(NewsIntentService.ACTION_NEWS);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_RESULT_RECEIVER, receiver);
        intent.putExtra(NewsIntentService.EXTRA_NEWS_TEXT, NewsIntentService.SCHEDULED);
        context.startService(intent);

        return intent;
    }

    void removeListener(final int id) {
        NewsResultReceiver receiver = mResultReceivers.remove(id);
        if (receiver != null) {
            receiver.setListener(null);
        }
    }

    interface NewsResultListener {
        void onNewsResult(final boolean success, final String result);
    }

}
