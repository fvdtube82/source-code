package com.download.fdv.youtubesdk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.download.fvd.activity.Youtube_Data_Setter_Getter;
import com.downoad.fdv.volley_resource.AppController;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class YoutubeFileExtractor 
{

	 String urlInfoStarts = "http://www.youtube.com/get_video_info?el=detailpage&hl=en_US&asv=3&sts=1588&video_id=";
		ProgressDialog pDialog ;
		 Hashtable urlParameters;
		String[] formats = {"13", "17", "36", // 3GP
			    "5", "6", "34", "35", // FLV
			    "18", "22", "37", "83", "82", "85", "84", "38", // MP4
			    "43", "44", "45", "46", "100", "101", "102"}; // WebM
		int lenghtOfFile;
		//ProgressDialog pbar ;//= new ProgressDialog(MainActivity.this);
		String url;
		 String videoId = null;
		 Context context;
		 
		 youtubedatalistener youtubelistener;
		 
		 //these arraylist use for send data 
		 /*ArrayList<String> formate_list=new ArrayList<String>();
		 ArrayList<String> links_list=new ArrayList<String>();
		 ArrayList<String> title_list=new ArrayList<String>();
		 ArrayList<String> rounded_size_list=new ArrayList<String>();
		 ArrayList<String> percent_list=new ArrayList<String>();
		*/ 
		 ArrayList<Youtube_Data_Setter_Getter> youtube_Data=new ArrayList<Youtube_Data_Setter_Getter>();
		 
		 public YoutubeFileExtractor(Context context,String url,youtubedatalistener youtubelistener) 
		 {
			this.context=context;
			this.youtubelistener=youtubelistener;
			// this.url=url;
			 LinksReceivers(url);
			// pbar = new ProgressDialog(context);
			 
		}
		 
		 public void LinksReceivers(String url) {
			    this.url = url;
			   // this.jsObject = jsObject;
			    
			    System.out.println("url: " + url);
			    Log.e("Url=", url);
			    getUrlParameters(url);
			   
			    
			    videoId = (String) urlParameters.get("v");
			    Log.e("Video id=", videoId);
			    if (videoId == null) {
			    	System.out.print("showError('VIDEO_NOT_FOUND");
			     // jsObject.eval("showError('VIDEO_NOT_FOUND');");
			      
			    }
			    if (videoId.indexOf("&") > 0) {
			      videoId = videoId.substring(0, videoId.indexOf("&"));
			    }
			    System.out.println("videoId: " + videoId);
			    Log.e("Video new Url=",url);
			    if (videoId == null) {
			    	
			    	Log.e("url null=","url null");
			     // videoId = getSubstring(getRedirectedLocation(url), "v=", "&");
			    }
			   // Log.e("Full url:= ", String.valueOf(urlInfoStarts+videoId));
			   // String page = openHTTPPage(urlInfoStarts + videoId, "GET");
			    
			   // new fetchdata().execute(urlInfoStarts+videoId);
			    fetchdata(urlInfoStarts+videoId);

			  }

			public void getUrlParameters(String url) {
			    int i = url.indexOf("?");
			    if (i > -1) {
			      String parameters = url.substring(url.indexOf("?") + 1);
			      System.out.println("parameters: " + parameters);
			      urlParameters = new Hashtable();
			      StringTokenizer tokenizer1 = new StringTokenizer(parameters, "&");
			      while (tokenizer1.hasMoreTokens()) {
			        StringTokenizer tokenizer2 = new StringTokenizer(tokenizer1.nextToken(), "=");
			        urlParameters.put(tokenizer2.nextToken(), tokenizer2.nextToken());
			      }
			    } else {
			    	System.out.print("showError('VIDEO_NOT_FOUND");
			      //jsObject.eval("showError('VIDEO_NOT_FOUND');");
			    }
			  }
				
			
			
			public void fetchdata(String fullurl) {
			
				// Tag used to cancel the request
				String  tag_string_req = "string_req";
				 
				String url = fullurl;          //"http://www.youtube.com/get_video_info?el=detailpage&hl=en_US&asv=3&sts=1588&video_id=IWP2-qkhtiM";
				         
				pDialog = new ProgressDialog(context);
				pDialog.setMessage("Loading...");
				pDialog.show();     
				         
				StringRequest strReq = new StringRequest(Method.GET,
				                url, new Response.Listener<String>() {
				 
				                    @Override
				                    public void onResponse(String response) {
				                        Log.e("stringc data=", response.toString());
				                        getPageinfo(response);
				                        
				                       pDialog.dismiss();
				 
				                    }
				                }, new Response.ErrorListener() {
				 
				                    @Override
				                    public void onErrorResponse(VolleyError error) {
				                        VolleyLog.d("error show=", "Error: " + error.getMessage());
				                        pDialog.dismiss();
				                        Toast.makeText(context, "Network or Server Probleam...",Toast.LENGTH_LONG).show();
				                        
				                      //  Toast.makeText(context, "Server or Network Probleam...", Toast.LENGTH_LONG).show();
				                        Log.e("Error Message=", String.valueOf(error));
				                        
				                    }
				                });
				 
				// Adding request to request queue
				AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
			}
			//start...............................
			/*private class fetchdata extends AsyncTask<String, Void, String> {
			    @Override 
			    protected String doInBackground(String... urls) {
			      String response = "";
			      Log.e("call background=", "yes call");
			      for (String url : urls) {
			        DefaultHttpClient client = new DefaultHttpClient();
			        HttpGet httpGet = new HttpGet(url);
			        try { 
			          HttpResponse execute = client.execute(httpGet);
			          InputStream content = execute.getEntity().getContent();
			 
			          BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
			          String s = "";
			          while ((s = buffer.readLine()) != null) {
			            response += s;
			           
			          } 
			 
			        } catch (Exception e) {
			          e.printStackTrace();
			        } 
			      } 
			      getPageinfo(response);
			      return response;
			    } 
			 
			    @Override 
			    protected void onPostExecute(String result) {
			    	  pDialog.dismiss();
			    } 
			    
			    @Override
			    protected void onPreExecute() {
			    	// TODO Auto-generated method stub
			    	super.onPreExecute();
			    	pDialog = new ProgressDialog(context);
					pDialog.setMessage("Loading...");
					pDialog.show(); 
					  Log.e("call call pree=", "yes call");
			    }
			  } 
			*/
			//end................................
			
			public void getPageinfo(String pages) {
				 String title;
				// String videoId="IWP2-qkhtiM";
				 String urlWatchStarts = "http://www.youtube.com/watch?v=";
				Log.e("Page info=",pages);
				  boolean getSizes=true;
				 boolean descending=true;
				String page = pages;
			    if (page.equals("")) {
			    	System.out.print("showError('VIDEO_NOT_FOUND');");
			     // jsObject.eval("showError('VIDEO_NOT_FOUND');");      
			     
			    }
			    
			    if (page.indexOf("errorcode=150") > -1) {
			    	System.out.print("showError('COUNTRY_RESTRICTION');");
			     // jsObject.eval("showError('COUNTRY_RESTRICTION');");
			      
			    }
			    
			    title = getSubstring(page, "title=", "&");
			    Log.e("Page Title=", title);
			    if (title == null) {
			    	System.out.println("showError('VIDEO_NOT_FOUND');");
			    	
			     // jsObject.eval("showError('VIDEO_NOT_FOUND');");
			      
			    }
			    Log.e("Title Name 1=", title);
			    try {
			      title = URLDecoder.decode(title, "UTF-8");
			      title = title.replaceAll("'", "\\\\'");      
			      Log.e("Title Name 2=", title);
			    } catch (UnsupportedEncodingException ex) {
			     // Logger.getLogger(JYDApplet.class.getName()).log(Level.SEVERE, null, ex);
			    }  
			    
			   
			    String length_seconds = getSubstring(page, "length_seconds=", "&");
			    Log.e("lengt in second=",length_seconds);
			    int totalSeconds = Integer.parseInt(length_seconds);
			    int minutes = totalSeconds / 60;
			    int seconds = totalSeconds - minutes * 60;
			    String s = String.valueOf(seconds);
			    String m = String.valueOf(minutes);
			    if (s.length() == 1) {
			      s = "0" + s;
			    }
			    
			    System.out.println("setYoutubeInfo('" + title
			            + "', '" + urlWatchStarts + videoId
			            + "', 'http://i.ytimg.com/vi/" + videoId + "/default.jpg', '"
			            + videoId + "', '"
			            + m + "', '"
			            + s + "');");
			    
			    //call the listener (user define listener...) 
			    youtubelistener.youtubeContentInfo(title, urlWatchStarts+videoId,"http://i.ytimg.com/vi/"+videoId+"/default.jpg",m+":"+s);
			    /*System.out.println("setYoutubeInfo('" + title
			            + "', '" + urlWatchStarts + videoId
			            + "', 'http://i.ytimg.com/vi/" + videoId + "/default.jpg', '"
			            + videoId + "', '"
			            + m + "', '"
			            + s + "');");*/

			  
			    Map links = getLinks(page, videoId);
			  
			    if (links == null) {
			    	Log.e("Link info 1","Link is null");
			    }
			    Log.e("number  of link=",String.valueOf(links.size()));
			    
			    int startI = 0;
			    int maxI = formats.length - 1;
			    int incI = 1;
			    int percent = 10;
			    int percentD = 90 / formats.length;    
			    Log.e("Percent id=",String.valueOf(percentD));
			    
			    if (descending) {
			      startI = formats.length - 1;
			      maxI = 0;
			      incI = -1;   
			      
			    }
			    
			    for (int i = startI; i != maxI; i = i + incI) {
			    	 
			      String format = formats[i];
			      Log.e("Formate of video no=", format);
			      percent += percentD;
			      Log.e("percent=",String.valueOf(percent ));
			      if (links.get(format) == null) {
			    	  Log.e("formate status=","formate null");
			        continue;
			      }
			      if (getSizes) {
			    	  Log.e("formate link=",String.valueOf(links.get(format)));
			        int size=0 ;//= getFileSize((String) links.get(format));
			        
			        try {
						
			        	
			        	//String length_of_video=new GetVideosize().execute(String.valueOf(links.get(format))).get();
			        	String length_of_video=GetVideosize(String.valueOf(links.get(format))) ;
			        	
			        	size=Integer.valueOf(length_of_video);
			        	
			        } catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
			        	
			        
			        //new GetVideosize().execute(strUrl).get()
			        Log.e("Video Size after extract by server=",String.valueOf(size));
			        if (size != -1) {
			        	
			        Youtube_Data_Setter_Getter data_Setter_Getter=new Youtube_Data_Setter_Getter();
			        	
			          float roundedSize = Math.round(size / 104857.6) / 10f;
			          System.out.println("setDownloadLinksss(" + format + ", '" + links.get(format) + "', '" + title + "', " + roundedSize + ", " + percent + ");");
			        
			          data_Setter_Getter.setFormate(findFormate(format));
			          data_Setter_Getter.setSongs_link(String.valueOf(links.get(format)));
			          data_Setter_Getter.setTitle(title);
			          data_Setter_Getter.setRounded_size(roundedSize );
			          data_Setter_Getter.setPercent(percent);
			          
			          youtube_Data.add(data_Setter_Getter);
			          //formate_list.add(format);//list send to callback method
			         //links_list.add(String.valueOf(links.get(format)));
			         // youtubelistener.youtubeUrlInfo(format,String.valueOf(links.get(format)),title,roundedSize,percent);
			          
			          // jsObject.eval("setDownloadLink(" + format + ", '" + links.get(format) + "', '" + title + "', " + roundedSize + ", " + percent + ");");
			          continue;
			        }
			      }
			      System.out.println("setDownloadLink(" + format + ", '" + links.get(format) + "', '" + title + "');");
			      //jsObject.eval("setDownloadLink(" + format + ", '" + links.get(format) + "', '" + title + "');");
			    }
			    
			    //add arraylistdata in class
			    //call callback method for send data...
			    youtubelistener.youtubeUrlInfo(youtube_Data);
		         
			    
			}
			
			
			
			private String getSubstring(String src, String begin, String end) {
			    String res = null;
			    try {
			      String[] arrayOfString1 = src.split(begin);
			      String[] arrayOfString2 = arrayOfString1[1].split(end);
			      res = arrayOfString2[0];
			    } catch (Exception e) {
			      return null;
			    }
			    return res;
			  }

			
			 public Map getLinks(String page, String videoId) {
				    HashMap res = new HashMap();
				    try {
				      String fmt = getSubstring(page, "url_encoded_fmt_stream_map=", "&");
				      if (fmt == null) {
				    	  System.out.print("showError('VIDEO_NOT_FOUND");
				       // jsObject.eval("showError('VIDEO_NOT_FOUND');");
				        return null;
				      }
				      fmt = URLDecoder.decode(fmt, "UTF-8");
				      fmt = URLDecoder.decode(fmt, "UTF-8");
				      	
				      fmt = "," + fmt;

				      // ищем первое =
				      int j = fmt.indexOf("=");

				      // запоминаем, разделитель, например ",type="
				      String devider = fmt.substring(0, j + 1);

				      // разбиваем fmt на массив по этому разделителю
				      String[] urls = fmt.split(devider);

				      for (int i = 1; i < urls.length; i++) {

				        String url = devider + urls[i];

				        // удаляем "," в начале и если она есть в конце
				        url = url.substring(1);
				     //   Log.e("Url Test1",url);
				        if (url.endsWith(",")) {
				          url = url.substring(0, url.length() - 1);
				        }
				     //   Log.e("Url Test2",url);
				        // находим url=
				        j = url.indexOf("url=");
				        
				        // переносим в начало всё от url= и до конца
				        if (j > 0) {
				          String urlPart = url.substring(j) + "&";
				          String otherPart = url.substring(0, j - 1);
				          url = urlPart + otherPart;
				        }
				      //  Log.e("Url Test3",url);
				        // убираем "url=" из начала
				        url = url.substring(4) + "&";
				        
				        // Убираем "&type=..."
				        url = url.replaceFirst("&type=.*?&", "&");

				        // Убираем "&quality=..."
				        url = url.replaceFirst("&quality=.*?&", "&");

				        // Убираем 1 "&itag=..."
				        url = url.replaceFirst("&itag=.*?&", "&");

				        //System.out.println(url);
				      //  Log.e("Url Test4",url);
				        // signature
				        if (url.indexOf("&sig=") > -1) {
				          // "&sig=" меняем на "&signature="
				          url = url.replaceFirst("&sig=", "&signature=");
				         // Log.e("Url Test5",url);
				        } else {

				          if (url.indexOf("&s=") > -1) {
				            //return getLinksWithEncryptedSignatures(videoId);
				            int sb = url.indexOf("&s=") + 3;
				            int se = url.indexOf("&", sb);
				            String sigO = url.substring(sb, se);
				          //  Log.e("Current url check=", sigO);
				            String sigN = (String) "decryptSignature('" + sigO + "');";
				            Log.e("Url Test6",url);
				            if (sigN == null) {
				            	System.out.print("'SIGNATURE_DECODING_ERROR'");
				            	//  Log.e("Url Test7",url);
				             //jsObject.eval("showError('SIGNATURE_DECODING_ERROR');");
				              return null;
				            }

				            url = url.replaceFirst(sigO, sigN);
				            url = url.replaceFirst("&s=", "&signature=");
				         //   Log.e("Url Test8",url);
				          }
				          
				        }
				        
				        // удаляем "&" в конце
				        url = url.substring(0, url.length() - 1);
				       // Log.e("Url Test9",url);
				        // запоминаем ссылку на видео
				        String key = getSubstring(url, "&itag=", "&");
				        if (key == null) {
				          key = getSubstring(url, "\\?itag=", "&");
				        }
				        res.put(key, url);
				       // Log.e("Url Test10",url);
				      }
				    } catch (UnsupportedEncodingException e) {
				      e.printStackTrace();
				    }
				   // Log.e("Original url=",url);
				    return res;
				  }  

			 
			public String GetVideosize (String url_video) {
				
				Log.e("Get Video Size call==","yes hang");
				try
				{
					
					Log.e("Async=","call onbackground");
				
				//Uri uri=Uri.parse("http://r1---sn-a5m7zu7z.c.youtube.com/CiILENy73wIaGQnzBeEsgVIz2xMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
				
				
				URL  url = new URL(url_video);
				URLConnection conexion = url.openConnection();
				
				conexion.connect();

			 lenghtOfFile = conexion.getContentLength();
				
				//Log.e("Length of video Size= ",String.valueOf(lenghtOfFile));
				}
				catch(Exception ex)
				{
					Log.e("Exception in backgrount=",String.valueOf(ex));
					
				}
				// pbar.dismiss();
				return String.valueOf(lenghtOfFile);

			} 
			 
			/* 
			 class GetVideosize extends AsyncTask<String, String, String>
			 {

				@Override
				protected String doInBackground(String... urls) {
				
					try
					{
						
						Log.e("Async=","call onbackground");
					
					//Uri uri=Uri.parse("http://r1---sn-a5m7zu7z.c.youtube.com/CiILENy73wIaGQnzBeEsgVIz2xMYESARFEgGUgZ2aWRlb3MM/0/0/0/video.3gp");
					
					
					URL  url = new URL(urls[0]);
					URLConnection conexion = url.openConnection();
					
					conexion.connect();

				 lenghtOfFile = conexion.getContentLength();
					
					//Log.e("Length of video Size= ",String.valueOf(lenghtOfFile));
					}
					catch(Exception ex)
					{
						Log.e("Exception in backgrount=",String.valueOf(ex));
						
					}
					 pbar.dismiss();
					return String.valueOf(lenghtOfFile);
				}
				 
				@Override
				protected void onPreExecute() {
					
					super.onPreExecute();
					Log.e("Async=","call onpre");
					
					 pbar.setMessage("Loading...");
					 pbar.show();  
				}
				
				@Override
				protected void onPostExecute(String result) {
			
					super.onPostExecute(result);
					Log.e("Video size post",result);
					Log.e("Async=","call onPost");
					Toast.makeText(context, "Yahoo call onpostExecte..", Toast.LENGTH_LONG).show();
					
				}
			 }*/
 
		 
		public interface youtubedatalistener
		{
			public void youtubeContentInfo(String title,String link,String image,String time);
		
			public void youtubeUrlInfo(ArrayList<Youtube_Data_Setter_Getter> youtube_data);
			
			
		}
		 
		public String findFormate(String formate) {
			
			int format=Integer.valueOf(formate);
			String formate_data=null;
			if(format==13)
			{
				//Toast.makeText(context, "formate", Toast.LENGTH_LONG).show();
				
				return formate_data="176x144(3GP 144p)";
			}
			else if(format==17)
			{
				//Toast.makeText(context, "formate", Toast.LENGTH_LONG).show();
				
				return formate_data="176x144(3GP 144p) ";
			}
			else if(format==36)
			{
				return formate_data="320x240(3GP 240p) ";
			}
			else if(format==5)
			{
				return formate_data="FLV Video";
			}
			else if(format==6)
			{
				return formate_data="FLV(426x240)";
			}
			else if(format==34)
			{
				return formate_data="FLV(426x240)";
			}
			else if(format==35)
			{
				return formate_data="FLV(426x240)";
			}
			else if(format==18)
			{
				return formate_data="640x360(MP4 360p)";
			}
			else if(format==22)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==37)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==83)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==82)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==85)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==84)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==38)
			{
				return formate_data="1280x720(MP4 720p)";
			}
			else if(format==43)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==44)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==45)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==46)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==100)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==101)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else if(format==102)
			{
				return formate_data="640x360(WEBM 360p)";
			}
			else return "invalid ";
		}
}
