package ru.jenyaiu90.ytest.data;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public class TaskLong extends Task
{
	protected String inAnswerS;

	public TaskLong(String text, int cost)
	{
		super(text, TaskType.LONG, cost);
		inAnswerS = "";
	}

	public TaskLong(TaskEntity entity)
	{
		super(entity);
		inAnswerS = "";
	}

	@Override
	protected void _fromEntity(TaskEntity entity)
	{

	}

	@Override
	protected TaskEntity _toEntity()
	{
		return new TaskEntity();
	}

	public void inputAnswer(String ans)
	{
		inAnswerS = ans;
	}

	public boolean isAnswer()
	{
		return !inAnswerS.isEmpty();
	}

	public String getInAnswerS()
	{
		return inAnswerS;
	}
}
