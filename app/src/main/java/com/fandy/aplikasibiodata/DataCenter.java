package com.fandy.aplikasibiodata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataCenter extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "crud.db";
	private static final int DATABASE_VERSION = 1;
	public DataCenter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table biodata(nomer integer primary key, kode text null, nama text null, phone text null, jk text null, keterangan text null);";
		Log.d("Data", "onCreate: " + sql);
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {	
	}
}