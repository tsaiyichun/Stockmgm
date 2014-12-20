package tw.com.am_volvocars.stockmgm;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	/*private static final int SUCCESS = 1;
	private static final int ERROR_PARAMETER_ERROR = 2;
	private static final int ERROR_ACCOUNT_NOT_EXISTS = 3;
	private static final int ERROR_ACCOUNT_NOT_REGISTRATION = 4;
	private static final int ERROR_ACCOUNT_REGISTRATION_NOT_MATCH = 5;
	private static final int ERROR_PWD_DECRYPTION = 6;
	private static final int ERROR_PWD_WRONG = 7;
	private static final int ERROR_SESSION_KEY_EXPIRATION = 8;
		*/

//	private static final int ERROR_PARAMETER_ERROR = 99;
	private static final int ERROR_NO_INTERNET = 100;
	private static final int ERROR_NO_USER_INFO = 101;
	private static final int ERROR_SERVER_FAIL = 102;
	private static final int ERROR_MCRYPTION_FAIL = 103;
	private static final int SUCCESS = 1;
	private static final int ERROR = 99;
	private Activity instance;
	private Handler handler;
	//private String registrationId;
	//private String URL_GET_KEY = Helper.URL_PREFIX + "getkey.php?";
	private String URL_APP_LOGIN = Helper.URL_PREFIX + "login.php?";
	//private String URL_REGISTRATION = Helper.URL_PREFIX + "registration.php?";
	private EditText editTextAccount;
	private EditText editTextPwd;
	private TextView textViewErr;
	private Button btnLogin;
	
	private String account;
	private String pwd;
	private String sessionkey;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        findViews();
        initHandler();
        setListener();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		btnLogin.setEnabled(true);
		btnLogin.setText(R.string.login);
		doLogin();
	}
    
    private void findViews() {
    	editTextAccount = (EditText) findViewById(R.id.editTextAccount);
    	editTextPwd = (EditText) findViewById(R.id.editTextPwd);
    	textViewErr = (TextView) findViewById(R.id.textViewErr);
    	btnLogin = (Button) findViewById(R.id.btnLogin);
    }
    
    private void setListener() {
    	btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				account = editTextAccount.getText().toString();
				pwd = editTextPwd.getText().toString();
				Helper.setAccount(instance, account);
				Helper.setPassword(instance, pwd);
								
				doLogin();
			}
		});
    }
    
    private void initHandler() {
    	handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch(msg.what) {
				/*case HTTPQuery.REGISTRATION_USER:
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						//int status = jsonObject.getInt("Status");
						int status = 1;
						switch(status) {
						case SUCCESS:
							new HTTPQuery(URL_GET_KEY + "UserID=" + account, HTTPQuery.GET_KEY_THREAD, handler).start();
							break;
						case ERROR_PARAMETER_ERROR:
							throw new Exception(String.valueOf(ERROR_PARAMETER_ERROR));
						case ERROR_ACCOUNT_NOT_EXISTS:
							throw new Exception(String.valueOf(ERROR_ACCOUNT_NOT_EXISTS));
						case ERROR_ACCOUNT_REGISTRATION_NOT_MATCH:
							throw new Exception(String.valueOf(ERROR_ACCOUNT_REGISTRATION_NOT_MATCH));
						}
					} catch (JSONException e) {
						e.printStackTrace();
						errorHandle(ERROR_SERVER_FAIL);
					} catch (Exception e) {
						e.printStackTrace();
						if(Helper.isNumeric(e.getMessage())) {
							errorHandle(Integer.parseInt(e.getMessage()));
						}
					}
					break;
				case HTTPQuery.GET_KEY_THREAD:
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						//int status = jsonObject.getInt("Status");
						int status = 1;
						switch(status) {
						case SUCCESS:
							String iv = jsonObject.getString("iv");
					        int keyLoc = jsonObject.getInt("KeyLoc");
					        String key = registrationId.substring(keyLoc, keyLoc + 16);
					        byte[] plaintextBytes = Arrays.copyOf(pwd.getBytes(), (pwd.getBytes().length / 16 + 1) * 16);
					        SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "AES/CBC/NoPadding");
					        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
					        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
					        cipher.init(Cipher.ENCRYPT_MODE, spec, ivspec);
					        
					        byte[] encryptedByteArray = cipher.doFinal(plaintextBytes);
					    	byte[] base64EncryptedByteArray = Base64.encode(encryptedByteArray, Base64.URL_SAFE);
					    	
					    	String pCode = new String(base64EncryptedByteArray).replace("\n", "");
					    	new HTTPQuery(URL_APP_LOGIN + "ID=" + account + "&pCode=" + pCode, HTTPQuery.APP_LOGIN_THREAD, handler).start();
							break;
						case ERROR_PARAMETER_ERROR:
							throw new Exception(String.valueOf(ERROR_PARAMETER_ERROR));
						case ERROR_ACCOUNT_NOT_EXISTS:
							throw new Exception(String.valueOf(ERROR_ACCOUNT_NOT_EXISTS));
						case ERROR_ACCOUNT_NOT_REGISTRATION:
							new HTTPQuery(URL_REGISTRATION + "UserID=" + account + "&RegistID=" + registrationId, HTTPQuery.REGISTRATION_USER, handler).start();
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
						errorHandle(ERROR_SERVER_FAIL);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (InvalidKeyException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (InvalidAlgorithmParameterException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (IllegalBlockSizeException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (BadPaddingException e) {
						e.printStackTrace();
						errorHandle(ERROR_MCRYPTION_FAIL);
					} catch (Exception e) {
						e.printStackTrace();
						if(Helper.isNumeric(e.getMessage())) {
							errorHandle(Integer.parseInt(e.getMessage()));
						}
					}
					break;
				*/
				
				case HTTPQuery.APP_LOGIN_THREAD:
					try {
						JSONObject jsonObject = (JSONObject) msg.obj;
						int status = jsonObject.getInt("Status");

						
						//int status = SUCCESS;
						switch(status) {
						case SUCCESS:
						    sessionkey = jsonObject.getString("SessionKey");
						    
							Intent intent = new Intent(instance, BrowserActivity.class);
						    intent.putExtra(Helper.EXTRA_SESSION_KEY, sessionkey);
						    intent.putExtra(Helper.EXTRA_LOGIN_ID, account);
						    startActivity(intent);
						    finish();
						    break;
						
						/*
						case ERROR_PARAMETER_ERROR:
							throw new Exception(String.valueOf(ERROR_PARAMETER_ERROR));
						case ERROR_ACCOUNT_NOT_EXISTS:
							throw new Exception(String.valueOf(ERROR_ACCOUNT_NOT_EXISTS));
						case ERROR_PWD_DECRYPTION:
							throw new Exception(String.valueOf(ERROR_PWD_DECRYPTION));
						case ERROR_PWD_WRONG:
							throw new Exception(String.valueOf(ERROR_PWD_WRONG));
						case ERROR_SESSION_KEY_EXPIRATION:
							throw new Exception(String.valueOf(ERROR_SESSION_KEY_EXPIRATION));
							//new HTTPQuery( + "UserID=" + account, HTTPQuery.GET_KEY_THREAD, handler).start();
							//break;
							 * 
							 * */
						    
						case ERROR :
							//throw new Exception(String.valueOf(ERROR));
				    		Toast.makeText(instance, "yoyoyo", Toast.LENGTH_SHORT).show();
				    		btnLogin.setEnabled(true);
				    		btnLogin.setText(R.string.login);


							
						}		
						
										
					} 
					
				
					catch (JSONException e) {
						e.printStackTrace();
					errorHandle(ERROR_SERVER_FAIL);
					} 
					
					catch (Exception e) {
						e.printStackTrace();
						if(Helper.isNumeric(e.getMessage())) {
							errorHandle(Integer.parseInt(e.getMessage()));
						}
						
						
						
					}
					break;
				case HTTPQuery.ERROR_OCCURENCE:
					errorHandle(ERROR_SERVER_FAIL);
					break;
				}
				return false;
			}
		});
    }
    
    private void doLogin() {
    	btnLogin.setEnabled(false);
		btnLogin.setText(R.string.login_processing);

    	account = Helper.getAccount(instance);
    	pwd = Helper.getPassword(instance);
    	editTextAccount.setText(account);
    	editTextPwd.setText(pwd);
   
    	if(account.length() == 0 || pwd.length() == 0) {
    		errorHandle(99);
    		return;
    	}
    	if(!Helper.haveInternet(instance)) {
    		errorHandle(ERROR_NO_INTERNET);
    		return;
    	}
    	//registrationId = Helper.getRegistrationID(account);
    	new HTTPQuery(URL_APP_LOGIN + "ID=" + account + "&PWD=" + pwd, HTTPQuery.APP_LOGIN_THREAD, handler).start();
    }
    
    private void errorHandle(int errorType) {
    	switch(errorType) {
    	case ERROR_NO_INTERNET:
    		textViewErr.setText(R.string.ERROR_NO_INTERNET);
    		
    		break;
    	case ERROR_NO_USER_INFO:
    		textViewErr.setText(R.string.ERROR_NO_USER_INFO);
    		Toast.makeText(instance, getString(R.string.ERROR_NO_USER_INFO), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_SERVER_FAIL:
    		textViewErr.setText(R.string.ERROR_SERVER_FAIL);
    		Toast.makeText(instance, getString(R.string.ERROR_SERVER_FAIL), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_MCRYPTION_FAIL:
    		textViewErr.setText(R.string.ERROR_MCRYPTION_FAIL);
    		Toast.makeText(instance, getString(R.string.ERROR_MCRYPTION_FAIL), Toast.LENGTH_SHORT).show();
    		break;
    /*	case ERROR_PARAMETER_ERROR:
    		textViewErr.setText(R.string.ERROR_PARAMETER_ERROR);
    		Toast.makeText(instance, getString(R.string.ERROR_PARAMETER_ERROR), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_ACCOUNT_NOT_EXISTS:
    		textViewErr.setText(R.string.ERROR_ACCOUNT_NOT_EXISTS);
    		Toast.makeText(instance, getString(R.string.ERROR_ACCOUNT_NOT_EXISTS), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_ACCOUNT_NOT_REGISTRATION:
    		textViewErr.setText(R.string.ERROR_ACCOUNT_NOT_REGISTRATION);
    		Toast.makeText(instance, getString(R.string.ERROR_ACCOUNT_NOT_REGISTRATION), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_ACCOUNT_REGISTRATION_NOT_MATCH:
    		textViewErr.setText(R.string.ERROR_ACCOUNT_REGISTRATION_NOT_MATCH);
    		Toast.makeText(instance, getString(R.string.ERROR_ACCOUNT_REGISTRATION_NOT_MATCH), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_PWD_DECRYPTION:
    		textViewErr.setText(R.string.ERROR_PWD_DECRYPTION);
    		Toast.makeText(instance, getString(R.string.ERROR_PWD_DECRYPTION), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_PWD_WRONG:
    		textViewErr.setText(R.string.ERROR_PWD_WRONG);
    		Toast.makeText(instance, getString(R.string.ERROR_PWD_WRONG), Toast.LENGTH_SHORT).show();
    		break;
    	case ERROR_SESSION_KEY_EXPIRATION:
    		textViewErr.setText(R.string.ERROR_SESSION_KEY_EXPIRATION);
    		Toast.makeText(instance, getString(R.string.ERROR_SESSION_KEY_EXPIRATION), Toast.LENGTH_SHORT).show();
    		break;
    		
    		*/
    	}
    	
    	btnLogin.setEnabled(true);
		btnLogin.setText(R.string.login);
		
    }
}