package com.rngrp;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.R;
import android.widget.TextView;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Arguments;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.StringBuilder;
import android.util.Base64;

public class GRP extends ReactContextBaseJavaModule {

  public GRP(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "GRP";
  }

  private WritableMap makeErrorPayload(Exception ex) {
    WritableMap error = Arguments.createMap();
    error.putString("message", ex.getMessage());
    return error;
  }

  @ReactMethod
  public void getContentFromURI(String uri, Callback callback) {
    try {
      Context context = getReactApplicationContext();
      Uri u;
      u = Uri.parse(uri);
      InputStream in = context.getContentResolver().openInputStream(Uri.parse(uri));

      byte[] newData = new byte[in.available()];
      in.read(newData);
      callback.invoke(null, Base64.encodeToString(newData, Base64.DEFAULT));

    } catch (Exception ex) {
      ex.printStackTrace();
      callback.invoke(makeErrorPayload(ex));
    }
  }

  @ReactMethod
  public void getRealPathFromURI(String uri, Callback callback) {
    try {
      Context context = getReactApplicationContext();
      String [] proj = {MediaStore.Images.Media.DATA};
      Cursor cursor = context.getContentResolver().query(Uri.parse(uri), proj,  null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      String path = cursor.getString(column_index);
      cursor.close();

      callback.invoke(null, path);
    } catch (Exception ex) {
      ex.printStackTrace();
      callback.invoke(makeErrorPayload(ex));
    }
  }

}
