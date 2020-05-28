package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.activities.ResultActivity;
import ru.jenyaiu90.ytest.entity.AnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;

public class ResultAdapter extends ArrayAdapter<ResultAdapter.Answers>
{
	public static class Answers
	{
		public TaskEntity task;
		public AnswerEntity answer;
	}

	protected String login, password;
	protected View[] views;

	public ResultAdapter(Context context, Answers[] content, String login, String password)
	{
		super(context, R.layout.list_item_result, content);
		this.login = login;
		this.password = password;
		views = new View[content.length];
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		Answers ans = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_result, null);
		}
		views[position] = convertView;
		((TextView)convertView.findViewById(R.id.pointsTV)).setText(ans.answer.getIsChecked() ? Integer.toString(ans.answer.getPoints()) : "?");
		((TextView)convertView.findViewById(R.id.questionTV)).setText(ans.task.getText());
		String buff = "";
		if (ans.answer.getAnswer() != null)
		{
			for (String i : ans.answer.getAnswer().split("/=@/"))
			{
				buff += i + "\n";
			}
			if (!buff.isEmpty())
			{
				buff = buff.substring(0, buff.length() - 1);
			}
		}
		((TextView)convertView.findViewById(R.id.answerTV)).setText(buff);
		buff = "";
		if (ans.task.getAnswer() != null)
		{
			for (String i : ans.task.getAnswer())
			{
				if (buff.split("\n").length > 3)
				{
					buff += getContext().getResources().getString(R.string.more) + "\n";
					break;
				}
				else
				{
					buff += i + "\n";
				}
			}
			if (!buff.isEmpty())
			{
				buff = buff.substring(0, buff.length() - 1);
			}
		}
		((TextView)convertView.findViewById(R.id.rightTV)).setText(buff);
		if (!ans.answer.getIsChecked())
		{
			convertView.setBackgroundColor(getContext().getColor(R.color.colorNotChecked));
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
					if (pos < views.length && getContext().getClass() == ResultActivity.class)
					{
						((ResultActivity) getContext()).check(getItem(pos).answer, getItem(pos).task);
					}
				}
			});
		}
		return convertView;
	}
}
