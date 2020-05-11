package ru.jenyaiu90.ytest.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Test
{
	protected ArrayList<Task> tasks;

	public Test(@NonNull ArrayList<Task> tasks)
	{
		this.tasks = tasks;
	}

	public int size()
	{
		return tasks.size();
	}

	public Task getTask(int i)
	{
		return tasks.get(i);
	}
}
