package com.download.fvd.activity;

import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.download.fvd.activity.pushnotification.Constants;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class TopSitesAppsGamesAsynckTask extends AsyncTask<Void, Void, Void> {
	ProgressDialog pDialog;
	String xml;
	String catName;
	// ArrayList<String> listTitle,listImageURL,listLink,listID;
	boolean mReachedLastPage = true;

	ListView listview;
	public static TopSitesAndAppsGames topsitegame;
	public static ArrayList<TopSitesAndAppsGames> arrlisttopsite;
	int arraylength_new, arraylength_new12, number_count, countRow;
	Activity activity;
	// private TextView textView_pleasewait;
	
	ProgressBar progressBar1;

	public TopSitesAppsGamesAsynckTask(Activity activity2,
			ListView popular_listview_bg, ProgressBar progressBar1) {
		this.activity = activity2;
		this.progressBar1 = progressBar1;
		this.listview = popular_listview_bg;
	}

	@Override
	protected void onPreExecute() {

		progressBar1.setVisibility(View.VISIBLE);

		// textView_pleasewait.setVisibility(View.VISIBLE);
		// pd.show();
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {

			HttpPost request = new HttpPost(Constants.TopApps_URL+Constants.COUNTRY_NAME);
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");


			// Build JSON string
			JSONStringer myrequest = new JSONStringer().object().endObject();

			// JSON REquest =======

			Log.i("JSON REquest  : ", myrequest.toString());

			StringEntity entity = new StringEntity(myrequest.toString());

			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));

			entity.setContentType("application/json");

			request.setEntity(entity);

			// Send request to WCF service
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			HttpEntity httpEntity = response.getEntity();
			xml = EntityUtils.toString(httpEntity);
			Log.i("JSON REsponse length ", "" + xml);
			JSONObject jsnobj = new JSONObject(xml);
			String response_12 = jsnobj.getString("ads");
			JSONArray jsonarray = new JSONArray(response_12);
			arraylength_new12 = jsonarray.length();
			arrlisttopsite = new ArrayList<TopSitesAndAppsGames>();

			for (int i = 0; i < arraylength_new12; i++) {

				topsitegame = new TopSitesAndAppsGames();
				JSONObject jsonobjarray = jsonarray.getJSONObject(i);
				topsitegame.setTitle(jsonobjarray.getString("title"));
				topsitegame.setImageUrl(jsonobjarray
						.getString("icon_image_url"));
				topsitegame.setLink(jsonobjarray.getString("click_track_url"));
				arrlisttopsite.add(topsitegame);
				System.out.println("track link in async"
						+ arrlisttopsite.get(i).getLink());
			}

		} catch (Exception e) {

		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		progressBar1.setVisibility(View.GONE);
		if (null != pDialog && pDialog.isShowing()) {
			// pDialog.dismiss();

		}

		try {
			if (arrlisttopsite.size() > 0) {

				Log.e("print list view=","call adapter data");
				TopSitesApssGamesAdapter thadapter = new TopSitesApssGamesAdapter(
						activity, arrlisttopsite);
				
				listview.setAdapter(thadapter);

				// thadapter.notifyDataSetChanged();
				
			}

			else 
			{

			}
		} catch (Exception e) 
		{
		}
		super.onPostExecute(result);
	}
}
