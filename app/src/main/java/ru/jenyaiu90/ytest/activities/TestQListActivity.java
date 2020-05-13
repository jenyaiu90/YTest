package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.QuestionAdapter;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;

public class TestQListActivity extends Activity
{
	public static final int PHOTO_REQUEST = 1; //Код запроса на получение фотографии
	public static final String TASK_ = "task_"; //Для намерения: задание номер ...
	public static final String TASK_TYPE_ = "task_type_"; //Для намерения: тип задания номер ...
	public static final String TASKS = "tasks"; //Для намерения: количество заданий

	protected Test test;

	ListView questionsLV;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q_list);

		questionsLV = (ListView)findViewById(R.id.questionsLV);

		int count = getIntent().getIntExtra(TASKS, 0);
		ArrayList<Task> alist = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			switch ((Task.TaskType)(getIntent().getSerializableExtra(TASK_TYPE_ + i)))
			{
				case ONE:
					alist.add(new Gson().fromJson(getIntent().getStringExtra(TASK_ + i), TaskOne.class));
					break;
				case MANY:
					alist.add(new Gson().fromJson(getIntent().getStringExtra(TASK_ + i), TaskMany.class));
					break;
				case SHORT:
					alist.add(new Gson().fromJson(getIntent().getStringExtra(TASK_ + i), TaskShort.class));
					break;
				case LONG:
					alist.add(new Gson().fromJson(getIntent().getStringExtra(TASK_ + i), TaskLong.class));
					break;
			}
		}
		test = new Test(alist);

		String tasks[][] = new String[test.size()][2];

		for (int i = 0; i < alist.size(); i++)
		{
			String type;
			switch (alist.get(i).getType())
			{
				case ONE:
					type = getResources().getString(R.string.one_q);
					break;
				case MANY:
					type = getResources().getString(R.string.many_q);
					break;
				case SHORT:
					type = getResources().getString(R.string.short_q);
					break;
				case LONG:
					type = getResources().getString(R.string.long_q);
					break;
				default:
					type = "This value is impossible!";
			}
			tasks[i][0] = alist.get(i).getText();
			tasks[i][1] = type;
		}
		QuestionAdapter adapter = new QuestionAdapter(TestQListActivity.this, tasks);
		questionsLV.setAdapter(adapter);
	}
}
