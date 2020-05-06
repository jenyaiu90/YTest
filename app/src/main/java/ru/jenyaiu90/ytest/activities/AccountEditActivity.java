package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

import ru.jenyaiu90.ytest.R;

public class AccountEditActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String EMAIL = "email";
	public static final String PHONE_NUMBER = "phone_number";
	public static final int GET_IMAGE_REQUEST = 1;

	protected String login, name, surname, email, phoneNumber;
	protected Drawable image;

	protected ImageView imageIV;
	protected TextView loginTV;
	protected EditText nameET, surnameET, emailET, phoneNumberET, oldPasswordET, newPasswordET;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_edit);

		login = getIntent().getStringExtra(LOGIN);
		name = getIntent().getStringExtra(NAME);
		surname = getIntent().getStringExtra(SURNAME);
		email = getIntent().getStringExtra(EMAIL);
		phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
		image = getResources().getDrawable(R.drawable.account);

		imageIV = (ImageView)findViewById(R.id.imageIV);
		loginTV = (TextView)findViewById(R.id.loginTV);
		nameET = (EditText)findViewById(R.id.nameET);
		surnameET = (EditText)findViewById(R.id.surnameET);
		emailET = (EditText)findViewById(R.id.emailET);
		phoneNumberET = (EditText)findViewById(R.id.phoneNumberET);
		oldPasswordET = (EditText)findViewById(R.id.oldPasswordET);
		newPasswordET = (EditText)findViewById(R.id.newPasswordET);

		imageIV.setImageDrawable(image == null ? getResources().getDrawable(R.drawable.account) : image);
		loginTV.setText(login);
		nameET.setText(name);
		surnameET.setText(surname);
		emailET.setText(email);
		phoneNumberET.setText(phoneNumber);
	}

	public void load(View view)
	{
		Intent getImage = new Intent(Intent.ACTION_PICK);
		getImage.setType("image/*");
		startActivityForResult(getImage, GET_IMAGE_REQUEST);
	}

	public void remove(View view)
	{
		if (image != null)
		{
			image = null;
			imageIV.setImageDrawable(getResources().getDrawable(R.drawable.account));
		}
	}

	public void save(View view)
	{
		if (newPasswordET.getText().toString().isEmpty() ^ oldPasswordET.getText().toString().isEmpty())
		{
			Toast.makeText(AccountEditActivity.this, R.string.no_password, Toast.LENGTH_LONG).show();
			return;
		}
		//Todo: Check password
		//Todo: Send to server
		setResult(RESULT_OK);
		finish();
	}

	public void cancel(View view)
	{
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case GET_IMAGE_REQUEST:
				if (resultCode == RESULT_OK)
				{
					try
					{
						Uri imageUri = data.getData();
						InputStream imageStream = getContentResolver().openInputStream(imageUri);
						Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
						image = new BitmapDrawable(getResources(), selectedImage);
						imageIV.setImageDrawable(image);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
		}
	}
}
