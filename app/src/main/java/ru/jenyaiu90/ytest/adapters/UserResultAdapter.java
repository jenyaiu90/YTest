package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.activities.ResultActivity;
import ru.jenyaiu90.ytest.entity.UserEntity;

public class UserResultAdapter extends ArrayAdapter<UserResultAdapter.UserResult>
{
	protected String login, password;
	protected int testId;
	protected View[] views;

	public static class UserResult
	{
		public UserEntity user;
		public int points;
		public int may;
		public int max;
	}

	public UserResultAdapter(Context context, UserResult[] content, String login, String password, int testId)
	{
		super(context, R.layout.list_item_user_result, content);
		this.login = login;
		this.password = password;
		this.testId = testId;
		views = new View[content.length];
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		UserResult result = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_user_result, null);
		}
		views[position] = convertView;
		((TextView)convertView.findViewById(R.id.loginTV)).setText(result.user.getLogin());
		((TextView)convertView.findViewById(R.id.nameTV)).setText(result.user.getSurname() + " " + result.user.getName());
		((ProgressBar)convertView.findViewById(R.id.resultPB)).setProgress((int)Math.round(((double)result.points / result.max) * 100));
		((ProgressBar)convertView.findViewById(R.id.resultPB)).setSecondaryProgress((int)Math.round(((double)result.may / result.max) * 100));
		((TextView)convertView.findViewById(R.id.resultTV)).setText(result.points + "/" + result.max);
		((TextView)convertView.findViewById(R.id.resultPercentTV)).setText((int)Math.round(((double)result.points / result.max) * 100) + " %");
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				int pos;
				for (pos = 0; pos < views.length; pos++)
				{
					if (v == views[pos])
					{
						break;
					}
				}
				if (pos < views.length)
				{
					Intent resultIntent = new Intent(getContext(), ResultActivity.class);
					resultIntent.putExtra(ResultActivity.LOGIN, login);
					resultIntent.putExtra(ResultActivity.PASSWORD, password);
					resultIntent.putExtra(ResultActivity.STUDENT, getItem(pos).user.getLogin());
					resultIntent.putExtra(ResultActivity.TEST_ID, testId);
					getContext().startActivity(resultIntent);
				}
			}
		});
		return convertView;
	}
}
