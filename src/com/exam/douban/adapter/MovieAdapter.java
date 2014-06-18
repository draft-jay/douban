package com.exam.douban.adapter;

import java.util.List;
import java.util.zip.Inflater;

import com.exam.douban.activity.HistoryActivity;
/**
 * 自定义适配器
 */
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieAdapter extends BaseAdapter {
	private Context context;
	private List<MovieData> ml;
	private List<PersonData> pl;
	private boolean busy = true;
	private String info;
	
	public MovieAdapter(Context context,List<MovieData> movieList) {
		this.context = context;
		ml = movieList;
	}

	public MovieAdapter(HistoryActivity context2, List<PersonData> personList) {
		// TODO Auto-generated constructor stub
		this.context = context2;
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
	public void setFlagBusy(boolean busy) {
		this.busy = busy;
	}

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
			String t = ml.get(position).getTitle();
			String y = ml.get(position).getRating();
			String c = ml.get(position).getYear();
			title.setText(t+"\n"+y+"\n"+c);
			cover.setImageBitmap(ml.get(position).getImg());
		}
		if(pl!=null){
			String t = pl.get(position).getName();
			String y = pl.get(position).getName_en();
			String c = pl.get(position).getBorn_place();
			title.setText(t+"\n"+y+"\n"+"出生地："+c);
			cover.setImageBitmap(pl.get(position).getImg());
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
