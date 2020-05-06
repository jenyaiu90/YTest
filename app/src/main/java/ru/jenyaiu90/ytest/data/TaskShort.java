package ru.jenyaiu90.ytest.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TaskShort extends Task
{
	protected String answer;

	public TaskShort(@NonNull String text, @Nullable Drawable image, int cost,
					 String answer)
	{
		super(text, image, TaskType.SHORT, cost);
		this.answer = answer;
	}
}
