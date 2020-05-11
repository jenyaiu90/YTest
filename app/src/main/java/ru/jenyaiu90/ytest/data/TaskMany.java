package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

public class TaskMany extends Task
{
	protected LinkedList<String> choice;
	protected LinkedList<Integer> answer;
	protected LinkedList<Integer> inAnswer;

	public TaskMany(String text, @Nullable Bitmap image, int cost, @NonNull LinkedList<String> choice, @NonNull LinkedList<Integer> answer)
	{
		super(text, image, TaskType.MANY, cost);
		this.choice = choice;
		this.answer = answer;
		inAnswer = null;
	}

	public LinkedList<String> getChoice()
	{
		return choice;
	}

	public void inputAnswer(LinkedList<Integer> ans)
	{
		inAnswer = ans;
	}

	public boolean isAnswer()
	{
		return inAnswer != null;
	}

	public LinkedList<Integer> getInAnswer()
	{
		return inAnswer;
	}
}
