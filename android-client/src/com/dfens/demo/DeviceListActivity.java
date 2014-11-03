package com.dfens.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.dfens.demo.domain.PollEventResponse;
import com.dfens.demo.network.HttpClientUtils;
import com.google.gson.Gson;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by mattias on 2014-11-02.
 */

@NoTitle
@EActivity(R.layout.activity_device_list)
public class DeviceListActivity extends Activity {

    private Timer pollTimer = new Timer();

    private TimerTask checkForEventTask = new TimerTask() {

        @Override
        public void run() {
            try {
                DefaultHttpClient client = HttpClientUtils.getAllTrustingHttpClient();
                HttpGet httpGet = new HttpGet("https://mattias:kalaskartoffel49@2.248.42.179:6984/events/_changes?since=1");
                HttpResponse response = client.execute(httpGet);

                if (response.getStatusLine().getStatusCode() == 200) {
                    InputStream is = response.getEntity().getContent();
                    int a = 0;
                    StringBuilder buf = new StringBuilder();
                    while ((a = is.read()) != -1) {
                        buf.append((char) a);
                    }

                    Gson gson = new Gson();
                    PollEventResponse pollEventResponse = gson.fromJson(buf.toString(), PollEventResponse.class);

                    Log.d("Dfens", "Number of events:" + pollEventResponse.results.size());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pollTimer.scheduleAtFixedRate(checkForEventTask, 2000, 3000);
    }

}
