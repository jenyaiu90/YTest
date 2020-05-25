package ru.jenyaiu90.ytest.entity;

import ru.jenyaiu90.ytest.data.Task;

public class TaskEntity
{
	protected int id;
	protected Task.TaskType type;
	protected int num;
	protected String image;
	protected String text;
	protected int cost;
	protected String[] choice;
	protected String[] answer;
	protected int test;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public Task.TaskType getType()
	{
		return type;
	}
	public void setType(Task.TaskType type)
	{
		this.type = type;
	}
	public int getNum()
	{
		return num;
	}
	public void setNum(int num)
	{
		this.num = num;
	}
	public String getImage()
	{
		return image;
	}
	public void setImage(String image)
	{
		this.image = image;
	}
	public String getText()
	{
		return text;
	}
	public void setText(String text)
	{
		this.text = text;
	}
	public int getCost()
	{
		return cost;
	}
	public void setCost(int cost)
	{
		this.cost = cost;
	}
	public String[] getChoice()
	{
		return choice;
	}
	public void setChoice(String[] choice)
	{
		this.choice = choice;
	}
	public String[] getAnswer()
	{
		return answer;
	}
	public void setAnswer(String[] answer)
	{
		this.answer = answer;
	}
	public int getTest()
	{
		return test;
	}
	public void setTest(int test)
	{
		this.test = test;
	}
}