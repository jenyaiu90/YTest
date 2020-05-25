package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public abstract class Task
{
	public enum TaskType
	{
		ONE,
		MANY,
		SHORT,
		LONG
	}

	protected int id;
	protected String text;
	protected TaskType type;
	protected int num;
	protected int cost;

	protected Task(String text, TaskType type, int cost)
	{
		this.text = text;
		this.type = type;
		this.cost = cost;
	}

	public Task(TaskEntity entity)
	{
		id = entity.getId();
		type = entity.getType();
		num = entity.getNum();
		text = entity.getText();
		cost = entity.getCost();
		_fromEntity(entity);
	}

	protected abstract void _fromEntity(TaskEntity entity);

	protected abstract TaskEntity _toEntity();

	public TaskEntity toEntity()
	{
		TaskEntity entity = _toEntity();
		entity.setId(id);
		entity.setType(type);
		entity.setNum(num);
		entity.setText(text);
		entity.setCost(cost);
		return entity;
	}

	public String getText()
	{
		return text;
	}

	public TaskType getType()
	{
		return type;
	}

	public int getCost()
	{
		return cost;
	}

	public abstract boolean isAnswer();
}
