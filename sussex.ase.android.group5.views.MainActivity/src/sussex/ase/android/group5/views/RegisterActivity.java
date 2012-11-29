package sussex.ase.android.group5.views;


import com.androidhive.googleplacesandmaps.R;

import sussex.ase.android.group5.api.Register;
import sussex.ase.android.group5.util.AlertDialogManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class RegisterActivity extends Activity {

	
	AlertDialogManager alert = new AlertDialogManager();
	@Override
	public void onCreate(Bundle savedInstanceState){
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
		
				
				
				if(pWord.equals(cpWord)==false || pWord=="" || uName==""){
				
					alert.showAlertDialog(RegisterActivity.this, "Sorry", 
							"Your password doesn't match, please try again", false);
					return;
				}
								
				Thread thread = new Thread(new Runnable() {
					public void run() {
						Register register = new Register();
						if (register.register(uName, pWord)) {
							Intent intent = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(intent);
						} 
					}
				});
				thread.start();
				
			}
		});
		canButton();
	}

	public void canButton() {
		// cancel button
		Button canButton = (Button) findViewById(R.id.cancelbutton);
		canButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				//setResult(RESULT_CANCELED);
				//finish(); // go back to home
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
		});
	  }
	}          
		