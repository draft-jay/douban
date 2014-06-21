package com.exam.douban.entity;

import java.util.List;

import android.graphics.Bitmap;
/**
 * 影人数据结构
 */
public class PersonData {
	private String name=null;
	private String imgUrl=null;
	private String id=null;
	private String name_en=null;
	private String gender=null;
	private String birthday = "不详";
	private String born_place=null;
	private List<MovieData> works=null;
	
	
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String string) {
		this.imgUrl = string;
	}
	public List<MovieData> getWorks() {
		return works;
	}
	public void setWorks(List<MovieData> works) {
		this.works = works;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void print() {
		// TODO Auto-generated method stub
		System.out.println(getName() + "\n" + getName_en() + "\n\n"
				+ getBirthday() + "\n" + getBorn_place());
	}

	
}
