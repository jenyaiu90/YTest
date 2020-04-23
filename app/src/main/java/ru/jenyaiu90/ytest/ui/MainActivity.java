package ru.jenyaiu90.ytest.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import ru.jenyaiu90.ytest.R;

public class MainActivity extends AppCompatActivity
{
	protected String login;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Intent i = new Intent(MainActivity.this, AuthActivity.class);
		startActivityForResult(i, 1);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode)
		{
			case 1:
				login = data.getStringExtra("login");
		}
	}
}
