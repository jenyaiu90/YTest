package ru.jenyaiu90.ytest.data;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.entity.TaskEntity;

//Задание с выбором одного варианта
public class TaskOne extends Task
{
	protected LinkedList<String> choice; //Варианты ответа
	protected int answer; //Верный ответ
	protected int inAnswer; //Ответ ученика
	protected boolean hasAnswer; //Ввёл ли ученик ответ

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
		String[] choiceA = new String[choice.size()];
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

	//Ввод ответа ученика
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
