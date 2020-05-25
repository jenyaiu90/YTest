package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.User;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.UserService;

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
	protected User user;
	protected boolean success_up;
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
				ProgressBar loadPB = new ProgressBar(AuthActivity.this);
				loadPB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				loadPB.setIndeterminate(true);
				inputLL.addView(loadPB);
				new SignInAsync().execute(loginET.getText().toString(), passwordET.getText().toString());
			}
		});
	}
	public void in(View view) //Переключение полей на вход
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
				ProgressBar loadPB = new ProgressBar(AuthActivity.this);
				loadPB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				loadPB.setIndeterminate(true);
				inputLL.addView(loadPB);
				new SignInAsync().execute(loginET.getText().toString(), passwordET.getText().toString());
			}
		});
	}
	public void up(View view) //Переключение полей на регистрацию
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
				else if (loginET.getText().toString().contains(" "))
				{
					Toast.makeText(AuthActivity.this, R.string.login_pass_space, Toast.LENGTH_LONG).show();
					return;
				}
				if (passwordET.getText().toString().isEmpty())
				{
					Toast.makeText(AuthActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
					return;
				}
				else if (passwordET.getText().toString().contains(" "))
				{
					Toast.makeText(AuthActivity.this, R.string.login_pass_space, Toast.LENGTH_LONG).show();
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
				UserEntity user = new UserEntity();
				user.setLogin(loginET.getText().toString());
				user.setPassword(passwordET.getText().toString());
				user.setName(nameET.getText().toString());
				user.setSurname(surnameET.getText().toString());
				user.setTeacher(teacherCB.isChecked());
				user.setId(0);
				user.setEmail(null);
				user.setPhone_number(null);
				user.setImage(null);
				ProgressBar loadPB = new ProgressBar(AuthActivity.this);
				loadPB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				loadPB.setIndeterminate(true);
				inputLL.addView(loadPB);
				new SignUpAsync().execute(user);
			}
		});
	}
	public void signIn() //Вход
	{
		Intent i = new Intent();
		i.putExtra(MainActivity.LOGIN, user.getLogin());
		i.putExtra(MainActivity.PASSWORD, passwordET.getText().toString());
		i.putExtra(MainActivity.IS_TEACHER, user.getIsTeacher());

		setResult(RESULT_OK, i);
		finish();
	}

	class SignUpAsync extends AsyncTask<UserEntity, String, Boolean>
	{
		@Override
		protected Boolean doInBackground(UserEntity... user) {
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			UserService uService = rf.create(UserService.class);
			Call<UserEntity> resp = uService.createUser(user[0]);
			boolean b = false;
			try
			{
				Response<UserEntity> response = resp.execute();
				UserEntity res = response.body();
				b = res != null;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return b;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
			super.onPostExecute(result);
			inputLL.removeViewAt(inputLL.getChildCount() - 1);
			Toast.makeText(AuthActivity.this, result ? R.string.sign_up_success : R.string.login_already_exists, Toast.LENGTH_LONG).show();
			if (result)
			{
				ProgressBar loadPB = new ProgressBar(AuthActivity.this);
				loadPB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				loadPB.setIndeterminate(true);
				inputLL.addView(loadPB);
				new SignInAsync().execute(loginET.getText().toString(), passwordET.getText().toString());
			}
		}
	}

	class SignInAsync extends AsyncTask<String, String, UserEntity>
	{
		@Override
		protected UserEntity doInBackground(String... login_password) {
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			UserService uService = rf.create(UserService.class);
			Call<UserEntity> resp = uService.signIn(login_password[0], login_password[1]);
			boolean b = false;
			UserEntity res = null;
			try
			{
				Response<UserEntity> response = resp.execute();
				res = response.body();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return res;
		}

		@Override
		protected void onPostExecute(UserEntity result)
		{
			super.onPostExecute(result);
			inputLL.removeViewAt(inputLL.getChildCount() - 1);
			if (result == null)
			{
				Toast.makeText(AuthActivity.this, R.string.login_pass_incorrect, Toast.LENGTH_LONG).show();
			}
			else
			{
				user = new User(result);
				signIn();
			}
		}
	}
}
