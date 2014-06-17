package com.exam.douban.entity;

import java.util.List;

import android.graphics.Bitmap;
/**
 * 影人数据结构
 */
public class PersonData {
	private String name;
	private Bitmap img;
	private Bitmap imgLarge;
	private String id;
	private String name_en;
	private String gender;
	private String birthday;
	private String born_place;
	private List<MovieData> works;
	
	
	
	public List<MovieData> getWorks() {
		return works;
	}
	public void setWorks(List<MovieData> works) {
		this.works = works;
	}
	public Bitmap getImgLarge() {
		return imgLarge;
	}
	public void setImgLarge(Bitmap imgLarge) {
		this.imgLarge = imgLarge;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBorn_place() {
		return born_place;
	}
	public void setBorn_place(String born_place) {
		this.born_place = born_place;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getImg() {
		return img;
	}
	public void setImg(Bitmap img) {
		this.img = img;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	
}
