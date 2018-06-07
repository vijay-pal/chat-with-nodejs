package com.example.vijay.clientchat.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by vijay on 1/9/17.
 */

public class Utils {
  public static String encodeImage(Bitmap bm) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] b = baos.toByteArray();
    String encImage = Base64.encodeToString(b, Base64.DEFAULT);
    //Base64.de
    return encImage;
  }

  public static Bitmap decodeBase64(String encodedImage) {
    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
  }

  public static String getFileMineType(Context context, Uri uri) {
    ContentResolver cR = context.getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
    return cR.getType(uri);
  }

  public static Bitmap scaleDownBitmap(@NonNull Bitmap bitmap, int maxWidth) {
    int bWidth = bitmap.getWidth();
    int bHeight = bitmap.getHeight();

    if (maxWidth < bWidth) {
      Matrix matrix = new Matrix();
      float scaleW = (float) maxWidth / bWidth;
      float scaleH = ((float) maxWidth * bHeight / bWidth) / bHeight;
      matrix.postScale(scaleW, scaleH);
      Bitmap resizedBitmap = Bitmap.createBitmap(
        bitmap, 0, 0, bWidth, bHeight, matrix, false);
      bitmap.recycle();
      return resizedBitmap;
    }
    return bitmap;
  }
}
