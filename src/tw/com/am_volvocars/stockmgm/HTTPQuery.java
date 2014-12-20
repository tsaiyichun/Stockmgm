package tw.com.am_volvocars.stockmgm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

public class HTTPQuery extends Thread {
	public static final int REGISTRATION_USER = 1;
	public static final int GET_KEY_THREAD = 2;
	public static final int APP_LOGIN_THREAD = 3;
	public static final int ERROR_OCCURENCE = 99;
	
	private Activity instance;
	
	private String USER_AGENT = "Volvo";
	private int getType;
	private String URL;
	private Handler handler;
	//private btnLogin = (Button) findViewById(R.id.btnLogin);
		
    public HTTPQuery(String URL, int getType, Handler handler) {
		super();
		this.URL = URL;
		this.getType = getType;
		this.handler = handler;

		
	}
    	
	@Override
	public void run() {
	    try {
	    	
			

	    	HttpClient httpclient;
	        HttpResponse response;
	        HttpGet httpget;
	        StatusLine statusLine;
	    	switch(getType) {
	    /*	case REGISTRATION_USER:
	    		httpclient = new DefaultHttpClient();
	    		httpget = new HttpGet(URL);
		    	httpget.setHeader("User-Agent", USER_AGENT);
		        response = httpclient.execute(httpget);
		        statusLine = response.getStatusLine();
		        if(statusLine.getStatusCode() == HttpStatus.SC_OK) {    
		        	String rsStr = EntityUtils.toString(response.getEntity());
		        	JSONObject jsonObject = new JSONObject(rsStr);
		            Message msg = new Message();
	            	msg.what = REGISTRATION_USER;
	            	msg.obj = jsonObject;
	            	handler.sendMessage(msg);
		        } else{
		            response.getEntity().getContent().close();
		            throw new IOException(statusLine.getReasonPhrase());
		        }
	    		break;
	    	case GET_KEY_THREAD:
		    	httpclient = new DefaultHttpClient();
		    	httpget = new HttpGet(URL);
		    	httpget.setHeader("User-Agent", USER_AGENT);
		        response = httpclient.execute(httpget);
		        statusLine = response.getStatusLine();
		        if(statusLine.getStatusCode() == HttpStatus.SC_OK) {    
		        	String rsStr = EntityUtils.toString(response.getEntity());
		        	JSONObject jsonObject = new JSONObject(rsStr);
		            Message msg = new Message();
	            	msg.what = GET_KEY_THREAD;
	            	msg.obj = jsonObject;
	            	handler.sendMessage(msg);
		        } else{
		            response.getEntity().getContent().close();
		            throw new IOException(statusLine.getReasonPhrase());
		        }
		        break;*/
	    	case APP_LOGIN_THREAD:
	    		
	    		//btnLogin.setText(R.string.login_processing);
	    		
	    		httpclient = new DefaultHttpClient();
	    		httpget = new HttpGet(URL);
		    	httpget.setHeader("User-Agent", USER_AGENT);
		        response = httpclient.execute(httpget);
		        statusLine = response.getStatusLine();
		        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
		        	String rsStr = EntityUtils.toString(response.getEntity());
		        	JSONObject jsonObject = new JSONObject(rsStr);
		            Message msg = new Message();
	            	msg.what = APP_LOGIN_THREAD;
	            	msg.obj = jsonObject;
	            	handler.sendMessage(msg);
		        } else{
		            response.getEntity().getContent().close();
		           // throw new IOException(statusLine.getReasonPhrase());
		        }
		        break;
	    	}
	    } catch (ClientProtocolException e) {
	    	e.printStackTrace();
	    	Message msg = new Message();
        	msg.what = ERROR_OCCURENCE;
        	handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			Message msg = new Message();
        	msg.what = ERROR_OCCURENCE;
        	handler.sendMessage(msg);
		} catch (Exception e) {
		    e.printStackTrace();
		    Message msg = new Message();
        	msg.what = ERROR_OCCURENCE;
        	handler.sendMessage(msg);
		}
	}
}