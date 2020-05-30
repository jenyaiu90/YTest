package ru.jenyaiu90.ytest.data;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.entity.TaskEntity;

//Задание с кратким ответом
public class TaskShort extends Task
{
	protected String answers[]; //Верные ответы
	protected String inAnswer; //Ответ ученика

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

	//Ввод ответа ученика
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
