package com.fandy.aplikasibiodata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;
import com.fandy.aplikasibiodata.R;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONArray;

public class BaruActivity extends Activity {
	private static int splashInterval = 3000;
	protected Cursor cursor;
	DataCenter dbHelper;
	Button ton1;
	EditText text1, text2, text3, text4, text5, text6, text7, text8, text9, mSheetId;
	Spinner mCashier;
    ImageView mSheetButton;
    int cashierPos = 0;
	JSONArray cashierNameSet = null;
	JSONArray cashierCodeSet = null;
	JSONArray cashierPhoneSet = null;
	JSONArray cashierSheetSet = null;
	ImageView logo, background, bt_closepengaturan;
	Spinner opsipengiriman;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_baru);

		dbHelper = new DataCenter(this);
		text1 = (EditText) findViewById(R.id.nama_toko);
		text2 = (EditText) findViewById(R.id.slogan_toko);
		text3 = (EditText) findViewById(R.id.alamat_toko);
		text4 = (EditText) findViewById(R.id.prefix);
		opsipengiriman = (Spinner) findViewById(R.id.opsipengiriman);
		text5 = (EditText) findViewById(R.id.isiopsipengiriman);
		text6 = (EditText) findViewById(R.id.namaagen);
		text7 = (EditText) findViewById(R.id.kodecashier);
		text8 = (EditText) findViewById(R.id.telpchasier);
		text9 = (EditText) findViewById(R.id.keterangan);
		mCashier = (Spinner) findViewById(R.id.cashier);
		mSheetId = (EditText) findViewById(R.id.spreadsheet_id);
        mSheetButton = (ImageView) findViewById(R.id.btn_sheet_apply);
		ton1 = (Button) findViewById(R.id.bt_simpan);
		logo = (ImageView) findViewById(R.id.logo);
		background = (ImageView) findViewById(R.id.background);
		bt_closepengaturan = (ImageView) findViewById(R.id.bt_closepengaturan);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ArrayList<String> cashierNameList = new ArrayList<String>();
        final ArrayList<String> cashierSheetList = new ArrayList<String>();

        try {
            cashierNameSet = new JSONArray(prefs.getString("cashierNameSet", "[]"));
            for (int i = 0; i < cashierNameSet.length(); i++){
                cashierNameList.add(cashierNameSet.get(i).toString());
                Log.d("cashierNameSet: ", cashierNameList.toString());
            }
            cashierCodeSet = new JSONArray(prefs.getString("cashierCodeSet", "[]"));
            for (int i = 0; i < cashierCodeSet.length(); i++) {
                Log.d("cashierCodeSet: ", cashierCodeSet.toString());
            }
            cashierPhoneSet = new JSONArray(prefs.getString("cashierPhoneSet", "[]"));
            for (int i = 0; i < cashierPhoneSet.length(); i++) {
                Log.d("cashierPhoneSet: ", cashierPhoneSet.toString());
            }
            cashierSheetSet = new JSONArray(prefs.getString("cashierSheetSet", "[]"));
            for (int i = 0; i < cashierSheetSet.length(); i++){
                cashierSheetList.add(cashierSheetSet.get(i).toString());
                Log.d("cashierSheetSet: ", cashierSheetSet.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cashierNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCashier.setAdapter(adapter);

		ton1.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));

		Bitmap bmp = BitmapFactory.decodeFile("/sdcard/Pictures/logo.png"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		logo.setImageBitmap(bmp); 
		Bitmap bmp2 = BitmapFactory.decodeFile("/sdcard/Pictures/background.jpg"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		background.setImageBitmap(bmp2); 
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		cursor = db.rawQuery("SELECT * FROM biodata",null);
		cursor.moveToFirst();
		if (cursor.getCount()<1)
		{
			db.execSQL("insert into biodata(kode, nama, phone, jk, keterangan) values('','','','','')");
			Toast.makeText(getApplicationContext(), "Berhasil tersimpan", Toast.LENGTH_LONG).show();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Intent i = new Intent(BaruActivity.this, TambahActivity.class);
					startActivity(i);

					// jeda selesai Splashscreen
					this.finish();
				}

				private void finish() {
					// TODO Auto-generated method stub
				}
			}, splashInterval);
		} else
		{
			cursor.moveToPosition(0);
			text1.setText(cursor.getString(1).toString());
			text2.setText(cursor.getString(2).toString());
			text3.setText(cursor.getString(3).toString());
			text4.setText(cursor.getString(4).toString());
			text9.setText(cursor.getString(5).toString());
		}
		
		/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities);
		opsipengiriman.setAdapter(adapter);
		if (text5.getText().toString().equals("Akun Bank"))
		{
			opsipengiriman.setSelection(0);
		} else
			if (text5.getText().toString().equals("Pos Indonesia"))
			{
				opsipengiriman.setSelection(1);
			}
		else {opsipengiriman.setSelection(2);}
		opsipengiriman.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int sid = opsipengiriman.getSelectedItemPosition();
				text5.setText(cities[sid]);
			}

			public void onNothingSelected(AdapterView<?> parent) {
			}
		});*/

		// daftarkan even onClick pada btnSimpan
		ton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if ((text1.getText().toString().equals("")) || (text2.getText().toString().equals("") || (text3.getText().toString().equals("")) || (text4.getText().toString().equals(""))))
				{
					Toast.makeText(getApplicationContext(), "Isi semua inputan dengan benar", Toast.LENGTH_LONG).show();
				} else 
				{
					SQLiteDatabase db = dbHelper.getWritableDatabase();
					cursor = db.rawQuery("SELECT * FROM biodata",null);
					cursor.moveToFirst();
					if (cursor.getCount()<1)
					{
						db.execSQL("insert into biodata(kode, nama, phone, jk, keterangan) values('" +
								text1.getText().toString()+"','"+
								text2.getText().toString() +"','" +
								text3.getText().toString()+"','"+
								text4.getText().toString() +"','" +
								/*text5.getText().toString() +"','" +
								text6.getText().toString() +"','" +
								text7.getText().toString() +"','" +
								text8.getText().toString() +"','" +*/
								text9.getText().toString() + "'"+
										 ")");
						Toast.makeText(getApplicationContext(), "Berhasil tersimpan", Toast.LENGTH_LONG).show();
						home();
					} else 
					{
						db.execSQL("update biodata set kode='"+
								text1.getText().toString() +"',nama='"+
								text2.getText().toString() +"', phone='" +
								text3.getText().toString()+"', jk='"+
								text4.getText().toString() +"', keterangan='" +
								text9.getText().toString() + "'");
						Toast.makeText(getApplicationContext(), "Berhasil tersimpan", Toast.LENGTH_LONG).show();
						home();
					}

					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(BaruActivity.this);
					SharedPreferences.Editor editor = prefs.edit();
                    cashierSheetSet = new JSONArray(cashierSheetList);
					editor.putString("cashierSheetSet", cashierSheetSet.toString());
					editor.commit();
				}}
		});

        mCashier.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSheetId.setText(cashierSheetList.get(i));
                cashierPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSheetId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mSheetButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cashierPos >= 0) {
                    cashierSheetList.set(cashierPos, mSheetId.getText().toString());
                    mSheetButton.setVisibility(View.GONE);
                }
            }
        });

		bt_closepengaturan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				bt_closepengaturan();
			}
		});
	}

	public void bt_closepengaturan() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Anda yakin tidak menyimpan pengaturan?");
		alertDialog.setButton("Iya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				home();
				return;
			}
		});
		alertDialog.setButton2("Tidak", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				return;
			}
		});
		alertDialog.show();
	}

	public void onBackPressed() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Anda yakin tidak menyimpan pengaturan?");
		alertDialog.setButton("Iya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				home();
				return;
			}
		});
		alertDialog.setButton2("Tidak", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				return;
			}
		});
		alertDialog.show();
	}

	public void home() {
		Intent i = new Intent(this, TambahActivity.class);
		startActivity(i);
	}
}
