package com.exam.douban.loader;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exam.douban.entity.MovieData;
import com.exam.douban.entity.PersonData;
import com.exam.douban.util.Util;

public class DetailLoader {
	Util util = new Util();
	/**
	 * 解析出演员和导演的信息，因为这两个信息格式是一样的
	 * @param json 从豆瓣返回的json数据
	 * @param title
	 * @return List<PersonData>
	 */
	public MovieData loadDetailInfo(String url) {
		MovieData movie = new MovieData();
//		List<MovieData> movielist = new ArrayList<MovieData>();
		// TODO Auto-generated method stub
		try {
			String result = util.download(url);
			if(result.equals("ERROR")){
				return null;
			}
			JSONObject json = new JSONObject(result);
			movie.setCastList(parsePersonArray(json, "casts"));
			movie.setDirList(parsePersonArray(json, "directors"));

			movie.setYear(json.getString("year"));
			JSONObject rating = json.getJSONObject("rating");
			movie.setRating(rating.getString("average"));// 表示评到几分

			JSONObject images = json.getJSONObject("images");
			movie.setImgUrl(images.getString("large"));
			movie.setTitle(json.getString("title"));
			JSONArray genres = json.getJSONArray("genres");
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < genres.length(); j++) {
				if(j==genres.length()-1)
					buffer = buffer.append(genres.getString(j));
				else
					buffer = buffer.append(genres.getString(j)+" / ");
					
			}
			movie.setTag(buffer.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movie;
	}
	
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
				cast.setImgUrl(a.getString("medium"));

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
