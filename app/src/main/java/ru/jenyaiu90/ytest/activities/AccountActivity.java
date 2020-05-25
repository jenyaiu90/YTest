package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.InfoAdapter;
import ru.jenyaiu90.ytest.data.User;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.UserService;

public class AccountActivity extends Activity
{
	public static final String LOGIN = "login"; //Для намерения: логин просматриващего
	public static final String ACC_LOGIN = "accountLogin"; //Для намерения: логин просматриваемого аккаунта
	public static final int EDIT_REQUEST = 1; //Код запроса активности редактирования аккаунта

	protected LinearLayout accountLL;
	protected ListView infoLV;

	protected User user;
	protected String login, accountLogin;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		login = getIntent().getStringExtra(LOGIN);
		accountLogin = getIntent().getStringExtra(ACC_LOGIN);

		accountLL = (LinearLayout)findViewById(R.id.accountLL);
		infoLV = (ListView)findViewById(R.id.infoLV);

		load();

		if (login.equals(accountLogin)) //Добавление кнопки редактирования, если пользователь просматривает свой аккаунт
		{
			Button editBT = new Button(AccountActivity.this);
			editBT.setText(R.string.edit);
			editBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent i = new Intent(AccountActivity.this, AccountEditActivity.class);
					i.putExtra(AccountEditActivity.USER, new Gson().toJson(user));
					startActivityForResult(i, EDIT_REQUEST);
				}
			});
			accountLL.addView(editBT, 1 );
		}
	}

	protected void load() //Загрузка информации о пользователе
	{
		ProgressBar loadPB = new ProgressBar(AccountActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		accountLL.addView(loadPB, 0);
		new LoadUserAsync().execute(accountLogin);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case EDIT_REQUEST:
				if (resultCode == RESULT_OK) //Если изменения внесены, перезагрузить данные пользователя
				{
					load();
				}
		}
	}

	class LoadUserAsync extends AsyncTask<String, String, UserEntity>
	{
		@Override
		protected UserEntity doInBackground(String... login)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			UserService uService = rf.create(UserService.class);
			Call<UserEntity> resp = uService.getUser(login[0]);
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
			accountLL.removeViewAt(0);
			if (result != null)
			{
				user = new User(result);

				String infos[][] = new String[][]
						{{ getResources().getString(R.string.login), accountLogin },
								{ getResources().getString(R.string.name), user.getName() + " " + user.getSurname() },
								{ "Is teacher", getResources().getString(user.getIsTeacher() ? R.string.teacher : R.string.student) },
								{ getResources().getString(R.string.email), user.getEmail() == null ? getResources().getString(R.string.no) : user.getEmail() },
								{ getResources().getString(R.string.phone_number), user.getPhone_number() == null ? getResources().getString(R.string.no) : user.getPhone_number() }};
				InfoAdapter infoAdapter = new InfoAdapter(AccountActivity.this, infos);
				infoLV.setAdapter(infoAdapter);
			}
			else
			{
				Toast.makeText(AccountActivity.this, R.string.couldnt_load_user, Toast.LENGTH_LONG).show();
			}
		}
	}
}
