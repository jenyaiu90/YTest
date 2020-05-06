package ru.jenyaiu90.ytest.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

public class TaskMany extends Task
{
	protected LinkedList<String> choice;
	protected int[] answer;

	public TaskMany(@NonNull String text, @Nullable Drawable image, int cost,
					@NonNull LinkedList<String> choice, int[] answer)
	{
		super(text, image, TaskType.MANY, cost);
		this.choice = choice;
		this.answer = answer;
	}
}
