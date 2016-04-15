package com.download.fvd.activity.pushnotification;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
	private final String DBNAME = "FVDTube";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor spEditor;
	private Context context;

	private static String IP_Share_pref = "ip";
	private static String Country_Share_pref = "Country";
	
	public SharePref(Context context) {
        this.context= context;
        sharedPreferences=this.context.getSharedPreferences(DBNAME, Context.MODE_PRIVATE);
    }
	
	
	
	public void setIP_Share_pref(String  ip) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(IP_Share_pref,ip);
        spEditor.commit();
    }
    public String getIP_Share_pref() {
        return sharedPreferences.getString(IP_Share_pref,"");
    }
    
    public void setCountry_Share_pref(String  Country) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(Country_Share_pref,Country);
        spEditor.commit();
    }
    public String getCountry_Share_pref() {
        return sharedPreferences.getString(Country_Share_pref,"");
    }
}
