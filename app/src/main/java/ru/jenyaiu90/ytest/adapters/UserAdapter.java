package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.activities.AccountActivity;
import ru.jenyaiu90.ytest.entity.UserEntity;

public class UserAdapter extends ArrayAdapter<UserEntity>
{
	protected String login, password;
	protected View[] views;

	public UserAdapter(Context context, UserEntity[] arr, String login, String password)
	{
		super(context, R.layout.list_item_info, arr);
		this.login = login;
		this.password = password;
		views = new View[arr.length];
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		UserEntity user = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_info, null);
		}
		views[position] = convertView;
		((TextView)convertView.findViewById(R.id.hintTV)).setText(user.getLogin());
		((TextView)convertView.findViewById(R.id.dataTV)).setText(user.getSurname() + " " + user.getName());
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int pos;
				for (pos = 0; pos < views.length; pos++)
				{
					if (views[pos] == v)
					{
						break;
					}
				}
				if (pos >= views.length)
				{
					return;
				}
				Intent accountIntent = new Intent(getContext(), AccountActivity.class);
				accountIntent.putExtra(AccountActivity.LOGIN, login);
				accountIntent.putExtra(AccountActivity.ACC_LOGIN, getItem(pos).getLogin());
				getContext().startActivity(accountIntent);
			}
		});
		return convertView;
	}
}
