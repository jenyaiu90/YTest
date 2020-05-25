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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskEntityComparator;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.data.Util;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.services.TaskService;
import ru.jenyaiu90.ytest.services.TestService;

public class TestQActivity extends Activity
{
	public static final String TEST_ID = "test_id";
	public static final String LOGIN = "login";
	public static final String PASSWORD = "password";

	protected Test test;
	protected String login, password;

	protected LinearLayout counterLL, choiceLL;
	protected TextView taskTV;

	protected int task;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q);

		counterLL = (LinearLayout)findViewById(R.id.counterLL);
		choiceLL = (LinearLayout)findViewById(R.id.choiceLL);
		taskTV = (TextView)findViewById(R.id.taskTV);

		login = getIntent().getStringExtra(LOGIN);
		password = getIntent().getStringExtra(PASSWORD);

		ProgressBar loadPB = new ProgressBar(TestQActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		counterLL.addView(loadPB);
		new LoadTestAsync().execute(getIntent().getIntExtra(TEST_ID, 1));
	}

	protected void draw(int n)
	{
		task = n;
		counterLL.removeAllViews();
		for (int i = 0; i < test.size(); i++) //Добавление номеров заданий в верхнюю часть экрана
		{
			Button taskBT = new Button(TestQActivity.this);
			taskBT.setText(Integer.toString(i + 1));
			taskBT.setWidth(25);
			if (i == n) //Выделить текущее задание
			{
				taskBT.setBackgroundColor(getResources().getColor(R.color.colorCurrentTask));
			}
			else if (test.getTask(i).isAnswer()) //Выделить решённые задания
			{
				taskBT.setBackgroundColor(getResources().getColor(R.color.colorSavedTask));
			}
			taskBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					draw(Integer.parseInt(((Button)v).getText().toString()) - 1);
				}
			});
			counterLL.addView(taskBT);
		}
		Task currentTask = test.getTask(n); //Получить текущее задание
		taskTV.setText(currentTask.getText());
		choiceLL.removeAllViews();
		switch (currentTask.getType())
		{
			case ONE:
			{
				TaskOne cTask = (TaskOne)currentTask;
				RadioGroup choiceRG = new RadioGroup(TestQActivity.this);
				choiceRG.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				for (String choice : cTask.getChoice()) //Добавление вариантов ответа
				{
					RadioButton choiceRB = new RadioButton(TestQActivity.this);
					choiceRB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceRB.setText(choice);
					choiceRG.addView(choiceRB);
				}
				if (cTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					((RadioButton)(choiceRG.getChildAt(cTask.getInAnswer()))).setChecked(true);
				}
				choiceLL.addView(choiceRG);
				break;
			}
			case MANY:
			{
				TaskMany cTask = (TaskMany)currentTask;
				for (String choice : cTask.getChoice()) //Добавление вариантов ответа
				{
					CheckBox choiceCB = new CheckBox(TestQActivity.this);
					choiceCB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceCB.setText(choice);
					choiceLL.addView(choiceCB);
				}
				if (cTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					LinkedList<Integer> answers = cTask.getInAnswer();
					for (Integer i : answers)
					{
						((CheckBox)(choiceLL.getChildAt(i))).setChecked(true);
					}
				}
				break;
			}
			case SHORT:
			{
				EditText choiceET = new EditText(TestQActivity.this);
				choiceET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				if (currentTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					choiceET.setText(((TaskShort)(currentTask)).getInAnswer());
				}
				choiceLL.addView(choiceET);
				break;
			}
			case LONG:
			{
				EditText answerET = new EditText(TestQActivity.this);
				answerET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				answerET.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

				if (currentTask.isAnswer()) //Добавление ответа, если он был дан
				{
					TaskLong cTask = (TaskLong)currentTask;
					answerET.setText(cTask.getInAnswerS());
				}

				choiceLL.addView(answerET);
			}
		}
	}

	public void save(View view) //Сохранение ответа
	{
		switch (test.getTask(task).getType())
		{
			case ONE:
			{
				TaskOne cTask = (TaskOne)test.getTask(task);
				RadioGroup choiceRG = (RadioGroup)choiceLL.getChildAt(0);
				int s = -1;
				for (int i = 0; i < cTask.getChoice().size(); i++)
				{
					if (((RadioButton)(choiceRG.getChildAt(i))).isChecked())
					{
						s = i;
						break;
					}
				}
				if (s == -1) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(s);
				break;
			}
			case MANY:
			{
				TaskMany cTask = (TaskMany)test.getTask(task);
				LinkedList<Integer> answers = new LinkedList<>();
				for (int i = 0; i < cTask.getChoice().size(); i++)
				{
					if (((CheckBox)(choiceLL.getChildAt(i))).isChecked())
					{
						answers.add(i);
					}
				}
				if (answers.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answers);
				break;
			}
			case SHORT:
			{
				TaskShort cTask = (TaskShort)test.getTask(task);
				String answer = ((EditText)(choiceLL.getChildAt(0))).getText().toString();
				if (answer.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answer);
				break;
			}
			case LONG:
			{
				TaskLong cTask = (TaskLong)test.getTask(task);
				String answer = ((EditText)(choiceLL.getChildAt(0))).getText().toString();
				if (answer.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answer);
				break;
			}
		}
		if (task != test.size() - 1) //Переход к следующему вопросу
		{
			next(null);
		}
	}

	public void prev(View view) //Переход к предыдущему вопросу
	{
		if (task != 0)
		{
			draw(task - 1);
		}
	}

	public void next(View view) //Переход к следующему вопросу
	{
		if (task == test.size() - 1)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(TestQActivity.this);
			builder.setMessage(R.string.answer_sure)
					.setIcon(R.drawable.info)
					.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							finishTest();
						}
					})
					.setNegativeButton(R.string.no, null)
					.setCancelable(true);
			AlertDialog alert = builder.create();
			alert.show();
		}
		else
		{
			draw(task + 1);
		}
	}

	protected void finishTest()
	{
		String[] answers = new String[test.getTasks().size()];
		for (int i = 0; i < test.getTasks().size(); i++)
		{
			if (test.getTask(i).isAnswer())
			{
				switch (test.getTask(i).getType())
				{
					case ONE:
						answers[i] = Integer.toString(((TaskOne)test.getTask(i)).getInAnswer());
						break;
					case MANY:
						Integer[] ans = new Integer[((TaskMany)test.getTask(i)).getInAnswer().size()];
						((TaskMany)test.getTask(i)).getInAnswer().toArray(ans);
						answers[i] = ans[0].toString();
						for (int j = 1; j < ans.length; j++)
						{
							answers[i] += "/=@/" + ans[j];
						}
						break;
					case SHORT:
						answers[i] = ((TaskShort)test.getTask(i)).getInAnswer();
						break;
					default:
						answers[i] = ((TaskLong)test.getTask(i)).getInAnswerS();
						break;
				}
			}
			else
			{
				answers[i] = "/@=/";
			}
		}
		counterLL.removeAllViews();
		ProgressBar loadPB = new ProgressBar(TestQActivity.this);
		loadPB.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		loadPB.setIndeterminate(true);
		counterLL.addView(loadPB);
		String[] data = new String[answers.length + 3];
		data[0] = login;
		data[1] = password;
		data[2] = Integer.toString(test.getId());
		for (int i = 0; i < answers.length; i++)
		{
			data[i + 3] = answers[i];
		}
		new SaveTestAsync().execute(data);
	}

	class LoadTestAsync extends AsyncTask<Integer, String, Test>
	{
		@Override
		protected Test doInBackground(Integer... id)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			Call<TestEntity> resp = testService.getTest(id[0]);
			Test result = null;
			try
			{
				Response<TestEntity> response = resp.execute();
				TestEntity res = response.body();

				if (res == null)
				{
					return null;
				}

				TaskService taskService = rf.create(TaskService.class);
				Call<List<TaskEntity>> taskResp = taskService.getTasksOfTest(id[0]);
				Response<List<TaskEntity>> taskResponse = taskResp.execute();
				ArrayList<TaskEntity> tasks = new ArrayList<>(taskResponse.body());
				tasks.sort(new TaskEntityComparator());
				result = new Test(res, tasks);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Test result)
		{
			test = result;
			draw(0);
		}
	}

	class SaveTestAsync extends AsyncTask<String, String, ServerAnswerEntity>
	{
		@Override
		protected ServerAnswerEntity doInBackground(String... data)
		{
			Retrofit rf = new Retrofit.Builder()
					.baseUrl(Util.IP)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			TestService testService = rf.create(TestService.class);
			String[] answers = Arrays.copyOfRange(data, 3, data.length);
			Call<ServerAnswerEntity> resp = testService.setAnswer(answers, Integer.parseInt(data[2]), data[0], data[1]);
			ServerAnswerEntity result = null;
			try
			{
				Response<ServerAnswerEntity> response = resp.execute();
				result = response.body();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ServerAnswerEntity result)
		{
			if (result != null && result.getAnswer().equals("OK"))
			{
				TestQActivity.this.finish();
			}
			else
			{
				draw(0);
				Toast.makeText(TestQActivity.this, "Error", Toast.LENGTH_LONG).show();
			}
		}
	}
}
