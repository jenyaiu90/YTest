package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.Serializable;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.InfoAdapter;

public class AccountActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String ACC_LOGIN = "accountLogin";
	public static final int EDIT_REQUEST = 1;

	protected LinearLayout accountLL;
	protected ImageView imageIV;
	protected ListView infoLV;

	protected String login, accountLogin, name, surname, email, phoneNumber;
	protected Drawable image;
	protected boolean isTeacher;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		login = getIntent().getStringExtra(LOGIN);
		accountLogin = getIntent().getStringExtra(ACC_LOGIN);
		isTeacher = false;

		accountLL = (LinearLayout)findViewById(R.id.accountLL);
		imageIV = (ImageView)findViewById(R.id.imageIV);
		infoLV = (ListView)findViewById(R.id.infoLV);

		load();

		if (login.equals(accountLogin))
		{
			Button editBT = new Button(AccountActivity.this);
			editBT.setText(R.string.edit);
			editBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent i = new Intent(AccountActivity.this, AccountEditActivity.class);
					i.putExtra(AccountEditActivity.LOGIN, login);
					i.putExtra(AccountEditActivity.NAME, name);
					i.putExtra(AccountEditActivity.SURNAME, surname);
					i.putExtra(AccountEditActivity.EMAIL, email);
					i.putExtra(AccountEditActivity.PHONE_NUMBER, phoneNumber);
					startActivityForResult(i, EDIT_REQUEST);
				}
			});
			accountLL.addView(editBT);
		}
	}

	protected void load()
	{
		name = "Name";
		surname = "Surname";
		email = "Email@example.com";
		phoneNumber = "+70000000000";
		image = getResources().getDrawable(R.drawable.account);

		imageIV.setImageDrawable(image == null ? getResources().getDrawable(R.drawable.account) : image);

		String infos[][] = new String[][]
				{{ getResources().getString(R.string.login), accountLogin },
						{ getResources().getString(R.string.name), name + " " + surname },
						{ "Is teacher", getResources().getString(isTeacher ? R.string.teacher : R.string.student) },
						{ getResources().getString(R.string.email), email },
						{ getResources().getString(R.string.phone_number), phoneNumber }};
		InfoAdapter infoAdapter = new InfoAdapter(AccountActivity.this, infos);
		infoLV.setAdapter(infoAdapter);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case EDIT_REQUEST:
				if (resultCode == RESULT_OK)
				{
					load();
				}
		}
	}
}
