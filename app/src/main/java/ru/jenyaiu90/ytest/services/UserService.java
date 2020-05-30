package ru.jenyaiu90.ytest.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;

public interface UserService
{
	//Создать пользователя
	@POST("/user/create")
	Call<ServerAnswerEntity> createUser(@Body UserEntity user);

	//Попытка входа в систему
	@GET("/user/auth")
	Call<UserEntity> signIn(@Query("login") String login, @Query("password") String password);

	//Получить пользователя
	@GET("/user/get")
	Call<UserEntity> getUser(@Query("login") String login);

	//Изменить данные пользователя
	@PUT("/user/update")
	Call<ServerAnswerEntity> updateUser(@Query("login") String login, @Query("name") String name,
										@Query("surname") String surname, @Query("email") String email,
										@Query("phone_number") String phone_number,
										@Query("old_password") String old_password, @Query("new_password") String new_password);
}
