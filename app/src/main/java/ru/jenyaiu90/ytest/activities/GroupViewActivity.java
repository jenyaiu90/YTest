package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.adapters.TestTeacherAdapter;
import ru.jenyaiu90.ytest.adapters.UserAdapter;
import ru.jenyaiu90.ytest.adapters.UserResultAdapter;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.AnswerEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;
import ru.jenyaiu90.ytest.services.GroupService;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

public class GroupViewActivity extends Activity
{
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String IS_TEACHER = "is_teacher";
	public static final String GROUP_ID = "group_id";
	public static final String GROUP_NAME = "group_name";

	protected String login, password, groupName;
	protected boolean isTeacher;
	protected int groupId;

	protected LinearLayout groupLL, buttonsLL;
	protected TextView groupIdTV;
	protected ListView usersLV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_view);

		groupLL = (LinearLayout)findViewById(R.id.groupLL);
		buttonsLL = (LinearLayout)findViewById(R.id.buttonsLL);
		groupIdTV = (TextView)findViewById(R.id.groupIdTV);
		usersLV = (ListView)findViewById(R.id.usersLV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);
		isTeacher = getIntent().getBooleanExtra(IS_TEACHER, false);
		groupId = getIntent().getIntExtra(GROUP_ID, 0);
		groupName = getIntent().getStringExtra(GROUP_NAME);

		groupIdTV.setText(groupName + " (" + groupId + ")");

		if (isTeacher)
		{
			Button setBT = new Button(GroupViewActivity.this);
			setBT.setLayoutParams(new LinearLayout.LayoutParams(
					0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			setBT.setText(R.string.set_test);
			setBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(GroupViewActivity.this);

					alert.setTitle(R.string.set_test_title);
					alert.setMessage(R.string.chose_test_set);

					ListView testsLV = new ListView(GroupViewActivity.this);
					alert.setView(testsLV);

					alert.setNegativeButton(R.string.done, null);
					alert.show();

					new LoadTestsForAsync(testsLV, TestTeacherAdapter.Action.SET_TEST).execute(groupId);
				}
			});

			Button resultBT = new Button(GroupViewActivity.this);
			resultBT.setLayoutParams(new LinearLayout.LayoutParams(
					0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
			resultBT.setText(R.string.results);
			resultBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(GroupViewActivity.this);

					alert.setTitle(R.string.view_results);
					alert.setMessage(R.string.chose_test_result);

					ListView testsLV = new ListView(GroupViewActivity.this);
					alert.setView(testsLV);

					alert.setNegativeButton(R.string.cancel, null);
					alert.show();

					new LoadTestsForAsync(testsLV, TestTeacherAdapter.Action.VIEW_RESULTS).execute(groupId);
				}
			});

			buttonsLL.addView(setBT);
			buttonsLL.addView(resultBT);
		}

		ProgressBar loadPB = new ProgressBar(GroupViewActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		groupLL.addView(loadPB, 2);
		new LoadUsersAsync().execute(groupId);
	}

	public void setTest(int testId)
	{
		new SetTestAsync().execute(Integer.toString(groupId), Integer.toString(testId), login, password);
	}

	class LoadTestsForAsync extends AsyncTask<Integer, String, List<TestEntity>>
	{
		protected ListView testsLV;
		protected TestTeacherAdapter.Action type;

		public LoadTestsForAsync(ListView testsLV, TestTeacherAdapter.Action action)
		{
			super();
			this.testsLV = testsLV;
			type = action;
		}

		@Override
		protected List<TestEntity> doInBackground(Integer... id)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			Call<List<TestEntity>> resp = type == TestTeacherAdapter.Action.SET_TEST ?
					gService.getTestsForSet(id[0]) : gService.getTestsForGroup(id[0]);
			List<TestEntity> result = null;
			try
			{
				Response<List<TestEntity>> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					TestEntity r = new TestEntity();
					r.setId(-1);
					result = new ArrayList<>(1);
					result.add(r);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<TestEntity> result)
		{
			if (result != null)
			{
				if (result.size() == 1 && result.get(0).getId() == -1)
				{
					Util.errorToast(GroupViewActivity.this, ServerAnswerEntity.NO_INTERNET);
				}
				else
				{
					TestEntity[] tests = new TestEntity[result.size()];
					result.toArray(tests);
					TestTeacherAdapter adapter = new TestTeacherAdapter(testsLV.getContext(), tests, login, password, groupId, type);
					testsLV.setAdapter(adapter);
				}
			}
			else
			{
				Util.errorToast(GroupViewActivity.this, ServerAnswerEntity.NO_INTERNET);
			}
		}
	}

	class SetTestAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			Call<ServerAnswerEntity> resp = gService.setTest(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], data[3]);
			ServerAnswerEntity result = null;
			try
			{
				Response<ServerAnswerEntity> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					result = new ServerAnswerEntity(ServerAnswerEntity.NO_INTERNET);
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
					Toast.makeText(GroupViewActivity.this, R.string.test_is_set, Toast.LENGTH_LONG).show();
				}
				else
				{
					Util.errorToast(GroupViewActivity.this, result.getAnswer());
				}
			}
		}
	}

	class LoadUsersAsync extends AsyncTask<Integer, String, List<UserEntity>>
	{
		@Override
		protected List<UserEntity> doInBackground(Integer... id)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			Call<List<UserEntity>> resp = gService.getUsers(id[0]);
			List<UserEntity> result = null;
			try
			{
				Response<List<UserEntity>> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					UserEntity r = new UserEntity();
					r.setId(-1);
					result = new ArrayList<>(1);
					result.add(r);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<UserEntity> result)
		{
			groupLL.removeViewAt(2);
			if (result != null)
			{
				if (result.size() == 1 && result.get(0).getId() == -1)
				{
					Util.errorToast(GroupViewActivity.this, ServerAnswerEntity.NO_INTERNET);
				}
				else
				{
					UserEntity[] users = new UserEntity[result.size()];
					result.toArray(users);
					UserAdapter adapter = new UserAdapter(GroupViewActivity.this, users, login, password);
					usersLV.setAdapter(adapter);
				}
			}
			else
			{
				Util.errorToast(GroupViewActivity.this, ServerAnswerEntity.NO_INTERNET);
			}
		}
	}
}
