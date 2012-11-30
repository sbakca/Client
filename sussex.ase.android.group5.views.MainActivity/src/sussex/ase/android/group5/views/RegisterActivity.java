package sussex.ase.android.group5.views;

import com.androidhive.googleplacesandmaps.R;

import sussex.ase.android.group5.api.Register;
import sussex.ase.android.group5.util.AlertDialogManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {

	private AlertDialogManager alert = new AlertDialogManager();
	private boolean flag = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Button submitbutton = (Button) findViewById(R.id.submitbutton);
		// submit button click
		submitbutton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				// get user username and password
				EditText userName = (EditText) findViewById(R.id.editText1);
				EditText passWord = (EditText) findViewById(R.id.editText2);
				EditText confirmpassWord = (EditText) findViewById(R.id.editText3);

				final String uName = userName.getText().toString();
				final String pWord = passWord.getText().toString();
				final String cpWord = confirmpassWord.getText().toString();

				if (pWord.equals(cpWord) == false || pWord == "" || uName == "") {

					alert.showAlertDialog(RegisterActivity.this, "Sorry",
							"Your password doesn't match, please try again",
							false);
					return;
				}

				new RegisterTask().execute(new String[] { uName, pWord });

			}
		});
		canButton();
	}

	public void canButton() {
		// cancel button
		Button canButton = (Button) findViewById(R.id.cancelbutton);
		canButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
	}

	class RegisterTask extends AsyncTask<String, String, String> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			flag = false;
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage(Html.fromHtml("<b>Hi!</b><br/>Registering..."));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			try {

				Register register = new Register();
				flag = register.register(args[0], args[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (flag == false) {
				alert.showAlertDialog(RegisterActivity.this, "Sorry",
						"Your username is token, please try again", false);
			} else {
				alert.showAlertDialog(RegisterActivity.this, "Congratulations",
						"Please log in", true);
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		}
	}

}
