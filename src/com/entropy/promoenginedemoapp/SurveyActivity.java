package com.entropy.promoenginedemoapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class SurveyActivity extends BaseActivity {
	
	private ArrayList<Integer> answersList = new ArrayList<Integer>();
	public static ArrayList<HashMap<String, String>> questionAndAnswer = new ArrayList<HashMap<String, String>>();
	private ProgressDialog pDialog;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey);
		questionAndAnswer.clear();
		//hypeSDK = new HypeSDK(getApplicationContext(), SurveyActivity.this);
		imageButton.setVisibility(View.INVISIBLE);
		mTitleTextView.setText("Survey");
		tvActionNext.setVisibility(View.INVISIBLE);
		
		Button btnSubmitAnswer = (Button) findViewById(R.id.submitAnswer);
		ListView lvSurvey = (ListView) findViewById(R.id.lvSurveys);
		SurveyAdapter adap = new SurveyAdapter(getApplicationContext(), SecondActivity.surveyFound.getQuestions());
		lvSurvey.setAdapter(adap);
		
		btnSubmitAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				answersList.clear();
				pDialog = ProgressDialog.show(SurveyActivity.this, null, "Loading ...");
				Log.d("BeamSDK", removeDuplicates(questionAndAnswer).toString() + " <questionAndAnswer");
				for(HashMap<String, String> answers : removeDuplicates(questionAndAnswer)) {
					if(answers.get("answerIndex") != null) {
						answersList.add(Integer.parseInt(answers.get("answerIndex")));
					}
				}
				Log.d("BeamSDK", answersList.toString() + " <_ANSWERS");
				new SubmitAnswer().execute((Void) null);
			}
		});
	}
	
	private void showAlertDialog(String msg, final String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
		builder.setMessage(msg)
		   .setTitle(title)
		   .setCancelable(false)
		   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int id) {
		    	   if(title.equals("Success")) {
		    		   finish();
		    	   } else {
		    		   //do nothing
		    	   }
		       }
		   });
		   
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	class SubmitAnswer extends AsyncTask<Void, Void, String> {
		
		@Override
		protected String doInBackground(Void... params) {
			return hypeSDK.submitAnswers(answersList, SecondActivity.surveyFound.getId());
		}
		
		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			if(result.equals("answers incomplete")) {
				showAlertDialog(result, "Error");
			} else if(result.equals("Error : 429 TOO MANY REQUESTS")) {
				showAlertDialog("Please try again later", "Failed");
			} else {
				try {
					JSONObject obj = new JSONObject(result);
					if(obj.getBoolean("success")) {
						showAlertDialog("Thanks For Joining!", "Success");
					} else {
						showAlertDialog(result, "Error");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			super.onPostExecute(result);
		}
	}
}
