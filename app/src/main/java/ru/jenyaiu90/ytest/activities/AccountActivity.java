package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.InfoAdapter;

public class AccountActivity extends Activity
{
	protected LinearLayout accountLL;
	protected ImageView imageIV;
	protected ListView infoLV;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		accountLL = (LinearLayout)findViewById(R.id.accountLL);
		imageIV = (ImageView)findViewById(R.id.imageIV);
		infoLV = (ListView)findViewById(R.id.infoLV);

		imageIV.setImageDrawable(getResources().getDrawable(R.drawable.account));

		String infos[][] = new String[][]
				{{ getResources().getString(R.string.login), "Login" }, { getResources().getString(R.string.name), "Name" }, { "Is teacher", "Is teacher" },
				{ getResources().getString(R.string.email), "E-mail" },  { getResources().getString(R.string.phone_number), "Phone number" }};
		InfoAdapter infoAdapter = new InfoAdapter(AccountActivity.this, infos);
		infoLV.setAdapter(infoAdapter);

		//Todo: if account is yours
		Button editBT = new Button(AccountActivity.this);
		editBT.setText(R.string.edit);
		accountLL.addView(editBT);
	}
}
