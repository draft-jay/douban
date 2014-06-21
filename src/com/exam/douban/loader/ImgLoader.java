package com.exam.douban.loader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.exam.douban.activity.DetailActivity;
import com.exam.douban.activity.HistoryActivity;
import com.exam.douban.activity.MainActivity;
import com.exam.douban.activity.PersonDetailActivity;
import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.entity.Properties;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 只获取10条结果，不考虑栈空间、内存
 */
public class ImgLoader {
	private Context context;
	private Util util = new Util();
	private Handler handl;
	List<MovieData> movieList = null;
	List<PersonData> personList = null;
	ExecutorService executorService;
	Class<?> targetCls;

	public ImgLoader(PersonDetailActivity context, Handler handl, Class<?> cls,
			List<MovieData> list) {
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
		this.handl = handl;
		this.targetCls = cls;
		movieList = list;

	}

	public ImgLoader(DetailActivity context, Handler handl, Class<?> cls,
			List<PersonData> list) {
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
		this.handl = handl;
		this.targetCls = cls;
		personList = list;
	}
	
	public ImgLoader(Context context,Handler handl,List<MovieData> list) {
		executorService = Executors.newFixedThreadPool(5);
		this.handl = handl;
		movieList = list;
	}

	public ImgLoader(Handler handl,List<PersonData> list) {
		executorService = Executors.newFixedThreadPool(5);
		this.handl = handl;
		personList = list;
	}

	
	/**
	 * 动态加载布局
	 * @param list
	 * @param lin
	 */
	public void loadPerson(LinearLayout lin) {
		
		if (movieList != null) {
			for (int i = 0; i < movieList.size(); i++) {
				System.out.println(i);
				String id = movieList.get(i).getId();
				String title = movieList.get(i).getTitle();
				String imgurl = movieList.get(i).getImgUrl();
				ViewGroup layout = showPerson(id, imgurl, title);// 返回布局对象
				lin.addView(layout);
			}
		}
		if (personList != null) {
			for (int i = 0; i < personList.size(); i++) {
				String id = personList.get(i).getId();
				String name = personList.get(i).getName();
				String imgurl = personList.get(i).getImgUrl();
				ViewGroup layout = showPerson(id, imgurl, name);// 返回布局对象
				lin.addView(layout);
			}
		}

	}

	// public void loadPerson(List<MovieData> works,LinearLayout lin_works) {
	// // TODO Auto-generated method stub
	// for (int i = 0; i < works.size(); i++) {
	// String id = works.get(i).getId();
	// String title = works.get(i).getTitle();
	// String imgurl = works.get(i).getImgUrl();
	// ViewGroup layout = showPerson(id, imgurl, title);// 返回布局对象
	// lin_works.addView(layout);
	// }
	// }

	/**
	 * 返回文字-图片的LinearLayout布局 动态布局
	 * 
	 * @param id
	 *            点击图片后跳转的url的Id
	 * @param img
	 *            要显示的图片
	 * @param context
	 * @return
	 */
	public ViewGroup showPerson(final String id, String imgurl, String text) {

		LinearLayout lin = new LinearLayout(context);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lin.setOrientation(LinearLayout.VERTICAL);

		final ImageView iv = new ImageView(context);
		iv.setBackgroundResource(R.drawable.img_medium);
		iv.setId(Integer.parseInt(id));

//		displayImg(imgurl, Integer.parseInt(id));
		displayImg(imgurl, iv);

		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, targetCls);
				intent.putExtra("id", id);
				util.saveHistory(context, Properties.HISTORY_NAME_PERSON, id);
				context.startActivity(intent);
			}
		});
		lin.addView(iv, lp);// addView(view,params)params对应view

		TextView tv = new TextView(context);
		tv.setTextAppearance(context, android.R.attr.textAppearanceLarge);
		tv.setText(text);
		tv.setWidth(155);
		lin.addView(tv, lp);
		Log.i("OUTPUT", "布局完成");
		return lin;
	}

	
	/**
	 * 下载图片的主要方法 两种方式更新UI：handler和RunOnUiThread()
	 * @param imgUrl
	 * @param handler
	 * @param viewId
	 */
	public void displayImg(String imgurl, ImageView iv) {
		PhotoToLoad p = new PhotoToLoad(imgurl, iv);
		executorService.submit(new PhotosLoader(p));

	}
	public void displayImg(String imgUrl, int viewId) {
		PhotoToLoad p = new PhotoToLoad(imgUrl, viewId);
		executorService.submit(new PhotosLoader(p));
	}
	/**
	 * 下载图片的队列
	 */
	class PhotosLoader extends Thread{
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			Bitmap bitmap = downloadImg(photoToLoad.imgUrl);
			// UI更新方法一
//			Message msg = new Message();
//			msg.obj = bitmap;
//			msg.arg1 = photoToLoad.viewId;
//			handl.sendMessage(msg);
			// 方法二
			// 这种方式虚拟机空间容易不够用,导致刷不出图片
			 BitmapDisplayer bd = new BitmapDisplayer(bitmap, photoToLoad);
			 Activity a = (Activity) photoToLoad.imageView.getContext();
			 Log.i("bit","");
			 a.runOnUiThread(bd);

		}
	}
	/**
	 * 接受图片url，返回bitmap
	 * @param imgurl
	 */
	public  Bitmap downloadImg(String imgUrl) {
		try {
			Bitmap bitMap = null;
			URL url = new URL(imgUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setInstanceFollowRedirects(true);//重定向跳转
			
			BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
			bitMap = BitmapFactory.decodeStream(bis);
//			System.out.println("downloadImgurl----"+imgUrl);
			Log.i("downloadImgurl",imgUrl);
			return bitMap;
		} catch (Exception e) {
			Log.i("download state","error");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 用于在UI线程中更新界面 最后通过a.runOnUiThread()方法更新在UI线程
	 * Activity.runOnUiThread(Runnable)是跟新UI界面的另一种方式 把更新ui的代码创建在Runnable中，
	 * 然后在需要更新ui时，把这个Runnable对象传给Activity.runOnUiThread(Runnable)。
	 * 这样Runnable对像就能在ui程序中被调用。如果当前线程是UI线程,那么行动是立即执行。
	 * 如果当前线程不是UI线程,操作是发布到事件队列的UI线程
	 */
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else
				System.out.println("bitmap is null");

		}
	}

	/**
	 * 创建一个简单的待下载的图片的数据结构
	 */
	class PhotoToLoad {
		public String imgUrl;
		public int viewId;
		public ImageView imageView;

		public PhotoToLoad(String imgUrl, int viewId) {
			this.imgUrl = imgUrl;
			this.viewId = viewId;
		}

		public PhotoToLoad(String imgUrl, ImageView iv) {
			this.imgUrl = imgUrl;
			imageView = iv;
		}
	}

}
