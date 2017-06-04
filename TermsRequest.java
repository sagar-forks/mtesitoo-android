package com.mtesitoo.backend.service;

import android.content.Context;
import android.net.Uri;

import com.mtesitoo.backend.R;
import com.mtesitoo.backend.model.AuthorizedStringRequest;
import com.mtesitoo.backend.model.Terms;
import com.mtesitoo.backend.model.URL;
import com.mtesitoo.backend.model.Unit;
import com.mtesitoo.backend.model.header.Authorization;
import com.mtesitoo.backend.model.url.TermsURL;
import com.mtesitoo.backend.model.url.VendorURL;
import com.mtesitoo.backend.service.logic.ICallback;
import com.mtesitoo.backend.service.logic.ITermsRequest;

import java.util.List;

/**
 * Created by Gwen on 5/20/2017.
 */

public class TermsRequest extends Request implements ITermsRequest
{

    public TermsRequest(Context context)
    {
        super(context);
        mILoginRequest = new LoginRequest(mContext);
    }


    @Override
    public void getTerms(ICallback<List<Terms>> callback) {

        URL url = new TermsURL(mContext, R.string.path_common_vendorterms);
        TermsResponse response = new TermsResponse(callback);//no variable after calback - don't need?
        AuthorizedStringRequest stringRequest = new AuthorizedStringRequest(mContext, com.android.volley.Request.Method.GET, url.toString(), response, response);

        stringRequest.setAuthorization(new Authorization(mContext, mAuthorizationCache.getAuthorization()).toString());
        mRequestQueue.add(stringRequest);
    }
}
