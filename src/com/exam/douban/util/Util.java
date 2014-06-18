package com.exam.douban.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.activity.DetailActivity;
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
	 * @param context
	 * @param type
	 * @param movieInfo
	 */
	public void saveHistory(Context context, String type, String movieInfo) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				type, Activity.MODE_APPEND);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(movieInfo, movieInfo);
		Log.i("movieInfo", movieInfo);

		if (editor.commit())
			Log.i("MainActivity", "历史记录成功");

		// 读取历史记录
	}

	/**
	 * 这里Intent跳转会报错，不知如何解决
	 * 
	 * @param id
	 * @param img
	 * @param text
	 * @param context
	 * @return
	 */
	public ViewGroup showPersonOrMoive(final String id, Bitmap img,
			String text, final Context context) {

		LinearLayout lin = new LinearLayout(context.getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);

		ImageView iv = new ImageView(context.getApplicationContext());
		iv.setImageBitmap(img);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context.getApplicationContext(),
						PersonDetailActivity.class);
				intent.putExtra("url", id);
				context.startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params对应view的布局

		TextView tv = new TextView(context.getApplicationContext());
		tv.setTextAppearance(context.getApplicationContext(),
				android.R.attr.textAppearanceLarge);
		tv.setText(text);
		lin.addView(tv, lp);
		Log.i("OUTPUT", "布局完成");
		return lin;
	}

	public String download(String urlstr) {
		StringBuffer sBuffer = new StringBuffer();
		try {
			URL url = new URL(urlstr);
			System.out.println(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(3000);
			connection.setRequestMethod("GET");

			String line;
			InputStreamReader isr = new InputStreamReader(
					connection.getInputStream(), "UTF-8");
			BufferedReader buffer = new BufferedReader(isr);
			System.out.println("response code---"
					+ connection.getResponseCode());
			while ((line = buffer.readLine()) != null) {
				sBuffer.append(line);
			}
			// isr.close();
			connection.disconnect();
		} catch (Exception e) {
			Log.i("OUT PUT", "download error");
			e.printStackTrace();

		}
		return sBuffer.toString();
	}

	/**
	 * 接受图片url，返回bit
	 * 
	 * @param bmurl
	 * @return 图片数据
	 */
	public Bitmap downloadImg(String bmurl) {
		Bitmap bm = null;
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			URL url = new URL(bmurl);
			URLConnection connection = url.openConnection();
			bis = new BufferedInputStream(connection.getInputStream());
			bm = BitmapFactory.decodeStream(bis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (is != null)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return bm;
	}

	/**
	 * 解析出演员和导演的信息，因为这两个信息格式是一样的
	 * 
	 * @param s
	 * @param title
	 * @return List<PersonData>
	 */
	public List<PersonData> parsePersonArray(JSONObject s, String title) {
		List<PersonData> avatars = new ArrayList<PersonData>();
		JSONArray dir;
		try {
			dir = s.getJSONArray(title);
			for (int j = 0; j < dir.length(); j++) {
				JSONObject d = dir.getJSONObject(j);
				PersonData cast = new PersonData();
				cast.setName(d.getString("name"));

				JSONObject a = d.getJSONObject("avatars");// 演员头像
				cast.setImg(downloadImg(a.getString("medium")));

				cast.setId(d.getString("id"));// 演员id
				avatars.add(cast);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return avatars;
	}

	/**
	 * 解析电影条目信息（简版）
	 * 
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
				movie.setImg(downloadImg(images.getString(imgType)));
				list.add(movie);
				movie.print();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
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
				context.startActivity(new Intent(context, MainActivity.class));
				((Activity) context).finish();
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
	 * 解析jsonarray数据到string
	 * 
	 * @param arr
	 * @return 字符串
	 */
	// public String parseJSONArraytoString(JSONArray arr) {
	// StringBuffer str = new StringBuffer();
	//
	// for (int i = 0; i < arr.length(); i++) {
	// try {
	// str = str.append(arr.getString(i)).append(" ");
	// Log.i("parse Json line", arr.getString(i));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// return str.toString();
	// }
	/**
	 * 把数据放到Movie数据结构里
	 * 
	 * @param str
	 *            json格式的字符串
	 * @return MovieData
	 */

}
