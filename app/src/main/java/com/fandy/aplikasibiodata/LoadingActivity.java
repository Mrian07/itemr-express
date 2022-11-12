package com.fandy.aplikasibiodata;

import java.util.ArrayList;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import com.fandy.aplikasibiodata.R;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingActivity extends Activity {
	private static int splashInterval = 3000;
	protected Cursor cursor;
	DataCenter dbHelper;
	ImageView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loading_layout);

		dbHelper = new DataCenter(this);
		
		logo = (ImageView) findViewById(R.id.logo);
		Bitmap bmp = BitmapFactory.decodeFile("/sdcard/Pictures/logo.png"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		logo.setImageBitmap(bmp); 
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		cursor = db.rawQuery("SELECT * FROM biodata",null);
		cursor.moveToFirst();
		if (cursor.getCount()<1)
		{
			db.execSQL("insert into biodata(kode, nama, phone, jk, keterangan) values('','','','','')");
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(LoadingActivity.this, TambahActivity.class);
				startActivity(i);

				// jeda selesai Splashscreen
				this.finish();
			}

			private void finish() {
				// TODO Auto-generated method stub
			}
		}, splashInterval);

	}
}
