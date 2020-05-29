package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.UserResultAdapter;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.AnswerEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.GroupService;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

public class UserResultActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String GROUP_ID = "group_id";
	public static final String TEST_ID = "test_id";

	protected String login;
	protected String password;
	protected int groupId;
	protected int testId;

	LinearLayout resultsLL;
	ListView resultsLV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_result);

		resultsLL = (LinearLayout)findViewById(R.id.resultsLL);
		resultsLV = (ListView)findViewById(R.id.resultsLV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);
		groupId = getIntent().getIntExtra(GROUP_ID, 0);
		testId = getIntent().getIntExtra(TEST_ID, 0);

		ProgressBar loadPB = new ProgressBar(UserResultActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		resultsLL.addView(loadPB, 0);
		new LoadTestResultsAsync().execute(testId, groupId);
	}

	class LoadTestResultsAsync extends AsyncTask<Integer, String, List<UserResultAdapter.UserResult>>
	{
		@Override
		protected List<UserResultAdapter.UserResult> doInBackground(Integer... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			TaskService taskService = rf.create(TaskService.class);
			GroupService groupService = rf.create(GroupService.class);
			Call<List<TaskEntity>> taskResp = taskService.getTasksOfTest(data[0]);
			Call<List<UserEntity>> groupResp = groupService.getUsers(data[1]);

			List<UserResultAdapter.UserResult> userResult = null;
			try
			{
				Response<List<TaskEntity>> taskResponse = taskResp.execute();
				Response<List<UserEntity>> groupResponse = groupResp.execute();

				List<TaskEntity> tasks = taskResponse.body();
				List<UserEntity> users = groupResponse.body();

				userResult = new ArrayList<>(users.size());

				for (UserEntity i : users)
				{
					Call<List<AnswerEntity>> testResp = testService.getAnswers(i.getLogin(), data[0]);
					Response<List<AnswerEntity>> testResponse = testResp.execute();
					List<AnswerEntity> answers = testResponse.body();

					UserResultAdapter.UserResult uResult = new UserResultAdapter.UserResult();
					uResult.user = i;
					uResult.points = 0;
					uResult.may = 0;
					uResult.max = 0;
					boolean isChecked = true;
					for (int j = 0; j < answers.size() && j < tasks.size(); j++)
					{
						uResult.max += tasks.get(j).getCost();
						if (answers.get(j).getIsChecked())
						{
							uResult.points += answers.get(j).getPoints();
							uResult.may += answers.get(j).getPoints();
						}
						else
						{
							uResult.may += tasks.get(j).getCost();
						}
					}
					userResult.add(uResult);
				}
			}
			catch (IOException e)
			{

			}
			return userResult;
		}

		@Override
		protected void onPostExecute(List<UserResultAdapter.UserResult> result)
		{
			resultsLL.removeViewAt(0);
			if (result == null)
			{
				Util.errorToast(UserResultActivity.this, ServerAnswerEntity.NO_INTERNET);
			}
			else
			{
				UserResultAdapter.UserResult[] uResult = new UserResultAdapter.UserResult[result.size()];
				result.toArray(uResult);
				UserResultAdapter adapter = new UserResultAdapter(UserResultActivity.this, uResult, login, password, testId);
				resultsLV.setAdapter(adapter);
			}
		}
	}
}
