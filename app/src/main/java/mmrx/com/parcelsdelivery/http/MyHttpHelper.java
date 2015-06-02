package mmrx.com.parcelsdelivery.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mmrx on 2015/5/29.
 */
public class MyHttpHelper {
    private final static String URL_JOB_AVAILABLE = "http://1.app1211104.sinaapp.com/get_available/";
    private final static String URL_JOB_HISTORY ="http://1.app1211104.sinaapp.com/get_history/";
    /*the following urls should append the id number at the end of String before useing
    * eg.http://http://1.app1211104.sinaapp.com/get_by_id/1/
    * */
    private final static String URL_JOB_GET_JOB = "http://1.app1211104.sinaapp.com/get_by_id/";
    private final static String URL_JOB_SET_DELIVERED = "http://1.app1211104.sinaapp.com/set_delivered/";
    private final static String URL_JOB_SET_COMPLETED = "http://1.app1211104.sinaapp.com/set_complete/";

    public static enum URL_TYPE{
        JOB_AVAIL,JOB_HIST,JOB_GET,JOB_SET_DE,JOB_SET_COMP
    };

    public static void performRequest(URL_TYPE url_type,String id,Handler handler
            ,Context context){
        switch (url_type){
            case JOB_AVAIL:
                performRequest(URL_JOB_AVAILABLE,handler,context);
                break;
            case JOB_HIST:
                performRequest(URL_JOB_HISTORY,handler,context);
                break;
            case JOB_GET:
                final String url1 = URL_JOB_GET_JOB + id + "/";
                Log.v("URL_JOB_GET_JOB",url1);
                performRequest(url1.trim(),handler,context);
                break;
            case JOB_SET_DE:
                final String url2 = URL_JOB_SET_DELIVERED + id + "/";
                Log.v("URL_JOB_SET_DELIVERED",url2);
                performRequest(url2.trim(),handler,context);
                break;
            case JOB_SET_COMP:
                final String url3 = URL_JOB_SET_COMPLETED + id + "/";
                Log.v("URL_JOB_SET_COMPLETED",url3);
                performRequest(url3.trim(),handler,context);
                break;
            default:
                break;
        }
    }

    private static void  performRequest(final String url,final Handler handler,
                                Context context){
        // use a response handler so we aren't blocking on the HTTP request
        final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            public String handleResponse(HttpResponse response) {
                // when the response happens close the notification and update UI
                HttpEntity entity = response.getEntity();
                String result = null;
                try {
                    //send the json string my message
                    result = inputStreamToString(entity.getContent());
                    Log.v("RESPONSE",result);
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("RESPONSE", result);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        };

        // do the HTTP dance in a separate thread (the responseHandler will fire when complete)
        new Thread() {

            @Override
            public void run() {
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet httpMethod = new HttpGet(url);
                    client.execute(httpMethod, responseHandler);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    /**
     * get string from inputStream
     * */
    private static String inputStreamToString(final InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
