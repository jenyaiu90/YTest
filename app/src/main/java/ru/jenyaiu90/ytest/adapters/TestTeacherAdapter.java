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
import ru.jenyaiu90.ytest.activities.GroupViewActivity;
import ru.jenyaiu90.ytest.activities.UserResultActivity;
import ru.jenyaiu90.ytest.entity.TestEntity;

public class TestTeacherAdapter extends ArrayAdapter<TestEntity>
{
	public enum Action
	{
		SET_TEST,
		VIEW_RESULTS
	}

	protected String login, password;
	protected int groupId;
	protected View[] views;
	protected Action action;

	public TestTeacherAdapter(Context context, TestEntity[] content, String login, String password, int groupId, Action action)
	{
		super(context, R.layout.list_item_info, content);
		views = new View[content.length];
		this.login = login;
		this.password = password;
		this.groupId = groupId;
		this.action = action;
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		TestEntity test = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_info, null);
		}
		((TextView)convertView.findViewById(R.id.dataTV)).setText(test.getName());
		((TextView)convertView.findViewById(R.id.hintTV)).setText(test.getSubject());
		views[position] = convertView;
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
					switch (action)
					{
						case SET_TEST:
							if (getContext().getClass() == GroupViewActivity.class)
							{
								((GroupViewActivity)getContext()).setTest(getItem(pos).getId());
							}
							break;
						case VIEW_RESULTS:
							Intent userResultIntent = new Intent(getContext(), UserResultActivity.class);
							userResultIntent.putExtra(UserResultActivity.GROUP_ID, groupId);
							userResultIntent.putExtra(UserResultActivity.TEST_ID, getItem(pos).getId());
							userResultIntent.putExtra(UserResultActivity.LOGIN, login);
							userResultIntent.putExtra(UserResultActivity.PASSWORD, password);
							getContext().startActivity(userResultIntent);
							break;
					}
				}
			}
		});
		return convertView;
	}
}
