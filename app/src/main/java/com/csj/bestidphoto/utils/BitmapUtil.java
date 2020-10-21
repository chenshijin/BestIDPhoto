package com.csj.bestidphoto.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.csj.bestidphoto.MApp;
import com.maoti.lib.utils.LogUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    /**
     * 保存bitmap到SD卡
     *
     * @param filename
     * @param mBitmap
     */
    public static int saveMyBitmap(String path, String filename, Bitmap mBitmap, int quality, boolean addToUri) {//0保存成功
        int result = 1;
        if (mBitmap == null) {
            return result;
        }
        LogUtil.i("BitmapUtil", "BitmapUtil path=" + path + filename);
        FileUtil.makeRootDirectory(path);
        File f = new File(path + filename);
        try {
            if (f.exists()) {
                f.delete();
            }

            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		/*ByteArrayOutputStream stream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
		byte[] byteArray = stream.toByteArray();


		FileOutputStream fstream;
		try {
			fstream = new FileOutputStream(f);
			BufferedOutputStream bStream = new BufferedOutputStream(fstream);
			bStream.write(byteArray);
			if (bStream != null) {
				bStream.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}*/

        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }
//        mBitmap.compress(android.os.Build.VERSION.SDK_INT >= 19?Bitmap.CompressFormat.WEBP:Bitmap.CompressFormat.JPEG, quality, fOut);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);//需要JPEG, PNG不能显示到相册
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (!mBitmap.isRecycled()) {
//            mBitmap.recycle();
//            mBitmap = null;
//        }
        if (addToUri) {
            // 把文件插入到系统图库  （注: 不能同时和addImgToUri()方法调用,不然会在相册看到两张相同的）
//            try {
//                MediaStore.Images.Media.insertImage(MApp.getInstance().getContentResolver(), f.getAbsolutePath(), filename, null);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }

//            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            Uri uri = Uri.fromFile(f);
//            intent.setData(uri);
//            MApp.getInstance().sendBroadcast(intent);

            addImgToUri(f);
        }

        result = 0;
        return result;
    }

    /**
     * 根据文件路径添加文件到Uri，获取授权（本地SD卡图片要显示到相册，就要添加到Uri）
     * @param path
     */
    public static void addImgToUri(String path){
        File f = new File(path);

//        Uri uri =  FileProvider.getUriForFile(MApp.getInstance(),MApp.getInstance().packageName + ".fileprovider",f);
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        intent.setData(uri);
//        MApp.getInstance().sendBroadcast(intent);

        //发送广播通知系统图库刷新数据
        System.out.println("发送广播通知系统图库刷新数据");


        MApp.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
    }

    /**
     * 添加文件到Uri，获取授权（本地SD卡图片要显示到相册，就要添加到Uri）
     * @param f
     */
    public static void addImgToUri(File f){
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri =  FileProvider.getUriForFile(MApp.getInstance(),MApp.getInstance().packageName + ".fileprovider",f);
//        intent.setData(uri);
//        MApp.getInstance().sendBroadcast(intent);

        MApp.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
    }
}
