package ru.jenyaiu90.ytest.data;

import android.content.Intent;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;

public class Util
{
	public static final String TASK_ = "task_"; //Для намерения: задание номер ...
	public static final String TASK_TYPE_ = "task_type_"; //Для намерения: тип задания номер ...
	public static final String TASKS = "tasks"; //Для намерения: количество заданий

	public static Test generateTest() //Временный метод, генерирующий тест
	{
		LinkedList<String> choiceOne = new LinkedList<>();
		choiceOne.add("1");
		choiceOne.add("2");
		choiceOne.add("4");
		choiceOne.add("8");
		TaskOne taskOne = new TaskOne("Сколько будет 2 + 2?", null, 1, choiceOne, 3);

		LinkedList<String> choiceMany = new LinkedList<>();
		choiceMany.add("А");
		choiceMany.add("Г");
		choiceMany.add("D");
		choiceMany.add("E");
		choiceMany.add("Ы");
		LinkedList<Integer> ansMany = new LinkedList<>();
		ansMany.add(0);
		ansMany.add(3);
		TaskMany taskMany = new TaskMany("Какие буквы есть и в русском, и в английском алфавитах?",
				null, 1, choiceMany, ansMany);

		TaskShort taskShort = new TaskShort("Какая часть речи отвечает на вопрос \"Какой\"?",
				null, 1, new String[] {"прилагательное", "Прилагательное",
				"имя прилагательное", "Имя прилагательное", "причастие", "Причастие"});

		TaskLong taskLong = new TaskLong("Назовите любой город Германии.",
				null, 2);

		ArrayList<Task> t = new ArrayList<>(4);
		t.add(taskOne);
		t.add(taskMany);
		t.add(taskShort);
		t.add(taskLong);

		return new Test(t);
	}
	public static void putTest(Test test, Intent intent)
	{
		intent.putExtra(TASKS, test.size());
		for (int j = 0; j < test.size(); j++)
		{
			Task.TaskType type = test.getTask(j).getType();
			intent.putExtra(TASK_TYPE_ + j, type);
			switch (type)
			{
				case ONE:
					intent.putExtra(TASK_ + j, new Gson().toJson((TaskOne)(test.getTask(j))));
					break;
				case MANY:
					intent.putExtra(TASK_ + j, new Gson().toJson((TaskMany)(test.getTask(j))));
					break;
				case SHORT:
					intent.putExtra(TASK_ + j, new Gson().toJson((TaskShort)(test.getTask(j))));
					break;
				case LONG:
					intent.putExtra(TASK_ + j, new Gson().toJson((TaskLong)(test.getTask(j))));
					break;
			}
		}
	}
	public static Test getTestAsExtra(Intent intent)
	{
		int count = intent.getIntExtra(TASKS, 0);
		ArrayList<Task> alist = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
		{
			switch ((Task.TaskType)(intent.getSerializableExtra(TASK_TYPE_ + i)))
			{
				case ONE:
					alist.add(new Gson().fromJson(intent.getStringExtra(TASK_ + i), TaskOne.class));
					break;
				case MANY:
					alist.add(new Gson().fromJson(intent.getStringExtra(TASK_ + i), TaskMany.class));
					break;
				case SHORT:
					alist.add(new Gson().fromJson(intent.getStringExtra(TASK_ + i), TaskShort.class));
					break;
				case LONG:
					alist.add(new Gson().fromJson(intent.getStringExtra(TASK_ + i), TaskLong.class));
					break;
			}
		}
		return new Test(alist);
	}
}
