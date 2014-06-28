package com.exam.douban.util;

import java.io.File;

import android.util.Log;

public class FileCacheUtil {
	
	
	
	
	
	
	public static boolean deleteFile(String filePath){
		
		if(filePath == null){
			Log.e("deleteFile",filePath);
			return false;
		}
		
		File file = new File(filePath);
		
		if(file == null || !file.exists()){
			Log.e("deleteFile",file.getAbsolutePath());
			return false;
		}
		
		if(file.isDirectory()){
			File[] list = file.listFiles();
			
			for(int i=0;i<list.length;i++){
				Log.d("deleteFile",list[i].getAbsolutePath());
				if(list[i].isDirectory()){
					deleteFile(list[i].getAbsolutePath());
				}else{
					list[i].delete();
				}
			}
		}
		
		Log.d("deleteFile",file.getAbsolutePath());
		file.delete();
		return true;
		
		
	}
}
