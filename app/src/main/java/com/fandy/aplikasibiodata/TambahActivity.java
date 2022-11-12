package com.fandy.aplikasibiodata;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.client.util.ObjectParser;
import com.google.api.services.sheets.v4.SheetsScopes;

import com.google.api.services.sheets.v4.model.*;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.fandy.aplikasibiodata.R;

import org.json.JSONArray;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class TambahActivity extends Activity implements EasyPermissions.PermissionCallbacks {

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};
    ProgressDialog mProgress;

    GoogleAccountCredential mCredential;
    TableRow layoutPengirim, layoutPenerima, layoutJasaPengiriman;
    LinearLayout layoutBank;
    ImageView bt_setting;
    Button bt_simpan;
    TextView nama_agen, opsipengiriman;
    TextView textView1;
    Cursor cursor;
    DataCenter dbHelper;
    SurfaceView sv;
    Canvas canvas;
    Bitmap mbitmap;
    int i = 0;
    RelativeLayout imgv_showscreenshot;
    static EditText pengirim, penerima, bank, no_rek, jumlah, keterangan;

    CharSequence[] cashier_name = {"ADMIN", "Ayu (1)", "Mbak Mona (2)", "Lisa Rose (3)", "Manja (4)", "Rima (5)", "Qalesya (6)", "Toko Berkah (7)", "A. Basar (8)", "Resti (9)", "Bunda Kharisma (10)", " Zahra (11)", "Vivi (12)"};
    CharSequence[] cashier_code = {"ID00", "ID01", "HK02", "ID03", "ID04", "ID05", "ID06", "ID07", "ID08", "ID09", "ID010", "ID11", "ID012"};
    CharSequence[] cashier_phone = {"+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329", "+6281929232329"};
    CharSequence[] jenis_pengiriman = {"Akun Bank", "Pos Indonesia", "Tunai"};
    CharSequence[] jenis_bank = {"BRI", "Mandiri", "BNI", "BCA", "Bank Lainnya"};
    String[] sheet_id = {"", "", "", "", "", "", "", "", "", "", "", "", ""};
    int mAdminFee = 15000;

    JSONArray cashierNameSet = null;
    JSONArray cashierCodeSet = null;
    JSONArray cashierPhoneSet = null;
    JSONArray cashierSheetSet = null;
    final ArrayList<String> cashierNameList = new ArrayList<String>();
    ArrayList<String> cashierCodeList = new ArrayList<String>();
    ArrayList<String> cashierPhoneList = new ArrayList<String>();
    ArrayList<String> cashierSheetList = new ArrayList<String>();
    String selectedCashierName = "";
    String selectedCashierCode = "";
    String selectedCashierPhone = "";
    String selectedJenisPengiriman = "";
    String spreadsheetId = "1ss_HA6ks7vw5MrDi8kBCD1lsqjTrQ296Ds8259SWD9A";
    int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tambah_resi_layout);

        layoutPengirim = (TableRow) findViewById(R.id.LayoutPengirim);
        layoutPenerima = (TableRow) findViewById(R.id.LayoutPenerima);
        layoutJasaPengiriman = (TableRow) findViewById(R.id.LayoutJasaPengiriman);
        layoutBank = (LinearLayout) findViewById(R.id.LayoutBank);
        textView1 = (TextView) findViewById(R.id.textView1);
        bt_setting = (ImageView) findViewById(R.id.bt_setting);
        bt_simpan = (Button) findViewById(R.id.bt_simpan);
        nama_agen = (TextView) findViewById(R.id.nama_agen);
        opsipengiriman = (TextView) findViewById(R.id.opsipengiriman);
        pengirim = (EditText) findViewById(R.id.pengirim);
        penerima = (EditText) findViewById(R.id.penerima);
        bank = (EditText) findViewById(R.id.bank);
        no_rek = (EditText) findViewById(R.id.no_rek);
        jumlah = (EditText) findViewById(R.id.jumlah);
        keterangan = (EditText) findViewById(R.id.keterangan);

        dbHelper = new DataCenter(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM biodata", null);
        cursor.moveToFirst();
        cursor.moveToPosition(0);
        //nama_agen.setText(cursor.getString(6).toString());
        //opsipengiriman.setText(cursor.getString(5).toString());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            cashierNameSet = new JSONArray(prefs.getString("cashierNameSet", null));
            Log.d("cashierNameSet: ", cashierNameSet.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cashierNameSet == null) {

            cashierNameSet = new JSONArray();
            for (int i = 0; i < cashier_name.length; i++) {
                cashierNameSet.put(cashier_name[i]);
            }
            Log.d("cashierNameSet: ", cashierNameSet.toString());
            cashierCodeSet = new JSONArray();
            for (int i = 0; i < cashier_code.length; i++) {
                cashierCodeSet.put(cashier_code[i]);
            }
            Log.d("cashierCodeSet: ", cashierCodeSet.toString());
            cashierPhoneSet = new JSONArray();
            for (int i = 0; i < cashier_phone.length; i++) {
                cashierPhoneSet.put(cashier_phone[i]);
            }
            Log.d("cashierPhoneSet: ", cashierPhoneSet.toString());
            cashierSheetSet = new JSONArray();
            for (int i = 0; i < sheet_id.length; i++) {
                cashierSheetSet.put(sheet_id[i]);
            }
            Log.d("cashierSheetSet: ", cashierSheetSet.toString());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("cashierNameSet", cashierNameSet.toString());
            editor.putString("cashierCodeSet", cashierCodeSet.toString());
            editor.putString("cashierPhoneSet", cashierPhoneSet.toString());
            editor.putString("cashierSheetSet", cashierSheetSet.toString());
            editor.commit();
        }

        try {
            cashierNameSet = new JSONArray(prefs.getString("cashierNameSet", "[]"));
            for (int i = 0; i < cashierNameSet.length(); i++) {
                cashierNameList.add(cashierNameSet.get(i).toString());
                Log.d("cashierNameSet: ", cashierNameSet.toString());
            }
            cashierCodeSet = new JSONArray(prefs.getString("cashierCodeSet", "[]"));
            for (int i = 0; i < cashierCodeSet.length(); i++) {
                cashierCodeList.add(cashierCodeSet.get(i).toString());
                Log.d("cashierCodeSet: ", cashierCodeSet.toString());
            }
            cashierPhoneSet = new JSONArray(prefs.getString("cashierPhoneSet", "[]"));
            for (int i = 0; i < cashierPhoneSet.length(); i++) {
                cashierPhoneList.add(cashierPhoneSet.get(i).toString());
                Log.d("cashierPhoneSet: ", cashierPhoneSet.toString());
            }
            cashierSheetSet = new JSONArray(prefs.getString("cashierSheetSet", "[]"));
            for (int i = 0; i < cashierSheetSet.length(); i++) {
                cashierSheetList.add(cashierSheetSet.get(i).toString());
                Log.d("cashierSheetSet: ", cashierSheetSet.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == 0) {
                    mode = 1;
                    textView1.setText("Tambah Saldo");
                    layoutJasaPengiriman.setVisibility(View.GONE);
                    layoutBank.setVisibility(View.GONE);
                } else {
                    mode = 0;
                    textView1.setText("Resi Baru");
                    layoutJasaPengiriman.setVisibility(View.VISIBLE);
                    layoutBank.setVisibility(View.VISIBLE);
                }
                pengirim.setText("");
                penerima.setText("");
                bank.setText("");
                no_rek.setText("");
                jumlah.setText("");
                keterangan.setText("");
            }
        });

        nama_agen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TambahActivity.this);
                builder.setItems(cashierNameList.toArray(new CharSequence[cashierNameList.size()]), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        nama_agen.setText(cashierNameList.get(which));
                        selectedCashierName = cashierNameList.get(which);
                        selectedCashierCode = cashierCodeList.get(which);
                        selectedCashierPhone = cashierPhoneList.get(which);
                        spreadsheetId = cashierSheetList.get(which);
                        //Toast.makeText(TambahActivity.this, "selectedCashierName" + selectedCashierName, Toast.LENGTH_SHORT).show();

                    }
                });
                builder.show();
            }
        });

        opsipengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TambahActivity.this);
                builder.setItems(jenis_pengiriman, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        opsipengiriman.setText(jenis_pengiriman[which]);
                        selectedJenisPengiriman = jenis_pengiriman[which].toString();
                        if (jenis_pengiriman[which].equals("Akun Bank")) {
                            bank.setText("");
                            no_rek.setText("");
                            bank.setHint("Bank");
                            bank.setInputType(InputType.TYPE_CLASS_TEXT);
                            bank.setFocusable(false);
                            bank.setFocusableInTouchMode(false);
                            no_rek.setHint("No Rekening");
                            bank.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TambahActivity.this);
                                    builder.setItems(jenis_bank, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            if (jenis_bank[which].equals("Bank Lainnya")) {
                                                bank.setFocusable(true);
                                                bank.setFocusableInTouchMode(true);
                                                bank.setText("");
                                            } else {
                                                bank.setText(jenis_bank[which]);
                                            }
                                            //Toast.makeText(TambahActivity.this, "selectedCashierName" + selectedCashierName, Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    builder.show();
                                }
                            });
                        } else if (jenis_pengiriman[which].equals("Pos Indonesia")) {
                            bank.setHint("NTP");
                            bank.setInputType(InputType.TYPE_CLASS_TEXT);
                            no_rek.setHint("PIN");
                            bank.setText("");
                            no_rek.setText("");
                            bank.setFocusable(true);
                            bank.setFocusableInTouchMode(true);
                        } else if (jenis_pengiriman[which].equals("Tunai")) {
                            bank.setText("");
                            no_rek.setText("");
                            bank.setHint("Nomor");
                            bank.setInputType(InputType.TYPE_CLASS_NUMBER);
                            no_rek.setHint("Serial");
                            bank.setFocusable(true);
                            bank.setFocusableInTouchMode(true);
                        }
                        //Toast.makeText(TambahActivity.this, "selectedJenisPengiriman" + selectedJenisPengiriman, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        bt_simpan.setTypeface(Typeface.createFromAsset(getAssets(), "Lobster_1.3.otf"));

        bt_setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt_setting();
            }
        });

        bt_simpan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mode == 0) {
                    if ((selectedJenisPengiriman.equals("")) || (selectedCashierName.equals("")) || (pengirim.getText().toString().equals("")) || (penerima.getText().toString().equals("")) || (bank.getText().toString().equals("")) || (no_rek.getText().toString().equals("")) || (jumlah.getText().toString().equals(""))) {
                        Toast.makeText(getApplicationContext(), "Isi semua inputan dengan benar", Toast.LENGTH_LONG).show();
                    } else {
                        getResultsFromApi();
                    }
                } else {
                    if ((selectedCashierName.equals("")) || (pengirim.getText().toString().equals("")) || (penerima.getText().toString().equals("")) || (jumlah.getText().toString().equals(""))) {
                        Toast.makeText(getApplicationContext(), "Isi semua inputan dengan benar", Toast.LENGTH_LONG).show();
                    } else {
                        getResultsFromApi();
                    }
                }
            }
        });

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Persiapan ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    private void changeProgressMessage(String message) {
        mProgress.setMessage(message);
    }

    public void bt_setting() {
        Intent i = new Intent(this, BaruActivity.class);
        startActivity(i);
    }

    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Anda yakin akan keluar?");
        alertDialog.setButton("Iya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent exit = new Intent(Intent.ACTION_MAIN);
                exit.addCategory(Intent.CATEGORY_HOME);
                exit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(exit);
                finish();
                System.exit(0);
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


    public void CetakResi() {
        Bundle dataIntent = new Bundle();
        dataIntent.putString("selectedCashierName", selectedCashierName);
        dataIntent.putString("selectedCashierCode", selectedCashierCode);
        dataIntent.putString("selectedCashierPhone", selectedCashierPhone);
        dataIntent.putString("selectedCashierPhonee", selectedCashierPhone);
        dataIntent.putString("selectedJenisPengiriman", selectedJenisPengiriman);
        dataIntent.putString("pengirim", pengirim.getText().toString());
        dataIntent.putString("penerima", penerima.getText().toString());
        dataIntent.putString("bank", bank.getText().toString());
        dataIntent.putString("no_rek", no_rek.getText().toString());
        dataIntent.putString("jumlah", jumlah.getText().toString());
        dataIntent.putString("keterangan", keterangan.getText().toString());
        Intent i = new Intent(this, CetakActivity.class).putExtras(dataIntent);
        startActivity(i);
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet.", Toast.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Tidak ada koneksi internet");
            builder.setMessage("Apakah Anda tetap ingin cetak resi?")
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CetakResi();
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "Aplikasi ini membutuhkan akses ke Google account Anda (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(getApplicationContext(),
                            "Aplikasi ini membutuhkan Google Play Services. Silahkan install " +
                                    "Google Play Services pada perangkat Anda dan buka ulang aplikasi.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                            String[] permissions,
                                            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);

        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                TambahActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;
        private int mLastRow;
        private int mLastNumber;
        private int mLastSaldo;
        private String mFormattedDate;
        private String mSendType;
        private String mSender;
        private String mReceiver;
        private String mBank;
        private String mNominal;
        private String mDescription;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = new HttpTransport() {
                @Override
                protected LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
                    return null;
                }
            };
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Spreadsheet")
                    .build();
            Date now = new Date();
            mFormattedDate = new SimpleDateFormat("MMM.yyyy", Locale.ENGLISH).format(now);
            mSendType = opsipengiriman.getText().toString();
            mSender = pengirim.getText().toString().toUpperCase();
            mReceiver = penerima.getText().toString().toUpperCase();
            mBank = bank.getText().toString().toUpperCase();
            mNominal = jumlah.getText().toString().toUpperCase();
            mDescription = keterangan.getText().toString();
        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeProgressMessage("Mengecek Google Sheets ...");
                    }
                });

                if (checkSheet() == false) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeProgressMessage("Membuat sheet baru ...");
                        }
                    });

                    createSheet();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeProgressMessage("Menyimpan ke Google Sheets ...");
                    }
                });

                if (insertData() == true) {
                    return "Berhasil memasukkan data";
                }

                return "Gagal memasukkan data";
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private boolean checkSheet() throws IOException {
            Spreadsheet sheetResponse = this.mService.spreadsheets().get(spreadsheetId).execute();
            Log.d("checkSheet", sheetResponse.toString());
            List<Sheet> sheets = sheetResponse.getSheets();

            for (Sheet sheet : sheets) {
                String sheetTitle = sheet.getProperties().getTitle();
                if (sheetTitle.equals(mFormattedDate)) {
                    Log.d("checkSheet", "found sheet " + mFormattedDate);

                    String range = mFormattedDate + "!A3:A1000";
                    ValueRange response = this.mService.spreadsheets().values()
                            .get(spreadsheetId, range)
                            .execute();
                    List<List<Object>> values = response.getValues();

                    mLastNumber = Integer.valueOf(values.get(values.size() - 1).get(0).toString());
                    Log.d("checkSheet", "Last number: " + mLastNumber);

                    range = mFormattedDate + "!J3:J1000";
                    response = this.mService.spreadsheets().values()
                            .get(spreadsheetId, range)
                            .execute();
                    Log.d("checkSheet", response.toString());
                    values = response.getValues();
                    Log.d("checkSheet", "Values: " + values.toString() + "; Values size: " + values.size());

                    mLastRow = values.size() + 2;
                    mLastSaldo = Integer.valueOf(values.get(values.size() - 1).get(0).toString().replace(".", ""));
                    Log.d("checkSheet", "Last row: " + mLastRow + "; Last saldo: " + mLastSaldo);

                    return true;
                }
            }

            return false;
        }

        private void createSheet() throws IOException {
            // Get sheet ID "template"
            List<String> ranges = new ArrayList<String>();
            ranges.add("template");
            Spreadsheet sheetResponse = this.mService.spreadsheets().get(spreadsheetId).setRanges(ranges).execute();
            Log.d("createSheet", sheetResponse.toString());
            List<Sheet> sheets = sheetResponse.getSheets();
            int sheetId = sheets.get(0).getProperties().getSheetId();

            // Copy sheet from "template"
            CopySheetToAnotherSpreadsheetRequest copyRequest = new CopySheetToAnotherSpreadsheetRequest();
            copyRequest.setDestinationSpreadsheetId(spreadsheetId);
            SheetProperties propertiesResponse = this.mService.spreadsheets().sheets().copyTo(spreadsheetId, sheetId, copyRequest).execute();
            Log.d("createSheet", propertiesResponse.toString());

            // Rename new created sheet
            UpdateSheetPropertiesRequest propertiesRequest = new UpdateSheetPropertiesRequest();
            propertiesResponse.setTitle(mFormattedDate);
            propertiesRequest.setProperties(propertiesResponse);
            propertiesRequest.setFields("*");
            Request request = new Request();
            request.setUpdateSheetProperties(propertiesRequest);
            List<Request> requests = new ArrayList<Request>();
            requests.add(request);
            BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest();
            batchRequest.setRequests(requests);
            BatchUpdateSpreadsheetResponse mSheet = this.mService.spreadsheets().batchUpdate(spreadsheetId, batchRequest).execute();
            Log.d("createSheet", mSheet.toString());

            // Modify inside the sheet
            String range = mFormattedDate + "!A1:A1";
            List<Object> data = new ArrayList<Object>();
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();
            data.add(months[month].toUpperCase() + " " + year);
            dataList.add(data);
            ValueRange content = new ValueRange();
            content.setRange(range);
            content.setValues(dataList);

            UpdateValuesResponse response = this.mService.spreadsheets().values()
                    .update(spreadsheetId, range, content)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d("createSheet", response.toString());

            range = mFormattedDate + "!A3:J3";
            data.clear();
            dataList.clear();
            cal.add(Calendar.MONTH, -1);
            month = cal.get(Calendar.MONTH);
            data.add("");
            data.add(new SimpleDateFormat("01/MM/yyyy", Locale.ENGLISH).format(now));
            data.add("SALDO " + months[month].toUpperCase() + " " + year);
            dataList.add(data);
            content.setRange(range);
            content.setValues(dataList);

            response = this.mService.spreadsheets().values()
                    .update(spreadsheetId, range, content)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d("createSheet", response.toString());

            mLastRow = 3;
        }

        private boolean insertData() throws IOException {
            String range = mFormattedDate + "!A" + (mLastRow + 1) + ":L" + (mLastRow + 1);
            List<Object> data = new ArrayList<Object>();
            List<List<Object>> dataList = new ArrayList<List<Object>>();
            Date now = new Date();
            if (mode == 0) {
                data.add(String.valueOf(mLastNumber + 1));
                data.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(now));
                data.add(mSender);
                data.add(mReceiver);
                data.add("");
                data.add(mNominal);
                Log.d("insertData", "Bank: " + mBank);
                int bankFee = 0;
                if (mSendType.equals("Akun Bank")) {
                    if (mBank.equals("BRI")) {
                        bankFee = 250;
                    } else if (mBank.equals("BCA")) {
                        bankFee = 3250;
                    } else if (mBank.equals("BNI")) {
                        bankFee = 0;
                    } else if (mBank.equals("MANDIRI")) {
                        bankFee = 0;
                    } else {
                        bankFee = 3250;
                    }
                } else if (mSendType.equals("Pos Indonesia")) {
                    bankFee = 25000;
                } else if (mSendType.equals("Tunai")) {
                    bankFee = 5000;
                }
                data.add(String.valueOf(bankFee));
                data.add(mAdminFee);
                data.add("=G" + (mLastRow + 1) + "+H" + (mLastRow + 1));
                data.add("=J" + mLastRow + "-F" + (mLastRow + 1) + "-I" + (mLastRow + 1));
            } else {
                data.add("");
                data.add(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(now));
                data.add(mSender);
                data.add(mReceiver);
                data.add(mNominal);
                data.add("");
                data.add("");
                data.add("");
                data.add("");
                data.add("=J" + mLastRow + "+E" + (mLastRow + 1));
            }
            data.add(mDescription);
            data.add(mBank);

            dataList.add(data);
            ValueRange content = new ValueRange();
            content.setRange(range);
            content.setValues(dataList);

            UpdateValuesResponse response = this.mService.spreadsheets().values()
                    .update(spreadsheetId, range, content)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d("insertData", response.toString());

            return (response.getUpdatedCells() == 12) && (response.getUpdatedRows() == 1);
        }

        private List<String> getDataFromApi() throws IOException {
            String range = "Sheet1!A1:T3";
            List<String> results = new ArrayList<String>();
            Log.d("getDataFromApi", "Before execute");
            ValueRange response = this.mService.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            Log.d("getDataFromApi", "After execute: " + response.toString());
            List<List<Object>> values = response.getValues();
            if (values != null) {
                results.add("Name, Major");
                for (List row : values) {
                    results.add(row.get(0) + ", " + row.get(1));
                }
            }
            return results;
        }

        @Override
        protected void onPreExecute() {
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output == null) {
                Toast.makeText(getApplicationContext(), "No results returned", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                Log.d("MakeRequestTask", output);
                if (output.equals("Berhasil memasukkan data")) {
                    if (mode == 0) {
                        CetakResi();
                    }
                    nama_agen.setText("");
                    opsipengiriman.setText("");
                    pengirim.setText("");
                    penerima.setText("");
                    bank.setText("");
                    no_rek.setText("");
                    jumlah.setText("");
                    keterangan.setText("");
                }
            }
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            TambahActivity.REQUEST_AUTHORIZATION);
                } else {
                    Toast.makeText(getApplicationContext(), "The following error occurred:\n"
                            + mLastError.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("MakeRequestTask", "The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Toast.makeText(getApplicationContext(), "Request cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}

