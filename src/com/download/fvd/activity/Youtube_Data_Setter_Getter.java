package com.download.fvd.activity;

import java.io.Serializable;

public class Youtube_Data_Setter_Getter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String formate;
	private String songs_link;
	private String title;
	private float rounded_size;
	private int percent;
	public String getFormate() {
		return formate;
	}
	public void setFormate(String formate) {
		this.formate = formate;
	}
	public String getSongs_link() {
		return songs_link;
	}
	public void setSongs_link(String songs_link) {
		this.songs_link = songs_link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getRounded_size() {
		return rounded_size;
	}
	public void setRounded_size(float rounded_size) {
		this.rounded_size = rounded_size;
	}
	public int getPercent() {
		return percent;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
	
}
