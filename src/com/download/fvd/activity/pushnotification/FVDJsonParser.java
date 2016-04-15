package com.download.fvd.activity.pushnotification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class FVDJsonParser {
	private static FVDJsonParser khoobsurtiParser;

	private FVDJsonParser() {

	}

	public static FVDJsonParser getInstance() {

		if (khoobsurtiParser == null) {
			khoobsurtiParser = new FVDJsonParser();
		}
		return khoobsurtiParser;

	}

	public JSONObject getJSONFromUrl(String url) {

		InputStream is = null;

		String json = null;

		JSONObject jObj = null;

		// Making HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			Log.e("FVD URl=",url);
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception ex){}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				Log.e("Message fdv by server=",String.valueOf(line));
			}
			is.close();
			json = sb.toString();
		} 
		catch (Exception e) 
		{
			Log.e("Buffer Error", "Error converting result " + e.toString());
			Log.e("FVD JSON Parser class error=",e.getMessage());
		}
		
		// try parse the string to a JSON object
		try {
			
			if(json!=null)
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			Log.e("FVD json=",e.getMessage());
			
		}
		catch(Exception ex)
		{
			Log.e("FDVJson=",ex.getMessage());
			
		}
		return jObj;
	}
}