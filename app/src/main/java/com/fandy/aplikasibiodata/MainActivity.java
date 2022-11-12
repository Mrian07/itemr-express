package com.fandy.aplikasibiodata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.fandy.aplikasibiodata.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.ImageView;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);

		// Create an actionbar
		ActionBar actionBar = getActionBar();
		actionBar.show();

		// Locate ImageView in activity_main.xml
		ImageView myimage = (ImageView) findViewById(R.id.image);

		// Attach image into ImageView
		myimage.setImageResource(R.drawable.ic_logo);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Create an actionbar menu
		menu.add("Share Image")
				// Add a new Menu Button
				.setOnMenuItemClickListener(this.ShareImageClickListener)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return super.onCreateOptionsMenu(menu);
	}

	// Capture actionbar menu item click
	OnMenuItemClickListener ShareImageClickListener = new OnMenuItemClickListener() {

		public boolean onMenuItemClick(MenuItem item) {

			Bitmap bitmap;
			OutputStream output;
			
			// Retrieve the image from the res folder
			bitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.ic_logo);

			// Find the SD Card path
			File filepath = Environment.getExternalStorageDirectory();

			// Create a new folder AndroidBegin in SD Card
			File dir = new File(filepath.getAbsolutePath() + "/Share Image Tutorial/");
			dir.mkdirs();

			// Create a name for the saved image
			File file = new File(dir, "sample_wallpaper.png");

			try {

				// Share Intent
				Intent share = new Intent(Intent.ACTION_SEND);

				// Type of file to share
				share.setType("image/jpeg");

				output = new FileOutputStream(file);

				// Compress into png format image from 0% - 100%
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
				output.flush();
				output.close();

				// Locate the image to Share
				Uri uri = Uri.fromFile(file);

				// Pass the image into an Intnet
				share.putExtra(Intent.EXTRA_STREAM, uri);

				// Show the social share chooser list
				startActivity(Intent.createChooser(share, "Share Image Tutorial"));
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;
		}
	};
}
