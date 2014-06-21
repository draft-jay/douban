package com.exam.douban.entity;

import java.util.List;

import android.graphics.Bitmap;
/**
 * 电影的数据结构
 * 以目前的功能来看，似乎不需要把影人的资料单独写个类
 * @author 
 *
 */
public class MovieData {
	
	private String title=null;
	private String id = null;
	private String year = null;
	private String rating = null;
	private String imgUrl = null;
	private String tag = null;
	private String country = null ;
	private List<PersonData> castList;
	private List<PersonData> dirList;
	
	
	
	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String tiitle) {
		this.title = tiitle;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}


	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<PersonData> getCastList() {
		return castList;
	}

	public void setCastList(List<PersonData> castList) {
		this.castList = castList;
	}

	public List<PersonData> getDirList() {
		return dirList;
	}

	public void setDirList(List<PersonData> dirList) {
		this.dirList = dirList;
	}

	public MovieData() {
		// TODO Auto-generated constructor stub
	}
	
	public void print(){
		System.out.println("id--"+id+"---title--"+title);
	}

}
