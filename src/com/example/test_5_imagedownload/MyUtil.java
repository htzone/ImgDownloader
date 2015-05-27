package com.example.test_5_imagedownload;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

public class MyUtil {
	private static String downPathRootDir = File.separator + "download" + File.separator;

	static String downPathImageDir = downPathRootDir + "cache_images" + File.separator;

	private static String downPathFileDir = downPathRootDir + "cache_files" + File.separator;

	private static int MB = 1048576;
	
	private static int freeSdSpaceNeededToCache = 200 * MB;
	
	static int downCount = 0;
	
	private static int dirSize = -1;
	
	private static int cacheSize = 100 * MB;
	
//	判断字符串是否为空
	public static boolean isEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}
	
//	判断手机是否能使用SD卡
	public static boolean isCanUseSD()
	 {
	   try
	   {
	     return Environment.getExternalStorageState().equals("mounted");
	   } catch (Exception e) {
	     e.printStackTrace();
	   }
	   return false;
	 }
	
//	SD卡空闲内存大小
	public static int freeSpaceOnSD()
	  {
	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    double sdFreeMB = stat.getAvailableBlocks() * stat.getBlockSize() / MB;
	    return (int)sdFreeMB;
	  }
	
//	获得手机的核心数
	public static int getNumCores()
	  {
	    try
	    {
	      File dir = new File("/sys/devices/system/cpu/");

	      File[] files = dir.listFiles(new FileFilter()
	      {
	        public boolean accept(File pathname)
	        {
	          return Pattern.matches("cpu[0-9]", pathname.getName());
	        }
	      });
	      return files.length;
	    } catch (Exception e) {
	    }
	    return 1;
	  }
	
//	裁剪图片
	 public static Bitmap cutImg(Bitmap bitmap, int newWidth, int newHeight)
	  {
	    if (bitmap == null) {
	      return null;
	    }

	    if ((newWidth <= 0) || (newHeight <= 0)) {
	      throw new IllegalArgumentException("裁剪图片的宽高设置不能小于0");
	    }

	    Bitmap resizeBmp = null;
	    try
	    {
	      int width = bitmap.getWidth();
	      int height = bitmap.getHeight();

	      if ((width <= 0) || (height <= 0)) {
	        return null;
	      }
	      int offsetX = 0;
	      int offsetY = 0;

	      if (width > newWidth)
	        offsetX = (width - newWidth) / 2;
	      else {
	        newWidth = width;
	      }

	      if (height > newHeight)
	        offsetY = (height - newHeight) / 2;
	      else {
	        newHeight = height;
	      }

	      resizeBmp = Bitmap.createBitmap(bitmap, offsetX, offsetY, newWidth, newHeight);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      if (resizeBmp != bitmap) {
	        bitmap.recycle();
	      }
	    }
	    return resizeBmp;
	  }
	 
//	缩放图片
	 public static Bitmap scaleImg(Bitmap bitmap, float scale)
	  {
	    Bitmap resizeBmp = null;
	    try
	    {
	      int bmpW = bitmap.getWidth();
	      int bmpH = bitmap.getHeight();

	      Matrix mt = new Matrix();

	      mt.postScale(scale, scale);
	      resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bmpW, bmpH, mt, true);
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      if (resizeBmp != bitmap) {
	        bitmap.recycle();
	      }
	    }
	    return resizeBmp;
	  }
	 
//	 缩放图片
	 public static Bitmap scaleImg(Bitmap bitmap, int newWidth, int newHeight)
	  {
	    Bitmap resizeBmp = null;
	    if (bitmap == null) {
	      return null;
	    }
	    if ((newWidth <= 0) || (newHeight <= 0)) {
	      throw new IllegalArgumentException("缩放图片的宽高设置不能小于0");
	    }

	    int srcWidth = bitmap.getWidth();
	    int srcHeight = bitmap.getHeight();

	    if ((srcWidth <= 0) || (srcHeight <= 0)) {
	      return null;
	    }

	    float scale = 0.0F;

	    float scaleWidth = newWidth / (float)srcWidth;
	    float scaleHeight = newHeight /(float)srcHeight;
	    if (scaleWidth > scaleHeight)
	      scale = scaleWidth;
	    else {
	      scale = scaleHeight;
	    }
	    resizeBmp = scaleImg(bitmap, scale);
	   
	    return resizeBmp;
	  }
	 
//	 统一根据Url来获取文件名
	 public static String getFileName(String url){
		 return Md5.MD5(url)+getSuffixFromNetUrl(url);
	 }
	 
//	从url获取bitmap
	public static Bitmap getBitmapFormURL(String imageUrl, int type, int newWidth, int newHeight)
	  {
	    Bitmap bm = null;
	    URLConnection con = null;
	    InputStream is = null;
	    try {
	      URL url = new URL(imageUrl);
	      con = url.openConnection();
	      con.setDoInput(true);
	      con.connect();
	      is = con.getInputStream();

	      Bitmap wholeBm = BitmapFactory.decodeStream(is, null, null);
	      if (type == 0)
	        bm = cutImg(wholeBm, newWidth, newHeight);
	      else if (type == 1)
	        bm = scaleImg(wholeBm, newWidth, newHeight);
	      else
	        bm = wholeBm;
	    }
	    catch (Exception e) {
//	      if (D) Log.d(TAG, e.getMessage());
	    	Log.d("ddd", ">>>"+e.toString());
	      try
	      {
	        if (is != null)
	          is.close();
	      }
	      catch (Exception e1) {
	        e1.printStackTrace();
	      }
	    }
	    finally
	    {
	      try
	      {
	        if (is != null)
	          is.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    return bm;
	  }
	

	public static String getRealFileNameFromUrl(String url)
	  {
	    String name = null;
	    try {
	      if (isEmpty(url)) {
	        return name;
	      }

	      URL mUrl = new URL(url);
	      HttpURLConnection mHttpURLConnection = (HttpURLConnection)mUrl.openConnection();
	      mHttpURLConnection.setConnectTimeout(5000);
	      mHttpURLConnection.setRequestMethod("GET");
	      mHttpURLConnection.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
	      mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
	      mHttpURLConnection.setRequestProperty("Referer", url);
	      mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
	      mHttpURLConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
	      mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
	      mHttpURLConnection.connect();
	      if (mHttpURLConnection.getResponseCode() == 200)
	        for (int i = 0; ; i++) {
	          String mine = mHttpURLConnection.getHeaderField(i);
	          if (mine == null) {
	            break;
	          }
	          if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
	            Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
	            if (m.find())
	              return m.group(1).replace("\"", "");
	          }
	        }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return name;
	  }
	
//	
	public static String getSuffixFromNetUrl(String url)
	  {
	    if (isEmpty(url)) {
	      return null;
	    }
	    String suffix = ".tmp";
	    try
	    {
	      if (url.lastIndexOf(".") != -1) {
	        suffix = url.substring(url.lastIndexOf("."));
	        if ((suffix.indexOf("/") != -1) || (suffix.indexOf("?") != -1) || (suffix.indexOf("&") != -1)) {
	          suffix = null;
	        }
	      }
	      if (suffix == null)
	      {
	        String fileName = getRealFileNameFromUrl(url);
	        if ((fileName != null) && (fileName.lastIndexOf(".") != -1))
	          suffix = fileName.substring(fileName.lastIndexOf("."));
	      }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    return suffix;
	  }
	
//	用MD5加密算法生成文件名字
	 public static String getImageFileName(String url, int width, int height, int type)
	  {
	    return Md5.MD5(url.length() + 12 + "#W" + width + 
	      "#H" + height + "#T" + type + url);
	  }
	 
	 public static class FileLastModifSort
	    implements Comparator<File>
	  {
	    public int compare(File arg0, File arg1)
	    {
	      if (arg0.lastModified() > arg1.lastModified())
	        return 1;
	      if (arg0.lastModified() == arg1.lastModified()) {
	        return 0;
	      }
	      return -1;
	    }
	  }
	 
//	 清除缓存
	  public static boolean removeCache()
	  {
	    try
	    {
	      if (!isCanUseSD()) {
	        return false;
	      }

	      File path = Environment.getExternalStorageDirectory();
	      File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	      File[] files = fileDirectory.listFiles();
	      if (files == null) {
	        return true;
	      }
	      if (dirSize == -1) {
	        dirSize += 1;
	        for (int i = 0; i < files.length; i++) {
	          dirSize = (int)(dirSize + files[i].length());
	        }

	      }

	      if (dirSize > cacheSize) {
	        int removeFactor = (int)(0.4D * files.length + 1.0D);
	        Arrays.sort(files, new FileLastModifSort());
	        for (int i = 0; i < removeFactor; i++) {
	          dirSize = (int)(dirSize - files[i].length());
	          files[i].delete();
	        }
	      }
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }
	    
	    return true;
	  }
	  
//	下载文件到SD卡
	 public static String downFileToSD(String url, String name)
	  {
		
	    InputStream in = null;
	    FileOutputStream fileOutputStream = null;
	    HttpURLConnection con = null;
	    String downFilePath = null;
	    File file = null;
	    try {
	      if (!isCanUseSD()) {
	        return null;
	      }
	      File path = Environment.getExternalStorageDirectory();
	      File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	      if (!fileDirectory.exists()) {
	        fileDirectory.mkdirs();
	      }

	      file = new File(fileDirectory, name);
	      if (!file.exists()) {
	        file.createNewFile();
	      } else {
	        String str1 = file.getPath();
	        return str1;
	      }
	      downFilePath = file.getPath();
	      URL mUrl = new URL(url);
	      con = (HttpURLConnection)mUrl.openConnection();
	      con.connect();
	      in = con.getInputStream();
	      fileOutputStream = new FileOutputStream(file);
	      byte[] b = new byte[1024];
	      int temp = 0;
	      while ((temp = in.read(b)) != -1)
	        fileOutputStream.write(b, 0, temp);
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    } finally {
	      try {
	        if (in != null)
	          in.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	      try {
	        if (fileOutputStream != null)
	          fileOutputStream.close();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	      try {
	        if (con != null)
	          con.disconnect();
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }

	      try
	      {
	        if (file.length() == 0L) {
	          Log.d("ddd", "下载出错了，文件大小为0");
	          file.delete();
	        } else {
	          downCount += 1;
	        }
	        if (downCount >= 10)
	        {
	          removeCache();
	          downCount = 0;
	        }
	      }
	      catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	    

	    return downFilePath;
	  }
	
//	public static Bitmap getBitmapFromSDCache(String url, int type, int newWidth, int newHeight)
//	  {
//	    Bitmap bit = null;
//	    try {
//	      if (isEmpty(url)) {
//	        return null;
//	      }
//
//	      if ((!isCanUseSD()) || (freeSdSpaceNeededToCache < freeSpaceOnSD())) {
//	        bit = getBitmapFormURL(url, type, newWidth, newHeight);
//	        return bit;
//	      }
//
//	      if ((type != 2) && ((newWidth <= 0) || (newHeight <= 0))) {
//	        throw new IllegalArgumentException("缩放和裁剪图片的宽高设置不能小于0");
//	      }
//
//	      File path = Environment.getExternalStorageDirectory();
//	      File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
//
//	      String suffix = getSuffixFromNetUrl(url);
//
//	      String fileName = getImageFileName(url, newWidth, newHeight, type);
//	      File file = new File(fileDirectory, fileName + suffix);
//	      if (!file.exists()) {
//	        String downFilePath = downFileToSD(url, file.getName());
//	        if (downFilePath != null) {
//	          return getBitmapFromSD(file, type, newWidth, newHeight);
//	        }
//	        return null;
//	      }
//
//	      if (type == 0)
//	        bit = cutImg(file, newWidth, newHeight);
//	      else if (type == 1)
//	        bit = scaleImg(file, newWidth, newHeight);
//	      else {
//	        bit = AbImageUtil.originalImg(file);
//	      }
//	    }
//	    catch (Exception e)
//	    {
//	      e.printStackTrace();
//	    }
//	    return bit;
//	  }
	 
	 public static boolean removeAllFileCache()
	  {
	    try
	    {
	      if (!isCanUseSD()) {
	        return false;
	      }

	      File path = Environment.getExternalStorageDirectory();
	      File fileDirectory = new File(path.getAbsolutePath() + downPathImageDir);
	      File[] files = fileDirectory.listFiles();
	      if (files == null) {
	        return true;
	      }
	      for (int i = 0; i < files.length; i++)
	        files[i].delete();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return false;
	    }
	    return true;
	  }
}
