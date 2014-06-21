package com.exam.douban.activity;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.Properties;
import com.exam.douban.loader.DetailLoader;
import com.exam.douban.loader.ImgLoader;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 
 * @author 用于详细信息显示的页面
 * 
 */

public class DetailActivity extends Activity {

	private TextView mInfo;// 显示 名称 导演等信息的 文本控件
	private ImageView mImg;// 显示图片的图片控件
	private LinearLayout lin_director;// 导演的四个显示位
	private LinearLayout lin_cast;// 演员的四个显示位
	private Button btn_back;
	private Button btn_home;
	private TextView tv_dir;
	private TextView tv_cast;
	private ProgressDialog proDialog;
	private String url;// 电影的具体url
	private Util util = new Util();
	private DetailLoader detailLoader;
	private ImgLoader imgLoader;
	private MovieData movie;// 电影信息的数据实体

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
		util.backClick(btn_back, btn_home, DetailActivity.this);

		new Thread(new LoadData()).start();
		proDialog.show();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message message) {
			mInfo.setText(movie.getTitle() + "\n" + movie.getRating() + "\n"
					+ movie.getYear() + "\n\n" + movie.getTag());
			//加载出图片的预设图片
			 mImg.setImageResource(R.drawable.img_medium);
			 //异步加载海报
//			 imgLoader.displayImg(movie.getImgUrl(),mImg);
			 imgLoader = new ImgLoader(DetailActivity.this,h,PersonDetailActivity.class,movie.getDirList());
			 imgLoader.displayImg(movie.getImgUrl(),mImg);
			// 动态加载LinearLayout布局，显示人物头像和姓名
			imgLoader.loadPerson(lin_director);
			
			imgLoader = new ImgLoader(DetailActivity.this,h,PersonDetailActivity.class,movie.getCastList());
			imgLoader.loadPerson(lin_cast);
			
			tv_cast.setVisibility(View.VISIBLE);
			tv_dir.setVisibility(View.VISIBLE);
			proDialog.dismiss();
		}
	};
	Handler h = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bitmap bm = (Bitmap) msg.obj;
			int id = msg.arg1;
			ImageView iv = (ImageView) findViewById(id);
			iv.setImageBitmap(bm);
		}
	};

	/**
	 * 初始化view
	 */
	private void initView() {
		mInfo = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		lin_director = (LinearLayout) findViewById(R.id.lin_director);
		lin_cast = (LinearLayout) findViewById(R.id.lin_cast);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_home = (Button) findViewById(R.id.btn_home);
		tv_cast = (TextView) findViewById(R.id.tv_cast);
		tv_dir = (TextView) findViewById(R.id.tv_dir);

		proDialog = new ProgressDialog(this);
		// proDialog.setMessage("Loading...");
	}

	/**
	 * 初始化数据(得到url)
	 */
	private void initData() {
		 Bundle extra = getIntent().getExtras();
		 String id = extra.getString("id");
		 url = "https://api.douban.com/v2/movie/subject/" + id;
//		url = "https://api.douban.com/v2/movie/subject/1764796";
		// System.out.println("id----"+id);
		detailLoader = new DetailLoader();
		
	}

	
	/**
	 * @author 加载数据（下载）
	 */
	private class LoadData implements Runnable {

		@Override
		public void run() {
			try {

				movie = detailLoader.loadDetailInfo(url);
				if(movie == null){
					proDialog.dismiss();
					Toast.makeText(getApplicationContext(), "加载失败", 0).show();
					((Activity) getApplicationContext()).finish();
				}
				// String result =
				// util.download("https://api.douban.com/v2/movie/subject/2049435");
				Log.i("OUTPUT", "detail download completed");
				// Log.i("Download Data", result);
				Log.i("OUTPUT", "detail parse completed");
				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}


}
