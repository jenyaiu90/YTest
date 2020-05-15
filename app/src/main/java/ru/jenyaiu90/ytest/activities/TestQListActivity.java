package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.QuestionAdapter;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.data.Util;

public class TestQListActivity extends Activity
{
	public static final int RESULT_DELETE = -2;

	protected Test test;
	protected ArrayList<Task> alist;

	ListView questionsLV;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q_list);

		questionsLV = (ListView)findViewById(R.id.questionsLV);

		test = Util.getTestAsExtra(getIntent());
		alist = test.getTasks();

		loadTest();
	}

	protected void loadTest()
	{
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
		QuestionAdapter adapter = new QuestionAdapter(TestQListActivity.this, tasks, alist);
		questionsLV.setAdapter(adapter);
	}

	public void create(View view)
	{
		LinkedList<String> list = new LinkedList<>();
		list.add("1");
		list.add("2");
		TaskOne task = new TaskOne(getResources().getString(R.string.new_task), null, 1, list, 1);
		test.addTask(task);
		Intent i = new Intent(TestQListActivity.this, TestQEditActivity.class);
		i.putExtra(TestQEditActivity.TASK_TYPE, Task.TaskType.ONE);
		i.putExtra(TestQEditActivity.TASK, new Gson().toJson(task));
		startActivityForResult(i, test.getTasks().size() - 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			switch ((Task.TaskType)data.getSerializableExtra(TestQEditActivity.TASK_TYPE))
			{
				case ONE:
					test.setTask(requestCode, new Gson().fromJson(data.getStringExtra(TestQEditActivity.TASK), TaskOne.class));
					break;
				case MANY:
					test.setTask(requestCode, new Gson().fromJson(data.getStringExtra(TestQEditActivity.TASK), TaskMany.class));
					break;
				case SHORT:
					test.setTask(requestCode, new Gson().fromJson(data.getStringExtra(TestQEditActivity.TASK), TaskShort.class));
					break;
				case LONG:
					test.setTask(requestCode, new Gson().fromJson(data.getStringExtra(TestQEditActivity.TASK), TaskLong.class));
					break;
			}
			loadTest();
		}
		else if (resultCode == RESULT_DELETE)
		{
			test.deleteTask(requestCode);
			loadTest();
		}
	}

	public void sActivityForResult(Intent intent, int requestCode)
	{
		startActivityForResult(intent, requestCode);
	}
}
