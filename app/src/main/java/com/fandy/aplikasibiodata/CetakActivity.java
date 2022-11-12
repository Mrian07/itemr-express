package com.fandy.aplikasibiodata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner.Result;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;

import com.fandy.aplikasibiodata.R;

public class CetakActivity extends Activity  {
	NumberFormat rupiahFormat;  
	TextView a, b, c, d, e, f, g, h, ii, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, nomerresi, tanggal,tgl, nama, slogan, alamat, namapengirim, teksnamapengirim, teksnamapenerima, teksbank, tekrekening,
			namachasier,namachasierr, kodecashier,
			telpchasier,
			hp,
			koderesi,
			opsipengiriman,
			namapenerima,
			bank,
			norekening,
			jumlah,
			catatan,
			keterangan,
			eresijudul,
			eresitelp,
			eketerangan1;
	DataCenter dbHelper;
	Cursor cursor;
	Button bt_cetak;
	RelativeLayout imgv_showscreenshot, relativelayout;
	int i = 0;
	ImageView bt_closecetak, logo;
	Bundle dataIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cetakresi);
		nama = (TextView) findViewById(R.id.nama);
		slogan = (TextView) findViewById(R.id.slogan);
		alamat = (TextView) findViewById(R.id.alamat);
		eresijudul = (TextView) findViewById(R.id.eresijudul);
		eresitelp = (TextView) findViewById(R.id.eresitelp);
		namapengirim = (TextView) findViewById(R.id.namapengirim);
		teksnamapengirim = (TextView) findViewById(R.id.teksnamapengirim);
		teksnamapenerima = (TextView) findViewById(R.id.teksnamapenerima);
		teksbank = (TextView) findViewById(R.id.teksbank);
		tekrekening = (TextView) findViewById(R.id.tekrekening);
		namachasier = (TextView) findViewById(R.id.namachasier);
		namachasierr = (TextView) findViewById(R.id.namachasierr);
		kodecashier = (TextView) findViewById(R.id.kodechasier);
		telpchasier = (TextView) findViewById(R.id.telpchasier);
		hp = (TextView) findViewById(R.id.hp);
		koderesi = (TextView) findViewById(R.id.koderesi);
		opsipengiriman = (TextView) findViewById(R.id.jenispengiriman);
		namapenerima = (TextView) findViewById(R.id.namapeerima);
		bank = (TextView) findViewById(R.id.bank);
		norekening = (TextView) findViewById(R.id.norekening);
		jumlah = (TextView) findViewById(R.id.jumlah);
		catatan = (TextView) findViewById(R.id.catatan);
		keterangan = (TextView) findViewById(R.id.keterangan);
		tanggal = (TextView) findViewById(R.id.tanggal);
//		tgl = (TextView) findViewById(R.id.tgl);
		nomerresi = (TextView) findViewById(R.id.nomerresi);

		bt_cetak = (Button) findViewById(R.id.bt_cetak);
		bt_closecetak = (ImageView) findViewById(R.id.bt_closecetak);
		logo = (ImageView) findViewById(R.id.logo);
		relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
		a = (TextView) findViewById(R.id.a);
		b = (TextView) findViewById(R.id.b);
		c = (TextView) findViewById(R.id.c);
		d = (TextView) findViewById(R.id.d);
		e = (TextView) findViewById(R.id.e);
		f = (TextView) findViewById(R.id.f);
		g = (TextView) findViewById(R.id.g);
		h = (TextView) findViewById(R.id.h);
		ii = (TextView) findViewById(R.id.ii);
		j = (TextView) findViewById(R.id.j);
		k = (TextView) findViewById(R.id.k);
		l = (TextView) findViewById(R.id.l);
		m = (TextView) findViewById(R.id.m);

		dataIntent = getIntent().getExtras();

//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 1200);
//		relativelayout.setLayoutParams(layoutParams);

		bt_cetak.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));
		slogan.setTypeface(Typeface.createFromAsset(getAssets(), "sambung.ttf"));
		nama.setTypeface(Typeface.createFromAsset(getAssets(), "itmxfont.ttf"));
		alamat.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		namachasier.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		namachasierr.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		kodecashier.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		telpchasier.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		hp.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		tanggal.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
//		tgl.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		koderesi.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		opsipengiriman.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		eresijudul.setTypeface(Typeface.createFromAsset(getAssets(), "unikangka.otf"));
		eresitelp.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		namapengirim.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		namapenerima.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		bank.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		norekening.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		catatan.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		jumlah.setTypeface(Typeface.createFromAsset(getAssets(), "unikangka.otf"));
		keterangan.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		a.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		b.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		c.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		d.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		e.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		f.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		g.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		h.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		ii.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		j.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		k.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		l.setTypeface(Typeface.createFromAsset(getAssets(), "MyriadPro-Bold.otf"));
		m.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		teksnamapenerima.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		teksnamapengirim.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		teksbank.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));
		tekrekening.setTypeface(Typeface.createFromAsset(getAssets(), "campus.ttf"));

		
		dbHelper = new DataCenter(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		cursor = db.rawQuery("SELECT * FROM biodata",null);
		cursor.moveToFirst();
		cursor.moveToPosition(0);
		nama.setText(cursor.getString(1).toString());
		slogan.setText(cursor.getString(2).toString());
		alamat.setText(cursor.getString(3).toString());
		namachasier.setText(dataIntent.getString("selectedCashierName"));
		namachasierr.setText(dataIntent.getString("selectedCashierName"));
		kodecashier.setText(dataIntent.getString("selectedCashierCode"));
		telpchasier.setText(dataIntent.getString("selectedCashierPhone"));
		hp.setText(dataIntent.getString("selectedCashierPhonee"));
		
		opsipengiriman.setText(dataIntent.getString("selectedJenisPengiriman"));

		if(opsipengiriman.getText().toString().equals("Akun Bank")){
			teksbank.setText("BANK");
			tekrekening.setText("NO. REK.");

		}else if(opsipengiriman.getText().toString().equals("Pos Indonesia")){
			teksbank.setText("NTP");
			tekrekening.setText("PIN");
		}else if(opsipengiriman.getText().toString().equals("Tunai")){
			teksbank.setText("Nomor");
			tekrekening.setText("Serial");
		}

		keterangan.setText(cursor.getString(5).toString());
		namapenerima.setText(dataIntent.getString("penerima").toUpperCase());
		namapengirim.setText(dataIntent.getString("pengirim").toUpperCase());
		bank.setText(dataIntent.getString("bank").toUpperCase());
		norekening.setText(dataIntent.getString("no_rek").toString());
		catatan.setText(dataIntent.getString("keterangan").toString());

		 if (cursor.getString(0).toString().length()==1)
		 {
			 koderesi.setText(cursor.getString(4).toString()+"000"+cursor.getString(0).toString());
		 } else 
			 if (cursor.getString(0).toString().length()==2)
			 {
				 koderesi.setText(cursor.getString(4).toString()+"00"+cursor.getString(0).toString());
			 } else 
				 if (cursor.getString(0).toString().length()==3)
				 {
					 koderesi.setText(cursor.getString(4).toString()+"0"+cursor.getString(0).toString());
				 }
		 
		String a = dataIntent.getString("jumlah").toString();
		rupiahFormat = NumberFormat.getInstance(Locale.GERMANY);           
		String rupiah = rupiahFormat.format(Double.parseDouble(a));              
		jumlah.setText(rupiah);

		Bitmap bmp = BitmapFactory.decodeFile("/sdcard/Pictures/logo.jpg"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		logo.setImageBitmap(bmp);

		Bitmap bmp2 = BitmapFactory.decodeFile("/sdcard/Pictures/background.jpg"); //mendeklarasikan variabel bmp bertype Bitmap guna menyimpan gambar
		Drawable drawable = new BitmapDrawable(bmp2);
		
		File sdCard = Environment.getExternalStorageDirectory();
		File directory = new File(sdCard.getAbsolutePath()+ "/Pictures");
		File yourFile = new File(directory, "background.jpg");

		if (!yourFile.exists())
		{
			relativelayout.setBackgroundColor(Color.WHITE);
		} else relativelayout.setBackgroundDrawable(drawable);

		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Calendar cal = Calendar.getInstance();

//		SimpleDateFormat tgll = new SimpleDateFormat("dd MMM yyyy HH:mm");
//
//		String rus = tgll.format(cal.getTime());
//		rus = rus.trim();
//		rus = rus.replaceAll("May", "Mei");
//		rus = rus.replaceAll("Jun", "Juni");
//		rus = rus.replaceAll("Jul", "Juli");
//		rus = rus.replaceAll("Aug", "Agust");
//		rus = rus.replaceAll("Sep", "Sept");
//		rus = rus.replaceAll("Oct", "Okt");
//		rus = rus.replaceAll("Dec", "Des");
//		tgl.setText(rus);

		String res = dateFormat.format(cal.getTime());
		res = res.trim();
		res = res.replaceAll("May", "Mei");
		res = res.replaceAll("Jun", "Juni");
		res = res.replaceAll("Jul", "Juli");
		res = res.replaceAll("Aug", "Agust");
		res = res.replaceAll("Sep", "Sept");
		res = res.replaceAll("Oct", "Okt");
		res = res.replaceAll("Dec", "Des");
		tanggal.setText(res);


		bt_closecetak.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				bt_closecetak();
			}
		});

		bt_cetak.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				View view = findViewById(R.id.relativelayout);
				view.setDrawingCacheEnabled(true);
				Bitmap bitmap = view.getDrawingCache();
				BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
				imgv_showscreenshot = (RelativeLayout) findViewById(R.id.relativelayout);
				// set screenshot bitmapdrawable to imageview
				imgv_showscreenshot.setBackgroundDrawable(bitmapDrawable);

				if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
					// we check if external storage is available, otherwise
					// display an error message to the user using Toast Message
					File sdCard = Environment.getExternalStorageDirectory();
					File directory = new File(sdCard.getAbsolutePath()+ "/resi2016");
					directory.mkdirs();

					String filename = "Resi" + i + ".jpg";
					File yourFile = new File(directory, filename);

					while (yourFile.exists()) {
						i++;
						filename = "Resi" + i + ".jpg";
						yourFile = new File(directory, filename);
					}

					if (!yourFile.exists()) {
						if (directory.canWrite()) {
							try {
								FileOutputStream out = new FileOutputStream(yourFile, true);
								bitmap.compress(Bitmap.CompressFormat.PNG, 90,out);
								out.flush();
								out.close();

								int nomer = Integer.parseInt(cursor.getString(0).toString());
								nomer=nomer+1;
								SQLiteDatabase db = dbHelper.getWritableDatabase();
								db.execSQL("update biodata set nomer='"+nomer + "'");
								//////////////////////
								Intent share = new Intent(Intent.ACTION_SEND);

								// If you want to share a png image only, you can do:
								// setType("image/png"); OR for jpeg: setType("image/jpeg");
								share.setType("image/jpeg*");

								// Make sure you put example png image named myImage.png in your
								// directory
								String imagePath = Environment.getExternalStorageDirectory()+ "/resi2016/Resi"+i+".jpg";

								File imageFileToShare = new File(imagePath);

								Uri uri = Uri.fromFile(imageFileToShare);
								share.putExtra(Intent.EXTRA_STREAM, uri);

								startActivity(Intent.createChooser(share, "Share Image"));
								//////////////////////
								i++;
							} catch (IOException e) {
								e.printStackTrace();
							}

						}
					}

				} else {
					Toast.makeText(CetakActivity.this,
							"Sorry SD Card not available in your Device!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}



	public void Home() {
		Intent i = new Intent(this, TambahActivity.class);
		startActivity(i);
	}

	public void bt_closecetak() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Anda yakin tidak mencetak resi?");
		alertDialog.setButton("Iya", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Home();
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

}

