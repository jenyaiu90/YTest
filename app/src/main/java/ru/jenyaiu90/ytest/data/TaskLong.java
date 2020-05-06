package ru.jenyaiu90.ytest.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskLong extends Task
{
	public TaskLong(@NonNull String text, @Nullable Drawable image, int cost)
	{
		super(text, image, TaskType.LONG, cost);
	}
}
