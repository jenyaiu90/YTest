package ru.jenyaiu90.ytest.data;

import java.util.Comparator;

import ru.jenyaiu90.ytest.entity.TaskEntity;

public class TaskEntityComparator implements Comparator<TaskEntity>
{
	//Сравнение заданий по их номерам для сортировки
	@Override
	public int compare(TaskEntity o1, TaskEntity o2)
	{
		return o1.getNum() - o2.getNum();
	}
}
