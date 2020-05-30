package ru.jenyaiu90.ytest.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.jenyaiu90.ytest.entity.TaskEntity;
import ru.jenyaiu90.ytest.entity.TestEntity;

public interface TaskService
{
	//Получить список заданий теста
	@GET("/task/get")
	Call<List<TaskEntity>> getTasksOfTest(@Query("test_id") int test_id);
}
