package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.ArrayList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.activities.TestQEditActivity;
import ru.jenyaiu90.ytest.activities.TestQListActivity;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;

public class QuestionAdapter extends ArrayAdapter<String[]>
{
	ArrayList<Task> tasks;
	View views[];
	public QuestionAdapter(Context context, String[][] arr, ArrayList<Task> tasks)
	{
		super(context, R.layout.list_item_question, arr);
		this.tasks = tasks;
		views = new View[arr.length];
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		String[] str = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_question, null);
		}
		((TextView)convertView.findViewById(R.id.numTV)).setText(Integer.toString(position + 1));
		((TextView)convertView.findViewById(R.id.nameTV)).setText(str[0]);
		((TextView)convertView.findViewById(R.id.typeTV)).setText(str[1]);
		convertView.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				Intent i = new Intent(getContext(), TestQEditActivity.class);
				int pos;
				for (pos = 0; pos < views.length; pos++)
				{
					if (view == views[pos])
					{
						break;
					}
				}
				if (pos < views.length)
				{
					i.putExtra(TestQEditActivity.TASK_TYPE, tasks.get(pos).getType());
					switch (tasks.get(pos).getType())
					{
						case ONE:
							i.putExtra(TestQEditActivity.TASK, new Gson().toJson((TaskOne)tasks.get(pos)));
							break;
						case MANY:
							i.putExtra(TestQEditActivity.TASK, new Gson().toJson((TaskMany)tasks.get(pos)));
							break;
						case SHORT:
							i.putExtra(TestQEditActivity.TASK, new Gson().toJson((TaskShort)tasks.get(pos)));
							break;
						case LONG:
							i.putExtra(TestQEditActivity.TASK, new Gson().toJson((TaskLong)tasks.get(pos)));
							break;
					}
					((TestQListActivity)getContext()).sActivityForResult(i, pos);
				}
			}
		});
		views[position] = convertView;
		return convertView;
	}
}
