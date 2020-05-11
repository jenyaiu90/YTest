package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

public class TaskOne extends Task
{
	protected LinkedList<String> choice;
	protected int answer;
	protected int inAnswer;
	protected boolean hasAnswer;

	public TaskOne(String text, @Nullable Bitmap image, int cost, @NonNull LinkedList<String> choice, int answer)
	{
		super(text, image, TaskType.ONE, cost);
		this.choice = choice;
		this.answer = answer;
		inAnswer = 0;
		hasAnswer = false;
	}

	public LinkedList<String> getChoice()
	{
		return choice;
	}

	public void inputAnswer(int ans)
	{
		inAnswer = ans;
		hasAnswer = true;
	}

	public boolean isAnswer()
	{
		return hasAnswer;
	}

	public int getInAnswer()
	{
		return inAnswer;
	}
}
