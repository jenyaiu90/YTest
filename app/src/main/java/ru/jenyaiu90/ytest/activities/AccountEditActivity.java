package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.User;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.services.UserService;

public class AccountEditActivity extends Activity
{
	public static final String USER = "user"; //Для намерения: пользователь

	protected User user;

	protected LinearLayout accountLL;
	protected TextView loginTV;
	protected EditText nameET, surnameET, emailET, phoneNumberET, oldPasswordET, newPasswordET;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_edit);

		user = new Gson().fromJson(getIntent().getStringExtra(USER), User.class);

		accountLL = (LinearLayout)findViewById(R.id.accountLL);
		loginTV = (TextView)findViewById(R.id.loginTV);
		nameET = (EditText)findViewById(R.id.nameET);
		surnameET = (EditText)findViewById(R.id.surnameET);
		emailET = (EditText)findViewById(R.id.emailET);
		phoneNumberET = (EditText)findViewById(R.id.phoneNumberET);
		oldPasswordET = (EditText)findViewById(R.id.oldPasswordET);
		newPasswordET = (EditText)findViewById(R.id.newPasswordET);

		loginTV.setText(user.getLogin());
		nameET.setText(user.getName());
		surnameET.setText(user.getSurname());
		emailET.setText(user.getEmail());
		phoneNumberET.setText(user.getPhone_number());
	}

	public void save(View view) //Сохранение данных
	{
		if (oldPasswordET.getText().toString().isEmpty())
		{
			Toast.makeText(AccountEditActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
			return;
		}
		String email = emailET.getText().toString();
		if (!email.isEmpty()) //Проверка адреса элекронной почты на корректность
		{
			boolean wasA = false, wasD = false;
			for (int i = 0; i < email.length(); i++)
			{
				switch (email.charAt(i))
				{
					case ' ':
						Toast.makeText(AccountEditActivity.this, R.string.invalid_email, Toast.LENGTH_LONG).show();
						return;
					case '@':
						if (wasA)
						{
							Toast.makeText(AccountEditActivity.this, R.string.invalid_email, Toast.LENGTH_LONG).show();
							return;
						}
						else
						{
							wasA = true;
						}
						break;
					case '.':
						if (wasA)
						{
							wasD = true;
						}
				}

			}
			if (!wasA || !wasD) //Если в адресе не встретилось @ или . после неё
			{
				Toast.makeText(AccountEditActivity.this, R.string.invalid_email, Toast.LENGTH_LONG).show();
				return;
			}
		}

		if (phoneNumberET.getText().toString().isEmpty())
		{
			phoneNumberET.setText("+0");
		}

		ProgressBar loadPB = new ProgressBar(AccountEditActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		accountLL.addView(loadPB, 0);
		new SaveAsync().execute(user.getLogin(), nameET.getText().toString(),
				surnameET.getText().toString(), email, phoneNumberET.getText().toString(),
				oldPasswordET.getText().toString(),
				newPasswordET.getText().toString().isEmpty() ? oldPasswordET.getText().toString() :
						newPasswordET.getText().toString());
	}

	public void cancel(View view) //Отменить
	{
		setResult(RESULT_CANCELED);
		finish();
	}

	class SaveAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... user)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			UserService uService = rf.create(UserService.class);
			Call<ServerAnswerEntity> resp = uService.updateUser(user[0], user[1], user[2], user[3], user[4], user[5], user[6]);
			ServerAnswerEntity res = null;
			try
			{
				Response<ServerAnswerEntity> response = resp.execute();
				res = response.body();
			}
			catch (IOException e)
			{
				res = new ServerAnswerEntity(ServerAnswerEntity.NO_INTERNET);
			}
			return res;
		}

		@Override
		protected void onPostExecute(ServerAnswerEntity result)
		{
			accountLL.removeViewAt(0);
			if (result != null)
			{
				if (result.getAnswer().equals(ServerAnswerEntity.OK))
				{
					setResult(RESULT_OK);
					AccountEditActivity.this.finish();
				}
				else
				{
					Util.errorToast(AccountEditActivity.this, result.getAnswer());
				}
			}
		}
	}
}
