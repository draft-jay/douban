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
import org.json.JSONObject;

import com.exam.douban.activity.MovieData;
import com.exam.douban_movie_get.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.Log;

public class Util {

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
