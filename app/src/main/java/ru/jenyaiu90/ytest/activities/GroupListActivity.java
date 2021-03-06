package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import ru.jenyaiu90.ytest.adapters.GroupAdapter;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.GroupEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.services.GroupService;

//Список групп
public class GroupListActivity extends Activity
{
	//Для намерения
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";
	public static final String IS_TEACHER = "isTeacher";

	protected String login, password;
	protected boolean isTeacher;

	protected LinearLayout groupsLL;
	protected Button newBT;
	protected ListView groupsLV;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_list);

		groupsLL = (LinearLayout)findViewById(R.id.groupsLL);
		newBT = (Button)findViewById(R.id.newBT);
		groupsLV = (ListView)findViewById(R.id.groupsLV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);
		isTeacher = getIntent().getBooleanExtra(IS_TEACHER, false);

		if (isTeacher) //Добавление кнопки создания группы для учителя
		{
			newBT.setText(R.string.create_group);
			newBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(GroupListActivity.this);

					alert.setTitle(R.string.create_group);
					alert.setMessage(R.string.enter_group_name);

					final EditText nameET = new EditText(GroupListActivity.this);
					nameET.setHint(R.string.group_name);
					alert.setView(nameET);

					alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int whichButton)
						{
							if (nameET.getText().toString().isEmpty())
							{
								Toast.makeText(GroupListActivity.this, R.string.no_group_name, Toast.LENGTH_LONG).show();
							}
							else
							{
								ProgressBar loadPB = new ProgressBar(GroupListActivity.this);
								loadPB.setLayoutParams(new LinearLayout.LayoutParams(
										ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
								loadPB.setIndeterminate(true);
								groupsLL.addView(loadPB, 1);
								new CreateGroupAsync().execute(nameET.getText().toString(), login, password);
							}
						}
					});
					alert.setNegativeButton(R.string.cancel, null);
					alert.show();
				}
			});
		}
		else //Добавление кнопки присоединения к группе для ученика
		{
			newBT.setText(R.string.join_group);
			newBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(GroupListActivity.this);

					alert.setTitle(R.string.join_group);
					alert.setMessage(R.string.enter_group_id);

					final EditText idET = new EditText(GroupListActivity.this);
					idET.setHint(R.string.group_id);
					idET.setInputType(InputType.TYPE_CLASS_NUMBER);
					alert.setView(idET);

					alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int whichButton)
						{
							if (idET.getText().toString().isEmpty())
							{
								Toast.makeText(GroupListActivity.this, R.string.no_group_id, Toast.LENGTH_LONG).show();
							}
							else
							{
								ProgressBar loadPB = new ProgressBar(GroupListActivity.this);
								loadPB.setLayoutParams(new LinearLayout.LayoutParams(
										ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
								loadPB.setIndeterminate(true);
								groupsLL.addView(loadPB, 1);
								new JoinGroupAsync().execute(idET.getText().toString(), login, password);
							}
						}
					});
					alert.setNegativeButton(R.string.cancel, null);
					alert.show();
				}
			});
		}

		ProgressBar loadPB = new ProgressBar(GroupListActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		groupsLL.addView(loadPB, 1);
		new LoadGroupsAsync().execute(login, isTeacher ? "true" : "false");
	}

	//Присоединение к группе
	class JoinGroupAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			Call<ServerAnswerEntity> resp = gService.joinGroup(Integer.parseInt(data[0]), data[1], data[2]);
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
			groupsLL.removeViewAt(1);
			if (result != null)
			{
				if (result.getAnswer().equals(ServerAnswerEntity.OK))
				{
					ProgressBar loadPB = new ProgressBar(GroupListActivity.this);
					loadPB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					loadPB.setIndeterminate(true);
					groupsLL.addView(loadPB, 1);
					new LoadGroupsAsync().execute(login, isTeacher ? "true" : "false");
				}
				else
				{
					Util.errorToast(GroupListActivity.this, result.getAnswer());
				}
			}
		}
	}

	//Создание группы
	class CreateGroupAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			GroupEntity entity = new GroupEntity();
			entity.setName(data[0]);
			Call<ServerAnswerEntity> resp = gService.createGroup(entity, data[1], data[2]);
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
			groupsLL.removeViewAt(1);
			if (result != null)
			{
				if (result.getAnswer().equals(ServerAnswerEntity.OK))
				{
					ProgressBar loadPB = new ProgressBar(GroupListActivity.this);
					loadPB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					loadPB.setIndeterminate(true);
					groupsLL.addView(loadPB, 1);
					new LoadGroupsAsync().execute(login, isTeacher ? "true" : "false");
				}
				else
				{
					Util.errorToast(GroupListActivity.this, result.getAnswer());
				}
			}
		}
	}

	//Загрузка списка групп
	class LoadGroupsAsync extends AsyncTask<String, String, List<GroupEntity>>
	{
		@Override
		protected List<GroupEntity> doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			GroupService gService = rf.create(GroupService.class);
			//Загружаются разные списки в зависимости от того, является ли пользователь учителем
			Call<List<GroupEntity>> resp = data[1].equals("true") ? gService.getGroupsOf(data[0]) : gService.getGroupsWith(data[0]);
			List<GroupEntity> result = null;
			try
			{
				Response<List<GroupEntity>> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				if (e.getClass() == SocketTimeoutException.class)
				{
					GroupEntity r = new GroupEntity();
					r.setId(-1);
					result = new ArrayList<>(1);
					result.add(r);
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(List<GroupEntity> result)
		{
			groupsLL.removeViewAt(1);
			if (result != null && !result.isEmpty())
			{
				if (result.get(0).getId() == -1)
				{
					Util.errorToast(GroupListActivity.this, ServerAnswerEntity.NO_INTERNET);
				}
				else
				{
					GroupEntity[] arr = new GroupEntity[result.size()];
					result.toArray(arr);
					GroupAdapter adapter = new GroupAdapter(GroupListActivity.this, arr, login, password, isTeacher);
					groupsLV.setAdapter(adapter);
				}
			}
		}
	}
}
