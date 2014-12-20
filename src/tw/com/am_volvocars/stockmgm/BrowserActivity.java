package tw.com.am_volvocars.stockmgm;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BrowserActivity extends Activity {
	private Activity instance;
	private String loadURL;
	private WebView webView;
	private ProgressDialog progress;
	private String account;
	private String pwd;
	private String URL_LOGIN2 = Helper.URL_PREFIX + "login2.php?";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        account = Helper.getAccount(instance);
    	pwd = Helper.getPassword(instance);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_browser);
        findViewIDs();
        String sessionkey = getIntent().getStringExtra(Helper.EXTRA_SESSION_KEY);
        String account = getIntent().getStringExtra(Helper.EXTRA_LOGIN_ID);
        
        loadURL = URL_LOGIN2+"ID="+account+"&SKEY="+sessionkey;
        
        //loadURL = URL_SYSXFER + "ID=" + account + "&SessionKey=" + sessionKey;
		setupWebView();
    }

	private void findViewIDs() {
		webView = (WebView) findViewById(R.id.webview);
	}
	
	private void setupWebView() {
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				/*
				if(url.compareTo(loadURL) == 0) {
					progress = ProgressDialog.show(
						instance,
						getString(R.string.PROGRESS_DIALOG_TITLE),
						getString(R.string.PROGRESS_DIALOG_MESSAGE),
						true,
						true);
				}
				*/
				if(progress == null || !progress.isShowing()) {
				progress = ProgressDialog.show(
						instance,
						getString(R.string.PROGRESS_DIALOG_TITLE),
						getString(R.string.PROGRESS_DIALOG_MESSAGE),
						true,
						true);
				}
			}
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				//webView.loadUrl("http://google.com/");
//				webView.loadUrl("file:///android_asset/nointernet.html");
				Toast.makeText(instance, getString(R.string.ERROR_NO_INTERNET), Toast.LENGTH_LONG).show();
				if(progress.isShowing()) {
					progress.dismiss();
				}
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(progress.isShowing()) {
					progress.dismiss();
				}
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		webView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {  
					if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
						webView.goBack();
						return true;
	                }  
	            }
				return false;
			}
		});
		webView.loadUrl(loadURL);
	}
}