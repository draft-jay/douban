package com.exam.douban.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.adapter.MovieAdapter;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.Properties;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryActivity extends Activity {

	// private List<MovieData> movieList ;
	private MovieAdapter ma;
	private ListView lv;
	private Button btn_person;
	private Button btn_movie;
	private Button btn_back;
	private Button btn_home;
	private Button btn_clean;
	private Util util = new Util();
	private ProgressDialog mpd;
	private String type = "MovieHistory";

	private List<MovieData> movieList = null;
	private List<PersonData> personList = null;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
//			if (movieList == null || personList == null) {
//				Toast.makeText(getApplicationContext(), "无历史记录",
//						Toast.LENGTH_SHORT).show();
//			} else {

				if (type.equals(Properties.HISTORY_NAME_PERSON)) {
					ma = new MovieAdapter(HistoryActivity.this, personList);
				} else {
					ma = new MovieAdapter(HistoryActivity.this, movieList);
				}
				lv.setAdapter(ma);
//			}
			// 进度条消失
			mpd.dismiss();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		lv = (ListView) findViewById(R.id.lv_show);
		btn_clean = (Button) findViewById(R.id.btn_clean);
		btn_movie = (Button) findViewById(R.id.btn_movie);
		btn_person = (Button) findViewById(R.id.btn_person);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_home = (Button) findViewById(R.id.btn_home);
		btn_home.setVisibility(View.GONE);
		util.backClick(btn_back, btn_home, HistoryActivity.this);

		new Thread(new load()).start();
		mpd = new ProgressDialog(this);
		mpd.show();
		listener();

	}

	private void listener() {
		// TODO Auto-generated method stub
		btn_movie.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				type = Properties.HISTORY_NAME_MOVIE;
				new Thread(new load()).start();
				mpd.show();
			}
		});
		btn_person.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				type = Properties.HISTORY_NAME_PERSON;
				new Thread(new load()).start();
				mpd.show();
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent i = new Intent();
				if(type.equals(Properties.HISTORY_NAME_MOVIE)){
					i.setClass(getApplicationContext(), MovieDetailActivity.class);
					i.putExtra("id", movieList.get(position).getId());
				}else{
					i.setClass(getApplicationContext(), PersonDetailActivity.class);
					i.putExtra("id", personList.get(position).getId());
				}
				startActivity(i);
			}
		});
		/**
		 * 清除一列记录
		 */
		btn_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SharedPreferences shared = getSharedPreferences(type,
						Activity.MODE_PRIVATE);
				Editor e = shared.edit();
				if (e.clear().commit()) {
					Toast.makeText(HistoryActivity.this, "清空成功", 0).show();
					new Thread(new load()).start();
					handler.sendMessage(new Message());
				}
				// ma = new MovieAdapter(HistoryActivity.this, movieList);
				// lv.setAdapter(ma);

			}
		});

	}

	private class load implements Runnable {

		public void run() {
			/**
			 * 读取历史记录（id）
			 * 
			 * @param type
			 */

			movieList = new ArrayList<MovieData>();
			personList = new ArrayList<PersonData>();
			SharedPreferences shared = getSharedPreferences(type,
					Activity.MODE_PRIVATE);
			Iterator i = shared.getAll().keySet().iterator();
//			Message msg = new Message();
//			msg.arg1 = 1;
			while (i.hasNext()) {
				try {
					String url = null;
					if (type.equals(Properties.HISTORY_NAME_MOVIE)) {
						// movieList
						MovieData movie = new MovieData();
						movie.setId(i.next().toString());// key-value都是一样的
						url = "https://api.douban.com/v2/movie/subject/"
								+ movie.getId();
						String result = util.download(url);
						if (result.equals("ERROR")) {
//							msg.arg1 = 0;
							continue;
						}
						
						JSONObject s;
						s = new JSONObject(result);
						Log.i("Download Data", result);

						movie.setYear(s.getString("year"));
						JSONObject rating = s.getJSONObject("rating");
						movie.setRating(rating.getString("average"));// 表示评到几分
						JSONObject images = s.getJSONObject("images");
						movie.setImgUrl(images.getString("small"));
						movie.setTitle(s.getString("title"));
						movieList.add(movie);
						movie.print();

					} else {
						PersonData person = new PersonData();
						person.setId(i.next().toString());
						url = "https://api.douban.com/v2/movie/celebrity/"
								+ person.getId();
						String result = util.download(url);

						if (result.equals("ERROR")) {
//							msg.arg1 = 0;
							continue;
						}

						JSONObject s = new JSONObject(result);

						JSONObject images = s.getJSONObject("avatars");// 头像
						person.setImgUrl(images.getString("small"));
						// person.setBirthday(s.getString("birthday"));
						person.setName(s.getString("name"));
						person.setName_en(s.getString("name_en"));
						person.setBorn_place(s.getString("born_place"));
						person.setId(s.getString("id"));
						personList.add(person);
						person.print();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			handler.sendMessage(new Message());

		}

	}

}
