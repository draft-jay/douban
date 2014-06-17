package com.exam.douban.activity;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.exam.douban.adapter.MovieAdapter;
import com.exam.douban.entity.MovieData;
import com.exam.douban.util.Util;
import com.exam.douban_movie_get.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    private Button btn_search;
    private EditText edt_search;
    private Handler handler;
    private ProgressDialog mpd;
    private MovieAdapter ma;
    private List<MovieData> movieList = new ArrayList<MovieData>();;
    private ListView lv;
	private Util util = new Util();
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_search = (EditText) findViewById(R.id.et_search);
        btn_search=(Button)findViewById(R.id.btn_search);
        lv = (ListView) findViewById(R.id.lv_show);
        
        Listener();
        
        
        
        handler=	new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                ma = new MovieAdapter(MainActivity.this, movieList);
                lv.setAdapter(ma);
                ma.notifyDataSetChanged();
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
//        String urlstr="https://api.douban.com/v2/movie/search?q="+data.getDataString()+"&count=10";
//        Log.i("OUTPUT",urlstr);
////        启动下载线程下载电影信息
//        new DownloadThread(urlstr).start();
//    }

    private void Listener() {
    	btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	loading();
            	new DownloadThread(edt_search.getText().toString()).start();
            }

        });
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				MovieData m = movieList.get(position);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, DetailActivity.class);
				intent.putExtra("id", m.getmId());
				startActivity(intent);
				
			}
		});
		
	}

	protected void loading() {
    	mpd=new ProgressDialog(this);
    	mpd.setMessage("Loading...");
    	mpd.show();
		
	}

	private class DownloadThread extends Thread
    {
        String title=null ;
        
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
				
				JSONObject s = new JSONObject(result);
				movieList = util.parseMovieData(s,"subjects");
				
				Log.i("OUTPUT", "parse completly");
//			Toast.makeText(MainActivity.this, "下载失败", 0).show();
				//给主线程UI界面发消息，提醒下载信息，解析信息完毕
				Message msg=new Message();
//            msg.obj=movie;
				handler.sendMessage(msg);
				Log.i("OUTPUT","msg send completly");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }
    

}
