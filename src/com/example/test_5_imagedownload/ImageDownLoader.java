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
	
//	��������
	public static int TYPE_SCALEING = 1;
//  �ü�����
	public static int TYPE_CUTTING  = 0;
	
//	ͼƬ���ڴ��еĻ���
	LruCache<String, Bitmap> mBitmapCache;
//	�̳߳�
	ExecutorService mExecutorService;
//	���ô����ImageView
	ImageView mImageView ;
//	���ô����url
	String url ;
	
//	Ĭ�ϳ��������
	private int width = 200;
	private int height = 200;
	private int type = 1;
	
//	���캯�� ��ʼ����������
	@SuppressLint("NewApi") 
	public ImageDownLoader(){
//		���ϵͳ����ʱ�ڴ��С
		int maxMemory = (int) Runtime.getRuntime().maxMemory();  
//		ȡ��˷�֮һ��Ϊ�ڴ滺��Ĵ�С
        int mCacheSize = maxMemory / 8;
		mBitmapCache = new LruCache<String,Bitmap>(mCacheSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// TODO Auto-generated method stub
//				return value.getRowBytes()*value.getHeight();
				return value.getByteCount();
			}
			
		};
//		��ʼ���̳߳أ��̶���С���̳߳أ��ص��ǵ����е��߳��������̳߳ع涨��Сʱ��δִ�е��̻߳�ȴ�ִ���߳���ɺ���ִ��
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
	
//	����ͼƬ�������õ������imageview��
	public void downloadAndSet(final ImageView imageView, final String url){
		
		imageView.setTag(url);
		
//			�첽����UI�߳�
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
		
//				�����̳߳�ִ���߳�
		mExecutorService.execute(new Runnable() {
					
			@Override
			public void run() {
						// TODO Auto-generated method stub
//				�ж�URL�Ƿ�Ϊ�գ�Ϊ���򷵻�false
				if(MyUtil.isEmpty(url)){
					Log.d("ddd", "ͼƬurlΪ�գ�");
					return ;
				}
//						�ӻ����ж�ȡbitmap�����ȴ��ڴ��л�ȡ�����û����Ӵ洢���л�ȡ
				Bitmap bitmap = getBitmapFromCache(url);
//				�������Ϊ�գ����������
				if(bitmap == null){
//							ͼƬ����
					bitmap = MyUtil.getBitmapFormURL(url, type, width, height);
//					��ͼƬд�뻺�棬����д���ڴ棬��д��SD����
					writeBitmapToCache(url, bitmap);
				}
//				����ͼƬ
				Message msg = mHandler.obtainMessage();
				msg.obj = bitmap;
				mHandler.sendMessage(msg);
						
			}
		});
	
	}
//	�ӻ����л�ȡͼƬ
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
//	�Ӵ洢���л�ȡͼƬ
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
