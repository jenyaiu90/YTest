package ru.jenyaiu90.ytest.entity;

public class ServerAnswerEntity
{
	public static final String OK = "OK";
	public static final String PASSWORD = "Password";
	public static final String NO_USER = "No_user";
	public static final String NO_GROUP = "No_group";
	public static final String NO_TEST = "No_test";
	public static final String NO_ACCESS = "No_access";
	public static final String USER_ALREADY_EXISTS = "User_already_exists";
	public static final String NO_INTERNET = "No_internet";

	protected String answer;

	public ServerAnswerEntity()
	{
		answer = "";
	}
	public ServerAnswerEntity(String answer)
	{
		this.answer = answer;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}
