package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.ResultAdapter;
import ru.jenyaiu90.ytest.adapters.TestStudentAdapter;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.AnswerEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

public class ResultActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String STUDENT = "student";
	public static final String TEST_ID = "test_id";

	protected String login;
	protected String password;
	protected String student;
	protected int testId;

	protected LinearLayout resultLL;
	protected ListView resultLV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		resultLL = (LinearLayout)findViewById(R.id.resultLL);
		resultLV = (ListView)findViewById(R.id.resultLV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);
		student = getIntent().getStringExtra(STUDENT);
		testId = getIntent().getIntExtra(TEST_ID, 0);

		load();
	}

	public void load()
	{
		resultLV.setAdapter(null);

		ProgressBar loadPB = new ProgressBar(ResultActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		resultLL.addView(loadPB, 0);
		new LoadAnswersAsync().execute(student, Integer.toString(testId));
	}

	public void check(final AnswerEntity answer, final TaskEntity task)
	{
		final AlertDialog.Builder alert = new AlertDialog.Builder(ResultActivity.this);

		alert.setTitle(R.string.check);
		alert.setMessage(answer.getAnswer());

		final EditText pointsET = new EditText(ResultActivity.this);
		pointsET.setHint(R.string.points);
		pointsET.setInputType(InputType.TYPE_CLASS_NUMBER);
		alert.setView(pointsET);

		alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int whichButton)
			{
				if (pointsET.getText().toString().isEmpty() ||
						Integer.parseInt(pointsET.getText().toString()) > task.getCost() ||
						Integer.parseInt(pointsET.getText().toString()) < 0)
				{
					Toast.makeText(ResultActivity.this, getResources().getString(R.string.enter_points) + " " + task.getCost(), Toast.LENGTH_LONG).show();
				}
				else
				{
					ProgressBar loadPB = new ProgressBar(ResultActivity.this);
					loadPB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					loadPB.setIndeterminate(true);
					alert.setView(loadPB);
					new CheckAsync().execute(new Gson().toJson(answer), pointsET.getText().toString(), login, password);
				}
			}
		});
		alert.setNegativeButton(R.string.cancel, null);
		alert.show();
	}

	class CheckAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			TaskService taskService = rf.create(TaskService.class);

			Call<ServerAnswerEntity> testResp = testService.checkAnswer(
					new Gson().fromJson(data[0], AnswerEntity.class),
					Integer.parseInt(data[1]), data[2], data[3]);
			ServerAnswerEntity result = null;
			try
			{
				Response<ServerAnswerEntity> testResponse = testResp.execute();

				result = testResponse.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					result.setAnswer(ServerAnswerEntity.NO_INTERNET);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(ServerAnswerEntity result)
		{
			if (result != null)
			{
				if (result.getAnswer().equals(ServerAnswerEntity.OK))
				{
					load();
				}
				else
				{
					Util.errorToast(ResultActivity.this, result.getAnswer());
				}
			}
		}
	}

	class LoadAnswersAsync extends AsyncTask<String, String, List<ResultAdapter.Answers>>
	{
		@Override
		protected List<ResultAdapter.Answers> doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			TaskService taskService = rf.create(TaskService.class);

			Call<List<AnswerEntity>> testResp = testService.getAnswers(data[0], Integer.parseInt(data[1]));
			Call<List<TaskEntity>> taskResp = taskService.getTasksOfTest(Integer.parseInt(data[1]));
			List<ResultAdapter.Answers> result = null;
			try
			{
				Response<List<AnswerEntity>> testResponse = testResp.execute();
				Response<List<TaskEntity>> taskResponse = taskResp.execute();

				List<AnswerEntity> answers = testResponse.body();
				List<TaskEntity> tasks = taskResponse.body();

				result = new ArrayList<>(tasks.size());

				for (int i = 0; i < answers.size() && i < tasks.size(); i++)
				{
					ResultAdapter.Answers a = new ResultAdapter.Answers();
					a.task = tasks.get(i);
					a.answer = answers.get(i);
					result.add(a);
				}
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					ResultAdapter.Answers r = new ResultAdapter.Answers();
					r.answer = new AnswerEntity();
					r.answer.setId(-1);
					result = new ArrayList<>(1);
					result.add(r);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<ResultAdapter.Answers> result)
		{
			resultLL.removeViewAt(0);
			if (result != null)
			{
				if (!result.isEmpty())
				{
					if (result.get(0).answer != null && result.get(0).answer.getId() == -1)
					{
						Util.errorToast(ResultActivity.this, ServerAnswerEntity.NO_INTERNET);
					}
					else
					{
						ResultAdapter.Answers[] answers = new ResultAdapter.Answers[result.size()];
						result.toArray(answers);
						ResultAdapter adapter = new ResultAdapter(ResultActivity.this, answers, login, password);
						resultLV.setAdapter(adapter);
					}
				}
			}
		}
	}
}
