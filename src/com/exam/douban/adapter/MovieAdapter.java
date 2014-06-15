package com.exam.douban.adapter;

import java.util.List;
import java.util.zip.Inflater;

/**
 * 自定义适配器
 */
import com.exam.douban.activity.MovieData;
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
	public MovieAdapter(Context context,List<MovieData> movieList) {
		this.context = context;
		ml = movieList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
	 */
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.search_row, null);
		TextView title = (TextView) convertView.findViewById(R.id.tv_row_search_title);
		TextView rating = (TextView) convertView.findViewById(R.id.tv_row_search_rating);
		TextView year = (TextView) convertView.findViewById(R.id.tv_row_searc_date);
		ImageView cover = (ImageView) convertView.findViewById(R.id.img_row_search);

		title.setText(ml.get(position).getmTitle());
		rating.setText(ml.get(position).getmRating());
		year.setText(ml.get(position).getmYear());
		cover.setImageBitmap(ml.get(position).getmImgSmall());
		System.out.println("--------");
//		cover.setImageBitmap(ml.get(position).getmImg());
		return null;
	}

}
