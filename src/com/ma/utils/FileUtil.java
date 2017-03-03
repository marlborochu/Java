package com.ma.utils;
/**
 * @author marlboro.chu@gmail.com
 **/
import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class FileUtil {

	public void downloadMP4(String url, String toFile) {

		try {
			InputStream is = new URL(url).openConnection().getInputStream();
			FileOutputStream fos = new FileOutputStream(toFile);
			try{
			byte[] buffer = new byte[1024];
			for (int length; (length = is.read(buffer)) > 0; fos.write(buffer,
					0, length))
				;
			}finally{
				fos.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
//		new FileUtil().downloadMP4("http://st108.u1.videomega.tv/v/58b13fd38d8c2c608070e5c330d5f7c3.mp4?st=VL_jtgQIATU-9slYscL_8w", "D:\\Movies\\黑魔女.mp4");
		final ArrayList movieList = new ArrayList();
//		{
//			String[] movie = {"http://st112.u1.videomega.tv/v/8f40a03d592424d44995ae4cc6cda82a.mp4?st=kOtnaa4XDNbkt6OKx5ocwA","D:\\Movies\\變形金剛4.mp4"};
//			movieList.add(movie);
//		}
//		{
//			String[] movie = {"http://vid2.ec.dmcdn.net/video/968/329/119923869_mp4_h264_aac_hd.mp4", "D:\\Movies\\魔警_1.mp4"};
//			movieList.add(movie);
//		}
//		{
//			String[] movie = {"http://vid2.ec.dmcdn.net/video/933/729/119927339_mp4_h264_aac_hd.mp4", "D:\\Movies\\魔警_2.mp4"};
//			movieList.add(movie);
//		}
		// 
//		{
//			String[] movie = {"http://vlog.xuite.net/play/UmpFajlaLTE5ODU3NTIyLmZsdg==/%E9%BB%91%E9%AD%94%E5%A5%B3%E6%B2%89%E7%9D%A1%E9%AD%94%E5%92%92201405", "D:\\Movies\\aa.mp4"};
//			movieList.add(movie);
//		}
		//
		{
			String[] movie = {"http://5.mms.vlog.xuite.net/stream/qw6688/a3czU3hsLTIwMzA0NDc3LmZsdg==?k=78231ea1b0454a84db0c707db83feb15&q=360&start=0", "D:\\Movies\\冒牌條子.mp4"};
			movieList.add(movie);
		}
		{
			String[] movie = {"http://f.mms.vlog.xuite.net/stream/qw6688/NFVIVXUzLTIwNDg4MjQ4LmZsdg==?k=cca106cf68df308d7c7149b345caf44f&q=360&start=0","德古拉.mp4"};
			movieList.add(movie);
		}
		Thread[] ts = new Thread[3];
		for(int i = 0;i<ts.length;i++){
			ts[i] = new Thread(){
				public void run(){
					while(true){
						String[] movie = null;
						synchronized(movieList){
							if(!movieList.isEmpty()){
								movie = (String[])movieList.remove(0);
							}else{
								break;
							}
						}
						if(movie != null){
							System.out.println("begin download ["+movie[1]+"]");
							new FileUtil().downloadMP4(movie[0],movie[1]);
						}
					}	
				}
			};
		}
		for(int i = 0;i<ts.length;i++){
			ts[i].start();
		}
		for(int i = 0;i<ts.length;i++){
			try {
				ts[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
