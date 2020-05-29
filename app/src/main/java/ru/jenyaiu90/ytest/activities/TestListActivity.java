package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.TestStudentAdapter;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.TestService;

public class TestListActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";

	protected LinearLayout testsLL;
	protected ListView testsLV;

	protected String login, password;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_list);

		testsLL = (LinearLayout)findViewById(R.id.testsLL);
		testsLV = (ListView)findViewById(R.id.testsLV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);

		ProgressBar loadPB = new ProgressBar(TestListActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		testsLL.addView(loadPB, 0);
		new LoadTestsAsync().execute(login);
	}

	class LoadTestsAsync extends AsyncTask<String, String, List<LoadTestsAsync.Result>>
	{
		public class Result
		{
			public TestEntity test;
			public UserEntity user;
			public Boolean solved;
		}

		@Override
		protected List<Result> doInBackground(String... login)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService tService = rf.create(TestService.class);

			Call<List<TestEntity>> resp = tService.getTestsForUser(login[0]);
			List<TestEntity> res = null;
			List<Result> result = null;
			try
			{
				Response<List<TestEntity>> response = resp.execute();
				res = response.body();
				result = new ArrayList<Result>(res.size());

				for (TestEntity test : res)
				{
					Call<UserEntity> author_resp = tService.getAuthorOfTest(test.getId());
					Response<UserEntity> author_response = author_resp.execute();
					UserEntity author = author_response.body();

					Result r = new Result();
					Call<ServerAnswerEntity> solved_resp = tService.getIsSolved(login[0], test.getId());
					Response<ServerAnswerEntity> solved_response = solved_resp.execute();
					ServerAnswerEntity solved = solved_response.body();
					r.solved = solved.getAnswer().equals("Solved");
					r.test = test;
					r.user = author;
					result.add(r);
				}
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					Result r = new Result();
					r.user = new UserEntity();
					r.user.setId(-1);
					result = new ArrayList<>(1);
					result.add(r);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<Result> result)
		{
			testsLL.removeViewAt(0);
			if (result != null)
			{
				if (!result.isEmpty())
				{
					if (result.get(0).user != null && result.get(0).user.getId() == -1)
					{
						Util.errorToast(TestListActivity.this, ServerAnswerEntity.NO_INTERNET);
					}
					else
					{
						TestStudentAdapter.TestSolve[] tests = new TestStudentAdapter.TestSolve[result.size()];
						for (int i = 0; i < result.size(); i++)
						{
							tests[i] = new TestStudentAdapter.TestSolve();
							tests[i].test = result.get(i).test;
							tests[i].author = result.get(i).user.getLogin();
							tests[i].isSolved = result.get(i).solved;
						}
						TestStudentAdapter adapter = new TestStudentAdapter(TestListActivity.this, tests, login, password);
						testsLV.setAdapter(adapter);
					}
				}
			}
		}
	}
}
