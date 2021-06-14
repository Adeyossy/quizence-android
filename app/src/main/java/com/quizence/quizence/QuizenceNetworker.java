package com.quizence.quizence;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class QuizenceNetworker {
    //This class establishes communication with the backend

    private static final String BACKEND_URL = "https://quizence.herokuapp.com/";
    public static final String COLLATION_APPEND = "/collation";
    private Context mContext;
    private JSONArray mReturnedResponse;

    private NetworkResponseListener mNetworkResponseListener;
    private StringResponseListener mStringResponseListener;

    QuizenceNetworker(Context context){
        mContext = context;
    }

    public void initiateNetworkActivity(final String tag, final String url, NetworkResponseListener listener){
        //Volley data fetch
        mNetworkResponseListener = listener;
        String backendUrl = BACKEND_URL.concat(url.toLowerCase());
        mReturnedResponse = null;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, backendUrl,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v(tag, response.toString());
                Log.v(tag, url);
//                mReturnedResponse = response;
                mNetworkResponseListener.onNetworkResponseListener(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext, "An error occurred",
                        Toast.LENGTH_LONG).show();
                Log.e(tag, Objects.requireNonNull(error.getMessage()));
            }
        });

        jsonArrayRequest.setTag(tag);

        QuizenceDataHolder.get().addToRequestQueue(jsonArrayRequest, mContext);
    }

    public void postCollation(final String tag, String url, final String stringJSON,
                              StringResponseListener responseListener){
        mStringResponseListener = responseListener;
        String backendUrl = BACKEND_URL.concat(url.toLowerCase());
        StringRequest request = new StringRequest(Request.Method.POST, backendUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //handle response
                Log.v(tag, response.toString());
                mStringResponseListener.onStringResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //handle errors
                Log.e(tag, error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                //return requestBody == null ? null : requestBody.getBytes("utf-8");
                return stringJSON.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        request.setTag(tag);

        QuizenceDataHolder.get().addToRequestQueue(request, mContext);
    }

    public void updateCollationQuestion(String tag, String url, String questionJSON,
                                        StringResponseListener listener){
        mStringResponseListener = listener;
    }

    public void setNetworkResponseListener(NetworkResponseListener networkResponseListener){
        mNetworkResponseListener = networkResponseListener;
    }

    public void setStringResponseListener(StringResponseListener responseListener){
        mStringResponseListener = responseListener;
    }

    public interface NetworkResponseListener{
        public void onNetworkResponseListener(JSONArray jsonArray);
    }

    public interface StringResponseListener{
        public void onStringResponse(String response);
    }
}
