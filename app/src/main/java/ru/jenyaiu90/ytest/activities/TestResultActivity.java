package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.AnswerEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

//Просмотр результата прохождения теста
public class TestResultActivity extends Activity
{
	//Для намерения
	public static final String LOGIN = "login";
	public static final String TEST_ID = "test_id";
	public static final String TEST_NAME = "test_name";

	protected String login, test_name;
	protected int test_id;

	protected TextView loginTV, testTV, resultTV, resultPercentTV;
	protected LinearLayout resultLL;
	protected ProgressBar resultPB;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_result);

		loginTV = (TextView)findViewById(R.id.loginTV);
		testTV = (TextView)findViewById(R.id.testTV);
		resultTV = (TextView)findViewById(R.id.resultTV);
		resultPercentTV = (TextView)findViewById(R.id.resultPercentTV);
		resultLL = (LinearLayout)findViewById(R.id.resultLL);
		resultPB = (ProgressBar)findViewById(R.id.resultPB);

		login = getIntent().getStringExtra(LOGIN);
		test_id = getIntent().getIntExtra(TEST_ID, 0);
		test_name = getIntent().getStringExtra(TEST_NAME);
		loginTV.setText(login);
		testTV.setText(test_name);
		new LoadResultAsync().execute(login, Integer.toString(test_id));
	}

	//Загрузить результат
	class LoadResultAsync extends AsyncTask<String, String, LoadResultAsync.Result>
	{
		public class Result
		{
			public List<AnswerEntity> answers;
			public List<TaskEntity> tasks;
		}

		@Override
		protected Result doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			TaskService taskService = rf.create(TaskService.class);

			Call<List<AnswerEntity>> ansResp = testService.getAnswers(data[0], Integer.parseInt(data[1]));
			Call<List<TaskEntity>> taskResp = taskService.getTasksOfTest(Integer.parseInt(data[1]));
			Result res = null;
			try
			{
				Response<List<AnswerEntity>> ansResponse = ansResp.execute();
				Response<List<TaskEntity>> taskResponse = taskResp.execute();
				res = new Result();
				res.answers = ansResponse.body();
				res.tasks = taskResponse.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					AnswerEntity r = new AnswerEntity();
					r.setId(-1);
					res = new Result();
					res.answers = new ArrayList<>(1);
					res.answers.add(r);
				}
			}
			return res;
		}

		@Override
		protected void onPostExecute(Result result)
		{
			resultPB.setIndeterminate(false);
			if (result != null)
			{
				if (result.answers != null && result.answers.get(0).getId() == -1)
				{
					Util.errorToast(TestResultActivity.this, ServerAnswerEntity.NO_INTERNET);
				}
				else
				{
					if (result.answers != null && result.tasks != null &&
						result.answers.size() == result.tasks.size())
					{
						int score = 0, mayScore = 0, max = 0;
						boolean isChecked = true;
						for (int i = 0; i < result.answers.size(); i++)
						{
							max += result.tasks.get(i).getCost();
							if (result.answers.get(i).getIsChecked())
							{
								score += result.answers.get(i).getPoints();
								mayScore += result.answers.get(i).getPoints();
							} else
							{
								isChecked = false;
								mayScore += result.tasks.get(i).getCost();
							}
						}
						resultTV.setText(score + " / " + max);
						resultPercentTV.setText("(" + Math.round(((double) score / max) * 100) + " %)");
						resultPB.setProgress((int) Math.round(((double) score / max) * 100));
						if (!isChecked)
						{
							resultPB.setSecondaryProgress((int) Math.round(((double) mayScore / max) * 100));
							TextView notCheckedTV = new TextView(TestResultActivity.this);
							notCheckedTV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
							notCheckedTV.setText(R.string.not_checked);
							resultLL.addView(notCheckedTV);
						}
					}
				}
			}
		}
	}
}
