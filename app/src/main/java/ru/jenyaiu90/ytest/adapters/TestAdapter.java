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
import ru.jenyaiu90.ytest.activities.TestQActivity;
import ru.jenyaiu90.ytest.activities.TestResultActivity;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.entity.TestEntity;

public class TestAdapter extends ArrayAdapter<TestAdapter.TestSolve>
{
	public static class TestSolve
	{
		public TestEntity test;
		public String author;
		public boolean isSolved;
	}

	protected View[] views;
	protected String login, password;

	public TestAdapter(Context context, TestSolve[] content, String login, String password)
	{
		super(context, R.layout.list_item_test, content);
		views = new View[content.length];
		this.login = login;
		this.password = password;
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		TestSolve test = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_test, null);
		}
		((TextView)convertView.findViewById(R.id.nameTV)).setText(test.test.getName());
		((TextView)convertView.findViewById(R.id.subjectTV)).setText(test.test.getSubject());
		((TextView)convertView.findViewById(R.id.authorTV)).setText(test.author);
		if (test.isSolved)
		{
			((TextView)convertView.findViewById(R.id.nameTV)).setBackgroundColor(convertView.getResources().getColor(R.color.colorSolved));
		}
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
					if (getItem(pos).isSolved)
					{
						Intent testResultIntent = new Intent(getContext(), TestResultActivity.class);
						testResultIntent.putExtra(TestResultActivity.LOGIN, login);
						testResultIntent.putExtra(TestResultActivity.TEST_ID, getItem(pos).test.getId());
						testResultIntent.putExtra(TestResultActivity.TEST_NAME, getItem(pos).test.getName());
						getContext().startActivity(testResultIntent);
					}
					else
					{
						Intent testQIntent = new Intent(getContext(), TestQActivity.class);
						testQIntent.putExtra(TestQActivity.TEST_ID, getItem(pos).test.getId());
						testQIntent.putExtra(TestQActivity.LOGIN, login);
						testQIntent.putExtra(TestQActivity.PASSWORD, password);
						getContext().startActivity(testQIntent);
					}
				}
			}
		});
		return convertView;
	}
}
