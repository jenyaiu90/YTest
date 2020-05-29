package ru.jenyaiu90.ytest.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;

public class Util
{
	public static final String IP = "http://192.168.1.43:8080"; //IP-адрес сервера

	public static void errorToast(Context context, String answer)
	{
		switch (answer)
		{
			case ServerAnswerEntity.PASSWORD:
				Toast.makeText(context, R.string.error_password, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.NO_USER:
				Toast.makeText(context, R.string.error_no_user, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.NO_GROUP:
				Toast.makeText(context, R.string.error_no_user, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.NO_TEST:
				Toast.makeText(context, R.string.error_no_test, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.NO_ACCESS:
				Toast.makeText(context, R.string.error_no_access, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.USER_ALREADY_EXISTS:
				Toast.makeText(context, R.string.error_user_already_exists, Toast.LENGTH_LONG).show();
				break;
			case ServerAnswerEntity.NO_INTERNET:
				Toast.makeText(context, R.string.no_internet, Toast.LENGTH_LONG).show();
				break;
			default:
				Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show();
				break;
		}
	}
}
