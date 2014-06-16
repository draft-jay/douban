//package com.exam.douban.activity;
//
//import java.io.InputStream;
//import java.net.URL;
//import java.util.List;
//
//import com.exam.douban_movie_get.R;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//
///**
// * 
// * @author 用于详细信息显示的页面
// * 
// */
//
//public class DetailActivity extends Activity {
//	private final static int LOAD_BOOK = 1;
//	private final static int LOAD_MUSIC = 2;
//	private final static int LOAD_MOVIE = 3;
//
//	private TextView singleTextView;// 显示 名称 导演等信息的 文本控件
//	private TextView singletext;// 显示内容简介的文本控件
//	private ImageView imageView;// 显示图片的图片控件
//	//private Button button;// "返回 "按钮
//	private List<MovieData> moiveList; // 电影所有信息的泛型LIST
//	private ProgressDialog proDialog;
//
//	private String imageUrl;// 图片的地址
//	private String url;// Entity的具体地址
//
//	private MovieData movie;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_detail);
//		initView();
//		initData();
//		addListener();
//		
//		new Thread(new LoadData()).start();
//		proDialog.show();
//	}
//
//	Handler mHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message message) {
//			int value = message.what;
//			switch (value) {
//			case LOAD_MOVIE:
//				for (MovieData m : moiveList) {
//					if (m.getWebSite() == null) {// 网址为空时: 为 "不详"
//						m.setWebSite("不详");
//					}
//					if (m.getWriter() == null) {// 编剧为空时: 为"不详"
//						m.setWriter("不详");
//					}
//					if (m.getAuthor() == null) {// 导演为空时:为"不详"
//						m.setAuthor("不详");
//					}
//
//					singleTextView.setText("影视:" + m.getTitle() + "\n导演:"
//							+ m.getAuthor() + "\n编剧:" + m.getWriter() + ".."
//							+ "\n官方网站:" + m.getWebSite());
//					String summary = m.getSummary();
//					if (summary.length() > 264) {
//						singletext.setText("简介：\n" + summary.substring(0, 264) + "...");
//					}
//					
//					try {
//						URL aryURI = new URL(m.getImageUrl());
//						InputStream is = aryURI.openStream();
//						Bitmap bm = BitmapFactory.decodeStream(is);
//						if (bm == null) {
//							imageView.setBackgroundColor(R.drawable.nopicture);
//						}
//						is.close();
//						imageView.setImageBitmap(bm);
//						proDialog.dismiss();
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//
//				}
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//
//	private void initView() {
//		singleTextView = (TextView) findViewById(R.id.singletextview);
//		singletext = (TextView) findViewById(R.id.singletext);
//		imageView = (ImageView) findViewById(R.id.singleimageview);
//		//button = (Button) findViewById(R.id.button);
//
//		proDialog = new ProgressDialog(this);
//		proDialog.setTitle(R.string.loading);
//		proDialog.setMessage("请您耐心等待...");
//	}
//
//	private void initData() {
//		Bundle bundle = getIntent().getExtras();
//		String type = bundle.getString("type");
//		String id = bundle.getString("url");
//		
//		if (type.equals("电影")) {
//			url = "https://api.douban.com/v2/movie/subject/" + id;
//		}else if(type.equals("音乐")){
//			url = "https://api.douban.com/v2/music/" + id;
//		}else if(type.equals("书籍"))
//		{
//			url = "https://api.douban.com/v2/book/" + id;
//		}
//		
//		DebugUtil.error(url);
//		
//		imageUrl = bundle.getString("imageurl");
//
//		detailedInfo = new DetailedInfoLoder();
//	}
//
//	class LoadData implements Runnable {
//
//		@Override
//		public void run() {
//			int choice = 0;
//			try {
//				if (url.contains("movie")) {
//					moiveList = detailedInfo.findMovieJsonTwo(url, imageUrl);
//					choice = LOAD_MOVIE;
//				} else if (url.contains("music")) {
//					musicList = detailedInfo.findJsonMusicTwo(url, imageUrl);
//					choice = LOAD_MUSIC;
//				} else if (url.contains("book")) {
//					bookList = detailedInfo.findJsonBookTwo(url, imageUrl);
//					choice = LOAD_BOOK;
//				}
//				mHandler.sendEmptyMessage(choice);
//
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	private void addListener() {
//		
//	}
//}
