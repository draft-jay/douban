package com.exam.douban.activity;

import android.graphics.Bitmap;

public class MovieData {
	
	private String mTitle=null;
	private String mId = null;
	private String mYear = null;
	private String mRating = null;
	private Bitmap mImgSmall = null;
	private Bitmap mImgMedium = null;
	private String mTag = null;
	private String mCountry = null ;
	private String pName = null;
	private String pImg= null;
	
	
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

	public String getpName() {
		return pName;
	}

	public void setpName(String pName) {
		this.pName = pName;
	}

	public String getpImg() {
		return pImg;
	}

	public void setpImg(String pImg) {
		this.pImg = pImg;
	}

	public MovieData() {
		// TODO Auto-generated constructor stub
	}
	
	public void print(){
		System.out.println("id--"+mId+"---title--"+mTitle);
	}

}
