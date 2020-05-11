package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class TaskLong extends Task
{
	protected String inAnswerS;
	protected LinkedList<Bitmap> inAnswerI;

	public TaskLong(String text, @Nullable Bitmap image, int cost)
	{
		super(text, image, TaskType.LONG, cost);
		inAnswerS = "";
		inAnswerI = new LinkedList<>();
	}

	public void inputAnswer(String ans)
	{
		inAnswerS = ans;
		inAnswerI = null;
	}

	public void inputAnswer(LinkedList<Bitmap> ans)
	{
		inAnswerS = "";
		inAnswerI = ans;
	}

	public boolean isAnswer()
	{
		return !inAnswerS.isEmpty() || !inAnswerI.isEmpty();
	}

	public String getInAnswerS()
	{
		return inAnswerS;
	}

	public LinkedList<Bitmap> getInAnswerI()
	{
		return inAnswerI;
	}
}
