package ru.jenyaiu90.ytest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.data.Util;

public class MainActivity extends AppCompatActivity
{
	public static final int AUTH_REQUEST = 1; //Код запроса на вход

	public static final String LOGIN = "login"; //Для намерения: логин
	public static final String PASSWORD = "password"; //Для намерения: пароль
	public static final String IS_TEACHER = "is_teacher"; //Для намерения: является ли учителем

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

	public void tests(@Nullable View view) //Перейти к тестам
	{
		if (isTeacher)
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
		else
		{
			Intent testListIntent;
			testListIntent = new Intent(MainActivity.this, TestListActivity.class);
			testListIntent.putExtra(TestListActivity.LOGIN, login);
			testListIntent.putExtra(TestListActivity.PASSWORD, password);
			startActivity(testListIntent);
		}
	}
	public void groups(@Nullable View view) //Перейти к группам
	{
		Intent groupListIntent = new Intent(MainActivity.this, GroupListActivity.class);
		groupListIntent.putExtra(GroupListActivity.LOGIN, login);
		groupListIntent.putExtra(GroupListActivity.PASSWORD, password);
		groupListIntent.putExtra(GroupListActivity.IS_TEACHER, isTeacher);
		startActivity(groupListIntent);
	}
	public void account(@Nullable View view) //Перейти к аккаунту
	{
		Intent i = new Intent(MainActivity.this, AccountActivity.class);
		i.putExtra(AccountActivity.LOGIN, login);
		i.putExtra(AccountActivity.ACC_LOGIN, login);
		startActivity(i);
	}
	public void auth(@Nullable View view) //Перейти ко входу
	{
		Intent i = new Intent(MainActivity.this, AuthActivity.class);
		startActivityForResult(i, AUTH_REQUEST);
	}
	public void info(View view) //Информация
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
			case AUTH_REQUEST: //Получение логина
				login = data.getStringExtra(LOGIN);
				password = data.getStringExtra(PASSWORD);
				isTeacher = data.getBooleanExtra(IS_TEACHER, false);
		}
	}
}
