package com.exam.douban.cache;

import java.io.File;

import com.exam.douban.util.FileCacheUtil;

import android.os.Environment;
import android.util.Log;

public class ImgFileCache {
	
	private String dirString = null;
	
	public ImgFileCache(){
		dirString = getCacheDir();
		boolean ret = createDirectory(dirString);
		Log.i("CreateDirectory:",  dirString + ", ret = " + ret);
	}
	
	public File getFile(String path) {
		File f = new File(getSavePath(path));
		return f;
	}

	private String getSavePath(String path) {
		String fileName = String.valueOf(path.hashCode());
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
	public String getCacheDir() {
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)){
			return Environment.getExternalStorageDirectory() + "/DBMovie/cache/";
		}else
			return Environment.getDataDirectory() + "/data/DBMovie/cache/";
	}

	public boolean clearFile(){
		return FileCacheUtil.deleteFile(dirString);
	}
}
