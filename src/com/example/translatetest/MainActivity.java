package com.example.translatetest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.model.TranslationsListResponse;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	private static final String TAG = "Translate";
	
	private TextView textbox;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		textbox = (TextView)findViewById(R.id.textbox);
		(new TranslateTask()).execute(textbox.getText().toString(), "en", "es");
	}
	
	@Override
	protected void onPause() {
		finish();
	}

	private class TranslateTask extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			Log.d(TAG, "Translating...");
			try {
				String key = "AIzaSyDdkIEwVWMse3s7ef5kBrwmBg8MfoNQea0";
				
				// Set up the HTTP transport and JSON factory
				HttpTransport httpTransport = new NetHttpTransport();
				JsonFactory jsonFactory = AndroidJsonFactory.getDefaultInstance();
				
				Translate.Builder translateBuilder = new Translate.Builder(httpTransport, jsonFactory, null);
				translateBuilder.setApplicationName(getString(R.string.app_name));
				
				Translate translate = translateBuilder.build();
				
				List<String> q = new ArrayList<String>();
				q.add(params[0]);
				
				Translate.Translations.List list = translate.translations().list(q, params[2]);
				list.setKey(key);
				list.setSource(params[1]);
				TranslationsListResponse translateResponse = list.execute();
				String response = translateResponse.getTranslations().get(0).getTranslatedText();
				
				Log.d(TAG, response);
				JSONObject json = new JSONObject(response);
				
				String result = json.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
				return result;
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return "error?!?";
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			textbox.setText(result);
			
		}
		
	}
}

