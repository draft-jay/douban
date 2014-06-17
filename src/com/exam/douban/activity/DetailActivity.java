package com.exam.douban.activity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.MovieData;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	private LinearLayout lin_director;//导演的四个显示位
	private LinearLayout lin_cast;//演员的四个显示位

	// private Button button;// "返回 "按钮
//	private List<MovieData> movieList; // 电影所有信息的泛型LIST
	private List<PersonData> castList;
	private List<PersonData> directorList;
	private ProgressDialog proDialog;
	private String url;// 电影的具体url
	private Util util = new Util();

	private MovieData movie = new MovieData();// 电影信息的数据实体

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
//		addListener();

		new Thread(new LoadData()).start();
		proDialog.show();
	}
	

	Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message message) {
			mInfo.setText(movie.getmTitle() + "\n" + movie.getmRating() + "\n"
					+ movie.getmYear() + "\n\n" + movie.getmTag());
			// imageView.setImageBitmap(bm);
			mImg.setImageBitmap(movie.getmImgLarge());
			
			for (int i = 0; i < directorList.size(); i++) {
				String id = directorList.get(i).getId();
				Bitmap img = directorList.get(i).getImg();
				String name = directorList.get(i).getName();
				ViewGroup layout = showPersonOrMoive(id, img,name);
				lin_director.addView(layout);
			}
			for (int j = 0; j < castList.size(); j++) {
				String id = castList.get(j).getId();
				Bitmap img = castList.get(j).getImg();
				String name = castList.get(j).getName();
				ViewGroup layout = showPersonOrMoive(id, img,name);
				lin_cast.addView(layout);
			}
			System.out.println("directorList.getClass().getName()变量类型-----"+directorList.getClass().getName());
			parse(directorList);
			proDialog.dismiss();
		}
		/**
		 * 想把解析导演/演员做成一个方法里
		 * @param list 作填充数据的list
		 */
		private void parse(List list) {
			// TODO Auto-generated method stub
			System.out.println("list.getClass().getAnnotations()变量类型-----"+list.getClass().getAnnotations());
			System.out.println("list.get(0).getClass()变量类型-----"+list.get(0).getClass());
//			if (list.getClass().getSimpleName()) {
				
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
		// button = (Button) findViewById(R.id.button);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}

	/**
	 * 初始化数据(得到url)
	 */
	private void initData() {
//		 Bundle bundle = getIntent().getExtras();
		Bundle extra = getIntent().getExtras();
		String id = extra.getString("id");
		url = "https://api.douban.com/v2/movie/subject/" + id;

	}
	
	/**
	 * 返回文字-图片的LinearLayout布局
	 * @param id 点击图片后跳转的url的Id
	 * @param img 要显示的图片
	 * @param context 
	 * @return
	 */
	public ViewGroup showPersonOrMoive(final String id,Bitmap img,String text){
		
		LinearLayout lin = new LinearLayout(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);
		
		ImageView iv = new ImageView(getApplicationContext());
		iv.setImageBitmap(img);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),PersonDetailActivity.class);
				intent.putExtra("url", id);
				startActivity(intent);
			}
		});
		lin.addView(iv, lp);//addView(view,params)params对应view的布局
		
		TextView tv = new TextView(getApplicationContext());
		tv.setTextAppearance(getApplicationContext(), android.R.attr.textAppearanceLarge);
		tv.setText(text);
		lin.addView(tv,lp);
		Log.i("OUTPUT", "布局完成");
		return lin;
	}

	/**
	 * @author 加载数据（下载）
	 */
	private class LoadData implements Runnable {

		@Override
		public void run() {
			try {
				String result = util.download(url);
//				String result = util.download("https://api.douban.com/v2/movie/subject/2049435");
				
				Log.i("OUTPUT", "detail download completed");
				Log.i("Download Data", result);
				parseDetailInfo(result);
				Log.i("OUTPUT", "detail parse completed");

				handler.sendMessage(new Message());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void parseDetailInfo(String str) {
			// TODO Auto-generated method stub
			try {
				JSONObject s = new JSONObject(str);
				castList = parsePersonArray(s, "casts");
				directorList = parsePersonArray(s, "directors");

				movie.setmYear(s.getString("year"));
				JSONObject rating = s.getJSONObject("rating");
				movie.setmRating(rating.getString("average"));// 表示评到几分

				JSONObject images = s.getJSONObject("images");
				movie.setmImgLarge(util.downloadImg(images.getString("large")));
				movie.setmTitle(s.getString("title"));
				JSONArray genres = s.getJSONArray("genres");
				StringBuffer buffer = new StringBuffer();
				for (int j = 0; j < genres.length(); j++) {
					if(j==genres.length()-1)
						buffer = buffer.append(genres.getString(j));
					else
						buffer = buffer.append(genres.getString(j)+" / ");
						
				}
				movie.setmTag(buffer.toString());
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 解析出演员和导演的信息，因为这两个信息格式是一样的
		 * @param s
		 * @param title
		 * @return
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
					cast.setImg(util.downloadImg(a.getString("medium")));
					cast.setId(d.getString("id"));// 演员id
					avatars.add(cast);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return avatars;
		}
	}
}
