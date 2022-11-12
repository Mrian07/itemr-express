package com.fandy.aplikasibiodata;

import com.fandy.aplikasibiodata.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UbahActivity extends Activity {
	protected Cursor cursor;
	DataCenter dbHelper;
	Button ton1;
	EditText text1, text2, text3, text4, text5;
	ImageView logo, background;
	TextView database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubah);

		dbHelper = new DataCenter(this);
		database = (TextView) findViewById(R.id.database);
		text1 = (EditText) findViewById(R.id.nama_toko);
		text2 = (EditText) findViewById(R.id.slogan_toko);
		text3 = (EditText) findViewById(R.id.alamat_toko);
		text4 = (EditText) findViewById(R.id.prefix);
		text5 = (EditText) findViewById(R.id.opsipengiriman);
		ton1 = (Button) findViewById(R.id.bt_simpan);
		logo = (ImageView) findViewById(R.id.logo);
		background = (ImageView) findViewById(R.id.background);

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("insert into biodata(kode, nama, phone, jk, alamat) values('" +
				text1.getText().toString()+"')");
		Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();

		/*Bitmap bmp = BitmapFactory.decodeFile("/Pictures/logo.png"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		logo.setImageBitmap(bmp); 
		Bitmap bmp2 = BitmapFactory.decodeFile("/Pictures/background.png"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		background.setImageBitmap(bmp2); 

		cursor = db.rawQuery("SELECT * FROM biodata",null);
		cursor.moveToFirst();
		if (cursor.getCount()>0)
		{
			cursor.moveToPosition(0);
			database.setText(cursor.getString(0).toString());
			text1.setText(cursor.getString(0).toString());
			text2.setText(cursor.getString(1).toString());
			text3.setText(cursor.getString(2).toString());
			text4.setText(cursor.getString(3).toString());
			text5.setText(cursor.getString(4).toString());
		}

		// daftarkan even onClick pada btnSimpan
		ton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//SQLiteDatabase db = dbHelper.getWritableDatabase();
				/*cursor = db.rawQuery("SELECT * FROM biodata",null);
				cursor.moveToFirst();
				if (cursor.getCount()>0)
				{
					db.execSQL("update biodata set nama='"+
							text2.getText().toString() +"', phone='" +
							text3.getText().toString()+"', jk='"+
							text4.getText().toString() +"', alamat='" +
							text5.getText().toString() + "' where nama='" +
							database.getText().toString()+"'");
					Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
				} else 
				{
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				db.execSQL("insert into biodata(kode, nama, phone, jk, alamat) values('" +
						text1.getText().toString()+"','"+
						text2.getText().toString() +"','" +
						text3.getText().toString()+"','"+
						text4.getText().toString() +"','" +
						text5.getText().toString() + "')");
				Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_LONG).show();
				//}
			}
		});*/
	}
}