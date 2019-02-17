package com.dpagliotto.gimb.view.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ImageHelper {

    /**
     * @author David Pagliotto
     * @param context
     * Return a URI with a temp image
     */
    public static Uri createTempImageUri(Context context) {
        File storageDir;
        File tempImage;

        Uri photoURI;

        if (Build.VERSION.SDK_INT <= 19) {
            storageDir = new File(Environment.getExternalStorageDirectory(), ".temp");
            if (!storageDir.exists())
                storageDir.mkdirs();

            tempImage = new File(storageDir, "tmp.JPEG");
            if (tempImage.exists()) tempImage.delete();

            photoURI =  Uri.fromFile(tempImage);
        }
        else {
            storageDir = new File(context.getFilesDir(), "tmp_images");
            tempImage = new File(storageDir, "tmp.JPEG");
            if (tempImage.exists()) tempImage.delete();

            String provider = context.getPackageName() + ".fileprovider";
            photoURI = FileProvider.getUriForFile(context,  provider,  tempImage);
        }

        return photoURI;
    }

    /**
     * @author David Pagliotto
     * @param context
     * @return a Bitmap Temp Image
     */
    public static Bitmap getTempImage(Context context) {
        File tempImage;
        File storageDir;

        if (Build.VERSION.SDK_INT <= 19) {
            storageDir = new File(Environment.getExternalStorageDirectory(), ".temp");
        }
        else {
            storageDir = new File(context.getFilesDir(), "tmp_images");
        }

        tempImage = new File(storageDir, "tmp.JPEG");

        Bitmap bitmap = null;
        if (tempImage.exists())
            bitmap = BitmapFactory.decodeFile(tempImage.getAbsolutePath(), new BitmapFactory.Options());

        return bitmap;
    }

    public static Bitmap resizeBitmap(Bitmap imageBitmap) {
        int h = imageBitmap.getHeight();
        int w = imageBitmap.getWidth();

        while (w > 2064) {
            w = (int) (w * 0.5);
            h = (int) (h * 0.5);
        }
        return Bitmap.createScaledBitmap(imageBitmap, w, h, true);
    }

    public static Bitmap compressBitmap(String filename, Bitmap bmpImage) throws Exception {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return bmpImage;
    }

}
