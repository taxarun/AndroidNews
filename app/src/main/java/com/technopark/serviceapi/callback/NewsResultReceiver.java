package com.technopark.serviceapi.callback;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

@SuppressLint("ParcelCreator")
class NewsResultReceiver extends ResultReceiver {
    private final int requestId;
    private ServiceHelper.NewsResultListener mListener;

    public NewsResultReceiver(int requestId, final Handler handler) {
        super(handler);
        this.requestId = requestId;
    }

    void setListener(final ServiceHelper.NewsResultListener listener) {
        mListener = listener;
    }

    @Override
    protected void onReceiveResult(final int resultCode, final Bundle resultData) {
        if (mListener != null) {
            final boolean success = (resultCode == NewsIntentService.RESULT_SUCCESS);
            final String result = (resultData.getString(NewsIntentService.EXTRA_NEWS_RESULT));
            mListener.onNewsResult(success, result);
        }
        String scheduled = (resultData.getString(NewsIntentService.SCHEDULED));
        while (scheduled == null)
        {
            System.out.println("WTF?");
            scheduled = (resultData.getString(NewsIntentService.SCHEDULED));
        }
        if (scheduled == null)
        {
            System.out.println("WTF?!!!!");
        }
        System.out.println(ServiceHelper.getInstance().getCountListeners() + " " + scheduled);
        if (!scheduled.equals(NewsIntentService.SCHEDULED))
            ServiceHelper.getInstance().removeListener(requestId);
        System.out.println(ServiceHelper.getInstance().getCountListeners() + " " + scheduled);
    }
}
