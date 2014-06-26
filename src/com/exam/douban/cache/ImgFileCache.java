package com.exam.douban.cache;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class ImgFileCache {
	private String dirString;
	
	public ImgFileCache(){
		dirString = getCacheDir();
		boolean ret = createDirectory(dirString);
		Log.i("CreateDirectory:",  dirString + ", ret = " + ret);
	}
	
	public File getFile(String imgUrl) {
		File f = new File(getSavePath(imgUrl));
		return f;
	}

	private String getSavePath(String imgUrl) {
		String fileName = String.valueOf(imgUrl.hashCode());
		return getCacheDir() + fileName;
	}
	
	public boolean createDirectory(String filePath){
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()){
			return true;
		}
		
		return file.mkdirs();

	}

	/**
	 * 若有SD权限：filePath:/sdcard/DBMovie/cache/files/
	 * 若SD权限：   filePath: /data/data/data/DBMovie/cache/files/
	 * @return
	 */
	private String getCacheDir() {
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)){
			return Environment.getExternalStorageDirectory() + "/DBMovie/cache/files/";
		}else
			return Environment.getDataDirectory() + "/data/DBMovie/cache/files/";
	}

}
