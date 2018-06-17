package com.example.orionstark.barberrosadmin.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.orionstark.barberrosadmin.config.TAGList;
import com.example.orionstark.barberrosadmin.config.UrlList;

import org.json.JSONException;
import org.json.JSONObject;

public class AdminServices {

    public static void addBarber(
            String barberName, String barberPhone,
            String barberDescription, String barberImage,
            String barberAddress, Context context, final BarberCallback callback) throws JSONException {

        JSONObject params = new JSONObject();
        params.put("barber_name", barberName);
        params.put("no_telp", barberPhone);
        params.put("description", barberDescription);
        params.put("image", barberImage);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, UrlList.barberAdd_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ( response.getBoolean("status") ) {
                        callback.onSucceed(response.getString("message"));
                    } else {
                        callback.onError(response.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("Error in parsing data. PLease try it again later");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if ( error.networkResponse.data != null ) {
                        try {
                            String string = new String( error.networkResponse.data, HttpHeaderParser.parseCharset( error.networkResponse.headers));
                            JSONObject json = new JSONObject(string);
                            if ( json.getString("message") != null ) {
                                callback.onError(json.getString("message"));
                            } else {
                                callback.onError("Something went wrong");
                            }
                        } catch (Exception e) {
                            callback.onError("Something went wrong");
                        }
                    } else {
                        if ( error.getMessage() != null ) {
                            callback.onError(error.getMessage());
                        } else {
                            if ( error.getLocalizedMessage() != null ) {
                                callback.onError(error.getLocalizedMessage());
                            } else {
                                callback.onError("Something went wrong");
                            }
                        }
                    }
                } catch (Exception e) {
                    callback.onError("Something went wrong");
                }
            }
        });

        ServiceSingleton.getInstance(context).addRequestToQueue(request, TAGList.ADD_BARBER_TAG);
    }

    public static void doneBarber(
            String time, String name, String id,
            Context context, final BarberCallback callback) throws JSONException {

        JSONObject params = new JSONObject();
        params.put("time", time);
        params.put("name", name);
        params.put("id", id);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST, UrlList.barberDone_url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ( response.getBoolean("status") ) {
                        callback.onSucceed(response.getString("message"));
                    } else {
                        callback.onError(response.getString("message"));
                    }
                } catch (JSONException e) {
                    callback.onError("Error in parsing data. PLease try it again later");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    if ( error.networkResponse.data != null ) {
                        try {
                            String string = new String( error.networkResponse.data, HttpHeaderParser.parseCharset( error.networkResponse.headers));
                            JSONObject json = new JSONObject(string);
                            if ( json.getString("message") != null ) {
                                callback.onError(json.getString("message"));
                            } else {
                                callback.onError("Something went wrong");
                            }
                        } catch (Exception e) {
                            callback.onError("Something went wrong");
                        }
                    } else {
                        if ( error.getMessage() != null ) {
                            callback.onError(error.getMessage());
                        } else {
                            if ( error.getLocalizedMessage() != null ) {
                                callback.onError(error.getLocalizedMessage());
                            } else {
                                callback.onError("Something went wrong");
                            }
                        }
                    }
                } catch (Exception e) {
                    callback.onError("Something went wrong");
                }
            }
        });

        ServiceSingleton.getInstance(context).addRequestToQueue(request, TAGList.DONE_BARBER_TAG);
    }

    public interface BarberCallback {
        void onSucceed(String message);
        void onError(String message);
    }
}
