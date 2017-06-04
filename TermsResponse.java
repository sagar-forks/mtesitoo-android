package com.mtesitoo.backend.service;

import com.android.volley.VolleyError;
import com.mtesitoo.backend.model.Category;
import com.android.volley.Response;
import com.mtesitoo.backend.model.Terms;
import com.mtesitoo.backend.service.logic.ICallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gwen on 5/20/2017
 */
public class TermsResponse implements Response.Listener<String>, Response.ErrorListener {
    private ICallback<List<Terms>> mCallback;

    public TermsResponse(ICallback<List<Terms>> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mCallback.onError(error);
    }


}