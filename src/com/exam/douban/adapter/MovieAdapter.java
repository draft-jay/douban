package com.exam.douban.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.exam.douban.activity.MovieDetailActivity;
import com.exam.douban.activity.MainActivity;
import com.exam.douban.activity.HistoryActivity;
import com.exam.douban.activity.PersonDetailActivity;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.loader.ImgLoader;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Movie;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {
	private Context context;
	private List<MovieData> ml = null;
	private List<PersonData> pl = null;
//	private boolean busy = false;
	private String info;
	private ImgLoader imgLoader;
	
	public MovieAdapter(Context context,List<MovieData> movieList) {
		this.context = context;
		ml = movieList;
	}

	public MovieAdapter(HistoryActivity context, List<PersonData> personList) {
		this.context = context;
		pl = personList;
	}

	@Override
	public int getCount() {
		if(ml!=null)
		return ml.size();
		else if(pl!=null)
			return pl.size();
		return 0;
	}
	/**
	 * 文字加载完的flag
	 * @param busy
	 */
//	public void setFlagBusy(boolean busy) {
//		this.busy = busy;
//	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
//	ImageView cover;
//	Handler h = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			Bitmap bm = (Bitmap) msg.obj;
//			int id = msg.arg1;
//			View view = LayoutInflater.from(context).inflate(R.layout.search_row,null);
//			ImageView cover = (ImageView) view.findViewById(R.id.img_row_search);
//			cover.setImageBitmap(bm);
//		}};
	@Override
	/**
	 * 在这里把拿到到的<MovieData>格式的列表数据以search_row.xml的方式排列在activity_main.xml
	 * 拿到布局文件。在search_row.xml定义了一个id，这里就能找到
	 * 这里不能用setContentView(R.layout.activity_main)拿到布局
	 * 若inflate（）中第一个参数有对应的类，第二个参数需要非null，不然无法显示。
	 * @vonvertView 数据显示在这个view里
	 * @position 集合中元素的位置
	 * 下载图片的没有检测缓存，每次都是重新下载
	 */
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.search_row,null);
		TextView title = (TextView) convertView.findViewById(R.id.tv_row_search);
		ImageView cover = (ImageView) convertView.findViewById(R.id.img_row_search);
		
		if(ml!=null){
			imgLoader = new ImgLoader(context, ml);
			MovieData movie = ml.get(position);
			title.setText(movie.getTitle()+"\n"+"评分："+movie.getRating()+"\n"+"上映时间："+movie.getYear());
			String url = movie.getImgUrl();
			imgLoader.displayImg(url,cover);
//			imgLoader.displayImg(ml.get(position).getImgUrl());
			
			
		}
		if(pl!=null){
			imgLoader = new ImgLoader(pl);
			PersonData person = pl.get(position);
			String url = person.getImgUrl();
			imgLoader.displayImg(url,cover);
			title.setText(person.getName()+"\n"+person.getName_en()+"\n"+"出生地："+person.getBorn_place());
//			cover.setImageBitmap(pl.get(position).getImg());
		}
		
		
		
		return convertView;
	}
	
	public String getInfo(){
		return info;
	}
	public void setInfo(String s){
		info = s;
	}
	
}
