package ru.jenyaiu90.ytest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

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

	//TMP
	public static final String IS_TEACHER = "is_teacher"; //Для намерения: является ли учителем
	protected Test test;
	//End TMP

	protected String login;
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

		test = Util.generateTest();
	}

	public void tests(@Nullable View view) //Перейти к тестам
	{
		Intent i;
		if (isTeacher)
		{
			i = new Intent(MainActivity.this, TestQListActivity.class);
		}
		else
		{
			i = new Intent(MainActivity.this, TestQActivity.class);
		}
		Util.putTest(test, i);
		startActivity(i);
	}
	public void groups(@Nullable View view) //Перейти к группам
	{
		//TODO: to groups
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
				isTeacher = data.getBooleanExtra(IS_TEACHER, false); //TMP
		}
	}
}
