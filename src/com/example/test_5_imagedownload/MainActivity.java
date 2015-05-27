package com.example.test_5_imagedownload;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

//	ImageView imageView1, imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9,imageView10,imageView11,imageView12,imageView13;
	ImageDownLoader downLoader;
	Button button;
	ListView listView;
	ImageListAdapter adapter;
	List<String> urlList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		urlList = new ArrayList<String>();
		urlList.add("http://img3.3lian.com/2014/c2/61/11.jpg");
		urlList.add("http://img3.3lian.com/2014/c2/61/d/2.jpg");
		urlList.add("http://www.feizl.com/upload2007/2013_02/1302271412716514.jpg");
		urlList.add("http://img.dapixie.com/uploads/allimg/120105/1-120105111405.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-06-14/103732758.jpg");
		urlList.add("http://cdnweb.b5m.com/web/cmsphp/article/201504/33457ab33fa65d5d5d98495b5bba8437.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201409/02-173237_949.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201308/22/10/36/521579394f4bb419.jpg!200x200.jpg");
//		urlList.add("http://p2.gexing.com/G1/M00/2D/0A/rBACE1H3zIrD9n_NAAAbUhIXTY8637_200x200_3.jpg");
		urlList.add("http://hdn.xnimg.cn/photos/hdn421/20130222/2040/h_main_KJ78_2fdd00000450111a.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://v1.qzone.cc/avatar/201404/13/11/12/534a00b62633e072.jpg!200x200.jpg");
		urlList.add("http://www.qqzhi.com/uploadpic/2014-09-01/232535816.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		urlList.add("http://www.ttoou.com/qqtouxiang/allimg/120328/co12032PR929-3-lp.jpg");
		urlList.add("http://www.qqbody.com/uploads/allimg/201407/29-144410_871.jpg");
		
		
		initView();
		
		button.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("ShowToast") @Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				if(MyUtil.removeAllFileCache()){
					Toast.makeText(MainActivity.this, "清除成功！", 3000).show();
				}
				else {
					Toast.makeText(MainActivity.this, "清除失败！", 3000).show();
				}
				
			}
		});
//		downLoader = new ImageDownLoader();
//		downLoader.setType(ImageDownLoader.TYPE_SCALEING);
//		downLoader.setBitmapHeight(200);
//		downLoader.setBitmapWidth(200);

//		
//		downLoader.downloadAndSet(imageView1, "http://z.k1982.com/png/up/200712/20071206123827696.png");
//		downLoader.downloadAndSet(imageView2, "http://img.xiaba.cvimage.cn/4cc026d4205be6577e740200.jpg");
//		downLoader.downloadAndSet(imageView3, "http://z.k1982.com/png/up/200712/20071206123827696.png");
//		downLoader.downloadAndSet(imageView4, "http://img.xiaba.cvimage.cn/4ec75b66e568837361003418.jpg");
//		downLoader.downloadAndSet(imageView5, "http://www.sc115.com/uploads/png/130406/filetype-png-icon.png");
//		downLoader.downloadAndSet(imageView6, "http://img.xiaba.cvimage.cn/4d7efe10d1869ff8434e0000.jpg");
//		downLoader.downloadAndSet(imageView7, "http://img.xiaba.cvimage.cn/4ec75b66e568837361003418.jpg");
//		downLoader.downloadAndSet(imageView8, "http://www.sc115.com/uploads/png/130406/filetype-png-icon.png");
//		downLoader.downloadAndSet(imageView9, "http://pic1.ooopic.com/uploadfilepic/sheji/2008-10-30/OOOPIC_zp85297824_20081030998842b75716c62b.jpg");
//		downLoader.downloadAndSet(imageView10, "http://img.xiaba.cvimage.cn/4cc02710237797547e980200.jpg");
//		downLoader.downloadAndSet(imageView11, "http://www.icosky.com/icon/png/System/QuickPix%202007/Shamrock.png");
//		downLoader.downloadAndSet(imageView12, "http://www.sc115.com/uploads/png/130406/filetype-png-icon.png");
//		downLoader.downloadAndSet(imageView13, "http://www.icosky.com/icon/png/System/QuickPix%202007/Shamrock.png");
		
	}
	
	public void initView(){
		button = (Button) findViewById(R.id.button1);
		listView = (ListView) findViewById(R.id.listView1);
		adapter = new ImageListAdapter(this, urlList);
		listView.setAdapter(adapter);
//		button = (Button) findViewById(R.id.button1);
//		imageView1 = (ImageView) findViewById(R.id.imageView1);
//		imageView2 = (ImageView) findViewById(R.id.imageView2);
//		imageView3 = (ImageView) findViewById(R.id.imageView3);
//		imageView4 = (ImageView) findViewById(R.id.imageView4);
//		imageView5 = (ImageView) findViewById(R.id.imageView5);
//		imageView6 = (ImageView) findViewById(R.id.imageView6);
//		imageView7 = (ImageView) findViewById(R.id.imageView7);
//		imageView8 = (ImageView) findViewById(R.id.imageView8);
//		imageView9 = (ImageView) findViewById(R.id.imageView9);
//		imageView10 = (ImageView) findViewById(R.id.imageView10);
//		imageView11 = (ImageView) findViewById(R.id.imageView11);
//		imageView12 = (ImageView) findViewById(R.id.imageView12);
//		imageView13 = (ImageView) findViewById(R.id.imageView13);
		
	}


}
