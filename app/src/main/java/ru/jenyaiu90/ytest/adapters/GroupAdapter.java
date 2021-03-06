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
import ru.jenyaiu90.ytest.entity.GroupEntity;

public class GroupAdapter extends ArrayAdapter<GroupEntity>
{
	protected String login, password;
	protected boolean isTeacher;
	protected View[] views;

	public GroupAdapter(Context context, GroupEntity[] arr, String login, String password, boolean isTeacher)
	{
		super(context, R.layout.list_item_info, arr);
		this.login = login;
		this.password = password;
		this.isTeacher = isTeacher;
		views = new View[arr.length];
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		GroupEntity group = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_info, null);
		}
		views[position] = convertView;
		((TextView)convertView.findViewById(R.id.hintTV)).setText(Integer.toString(group.getId()));
		((TextView)convertView.findViewById(R.id.dataTV)).setText(group.getName());
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
				Intent groupViewIntent = new Intent(getContext(), GroupViewActivity.class);
				groupViewIntent.putExtra(GroupViewActivity.LOGIN, login);
				groupViewIntent.putExtra(GroupViewActivity.PASSWORD, password);
				groupViewIntent.putExtra(GroupViewActivity.IS_TEACHER, isTeacher);
				groupViewIntent.putExtra(GroupViewActivity.GROUP_ID, getItem(pos).getId());
				groupViewIntent.putExtra(GroupViewActivity.GROUP_NAME, getItem(pos).getName());
				getContext().startActivity(groupViewIntent);
			}
		});
		return convertView;
	}
}
