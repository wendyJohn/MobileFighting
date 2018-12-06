package com.sanleng.mobilefighting.net;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.sanleng.mobilefighting.MyApplication;


public class VolleyRequestController {
    /**
     * Log or request TAG
     */
    public static final String TAG = "VolleyRequestController";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;
    /**
     * A singleton instance of the application class for easy access in other
     * places
     */
    private static VolleyRequestController sInstance;

    private VolleyRequestController() {
        mRequestQueue = getRequestQueue();
    }

    /**
     * @return ApplicationController singleton instance
     */
    public static synchronized VolleyRequestController getInstance() {
        if (sInstance == null) {
            sInstance = new VolleyRequestController();
        }
        return sInstance;
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(MyApplication.getInstance(), null);
        }
        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified then
     * it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     * @param shouldCache Cache or Not
     */
    public <T> void addToRequestQueue(Request<T> req, Object tag, boolean shouldCache) {
        req.setTag(tag);
        req.setShouldCache(shouldCache);
        req.setRetryPolicy(new DefaultRetryPolicy(
                2000,//默认超时时间，应设置�?个稍微大点儿的，例如本处�?500000
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认�?大尝试次�?
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueueWithoutCache(Request<T> req, Object tag) {
        addToRequestQueue(req, tag, false);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        addToRequestQueue(req, TAG, false);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important to
     * specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
