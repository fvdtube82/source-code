package com.download.fvd.activity;

import java.io.Serializable;

import android.util.Log;

public class YoutubeContentInfo_Setter_Getter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String link;
	private String image;
	private String time;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	
}
