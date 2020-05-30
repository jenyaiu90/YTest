package ru.jenyaiu90.ytest.entity;

//Ответ сервера
public class ServerAnswerEntity
{
	public static final String OK = "OK";
	public static final String PASSWORD = "Password"; //Неверный пароль
	public static final String NO_USER = "No_user"; //Пользователь не найден
	public static final String NO_GROUP = "No_group"; //Группа не найдена
	public static final String NO_TEST = "No_test"; //Тест не найден
	public static final String NO_ACCESS = "No_access"; //Отказано в доступе
	public static final String NO_TASK = "No_task"; //Задание не найдено
	public static final String NO_AUTHOR = "No_author"; //Автор не найден
	public static final String USER_ALREADY_EXISTS = "User_already_exists"; //Пользователь с таким логином уже зарегистрирован
	public static final String NO_INTERNET = "No_internet"; //Нет соединения с сервером

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
