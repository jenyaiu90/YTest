package ru.jenyaiu90.ytest.data;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;

public class Test
{
	protected int id;
	protected String name;
	protected String subject;
	protected ArrayList<Task> tasks;

	public Test(String name, String subject, @NonNull ArrayList<Task> tasks)
	{
		id = 0;
		this.name = name;
		this.subject = subject;
		this.tasks = tasks;
	}

	public Test(TestEntity entity, ArrayList<TaskEntity> tasks)
	{
		id = entity.getId();
		name = entity.getName();
		subject = entity.getSubject();
		this.tasks = new ArrayList<>(tasks.size());
		for (TaskEntity task : tasks)
		{
			switch (task.getType())
			{
				case ONE:
					this.tasks.add(new TaskOne(task));
					break;
				case MANY:
					this.tasks.add(new TaskMany(task));
					break;
				case SHORT:
					this.tasks.add(new TaskShort(task));
					break;
				case LONG:
					this.tasks.add(new TaskLong(task));
					break;
			}
		}
	}

	public TestEntity toEntity()
	{
		TestEntity entity = new TestEntity();
		entity.setId(id);
		entity.setName(name);
		entity.setSubject(subject);
		return entity;
	}

	public List<TaskEntity> tasksToEntity()
	{
		ArrayList<TaskEntity> entities = new ArrayList<>(tasks.size());
		for (Task i : tasks)
		{
			TaskEntity entity = i.toEntity();
			entity.setTest(id);
			entities.add(entity);
		}
		return entities;
	}

	public String getName()
	{
		return name;
	}

	public int getId()
	{
		return id;
	}

	public int size()
	{
		return tasks.size();
	}

	public Task getTask(int i)
	{
		return tasks.get(i);
	}

	public ArrayList<Task> getTasks()
	{
		return tasks;
	}

	public void setTask(int i, Task task)
	{
		tasks.set(i, task);
	}

	public void addTask(Task task)
	{
		tasks.add(task);
	}

	public void deleteTask(int i)
	{
		tasks.remove(i);
	}
}
