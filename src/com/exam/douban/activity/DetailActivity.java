package com.exam.douban.activity;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author 用于详细信息显示的页面
 * 
 */

public class DetailActivity extends Activity {

	private TextView mInfo;// 显示 名称 导演等信息的 文本控件
	private ImageView mImg;// 显示图片的图片控件
	private ImageView dImg;// 显示导演照片
	private ImageView cImg;// 显示演员照片
	private TextView cName;// 演员名字
	private TextView dName;// 导演名字

	// private Button button;// "返回 "按钮
	private List<MovieData> moiveList; // 电影所有信息的泛型LIST
	private ProgressDialog proDialog;
	private String url;// 电影的具体url
	private Util util = new Util();

	private MovieData movie = new MovieData();// 电影信息的数据实体

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		System.out.println("111111");
		initView();
		System.out.println("22222222");
		initData();
		System.out.println("333333333");
		// addListener();

		new Thread(new LoadData()).start();
		proDialog.show();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			mInfo.setText(movie.getmTitle() + "\n" + movie.getmRating() + "\n"
					+ movie.getmYear() + "\n\n" + movie.getmTag());
			// imageView.setImageBitmap(bm);
			mImg.setImageBitmap(movie.getmImgMedium());
			
			proDialog.dismiss();

		};
	};
	/**
	 * 初始化view
	 */
	private void initView() {
		mInfo = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		dImg = (ImageView) findViewById(R.id.img_d);
		cImg = (ImageView) findViewById(R.id.im_cast);
		
		cName = (TextView) findViewById(R.id.tv_c_name);
		dName = (TextView) findViewById(R.id.tv_d_name);
		// button = (Button) findViewById(R.id.button);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
//		Bundle bundle = getIntent().getExtras();
//		String id = bundle.getString("id");

//		url = "https://api.douban.com/v2/movie/subject/" + id;

		// DebugUtil.error(url);
		

		// imageUrl = bundle.getString("imageurl");

	}
	/**
	 * 
	 * @author 加载数据（下载）
	 *
	 */
	class LoadData implements Runnable {

		@Override
		public void run() {
			try {
				String result = util.download("https://api.douban.com/v2/movie/subject/1764796");
				Log.i("OUTPUT","detail download completed");
				Log.i("Download Data",result);
				parseDetailInfo(result);
				Log.i("OUTPUT","detail parse completed");
				
				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private void parseDetailInfo(String str) {
		// TODO Auto-generated method stub
//		try {
//			JSONObject s = new JSONObject(str);
//			JSONArray total = s.getJSONArray("subjects");
//			for (int i = 0; i < total.length(); i++) {
//				JSONObject m = total.getJSONObject(i);
//				movie.setmTitle(m.getString("title"));
//				movie.setmId(m.getString("id"));
//				movie.setmYear(m.getString("year"));
//
//				JSONObject rating = m.getJSONObject("rating");
//				movie.setmRating(rating.getString("average"));// 表示评到几分
//				moiveList.add(movie);
//				movie.print();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
