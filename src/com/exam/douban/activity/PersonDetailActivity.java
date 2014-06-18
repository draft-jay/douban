package com.exam.douban.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PersonDetailActivity extends Activity {
	private TextView Info;// 显示 影人基本信息的 文本控件
	private ImageView mImg;// 显示图片的图片控件
	private LinearLayout lin_works;//
	// private List<MovieData> works = new ArrayList<MovieData>();

	// private Button button;// "返回 "按钮
	private ProgressDialog proDialog;
	private PersonData person;
	private String url;// 电影的具体url
	private Util util = new Util();
	private Button btn_back;
	private Button btn_home;
	private TextView tv_wokes;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_detail);

		initView();
		initData();

		util.backClick(btn_back, btn_home, PersonDetailActivity.this);
		new Thread(new Load()).start();
		proDialog.show();
	}

	/**
	 * 初始化view
	 */
	private void initView() {
		Info = (TextView) findViewById(R.id.tv_m);
		mImg = (ImageView) findViewById(R.id.img_m);
		lin_works = (LinearLayout) findViewById(R.id.lin_pserson_m);
		tv_wokes = (TextView) findViewById(R.id.tv_works);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_home = (Button) findViewById(R.id.btn_home);

		proDialog = new ProgressDialog(this);
		proDialog.setMessage("Loading...");
	}

	/**
	 * 初始化数据(得到url)
	 */
	private void initData() {
		// Bundle bundle = getIntent().getExtras();
		Bundle extra = getIntent().getExtras();
		String id = extra.getString("id");
		url = "https://api.douban.com/v2/movie/celebrity/" + id;

	}

	Handler handler = new Handler() {
		//返回的数据里都没有birthday这个字段
		@Override
		public void handleMessage(Message message) {
			Info.setText(person.getName() + "\n" + person.getName_en() + "\n\n"
					+ "生日：" +person.getBirthday()+ "\n" + "出生地" + person.getBorn_place());
			mImg.setImageBitmap(person.getImg());
			
			// 动态布局
			ArrayList<MovieData> works = (ArrayList<MovieData>)person.getWorks();
			System.out.println("works ---  "+works.toString());
			for (int i = 0; i < works.size(); i++) {
				String id = works.get(i).getId();
				Bitmap img = works.get(i).getImg();
				String name = works.get(i).getTitle();
				ViewGroup layout = showPersonOrMoive(id, img, name);
				lin_works.addView(layout);
				System.out.println("dongtaibuju----"+i);
			}
			
			tv_wokes.setVisibility(View.VISIBLE);
			proDialog.dismiss();
		};
	};

	/**
	 * 返回文字-图片的LinearLayout布局
	 * 
	 * @param id
	 *            点击图片后跳转的url的Id
	 * @param img
	 *            要显示的图片
	 * @param context
	 * @return
	 */
	public ViewGroup showPersonOrMoive(final String id, Bitmap img, String text) {

		LinearLayout lin = new LinearLayout(getApplicationContext());
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);

		ImageView iv = new ImageView(getApplicationContext());
		iv.setImageBitmap(img);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						DetailActivity.class);
				intent.putExtra("id", id);
				util.saveHistory(getApplicationContext(), Properties.HISTORY_NAME_MOVIE, id);
				startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params对应view的布局

		TextView tv = new TextView(getApplicationContext());
		tv.setTextAppearance(getApplicationContext(),
				android.R.attr.textAppearanceLarge);
		tv.setWidth(155);
		tv.setText(text);
		// if(text.length()>4){
		// tv.setText(text.substring(0,4)+"...");
		// }
		lin.addView(tv, lp);
		Log.i("OUTPUT", "布局完成");
		return lin;
	}

	/**
	 * @author 加载数据（下载）
	 */
	private class Load implements Runnable {

		@Override
		public void run() {
			try {
				String result = util.download(url);
				// String result =
				// util.download("https://api.douban.com/v2/movie/subject/2049435");

				Log.i("OUTPUT", "detail personData download completed");
				Log.i("Download Data", result);
				parseDetailInfo(result);
				Log.i("OUTPUT", "detail person parse completed");
				handler.sendMessage(new Message());
				Log.i("OUTPUT", "msg sent completed");
				

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * 解析电影详情信息并保存到列表
		 * 
		 * @param str
		 * @param works
		 *            影人作品，最多五个
		 */
		private void parseDetailInfo(String result) {

			// 这里和搜索电影返回的数据格式和豆瓣上写的不一样，作品条目不同，subject不是数组，
			person = new PersonData();
			try {
				JSONObject s = new JSONObject(result);

				person.setWorks(util.parseMovieData(s, "works","medium"));
				Log.i("OUTPUT", "works parse completly");

				JSONObject images1 = s.getJSONObject("avatars");// 头像
				person.setImg(util.downloadImg(images1.getString("medium")));
				// person.setBirthday(s.getString("birthday"));
				person.setName(s.getString("name"));
				person.setName_en(s.getString("name_en"));
				person.setGender(s.getString("gender"));
				person.setBorn_place(s.getString("born_place"));
				person.print();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
