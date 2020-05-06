package ru.jenyaiu90.ytest.data;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

public class User
{
	protected String login;
	protected String name;
	protected String surname;
	protected String email;
	protected String phone_number;
	protected Drawable image;
	protected boolean isTeacher;

	public User(String login)
	{
		this.login = login;
		name = "Name";
		surname = "Surname";
		email = "email@example.com";
		phone_number = "+70000000000";
		image = null;
		isTeacher = false;
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

	public Drawable getImage()
	{
		return image;
	}

	public void setImage(Drawable image)
	{
		this.image = image;
	}

	public boolean getIsTeacher()
	{
		return isTeacher;
	}
}
