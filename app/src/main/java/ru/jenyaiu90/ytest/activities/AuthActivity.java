package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import ru.jenyaiu90.ytest.R;

public class AuthActivity extends Activity
{
	protected RadioButton signInRB;
	protected RadioButton signUpRB;
	protected LinearLayout inputLL;
	protected EditText loginET;
	protected EditText passwordET;
	protected EditText nameET;
	protected EditText surnameET;
	protected CheckBox teacherCB;
	protected Button signBT;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		signInRB = (RadioButton)findViewById(R.id.signInRB);
		signUpRB = (RadioButton)findViewById(R.id.signUpRB);
		inputLL = (LinearLayout)findViewById(R.id.inputLL);
		loginET = (EditText)findViewById(R.id.loginET);
		passwordET = (EditText)findViewById(R.id.passwordET);

		nameET = new EditText(AuthActivity.this);
		nameET.setHint(R.string.name);

		surnameET = new EditText(AuthActivity.this);
		surnameET.setHint(R.string.surname);

		teacherCB = new CheckBox(AuthActivity.this);
		teacherCB.setText(R.string.im_teacher);

		signBT = (Button)findViewById(R.id.signBT);
		signBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (loginET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_login, Toast.LENGTH_LONG).show();
					return;
				}
				if (passwordET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
					return;
				}
				signIn();
			}
		});
	}
	public void in(View view)
	{
		inputLL.removeAllViews();
		inputLL.addView(loginET);
		inputLL.addView(passwordET);
		signBT.setText(R.string.sign_in);
		signBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (loginET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_login, Toast.LENGTH_LONG).show();
					return;
				}
				if (passwordET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
					return;
				}
				signIn();
			}
		});
	}
	public void up(View view)
	{
		inputLL.removeAllViews();
		inputLL.addView(loginET);
		inputLL.addView(passwordET);
		inputLL.addView(nameET);
		inputLL.addView(surnameET);
		inputLL.addView(teacherCB);
		signBT.setText(R.string.sign_up);
		signBT.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (loginET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_login, Toast.LENGTH_LONG).show();
					return;
				}
				if (passwordET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
					return;
				}
				if (nameET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_name, Toast.LENGTH_LONG).show();
					return;
				}
				if (surnameET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_surname, Toast.LENGTH_LONG).show();
					return;
				}
				signUp();
			}
		});
	}
	public void signIn()
	{
		//Todo: signing in
		Intent i = new Intent();
		i.putExtra("login", loginET.getText().toString());
		setResult(RESULT_OK, i);
		finish();
	}
	public void signUp()
	{
		//Todo: signing up
		signIn();
	}
}
