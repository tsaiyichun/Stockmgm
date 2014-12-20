package tw.com.am_volvocars.stockmgm;

import java.security.MessageDigest;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Helper {
	public static String EXTRA_SESSION_KEY = "SessionKey";
	public static String EXTRA_LOGIN_ID = "LoginID";

	public static String URL_PREFIX = "http://140.129.6.133/i4010/api/";
	public static String SETTING_ACCOUNT = "settingAccount";
	public static String SETTING_PASSWORD = "settingPassword";
	public static String SETTING_SESSIONKEY = "settingSessionkey";
	
	/*public static String getRegistrationID(String userId) {
		String accessID = "";
		try {
        	MessageDigest md = MessageDigest.getInstance("SHA-1");
        	String tmp = android.os.Build.SERIAL + userId;
        	md.update(tmp.getBytes());
        	accessID = byteArrayToHexString(md.digest());
        }catch(Exception e){
        	e.printStackTrace();
        }
		
		return accessID;
	}
	
	*/
	public static String getAccount(Context instance) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		return preferences.getString(SETTING_ACCOUNT, "");
	}
	
	public static String getPassword(Context instance) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		return preferences.getString(SETTING_PASSWORD, "");
	}
	
	public static String getSessionkey(Context instance) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		return preferences.getString(SETTING_SESSIONKEY, "");
	}
	
	public static void setAccount(Context instance, String account) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		preferences.edit().putString(SETTING_ACCOUNT, account).apply();
	}
	
	public static void setPassword(Context instance, String password) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		preferences.edit().putString(SETTING_PASSWORD, password).apply();
	}
	public static void setSessionkey(Context instance, String sessionkey) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(instance);
		preferences.edit().putString(SETTING_SESSIONKEY, sessionkey).apply();
	}
	
	public static boolean haveInternet(Context instance) {
    	boolean rs = false;
    	ConnectivityManager connManager = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE); 
    	NetworkInfo info = connManager.getActiveNetworkInfo();
    	
    	if(info == null || !info.isConnected()) {
    		rs = false;
    	} else  {
    		if(!info.isAvailable()) {
    			rs = false;
    		} else {
    			rs = true;
    		}
    	}
    	
    	return rs;
    }
	
	private static String byteArrayToHexString(byte [] byteId) {
        int i, j;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String stringId= "";
        for(i = 0 ; i < byteId.length ; i++) {
            int temp = (int) byteId[i] & 0xff;
            j = (temp >> 4) & 0x0f;
            stringId += hex[j];
            j = temp & 0x0f;
            stringId += hex[j];
        }

        return stringId;
    }
	
	public static boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("[0-9]*");  
	    return pattern.matcher(str).matches();     
	}  
}
