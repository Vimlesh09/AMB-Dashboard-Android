package com.ambdashboard;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GetTimeOut extends Activity {
	private final String NAMESPACE = "http://tempuri.org/ ";
	private final String URL = "http://reports.eonbi.com/amb/service.asmx";
	private final String METHOD_NAME = "getTimeOut";
	private final String SOAP_ACTION = " http://tempuri.org/getTimeOut";
	public volatile boolean parsingComplete = true;

	private String TAG = "PGGURU";

	// private static InputStream xml;
	private String result;
	private String timeOut;
	AnimationDrawable anim;
	Button b;
	TextView location, temperature, pressure, humidity, progress;
	String locstr, tempstr, prestr, humistr;
	ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.imageView1);
		image.setBackgroundResource(R.drawable.loaderimage);
		anim = (AnimationDrawable) image.getBackground();
		setContentView(R.layout.activity_main);
		AsyncCallWS task = new AsyncCallWS();
		task.execute();
		// Button Click Listener
	}

	private class AsyncCallWS extends AsyncTask<String, Void, Void> {
		@Override
		protected Void doInBackground(String... params) {
			Log.i(TAG, "doInBackground");
			getTimeOut();
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			image.setVisibility(View.GONE);
			anim.stop();

			Intent intent = new Intent(GetTimeOut.this, MainActivity.class);
			intent.putExtra("timeout", timeOut);
			startActivity(intent);

		}

		@Override
		protected void onPreExecute() {
			image.setVisibility(View.VISIBLE);
			anim.start();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			Log.i(TAG, "onProgressUpdate");
		}

	}

	public void getTimeOut() {
		// Create request
		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);

		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			// Invole web service
			androidHttpTransport.call(SOAP_ACTION, envelope);
			// Get the response
			SoapObject response = (SoapObject) envelope.getResponse();
			int i = 0;
			int RCount = response.getPropertyCount();
			for (i = 0; i < RCount; i++) {
				Object property = response.getProperty(i);
				if (property instanceof SoapObject) {
					SoapObject info = (SoapObject) property;
					result = info.getProperty("Result").toString();
					timeOut = info.getProperty("TimeOut").toString();

				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}
