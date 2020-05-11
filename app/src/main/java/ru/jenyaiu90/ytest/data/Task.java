package ru.jenyaiu90.ytest.data;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

public abstract class Task
{
	public enum TaskType
	{
		ONE,
		MANY,
		SHORT,
		LONG
	}

	protected Bitmap image;
	protected String text;
	protected TaskType type;
	protected int cost;

	protected Task(String text, @Nullable Bitmap image, TaskType type, int cost)
	{
		this.text = text;
		this.image = image;
		this.type = type;
		this.cost = cost;
	}

	public Bitmap getImage()
	{
		return image;
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
