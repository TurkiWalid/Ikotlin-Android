package com.androidprojects.esprit.ikotlin.webservices;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Odil on 18/01/2018.
 */

public interface StringCallbacks {
    void onSuccess(String result);
    void onError(VolleyError result);
}
