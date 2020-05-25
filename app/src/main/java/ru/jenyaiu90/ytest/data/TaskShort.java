package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public class TaskShort extends Task
{
	protected String answers[];
	protected String inAnswer;

	public TaskShort(String text, int cost, @NonNull String answers[])
	{
		super(text, TaskType.SHORT, cost);
		this.answers = answers;
		inAnswer = "";
	}

	public TaskShort(TaskEntity entity)
	{
		super(entity);
		inAnswer = "";
	}

	@Override
	protected void _fromEntity(TaskEntity entity)
	{
		answers = entity.getAnswer();
	}

	@Override
	protected TaskEntity _toEntity()
	{
		TaskEntity entity = new TaskEntity();
		entity.setAnswer(answers);
		return entity;
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
