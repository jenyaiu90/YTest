package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskShort extends Task
{
	protected String answers[];
	protected String inAnswer;

	public TaskShort(String text, @Nullable Bitmap image, int cost, @NonNull String answers[])
	{
		super(text, image, TaskType.SHORT, cost);
		this.answers = answers;
		inAnswer = "";
	}

	public void inputAnswer(String ans)
	{
		inAnswer = ans;
	}

	public boolean isAnswer()
	{
		return !(inAnswer.isEmpty());
	}

	public String getInAnswer()
	{
		return inAnswer;
	}

	public String[] getAnswers()
	{
		return answers;
	}
}
