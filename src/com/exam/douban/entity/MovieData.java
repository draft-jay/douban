package com.exam.douban.entity;

import android.graphics.Bitmap;
/**
 * 电影的数据结构
 * @author 
 *
 */
public class MovieData {
	
	private String mTitle=null;
	private String mId = null;
	private String mYear = null;
	private String mRating = null;
	private Bitmap mImgSmall = null;
	private Bitmap mImgMedium = null;
	private Bitmap mImgLarge = null;
	private String mTag = null;
	private String mCountry = null ;
	
	

	public Bitmap getmImgLarge() {
		return mImgLarge;
	}

	public void setmImgLarge(Bitmap mImgLarge) {
		this.mImgLarge = mImgLarge;
	}

	public Bitmap getmImgSmall() {
		return mImgSmall;
	}

	public void setmImgSmall(Bitmap mImgSmall) {
		this.mImgSmall = mImgSmall;
	}

	public Bitmap getmImgMedium() {
		return mImgMedium;
	}

	public void setmImgMedium(Bitmap mImgMedium) {
		this.mImgMedium = mImgMedium;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}

	public String getmYear() {
		return mYear;
	}

	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	public String getmRating() {
		return mRating;
	}

	public void setmRating(String mRating) {
		this.mRating = mRating;
	}


	public String getmTag() {
		return mTag;
	}

	public void setmTag(String mTag) {
		this.mTag = mTag;
	}

	public String getmCountry() {
		return mCountry;
	}

	public void setmCountry(String mCountry) {
		this.mCountry = mCountry;
	}


	public MovieData() {
		// TODO Auto-generated constructor stub
	}
	
	public void print(){
		System.out.println("id--"+mId+"---title--"+mTitle);
	}

}
