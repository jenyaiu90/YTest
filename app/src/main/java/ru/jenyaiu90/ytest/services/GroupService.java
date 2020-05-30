package ru.jenyaiu90.ytest.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.jenyaiu90.ytest.entity.GroupEntity;
import ru.jenyaiu90.ytest.entity.ServerAnswerEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;
import ru.jenyaiu90.ytest.entity.UserEntity;

public interface GroupService
{
	//Создать группу
	@POST("/group/create")
	public Call<ServerAnswerEntity> createGroup(@Body GroupEntity group, @Query("login") String login, @Query("password") String password);

	//Присоединиться к группе
	@PUT("/group/join")
	public Call<ServerAnswerEntity> joinGroup(@Query("group_id") int group_id, @Query("login") String login, @Query("password") String password);

	//Получить группы, созданные пользователем
	@GET("/group/get_groups_of")
	public Call<List<GroupEntity>> getGroupsOf(@Query("login") String login);

	//Получить группы, в которых есть пользователь
	@GET("/group/get_groups_with")
	public Call<List<GroupEntity>> getGroupsWith(@Query("login") String login);

	//Получить пользователей группы
	@GET("/group/get_users")
	public Call<List<UserEntity>> getUsers(@Query("group_id") int group_id);

	//Задать группе задание
	@POST("/group/set")
	public Call<ServerAnswerEntity> setTest(@Query("group_id") int group_id, @Query("test_id") int test_id, @Query("login") String login, @Query("password") String password);

	//Получить список тестов, которые можно задать группе
	@GET("/group/get_tests_for_set")
	public Call<List<TestEntity>> getTestsForSet(@Query("group_id") int group_id);

	//Получить список тестов, заданных группе
	@GET("/group/get_tests_for_group")
	public Call<List<TestEntity>> getTestsForGroup(@Query("group_id") int group_id);
}
