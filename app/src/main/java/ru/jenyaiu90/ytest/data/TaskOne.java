package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public class TaskOne extends Task
{
	protected LinkedList<String> choice;
	protected int answer;
	protected int inAnswer;
	protected boolean hasAnswer;

	public TaskOne(String text, int cost, @NonNull LinkedList<String> choice, int answer)
	{
		super(text, TaskType.ONE, cost);
		this.choice = choice;
		this.answer = answer;
		inAnswer = 0;
		hasAnswer = false;
	}

	public TaskOne(TaskEntity entity)
	{
		super(entity);
	}

	@Override
	protected void _fromEntity(TaskEntity entity)
	{
		choice = new LinkedList<>();
		choice.addAll(Arrays.asList(entity.getChoice()));
		answer = Integer.parseInt(entity.getAnswer()[0]);
	}

	@Override
	protected TaskEntity _toEntity()
	{
		TaskEntity entity = new TaskEntity();
		String[] choiceA = new String[] {};
		choice.toArray(choiceA);
		entity.setChoice(choiceA);
		String[] answerA = new String[] { Integer.toString(answer) };
		entity.setAnswer(answerA);
		return entity;
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

	public int getAnswer()
	{
		return answer;
	}
}
