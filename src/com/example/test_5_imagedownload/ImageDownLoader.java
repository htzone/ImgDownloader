package com.example.test_5_imagedownload;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownLoader {
	
//	伸缩类型
	public static int TYPE_SCALEING = 1;
//  裁剪类型
	public static int TYPE_CUTTING  = 0;
	
//	图片在内存中的缓存
	LruCache<String, Bitmap> mBitmapCache;
//	线程池
	ExecutorService mExecutorService;
//	引用传入的ImageView
	ImageView mImageView ;
//	引用传入得url
	String url ;
	
//	默认长宽和类型
	private int width = 200;
	private int height = 200;
	private int type = 1;
	
//	构造函数 初始化各类数据
	@SuppressLint("NewApi") 
	public ImageDownLoader(){
//		获得系统运行时内存大小
		int maxMemory = (int) Runtime.getRuntime().maxMemory();  
//		取其八分之一作为内存缓存的大小
        int mCacheSize = maxMemory / 8;
		mBitmapCache = new LruCache<String,Bitmap>(mCacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
//				return value.getRowBytes()*value.getHeight();
				return value.getByteCount();
			}
			
		};
//		初始化线程池，固定大小的线程池，特点是当运行的线程数超过线程池规定大小时，未执行的线程会等待执行线程完成后再执行
		mExecutorService = Executors.newFixedThreadPool(MyUtil.getNumCores()*3);
		
	}
	
	public void setBitmapHeight(int height){
		this.height = height;
	}
	
	public void setBitmapWidth(int width){
		this.width = width;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
//	下载图片并且设置到传入的imageview中
	public void downloadAndSet(final ImageView imageView, final String url){
		
		imageView.setTag(url);
		
//			异步更行UI线程
		final Handler mHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				if(((String)imageView.getTag()).equals(url)){
					
					if((Bitmap)msg.obj != null){
						imageView.setImageBitmap((Bitmap)msg.obj);	
					}
					else {
						imageView.setImageResource(R.drawable.image_no);
					}
				}
				
				
			}
		};
		
//				利用线程池执行线程
		mExecutorService.execute(new Runnable() {
					
			@Override
			public void run() {
						// TODO Auto-generated method stub
//				判断URL是否为空，为空则返回false
				if(MyUtil.isEmpty(url)){
					Log.d("ddd", "图片url为空！");
					return ;
				}
//						从缓存中读取bitmap，首先从内存中获取，如果没有则从存储卡中获取
				Bitmap bitmap = getBitmapFromCache(url);
//				如果缓存为空，则进行下载
				if(bitmap == null){
//							图片下载
					bitmap = MyUtil.getBitmapFormURL(url, type, width, height);
//					将图片写入缓存，首先写入内存，再写入SD卡中
					writeBitmapToCache(url, bitmap);
				}
//				设置图片
				Message msg = mHandler.obtainMessage();
				msg.obj = bitmap;
				mHandler.sendMessage(msg);
						
			}
		});
	
	}
//	从缓存中获取图片
	private Bitmap getBitmapFromCache(String url) {
		// TODO Auto-generated method stub
		Bitmap bitmap = null;

		bitmap = mBitmapCache.get(url);
		Log.d("ddd", "get....");

		if (bitmap == null) {
//			Log.d("ddd", "get(key)");
			String fileName = MyUtil.getFileName(url);
			bitmap = getBitmapFromStorage(fileName);
//			Log.d("ddd", "read end");
		} 
		
		return bitmap;
		

	}
//	从存储卡中获取图片
	private Bitmap getBitmapFromStorage(String fileName) {
		// TODO Auto-generated method stub
		FileInputStream iStream = null;
		BufferedInputStream buffer = null;
		Bitmap bitmap = null;
		try {
			File path = Environment.getExternalStorageDirectory();
			String filePath = path.getAbsolutePath()+MyUtil.downPathImageDir+fileName;
			File file = new File(filePath);
			if(file.exists()){
				iStream = new FileInputStream(file);
				buffer = new BufferedInputStream(iStream);
				bitmap = BitmapFactory.decodeStream(buffer);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("ddd", "getBitmapFromStorage:"+e.toString());
			return bitmap;
		}
		finally{
			try {
				if(iStream != null)
					iStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(buffer != null)
					buffer.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		return bitmap;
	}

	private void writeBitmapToCache(String url, Bitmap bitmap){
	    try {

			mBitmapCache.put(url, bitmap);	
			Log.d("ddd", "put success!");
			
			File path = Environment.getExternalStorageDirectory();
		    File fileDirectory = new File(path.getAbsolutePath() + MyUtil.downPathImageDir);
		    if (!fileDirectory.exists()) {
		      fileDirectory.mkdirs();
		    }
		    
		    String fileName = MyUtil.getFileName(url);
	    	File file = new File(fileDirectory, fileName);
	  	    if(!file.exists()){
	  	    	file.createNewFile();
	  	    }
	  	    
	  	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	  	    bitmap.compress(CompressFormat.PNG, 0, bos);
	  	    byte[] bitmapdata = bos.toByteArray(); 
	  	    FileOutputStream fos = new FileOutputStream(file);
	  	    fos.write(bitmapdata);
	  	    fos.flush();
	  	    fos.close();
	  	    
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("ddd", "writeBitmapToCache: "+e.toString());
		}
	 
		
	}

}
