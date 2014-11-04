package com.dfens.demo.network;

import android.content.Context;
import android.util.Log;
import com.dfens.demo.R;
import com.google.gson.Gson;
import org.androidannotations.annotations.EBean;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by mattias on 2014-11-04.
 */
@EBean
public class Caller {

    private Context context;

    public Caller(Context context) {
        this.context = context;
    }

    private DefaultHttpClient getClient() throws Exception {
        return HttpClientUtils.getAllTrustingHttpClient();
    }

    public <T> T executeGet(String urlPath, Class<T> returnClazz) throws Exception {
        T result = null;
        try {

            HttpGet httpGet = new HttpGet(context.getString(R.string.base_url) + urlPath);
            Log.d("Dfens", "GET  " + httpGet.getURI().toString());
            HttpResponse response = getClient().execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                int a = 0;
                StringBuilder buf = new StringBuilder();
                while ((a = is.read()) != -1) {
                    buf.append((char) a);
                }

                Log.d("Dfens", buf.toString());

                Gson gson = new Gson();
                result = gson.fromJson(buf.toString(), returnClazz);

                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new Exception("Something went wrong with request.");
    }

    public <T> T executePost(String urlPath, Class<T> returnClazz, Object body) throws Exception {
        T result = null;
        try {
            Gson gson = new Gson();
            HttpPost httpPost = new HttpPost(context.getString(R.string.base_url) + urlPath);
            httpPost.addHeader("Content-type", "application/json");
            if (body != null) {
                if (gson == null) {
                    gson = new Gson();
                }
                HttpEntity entity;
                try {
                    entity = new StringEntity(gson.toJson(body), "UTF-8");
                    httpPost.setEntity(entity);
                } catch (UnsupportedEncodingException e) {
                    throw new Exception("Unsupported encoding");
                }
            }

            Log.d("Dfens", "POST  " + httpPost.getURI().toString());
            HttpResponse response = getClient().execute(httpPost);

            Log.d("Dfens", "Status code: " + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                InputStream is = response.getEntity().getContent();
                int a = 0;
                StringBuilder buf = new StringBuilder();
                while ((a = is.read()) != -1) {
                    buf.append((char) a);
                }

                Log.d("Dfens", buf.toString());

                result = gson.fromJson(buf.toString(), returnClazz);

                return result;
            }else{
                InputStream is = response.getEntity().getContent();
                int a = 0;
                StringBuilder buf = new StringBuilder();
                while ((a = is.read()) != -1) {
                    buf.append((char) a);
                }

                Log.d("Dfens",  buf.toString());

                result = gson.fromJson(buf.toString(), returnClazz);

                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new Exception("Something went wrong with request.");
    }
}
