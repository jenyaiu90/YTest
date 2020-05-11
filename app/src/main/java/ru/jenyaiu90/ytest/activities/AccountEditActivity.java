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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.User;

public class AccountEditActivity extends Activity
{
	public static final String USER = "user"; //Для намерения: пользователь
	public static final int GET_IMAGE_REQUEST = 1; //Код запроса на получение фотографии

	protected User user;
	protected Bitmap image;

	protected ImageView imageIV;
	protected TextView loginTV;
	protected EditText nameET, surnameET, emailET, phoneNumberET, oldPasswordET, newPasswordET;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_edit);

		user = new Gson().fromJson(getIntent().getStringExtra(USER), User.class);

		imageIV = (ImageView)findViewById(R.id.imageIV);
		loginTV = (TextView)findViewById(R.id.loginTV);
		nameET = (EditText)findViewById(R.id.nameET);
		surnameET = (EditText)findViewById(R.id.surnameET);
		emailET = (EditText)findViewById(R.id.emailET);
		phoneNumberET = (EditText)findViewById(R.id.phoneNumberET);
		oldPasswordET = (EditText)findViewById(R.id.oldPasswordET);
		newPasswordET = (EditText)findViewById(R.id.newPasswordET);

		image = user.getImage();
		if (image == null)
		{
			imageIV.setImageDrawable(getResources().getDrawable(R.drawable.account));
		}
		else
		{
			imageIV.setImageBitmap(image);
		}
		loginTV.setText(user.getLogin());
		nameET.setText(user.getName());
		surnameET.setText(user.getSurname());
		emailET.setText(user.getEmail());
		phoneNumberET.setText(user.getPhone_number());
	}

	public void load(View view) //Загрузка изображения
	{
		Intent getImage = new Intent(Intent.ACTION_PICK);
		getImage.setType("image/*");
		startActivityForResult(getImage, GET_IMAGE_REQUEST);
	}

	public void remove(View view) //Удаление изображения
	{
		if (image != null)
		{
			image = null;
			imageIV.setImageDrawable(getResources().getDrawable(R.drawable.account));
		}
	}

	public void save(View view) //Сохранение данных
	{
		if (newPasswordET.getText().toString().isEmpty() ^ oldPasswordET.getText().toString().isEmpty()) //Если введён только один пароль
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
			if (!wasA || !wasD) //Если в адресе не встретилось ни @, ни . после неё
			{
				Toast.makeText(AccountEditActivity.this, R.string.invalid_email, Toast.LENGTH_LONG).show();
				return;
			}
		}
		//Todo: check password
		user.setName(nameET.getText().toString());
		user.setSurname(surnameET.getText().toString());
		user.setEmail(emailET.getText().toString());
		user.setPhone_number(phoneNumberET.getText().toString());
		user.setImage(image);
		//Todo: send to server
		setResult(RESULT_OK);
		finish();
	}

	public void cancel(View view) //Отменить
	{
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case GET_IMAGE_REQUEST: //Получение изображения
				if (resultCode == RESULT_OK)
				{
					try
					{
						Uri imageUri = data.getData();
						InputStream imageStream = getContentResolver().openInputStream(imageUri);
						image = BitmapFactory.decodeStream(imageStream);
						imageIV.setImageBitmap(image);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
		}
	}
}
