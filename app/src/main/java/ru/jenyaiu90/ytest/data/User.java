package ru.jenyaiu90.ytest.data;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.entity.UserEntity;

public class User
{
	protected String login;
	protected String name;
	protected String surname;
	protected String email;
	protected String phone_number;
	protected boolean isTeacher;

	public User(@NonNull UserEntity entity)
	{
		login = entity.getLogin();
		name = entity.getName();
		surname = entity.getSurname();
		email = entity.getEmail();
		phone_number = entity.getPhone_number();
		isTeacher = entity.getIsTeacher();
	}

	//Пока не используется, но когда-нибудь может пригодиться
	public UserEntity toEntity()
	{
		UserEntity entity = new UserEntity();
		entity.setLogin(login);
		entity.setName(name);
		entity.setSurname(surname);
		entity.setEmail(email);
		entity.setPhone_number(phone_number);
		entity.setTeacher(isTeacher);
		return entity;
	}

	public String getLogin()
	{
		return login;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone_number()
	{
		return phone_number;
	}

	public void setPhone_number(String phone_number)
	{
		this.phone_number = phone_number;
	}

	public boolean getIsTeacher()
	{
		return isTeacher;
	}
}
