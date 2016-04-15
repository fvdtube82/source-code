/*
 * Zirco Browser for Android
 * 
 * Copyright (C) 2010 - 2011 J. Devauchelle and contributors.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.download.fvd.activity;

import java.util.ArrayList;

public class TopSitesAndAppsGames {

	String Title,imageUrl,Link,ID;
	ArrayList<TopSitesAndAppsGames> catDetails;
	

	public ArrayList<TopSitesAndAppsGames> getCatDetails() {
		return catDetails;
	}

	public void setCatDetails(ArrayList<TopSitesAndAppsGames> catDetails) {
		this.catDetails = catDetails;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}
	
}
