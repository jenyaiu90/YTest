package ru.jenyaiu90.ytest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.jenyaiu90.ytest.R;

public class MainActivity extends AppCompatActivity
{
	//Для намерения
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String IS_TEACHER = "is_teacher";

	public static final int AUTH_REQUEST = 1; //Код запроса на вход

	protected String login, password;
	protected boolean isTeacher;

	protected ImageButton testsIB, groupsIB, accountIB, signOutIB;
	protected ImageView infoIV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		testsIB = (ImageButton)findViewById(R.id.testsIB);
		groupsIB = (ImageButton)findViewById(R.id.groupsIB);
		accountIB = (ImageButton)findViewById(R.id.accountIB);
		signOutIB = (ImageButton)findViewById(R.id.signOutIB);
		infoIV = (ImageView)findViewById(R.id.infoIV);

		infoIV.setImageDrawable(getResources().getDrawable(R.drawable.info));

		testsIB.setImageDrawable(getResources().getDrawable(R.drawable.tests));
		groupsIB.setImageDrawable(getResources().getDrawable(R.drawable.groups));
		accountIB.setImageDrawable(getResources().getDrawable(R.drawable.account));
		signOutIB.setImageDrawable(getResources().getDrawable(R.drawable.sign_out));

		auth(null);
	}

	//Кнопка «Тесты»
	public void tests(@Nullable View view)
	{
		if (isTeacher) //Для учителя: создание теста
		{
			AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

			alert.setTitle(R.string.create_test);
			alert.setMessage(R.string.enter_name_subject);

			LinearLayout inputLL = new LinearLayout(MainActivity.this);
			inputLL.setOrientation(LinearLayout.VERTICAL);
			final EditText nameET = new EditText(MainActivity.this);
			nameET.setHint(R.string.test_name);
			final EditText subjectET = new EditText(MainActivity.this);
			subjectET.setHint(R.string.subject);
			inputLL.addView(nameET);
			inputLL.addView(subjectET);
			alert.setView(inputLL);

			alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int whichButton)
				{
					Intent testQListIntent = new Intent(MainActivity.this, TestQListActivity.class);
					testQListIntent.putExtra(TestQListActivity.LOGIN, login);
					testQListIntent.putExtra(TestQListActivity.PASSWORD, password);
					testQListIntent.putExtra(TestQListActivity.TEST_NAME, nameET.getText().toString());
					testQListIntent.putExtra(TestQListActivity.SUBJECT, subjectET.getText().toString());
					startActivity(testQListIntent);
				}
			});
			alert.setNegativeButton(R.string.cancel, null);
			alert.show();
		}
		else //Для ученика: переход к списку заданных тестов
		{
			Intent testListIntent;
			testListIntent = new Intent(MainActivity.this, TestListActivity.class);
			testListIntent.putExtra(TestListActivity.LOGIN, login);
			testListIntent.putExtra(TestListActivity.PASSWORD, password);
			startActivity(testListIntent);
		}
	}

	//Кнопка «Группы»
	public void groups(@Nullable View view)
	{
		Intent groupListIntent = new Intent(MainActivity.this, GroupListActivity.class);
		groupListIntent.putExtra(GroupListActivity.LOGIN, login);
		groupListIntent.putExtra(GroupListActivity.PASSWORD, password);
		groupListIntent.putExtra(GroupListActivity.IS_TEACHER, isTeacher);
		startActivity(groupListIntent);
	}

	//Кнопка «Аккаунт»
	public void account(@Nullable View view)
	{
		Intent i = new Intent(MainActivity.this, AccountActivity.class);
		i.putExtra(AccountActivity.LOGIN, login);
		i.putExtra(AccountActivity.ACC_LOGIN, login);
		startActivity(i);
	}

	//Кнопка «Выход» / переход к экрану авторизации
	public void auth(@Nullable View view)
	{
		Intent i = new Intent(MainActivity.this, AuthActivity.class);
		startActivityForResult(i, AUTH_REQUEST);
	}

	//Информация о приложении
	public void info(View view)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(R.string.icons_copyright)
				.setIcon(R.drawable.info)
				.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case AUTH_REQUEST: //Получение некоторых данных пользователя
				login = data.getStringExtra(LOGIN);
				password = data.getStringExtra(PASSWORD);
				isTeacher = data.getBooleanExtra(IS_TEACHER, false);
		}
	}
}
