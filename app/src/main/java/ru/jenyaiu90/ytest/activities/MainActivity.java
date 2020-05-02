package ru.jenyaiu90.ytest.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import ru.jenyaiu90.ytest.R;

public class MainActivity extends AppCompatActivity
{
	public static final int AUTH_REQUEST = 1;

	protected String login;

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

	public void tests(@Nullable View view)
	{
		//TODO: to tests
	}
	public void groups(@Nullable View view)
	{
		//TODO: to groups
	}
	public void account(@Nullable View view)
	{
		Intent i = new Intent(MainActivity.this, AccountActivity.class);
		startActivity(i);
	}
	public void auth(@Nullable View view)
	{
		Intent i = new Intent(MainActivity.this, AuthActivity.class);
		startActivityForResult(i, AUTH_REQUEST);
	}
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
			case AUTH_REQUEST:
				login = data.getStringExtra("login");
		}
	}
}
