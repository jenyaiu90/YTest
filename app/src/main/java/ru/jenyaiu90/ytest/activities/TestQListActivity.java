package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.QuestionAdapter;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskEntityComparator;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

public class TestQListActivity extends Activity
{
	public static final int RESULT_DELETE = -2;
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String TEST_NAME = "test_name";
	public static final String SUBJECT = "subject";

	protected Test test;
	protected ArrayList<Task> alist;

	protected String login, password, test_name, subject;

	ListView questionsLV;
	LinearLayout testLL;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q_list);

		questionsLV = (ListView)findViewById(R.id.questionsLV);
		testLL = (LinearLayout)findViewById(R.id.testLL);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);

		test_name = getIntent().getStringExtra(TEST_NAME);
		subject = getIntent().getStringExtra(SUBJECT);
		alist = new ArrayList<>();
		test = new Test(test_name, subject, alist);
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
		TaskOne task = new TaskOne(getResources().getString(R.string.new_task), 1, list, 1);
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

	public void save(View view)
	{
		if (alist.isEmpty())
		{
			finish();
		}
		else
		{
			ProgressBar loadPB = new ProgressBar(TestQListActivity.this);
			loadPB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			loadPB.setIndeterminate(true);
			testLL.addView(loadPB, 2);

			Data data = new Data();
			data.login = login;
			data.password = password;
			data.test = test;

			new CreateTestAsync().execute(data);
		}
	}

	class CreateTestAsync extends AsyncTask<Data, String, TestEntity>
	{
		@Override
		protected TestEntity doInBackground(Data... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);

			List<TaskEntity> tasks = data[0].test.tasksToEntity();
			TestEntity test = data[0].test.toEntity();

			Call<TestEntity> resp = testService.createTest(test.getName(), test.getSubject(), tasks, data[0].login, data[0].password);
			TestEntity result = null;
			try
			{
				Response<TestEntity> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(TestEntity result)
		{
			testLL.removeViewAt(2);
			if (result != null)
			{
				TestQListActivity.this.finish();
			}
		}
	}

	class Data
	{
		public Test test;
		public String login;
		public String password;
	}
}
