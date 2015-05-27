package com.example.test_5_imagedownload;

import java.lang.ref.WeakReference;
import java.util.List;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter{
	
	private static int num = 0;
	private LayoutInflater inflater;
	private ImageDownLoader downLoader;
	//Í¼Æ¬ÏÂÔØÆ÷
    private AbImageDownloader mAbImageDownloader = null;
	private List<String> urlList;
	public ImageListAdapter(Context mContext, List<String> list){
		urlList = list;
		inflater = LayoutInflater.from(mContext);
		downLoader = new ImageDownLoader();
		
		//Í¼Æ¬ÏÂÔØÆ÷
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setWidth(100);
        mAbImageDownloader.setHeight(100);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return urlList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return urlList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = new ViewHolder();
		if(arg1 == null){
			arg1 = inflater.inflate(R.layout.list_item, arg2, false);
			holder.imageView = (ImageView) arg1.findViewById(R.id.testImage);
			holder.textView = (TextView) arg1.findViewById(R.id.testText);
			arg1.setTag(holder);
		}
		else{
			holder = (ViewHolder) arg1.getTag();
		}
		num++;
		
		holder.imageView.setImageResource(R.drawable.image_no);
//		holder.imageView.setTag(urlList.get(arg0));
		
		downLoader.downloadAndSet(holder.imageView, urlList.get(arg0));
//		notifyDataSetChanged();
//		mAbImageDownloader.display(holder.imageView, urlList.get(arg0));
		holder.textView.setText("hello"+num);
		
		
		
		return arg1;
	}
	
	private class ViewHolder{
		ImageView imageView;
		TextView textView;
	}



}
