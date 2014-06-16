package com.exam.douban.activity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exam.douban.adapter.MovieAdapter;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

//    private TextView tv_title;
//    private TextView tv_date;
//    private TextView tv_rating;
    private Button btn_search;
    private EditText edt_search;
    private Handler handler;
    private ProgressDialog mpd;
    private MovieAdapter ma;
    private List<MovieData> movieList = new ArrayList<MovieData>();
    private ListView lv;
    private Util util = new Util();
    MovieData movie = new MovieData();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        tv_title=(TextView)findViewById(R.id.tv_row_search_title);
//        tv_rating=(TextView)findViewById(R.id.tv_row_search_rating);
//        tv_date=(TextView)findViewById(R.id.tv_row_searc_date);
        edt_search = (EditText) findViewById(R.id.et_search);
        btn_search=(Button)findViewById(R.id.btn_search);
        lv = (ListView) findViewById(R.id.lv_show);
        
        ma = new MovieAdapter(MainActivity.this, movieList);
        lv.setAdapter(ma);
        
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	loading();
            	new DownloadThread(edt_search.getText().toString()).start();
            }

        });
        
        handler=	new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
//                movie= (MovieData) msg.obj;
//                ma.notifyDataSetChanged();
                ma = new MovieAdapter(MainActivity.this, movieList);
                lv.setAdapter(ma);
                //进度条消失
                mpd.dismiss();
            }
        };
    }
//    public void onActivityResult(int requestCode,int resultCode,Intent data)
//    {
//        mpd=new ProgressDialog(this);
//        mpd.setMessage("请稍候，正在读取信息...");
//        mpd.show();
//
//        String urlstr="https://api.douban.com/v2/"+result.getContents();
//        Log.i("OUTPUT",urlstr);
        //启动下载线程下载电影信息
//        new DownloadThread(urlstr).start();
//    }

    protected void loading() {
    	mpd=new ProgressDialog(this);
    	mpd.setMessage("请稍候，正在读取信息...");
    	mpd.show();
		
	}

	private class DownloadThread extends Thread
    {
        String title=null;
        
        public DownloadThread(String title) 
        {
//				url="https://api.douban.com/v2/movie/search?q="+URLEncoder.encode(title, "utf-8");
				this.title=title;
        }
        public void run()
        {
        	String ch;
			try {
				ch = URLEncoder.encode(title, "utf-8");
				String uString = "http://api.douban.com/v2/movie/search?q=" + ch+"&count=10";
				String result = util.download(uString);
				Log.i("Download Data", result);
				parseMovieData(result);
				Log.i("OUTPUT", "parse completly");
//			Toast.makeText(MainActivity.this, "下载失败", 0).show();
				//给主线程UI界面发消息，提醒下载信息，解析信息完毕
				Message msg=new Message();
//            msg.obj=movie;
				handler.sendMessage(msg);
				Log.i("OUTPUT","msg send completly");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
    }
    public void parseMovieData(String str) {

		try {
			JSONObject s = new JSONObject(str);
			JSONArray total = s.getJSONArray("subjects");
			for (int i = 0; i < total.length(); i++) {
				JSONObject m = total.getJSONObject(i);
				movie.setmTitle(m.getString("title"));
				movie.setmId(m.getString("id"));
				movie.setmYear(m.getString("year"));

				JSONObject rating = m.getJSONObject("rating");
				movie.setmRating(rating.getString("average"));// 表示评到几分

				JSONObject images = m.getJSONObject("images");
				movie.setmImgSmall(util.downloadImg(images.getString("small")));
				movie.setmImgMedium(util.downloadImg(images.getString("medium")));
				
				movieList.add(movie);
				movie.print();
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
