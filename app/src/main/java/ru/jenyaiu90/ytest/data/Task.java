package ru.jenyaiu90.ytest.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
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
	protected String text;
	protected Drawable image;
	protected TaskType type;
	protected int cost;

	protected Task(@NonNull String text, @Nullable Drawable image, TaskType type, int cost)
	{
		this.text = text;
		this.image = image;
		this.type = type;
		this.cost = cost;
	}
}
