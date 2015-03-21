package com.ambdashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context ctx = this;
	String webPagehtml;
	ImageView image;
	WebView webView;
	AnimationDrawable anim;
	static int load = 0;
	String URL = "https://mydashboard.ambulancerevenue.com/login/login.aspx";
	private final static int FILECHOOSER_RESULTCODE = 1;
	private ValueCallback<Uri> uploadMessage;

	@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.activity_main);
			webView = (WebView) findViewById(R.id.webview);
			image = (ImageView) findViewById(R.id.imageView1);
			image.setBackgroundResource(R.drawable.loaderimage);
			anim = (AnimationDrawable) image.getBackground();
			image.setVisibility(View.GONE);

			webView.getSettings().setLoadWithOverviewMode(true);
			webView.getSettings().setUseWideViewPort(false);
			webView.setWebViewClient(new OurViewClient());
			webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
			
			WebSettings webSettings = webView.getSettings();
			webSettings.setJavaScriptEnabled(true);
			webSettings.setBuiltInZoomControls(true);
			webSettings.setAllowFileAccess(true);
			if (isOnline()) {
				webView.loadUrl(URL);
			} else {
				alertDialog(URL);
			}

			webView.setWebChromeClient(new WebChromeClient() {
				// The undocumented magic method override
				// Eclipse will swear at you if you try to put @Override here
				// For Android 3.0+
				@SuppressWarnings("unused")
				public void openFileChooser(ValueCallback<Uri> uploadMsg) {

					try {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("image/*");
						MainActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Chooser"),
								FILECHOOSER_RESULTCODE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				// For Android 3.0+
				@SuppressWarnings("unused")
				public void openFileChooser(ValueCallback uploadMsg,
						String acceptType) {
					try {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("*/*");
						MainActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Browser"),
								FILECHOOSER_RESULTCODE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// For Android 4.1
				@SuppressWarnings("unused")
				public void openFileChooser(ValueCallback<Uri> uploadMsg,
						String acceptType, String capture) {
					try {
						uploadMessage = uploadMsg;
						Intent i = new Intent(Intent.ACTION_GET_CONTENT);
						i.addCategory(Intent.CATEGORY_OPENABLE);
						i.setType("image/*");
						MainActivity.this.startActivityForResult(
								Intent.createChooser(i, "File Chooser"),
								MainActivity.FILECHOOSER_RESULTCODE);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class OurViewClient extends WebViewClient {

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			try {
				super.onPageStarted(view, url, favicon);
				image.setVisibility(View.VISIBLE);
				anim.start();
				if (!isOnline()) {
					webView.stopLoading();

				}
				
				
				webView.setVisibility(View.GONE);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			try {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				image.setVisibility(View.GONE);
				anim.stop();
				webView.loadUrl("");
				alertDialog(failingUrl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public void onPageFinished(WebView view, String url) {
			try {
				image.setVisibility(View.GONE);
				anim.stop();
				webView.setVisibility(View.VISIBLE);
				// Toast.makeText(MainActivity.this, webView.getUrl(),
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void onConfigurationChanged(Configuration newConfig) {
		try {
			// TODO Auto-generated method stub
			super.onConfigurationChanged(newConfig);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	class MyJavaScriptInterface {
		public void showHTML(String html) {
			try {
				if (isOnline()) {
					webPagehtml = html;

				}
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void alertDialog(final String failingUrl) {
		try {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);

			// set title
			alertDialogBuilder.setTitle("Error!");

			// set dialog message
			alertDialogBuilder
					.setMessage("The internet connection appears to be offline")
					.setCancelable(false)
					.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {

							if (isOnline()) {

								webView.loadUrl(failingUrl);
							} else {
								alertDialog(failingUrl);
							}
						}
					});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		try {
			if (requestCode == FILECHOOSER_RESULTCODE) {

				if (uploadMessage == null)
					return;

				Uri result = intent == null || resultCode != RESULT_OK ? null
						: intent.getData();
				if (result != null) {

					String filePath = null;

					if ("content".equals(result.getScheme())) {

						Cursor cursor = this
								.getContentResolver()
								.query(result,
										new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
										null, null, null);
						cursor.moveToFirst();
						filePath = cursor.getString(0);
						cursor.close();

					} else {

						filePath = result.getPath();

					}

					Uri myUri = Uri.parse(filePath);
					uploadMessage.onReceiveValue(myUri);

				} else {

					uploadMessage.onReceiveValue(result);

				}

				// uploadMessage = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
