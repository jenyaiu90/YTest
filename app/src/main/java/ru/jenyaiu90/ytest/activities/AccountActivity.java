package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.InfoAdapter;
import ru.jenyaiu90.ytest.data.User;

public class AccountActivity extends Activity
{
	public static final String LOGIN = "login"; //Для намерения: логин просматриващего
	public static final String ACC_LOGIN = "accountLogin"; //Для намерения: логин просматриваемого аккаунта
	public static final int EDIT_REQUEST = 1; //Код запроса активности редактирования аккаунта

	protected LinearLayout accountLL;
	protected ImageView imageIV;
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
		imageIV = (ImageView)findViewById(R.id.imageIV);
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
		user = new User(accountLogin);

		if (user.getImage() == null)
		{
			imageIV.setImageDrawable(getResources().getDrawable(R.drawable.account));
		}
		else
		{
			imageIV.setImageBitmap(user.getImage());
		}

		String infos[][] = new String[][]
				{{ getResources().getString(R.string.login), accountLogin },
						{ getResources().getString(R.string.name), user.getName() + " " + user.getSurname() },
						{ "Is teacher", getResources().getString(user.getIsTeacher() ? R.string.teacher : R.string.student) },
						{ getResources().getString(R.string.email), user.getEmail() },
						{ getResources().getString(R.string.phone_number), user.getPhone_number() }};
		InfoAdapter infoAdapter = new InfoAdapter(AccountActivity.this, infos);
		infoLV.setAdapter(infoAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case EDIT_REQUEST:
				if (resultCode == RESULT_OK) //Если изменения внесены, перезагрузить данные  пользователя
				{
					load();
				}
		}
	}
}
