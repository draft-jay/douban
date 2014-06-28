package com.exam.douban.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.activity.MovieDetailActivity;
import com.exam.douban.activity.HistoryActivity;
import com.exam.douban.activity.MainActivity;
import com.exam.douban.activity.PersonDetailActivity;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 工具类
 */
public class Util {

	/**
	 * 保存正在浏览的条目
	 * 
	 * @param context
	 * @param type
	 * @param id
	 */
	public void saveHistory(Context context, String type, String id) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				type, Activity.MODE_APPEND);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(id, id);
		Log.i("movieInfo", id);

		if (editor.commit())
		Log.i("", "历史记录成功");

		// 读取历史记录
	}

	/**
	 * 顶部返回按钮的监听方法
	 * 
	 * @param back
	 * @param home
	 * @param context
	 */
	public void backClick(Button back, Button home, final Context context) {
		// TODO Auto-generated method stub
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				((Activity) context).finish();
				context.startActivity(new Intent(context, MainActivity.class));
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				((Activity) context).finish();
			}
		});

	}

	/**
	 * 向豆瓣发出请求，返回字符串数据
	 * 
	 * @param urlstr
	 * @return
	 * @throws IOException
	 */
	public String download(String urlstr) {

		try {
			URL url = new URL(urlstr);
			System.out.println(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(3000);
			connection.setRequestMethod("GET");

			String line;
			InputStreamReader isr;

			isr = new InputStreamReader(connection.getInputStream(), "UTF-8");

			if (isr != null) {
				BufferedReader buffer = new BufferedReader(isr);
				Log.i("Response Code", connection.getResponseCode() + "");
				StringBuffer sBuffer = new StringBuffer();
				while ((line = buffer.readLine()) != null) {
					sBuffer.append(line);
				}
				return sBuffer.toString();
			} 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "ERROR";
	}

	/**
	 * 解析搜索返回的电影条目信息（简版）
	 * 和人物的作品信息格式类似
	 * @param s
	 * @param str
	 * @param imgType
	 *            下载的电影尺寸
	 * @return
	 */
	public List<MovieData> parseMovieData(JSONObject s, String str,
			String imgType) {

		List<MovieData> list = new ArrayList<MovieData>();

		try {
			JSONArray total = s.getJSONArray(str);
			for (int i = 0; i < total.length(); i++) {
				MovieData movie = new MovieData();
				JSONObject m;
				if (str.equals("works")) {
					JSONObject mov = total.getJSONObject(i);
					m = mov.getJSONObject("subject");
				} else
					m = total.getJSONObject(i);
				movie.setTitle(m.getString("title"));
				movie.setId(m.getString("id"));
				movie.setYear(m.getString("year"));

				JSONObject rating = m.getJSONObject("rating");
				movie.setRating(rating.getString("average"));// 表示评到几分

				JSONObject images = m.getJSONObject("images");
				movie.setImgUrl(images.getString(imgType));
				list.add(movie);
				movie.print();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
