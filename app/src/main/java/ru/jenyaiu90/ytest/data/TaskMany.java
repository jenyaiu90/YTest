package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public class TaskMany extends Task
{
	protected LinkedList<String> choice;
	protected LinkedList<Integer> answer;
	protected LinkedList<Integer> inAnswer;

	public TaskMany(String text, int cost, @NonNull LinkedList<String> choice, @NonNull LinkedList<Integer> answer)
	{
		super(text, TaskType.MANY, cost);
		this.choice = choice;
		this.answer = answer;
		inAnswer = null;
	}

	public TaskMany(TaskEntity entity)
	{
		super(entity);
	}

	@Override
	protected void _fromEntity(TaskEntity entity)
	{
		choice = new LinkedList<>();
		choice.addAll(Arrays.asList(entity.getChoice()));
		answer = new LinkedList<>();
		for (String i : entity.getAnswer())
		{
			answer.add(Integer.parseInt(i));
		}
	}

	@Override
	protected TaskEntity _toEntity()
	{
		TaskEntity entity = new TaskEntity();
		String[] choiceA = new String[choice.size()];
		choice.toArray(choiceA);
		entity.setChoice(choiceA);
		String[] answerA = new String[answer.size()];
		Integer[] answerIA = new Integer[answer.size()];
		answer.toArray(answerIA);
		for (int i = 0; i < answer.size(); i++)
		{
			answerA[i] = Integer.toString(answerIA[i]);
		}
		entity.setAnswer(answerA);
		return entity;
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

	public LinkedList<Integer> getAnswer()
	{
		return answer;
	}
}
