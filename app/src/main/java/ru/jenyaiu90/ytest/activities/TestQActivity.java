package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;
import ru.jenyaiu90.ytest.data.Test;
import ru.jenyaiu90.ytest.data.Util;

public class TestQActivity extends Activity
{
	public static final int PHOTO_REQUEST = 1; //Код запроса на получение фотографии

	protected Test test;

	protected LinearLayout counterLL, choiceLL, imagesLL;
	protected ImageView imageIV;
	protected TextView taskTV;

	protected int task;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q);

		test = Util.getTestAsExtra(getIntent());

		counterLL = (LinearLayout)findViewById(R.id.counterLL);
		choiceLL = (LinearLayout)findViewById(R.id.choiceLL);
		imageIV = (ImageView)findViewById(R.id.imageIV);
		taskTV = (TextView)findViewById(R.id.taskTV);
		imagesLL = null;

		draw(0);
	}

	protected void draw(int n)
	{
		task = n;
		counterLL.removeAllViews();
		for (int i = 0; i < test.size(); i++) //Добавление номеров заданий в верхнюю часть экрана
		{
			Button taskBT = new Button(TestQActivity.this);
			taskBT.setText(Integer.toString(i + 1));
			taskBT.setWidth(25);
			if (i == n) //Выделить текущее задание
			{
				taskBT.setBackgroundColor(getResources().getColor(R.color.currentTask));
			}
			else if (test.getTask(i).isAnswer()) //Выделить решённые задания
			{
				taskBT.setBackgroundColor(getResources().getColor(R.color.savedTask));
			}
			taskBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					draw(Integer.parseInt(((Button)v).getText().toString()) - 1);
				}
			});
			counterLL.addView(taskBT);
		}
		Task currentTask = test.getTask(n); //Получить текущее задание
		imageIV.setImageBitmap(currentTask.getImage());
		taskTV.setText(currentTask.getText());
		choiceLL.removeAllViews();
		switch (currentTask.getType())
		{
			case ONE:
			{
				TaskOne cTask = (TaskOne)currentTask;
				RadioGroup choiceRG = new RadioGroup(TestQActivity.this);
				choiceRG.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				for (String choice : cTask.getChoice()) //Добавление вариантов ответа
				{
					RadioButton choiceRB = new RadioButton(TestQActivity.this);
					choiceRB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceRB.setText(choice);
					choiceRG.addView(choiceRB);
				}
				if (cTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					((RadioButton)(choiceRG.getChildAt(cTask.getInAnswer()))).setChecked(true);
				}
				choiceLL.addView(choiceRG);
				break;
			}
			case MANY:
			{
				TaskMany cTask = (TaskMany)currentTask;
				for (String choice : cTask.getChoice()) //Добавление вариантов ответа
				{
					CheckBox choiceCB = new CheckBox(TestQActivity.this);
					choiceCB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceCB.setText(choice);
					choiceLL.addView(choiceCB);
				}
				if (cTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					LinkedList<Integer> answers = cTask.getInAnswer();
					for (Integer i : answers)
					{
						((CheckBox)(choiceLL.getChildAt(i))).setChecked(true);
					}
				}
				break;
			}
			case SHORT:
			{
				EditText choiceET = new EditText(TestQActivity.this);
				choiceET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				if (currentTask.isAnswer()) //Добавление ответа, если он уже был дан
				{
					choiceET.setText(((TaskShort)(currentTask)).getInAnswer());
				}
				choiceLL.addView(choiceET);
				break;
			}
			case LONG:
			{
				HorizontalScrollView imagesHSV = new HorizontalScrollView(TestQActivity.this);
				imagesHSV.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				imagesLL = new LinearLayout(TestQActivity.this);
				imagesLL.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
				imagesLL.setOrientation(LinearLayout.HORIZONTAL);

				LinearLayout buttonsLL = new LinearLayout(TestQActivity.this);
				buttonsLL.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				buttonsLL.setOrientation(LinearLayout.HORIZONTAL);

				Button loadBT = new Button(TestQActivity.this);
				Button removeBT = new Button(TestQActivity.this);
				loadBT.setText(R.string.load_image);
				removeBT.setText(R.string.remove_images);
				loadBT.setLayoutParams(new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
				removeBT.setLayoutParams(new LinearLayout.LayoutParams(
						0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
				loadBT.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						//StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
						//StrictMode.setVmPolicy(builder.build());
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, PHOTO_REQUEST);
					}
				});
				removeBT.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						imagesLL.removeAllViews();
					}
				});

				EditText answerET = new EditText(TestQActivity.this);
				answerET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				answerET.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

				if (currentTask.isAnswer()) //Добавление ответа, если он был дан
				{
					TaskLong cTask = (TaskLong)currentTask;
					answerET.setText(cTask.getInAnswerS());
					LinkedList<Bitmap> images = cTask.getInAnswerI();
					if (images != null)
					{
						for (Bitmap i : images)
						{
							ImageView tmpImageIV = new ImageView(TestQActivity.this);
							tmpImageIV.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100));
							tmpImageIV.setImageBitmap(i);
							imagesLL.addView(tmpImageIV);
						}
					}
				}

				imagesHSV.addView(imagesLL);
				buttonsLL.addView(loadBT);
				buttonsLL.addView(removeBT);
				choiceLL.addView(imagesHSV);
				choiceLL.addView(buttonsLL);
				choiceLL.addView(answerET);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case PHOTO_REQUEST: //Получение фотографии
				if (resultCode == RESULT_OK && data != null && data.getExtras() != null)
				{
					ImageView newImageIV = new ImageView(TestQActivity.this);
					newImageIV.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT, 100));
					newImageIV.setImageBitmap((Bitmap)data.getExtras().get("data"));
					imagesLL.addView(newImageIV);
				}
		}
	}

	public void save(View view) //Сохранение ответа
	{
		switch (test.getTask(task).getType())
		{
			case ONE:
			{
				TaskOne cTask = (TaskOne)test.getTask(task);
				RadioGroup choiceRG = (RadioGroup)choiceLL.getChildAt(0);
				int s = -1;
				for (int i = 0; i < cTask.getChoice().size(); i++)
				{
					if (((RadioButton)(choiceRG.getChildAt(i))).isChecked())
					{
						s = i;
						break;
					}
				}
				if (s == -1) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(s);
				break;
			}
			case MANY:
			{
				TaskMany cTask = (TaskMany)test.getTask(task);
				LinkedList<Integer> answers = new LinkedList<>();
				for (int i = 0; i < cTask.getChoice().size(); i++)
				{
					if (((CheckBox)(choiceLL.getChildAt(i))).isChecked())
					{
						answers.add(i);
					}
				}
				if (answers.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answers);
				break;
			}
			case SHORT:
			{
				TaskShort cTask = (TaskShort)test.getTask(task);
				String answer = ((EditText)(choiceLL.getChildAt(0))).getText().toString();
				if (answer.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answer);
				break;
			}
			case LONG:
			{
				TaskLong cTask = (TaskLong)test.getTask(task);
				String answer = ((EditText)(choiceLL.getChildAt(2))).getText().toString();
				LinkedList<Bitmap> answers = new LinkedList<>();
				for (int i = 0; i < imagesLL.getChildCount(); i++)
				{
					answers.add(((BitmapDrawable)(((ImageView)(imagesLL.getChildAt(i))).getDrawable())).getBitmap());
				}
				if (answer.isEmpty() && answers.isEmpty()) //Если ответ не был дан
				{
					Toast.makeText(TestQActivity.this, R.string.no_answer, Toast.LENGTH_LONG).show();
					return;
				}
				cTask.inputAnswer(answers);
				cTask.inputAnswer(answer);
				break;
			}
		}
		if (task != test.size() - 1) //Переход к следующему вопросу
		{
			next(null);
		}
	}

	public void prev(View view) //Переход к предыдущему вопросу
	{
		if (task != 0)
		{
			draw(task - 1);
		}
	}

	public void next(View view) //Переход к следующему вопросу
	{
		if (task == test.size() - 1)
		{
			Toast.makeText(TestQActivity.this, "Vsyo!", Toast.LENGTH_LONG).show();
		}
		else
		{
			draw(task + 1);
		}
	}
}
