package ru.jenyaiu90.ytest.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import ru.jenyaiu90.ytest.entity.GroupEntity;

public interface GroupService
{
	@POST("/group/create")
	public Call<GroupEntity> createGroup(@Body GroupEntity group, @Query("login") String login, @Query("password") String password);

	@PUT("/group/join")
	public Call<GroupEntity> joinGroup(@Query("group_id") int group_id, @Query("login") String login, @Query("password") String password);

	@GET("/group/get_groups_of")
	public Call<List<GroupEntity>> getGroupsOf(@Query("login") String login);

	@GET("/group/get_groups_with")
	public Call<List<GroupEntity>> getGroupsWith(@Query("login") String login);
}
