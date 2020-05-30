package ru.jenyaiu90.ytest.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedList;

import ru.jenyaiu90.ytest.R;
import ru.jenyaiu90.ytest.data.Task;
import ru.jenyaiu90.ytest.data.TaskLong;
import ru.jenyaiu90.ytest.data.TaskMany;
import ru.jenyaiu90.ytest.data.TaskOne;
import ru.jenyaiu90.ytest.data.TaskShort;

//Редактирование задания
public class TestQEditActivity extends Activity
{
	//Для намерения
	public static final String TASK_TYPE = "task_type";
	public static final String TASK = "task";

	protected EditText textET;
	protected RadioButton[] modeRBs;
	protected LinearLayout ansLL;
	protected ArrayList<ImageButton> removeIBs;
	protected EditText costET;

	protected Task task;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_q_edit);

		textET = (EditText)findViewById(R.id.textET);
		modeRBs = new RadioButton[] {
				(RadioButton)findViewById(R.id.oneRB),
				(RadioButton)findViewById(R.id.manyRB),
				(RadioButton)findViewById(R.id.shortRB),
				(RadioButton)findViewById(R.id.longRB) };
		ansLL = (LinearLayout)findViewById(R.id.ansLL);
		costET = (EditText)findViewById(R.id.costET);

		removeIBs = new ArrayList<>();

		switch ((Task.TaskType)getIntent().getSerializableExtra(TASK_TYPE))
		{
			case ONE:
			{
				task = new Gson().fromJson(getIntent().getStringExtra(TASK), TaskOne.class);
				modeRBs[0].setChecked(true);
				mode(modeRBs[0]);

				//Кнопки удаления вариантов ответа
				LinearLayout imagesLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(0);

				//Радиокнопки для выбора верного ответа
				RadioGroup choiceRG = (RadioGroup) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(1);

				//Поля для ввода вариантов ответа
				LinearLayout choiceLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(2);

				imagesLL.removeAllViews();
				choiceRG.removeAllViews();
				choiceLL.removeAllViews();
				removeIBs.clear();

				TaskOne taskOne = (TaskOne) task;
				LinkedList<String> choices = taskOne.getChoice();
				for (String i : choices)
				{
					//Удаление варианта ответа
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 2)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(0);
								RadioGroup choiceRG = (RadioGroup) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(1);
								LinearLayout choiceLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(2);
								imagesLL.removeViewAt(i);
								choiceRG.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							}
							else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					//Выбор верного варианта ответа
					RadioButton choiceRB = new RadioButton(TestQEditActivity.this);
					choiceRB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceRB.setHeight(75);

					//Ввод варианта ответа
					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setText(i);
					choiceET.setHeight(75);

					imagesLL.addView(removeIB);
					choiceRG.addView(choiceRB);
					choiceLL.addView(choiceET);
				}
				((RadioButton) choiceRG.getChildAt(taskOne.getAnswer() - 1)).setChecked(true);

				break;
			}
			case MANY:
			{
				task = new Gson().fromJson(getIntent().getStringExtra(TASK), TaskMany.class);
				modeRBs[1].setChecked(true);
				mode(modeRBs[1]);

				//Кнопки удаления вариантов ответа
				LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);

				//Флажки для выбора верноых ответов
				LinearLayout choiceChLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);

				//Поля для ввода вариантов ответа
				LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);

				imagesLL.removeAllViews();
				choiceChLL.removeAllViews();
				choiceLL.removeAllViews();
				removeIBs.clear();

				TaskMany taskMany = (TaskMany)task;
				LinkedList<String> choices = taskMany.getChoice();
				for (String i : choices)
				{
					//Удаление варианта ответа
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 2)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
								LinearLayout choiceChLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
								LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);
								imagesLL.removeViewAt(i);
								choiceChLL.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							}
							else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					//Выбор верных вариантов ответа
					CheckBox choiceCB = new CheckBox(TestQEditActivity.this);
					choiceCB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceCB.setHeight(75);

					//Ввода вариантов ответа
					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setText(i);
					choiceET.setHeight(75);

					imagesLL.addView(removeIB);
					choiceChLL.addView(choiceCB);
					choiceLL.addView(choiceET);
				}

				for (int i : taskMany.getAnswer())
				{
					((CheckBox)choiceChLL.getChildAt(i)).setChecked(true);
				}

				break;
			}
			case SHORT:
			{
				task = new Gson().fromJson(getIntent().getStringExtra(TASK), TaskShort.class);
				modeRBs[2].setChecked(true);
				mode(modeRBs[2]);

				//Кнопки для удаления варианта ответа
				LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);

				//Поля для ввода верных ответов
				LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);

				imagesLL.removeAllViews();
				choiceLL.removeAllViews();
				removeIBs.clear();

				TaskShort taskShort = (TaskShort) task;
				for (String i : taskShort.getAnswers())
				{
					//Удаление варианта ответа
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 1)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
								LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
								imagesLL.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							}
							else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_one_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					//Ввод верного ответа
					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setText(i);
					choiceET.setHeight(75);

					imagesLL.addView(removeIB);
					choiceLL.addView(choiceET);
				}

				break;
			}
			case LONG:
			{
				task = new Gson().fromJson(getIntent().getStringExtra(TASK), TaskLong.class);
				modeRBs[3].setChecked(true);
				mode(modeRBs[3]);
				break;
			}
		}
		textET.setText(task.getText());
		costET.setText(Integer.toString(task.getCost()));
	}

	//Сохранение задания
	public void save(View view)
	{
		if (textET.getText().toString().isEmpty())
		{
			Toast.makeText(TestQEditActivity.this, R.string.empty_task, Toast.LENGTH_LONG).show();
			return;
		}
		if (costET.getText().toString().isEmpty())
		{
			costET.setText("1"); //Количество баллов по умолчанию
		}
		try
		{
			if (modeRBs[0].isChecked()) //Один из нескольких
			{
				LinkedList<String> choice = new LinkedList<>();
				LinearLayout answersLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(2);
				for (int i = 0; i < answersLL.getChildCount(); i++)
				{
					choice.add(((EditText) answersLL.getChildAt(i)).getText().toString());
					if (choice.getLast().isEmpty())
					{
						Toast.makeText(TestQEditActivity.this, R.string.empty_answer, Toast.LENGTH_LONG).show();
						return;
					}
					for (int j = 0; j < i; j++)
					{
						if (choice.get(j).equals(choice.getLast()))
						{
							Toast.makeText(TestQEditActivity.this, R.string.answers_eq, Toast.LENGTH_LONG).show();
							return;
						}
					}
				}

				RadioGroup answersRG = (RadioGroup) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(1);
				int i; //Верный ответ
				for (i = 0; i < answersRG.getChildCount(); i++)
				{
					if (((RadioButton) answersRG.getChildAt(i)).isChecked())
					{
						break;
					}
				}
				if (i >= answersRG.getChildCount())
				{
					Toast.makeText(TestQEditActivity.this, R.string.should_set_answer, Toast.LENGTH_LONG).show();
					return;
				}

				TaskOne taskOne = new TaskOne(textET.getText().toString(), Integer.parseInt(costET.getText().toString()), choice, i + 1);

				Intent intent = new Intent();
				intent.putExtra(TASK, new Gson().toJson(taskOne));
				intent.putExtra(TASK_TYPE, Task.TaskType.ONE);
				setResult(RESULT_OK, intent);
				finish();
			}
			else if (modeRBs[1].isChecked()) //Несколько из нескольких
			{
				LinkedList<String> choice = new LinkedList<>(); //Варианты ответа
				LinearLayout answersLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(2);
				for (int i = 0; i < answersLL.getChildCount(); i++)
				{
					choice.add(((EditText) answersLL.getChildAt(i)).getText().toString());
					if (choice.getLast().isEmpty())
					{
						Toast.makeText(TestQEditActivity.this, R.string.empty_answer, Toast.LENGTH_LONG).show();
						return;
					}
					for (int j = 0; j < i; j++)
					{
						if (choice.get(j).equals(choice.getLast()))
						{
							Toast.makeText(TestQEditActivity.this, R.string.answers_eq, Toast.LENGTH_LONG).show();
							return;
						}
					}
				}

				LinearLayout answersChLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(1);
				LinkedList<Integer> answer = new LinkedList<>(); //Верные ответы
				for (int i = 0; i < answersChLL.getChildCount(); i++)
				{
					if (((CheckBox) answersChLL.getChildAt(i)).isChecked())
					{
						answer.add(i + 1);
					}
				}
				if (answer.isEmpty())
				{
					Toast.makeText(TestQEditActivity.this, R.string.should_set_answer, Toast.LENGTH_LONG).show();
					return;
				}

				TaskMany taskMany = new TaskMany(textET.getText().toString(), Integer.parseInt(costET.getText().toString()), choice, answer);

				Intent intent = new Intent();
				intent.putExtra(TASK, new Gson().toJson(taskMany));
				intent.putExtra(TASK_TYPE, Task.TaskType.MANY);
				setResult(RESULT_OK, intent);
				finish();
			}
			else if (modeRBs[2].isChecked()) //Краткий ответ
			{
				LinkedList<String> choice = new LinkedList<>(); //Верные ответы
				LinearLayout answersLL = (LinearLayout) ((LinearLayout) ansLL.getChildAt(1)).getChildAt(1);
				for (int i = 0; i < answersLL.getChildCount(); i++)
				{
					choice.add(((EditText) answersLL.getChildAt(i)).getText().toString());
					if (choice.getLast().isEmpty())
					{
						Toast.makeText(TestQEditActivity.this, R.string.empty_answer, Toast.LENGTH_LONG).show();
						return;
					}
					for (int j = 0; j < i; j++)
					{
						if (choice.get(j).equals(choice.getLast()))
						{
							Toast.makeText(TestQEditActivity.this, R.string.answers_eq, Toast.LENGTH_LONG).show();
							return;
						}
					}
				}
				String ans[] = new String[choice.size()];
				int i = 0;
				for (String str : choice)
				{
					ans[i] = str;
					i++;
				}

				TaskShort taskShort = new TaskShort(textET.getText().toString(), Integer.parseInt(costET.getText().toString()), ans);

				Intent intent = new Intent();
				intent.putExtra(TASK, new Gson().toJson(taskShort));
				intent.putExtra(TASK_TYPE, Task.TaskType.SHORT);
				setResult(RESULT_OK, intent);
				finish();
			}
			else if (modeRBs[3].isChecked()) //Развёрнутый ответ
			{
				TaskLong taskLong = new TaskLong(textET.getText().toString(), Integer.parseInt(costET.getText().toString()));

				Intent intent = new Intent();
				intent.putExtra(TASK, new Gson().toJson(taskLong));
				intent.putExtra(TASK_TYPE, Task.TaskType.LONG);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
		catch (NumberFormatException e)
		{
			Toast.makeText(TestQEditActivity.this, R.string.invalid_cost, Toast.LENGTH_LONG).show();
			return;
		}
	}

	public void cancel(View view)
	{
		setResult(RESULT_CANCELED);
		finish();
	}

	//Выбор типа ответа
	public void mode(View view)
	{
		ansLL.removeAllViews();
		removeIBs.clear();
		if (view == modeRBs[0]) //Один из нескольких
		{
			TextView infoTV = new TextView(TestQEditActivity.this);
			infoTV.setText(R.string.answers);
			infoTV.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			LinearLayout choiceLL = new LinearLayout(TestQEditActivity.this);
			choiceLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			choiceLL.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout imagesLL = new LinearLayout(TestQEditActivity.this);
			imagesLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imagesLL.setOrientation(LinearLayout.VERTICAL);

			RadioGroup choiceRG = new RadioGroup(TestQEditActivity.this);
			choiceRG.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			LinearLayout answersLL = new LinearLayout(TestQEditActivity.this);
			answersLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			answersLL.setOrientation(LinearLayout.VERTICAL);

			Button addBT = new Button(TestQEditActivity.this);
			addBT.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			addBT.setText(R.string.add_answer);
			addBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(
							75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 2)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
								RadioGroup choiceRG = (RadioGroup)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
								LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);
								imagesLL.removeViewAt(i);
								choiceRG.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							}
							else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					RadioButton choiceRB = new RadioButton(TestQEditActivity.this);
					choiceRB.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceRB.setHeight(75);

					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setHeight(75);

					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0)).addView(removeIB);
					((RadioGroup)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1)).addView(choiceRB);
					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2)).addView(choiceET);
				}
			});

			for (int i = 0; i < 3; i++)
			{
				ImageButton removeIB = new ImageButton(TestQEditActivity.this);
				removeIB.setLayoutParams(new LinearLayout.LayoutParams(
						75, 75));
				removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
				removeIBs.add(removeIB);
				removeIB.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (removeIBs.size() > 2)
						{
							int i;
							for (i = 0; i < removeIBs.size(); i++)
							{
								if (v == removeIBs.get(i))
								{
									break;
								}
							}
							if (i >= removeIBs.size())
							{
								return;
							}
							LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
							RadioGroup choiceRG = (RadioGroup)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
							LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);
							imagesLL.removeViewAt(i);
							choiceRG.removeViewAt(i);
							choiceLL.removeViewAt(i);
							removeIBs.remove(i);
						}
						else
						{
							Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
						}
					}
				});

				RadioButton choiceRB = new RadioButton(TestQEditActivity.this);
				choiceRB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				choiceRB.setHeight(75);

				EditText choiceET = new EditText(TestQEditActivity.this);
				choiceET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				choiceET.setHint(R.string.answer);
				choiceET.setHeight(75);

				imagesLL.addView(removeIB);
				choiceRG.addView(choiceRB);
				answersLL.addView(choiceET);
			}

			choiceLL.addView(imagesLL);
			choiceLL.addView(choiceRG);
			choiceLL.addView(answersLL);
			ansLL.addView(infoTV);
			ansLL.addView(choiceLL);
			ansLL.addView(addBT);
		}
		else if (view == modeRBs[1]) //Несколько из нескольких
		{
			TextView infoTV = new TextView(TestQEditActivity.this);
			infoTV.setText(R.string.answers);
			infoTV.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			LinearLayout choiceLL = new LinearLayout(TestQEditActivity.this);
			choiceLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			choiceLL.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout imagesLL = new LinearLayout(TestQEditActivity.this);
			imagesLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imagesLL.setOrientation(LinearLayout.VERTICAL);

			LinearLayout choiceChLL = new RadioGroup(TestQEditActivity.this);
			choiceChLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			LinearLayout answersLL = new LinearLayout(TestQEditActivity.this);
			answersLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			answersLL.setOrientation(LinearLayout.VERTICAL);

			Button addBT = new Button(TestQEditActivity.this);
			addBT.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			addBT.setText(R.string.add_answer);
			addBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 2)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
								LinearLayout choiceChLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
								LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);
								imagesLL.removeViewAt(i);
								choiceChLL.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							} else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					CheckBox choiceCB = new CheckBox(TestQEditActivity.this);
					choiceCB.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceCB.setHeight(75);

					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setHeight(75);

					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0)).addView(removeIB);
					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1)).addView(choiceCB);
					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2)).addView(choiceET);
				}
			});

			for (int i = 0; i < 3; i++)
			{
				ImageButton removeIB = new ImageButton(TestQEditActivity.this);
				removeIB.setLayoutParams(new LinearLayout.LayoutParams(
						75, 75));
				removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
				removeIBs.add(removeIB);
				removeIB.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (removeIBs.size() > 2)
						{
							int i;
							for (i = 0; i < removeIBs.size(); i++)
							{
								if (v == removeIBs.get(i))
								{
									break;
								}
							}
							if (i >= removeIBs.size())
							{
								return;
							}
							LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
							RadioGroup choiceRG = (RadioGroup)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
							LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(2);
							imagesLL.removeViewAt(i);
							choiceRG.removeViewAt(i);
							choiceLL.removeViewAt(i);
							removeIBs.remove(i);
						}
						else
						{
							Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
						}
					}
				});

				CheckBox choiceCB = new CheckBox(TestQEditActivity.this);
				choiceCB.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				choiceCB.setHeight(75);

				EditText choiceET = new EditText(TestQEditActivity.this);
				choiceET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				choiceET.setHint(R.string.answer);
				choiceET.setHeight(75);

				imagesLL.addView(removeIB);
				choiceChLL.addView(choiceCB);
				answersLL.addView(choiceET);
			}

			choiceLL.addView(imagesLL);
			choiceLL.addView(choiceChLL);
			choiceLL.addView(answersLL);
			ansLL.addView(infoTV);
			ansLL.addView(choiceLL);
			ansLL.addView(addBT);
		}
		else if (view == modeRBs[2]) //Краткий ответ
		{
			TextView infoTV = new TextView(TestQEditActivity.this);
			infoTV.setText(R.string.answers);
			infoTV.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

			LinearLayout choiceLL = new LinearLayout(TestQEditActivity.this);
			choiceLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			choiceLL.setOrientation(LinearLayout.HORIZONTAL);

			LinearLayout imagesLL = new LinearLayout(TestQEditActivity.this);
			imagesLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			imagesLL.setOrientation(LinearLayout.VERTICAL);

			LinearLayout answersLL = new LinearLayout(TestQEditActivity.this);
			answersLL.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			answersLL.setOrientation(LinearLayout.VERTICAL);

			Button addBT = new Button(TestQEditActivity.this);
			addBT.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			addBT.setText(R.string.add_answer);
			addBT.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					ImageButton removeIB = new ImageButton(TestQEditActivity.this);
					removeIB.setLayoutParams(new LinearLayout.LayoutParams(75, 75));
					removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
					removeIBs.add(removeIB);
					removeIB.setOnClickListener(new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							if (removeIBs.size() > 2)
							{
								int i;
								for (i = 0; i < removeIBs.size(); i++)
								{
									if (v == removeIBs.get(i))
									{
										break;
									}
								}
								if (i >= removeIBs.size())
								{
									return;
								}
								LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
								LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
								imagesLL.removeViewAt(i);
								choiceLL.removeViewAt(i);
								removeIBs.remove(i);
							} else
							{
								Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
							}
						}
					});

					EditText choiceET = new EditText(TestQEditActivity.this);
					choiceET.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
					choiceET.setHint(R.string.answer);
					choiceET.setHeight(75);

					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0)).addView(removeIB);
					((LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1)).addView(choiceET);
				}
			});

			for (int i = 0; i < 3; i++)
			{
				ImageButton removeIB = new ImageButton(TestQEditActivity.this);
				removeIB.setLayoutParams(new LinearLayout.LayoutParams(
						75, 75));
				removeIB.setImageDrawable(getResources().getDrawable(R.drawable.remove));
				removeIBs.add(removeIB);
				removeIB.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						if (removeIBs.size() > 2)
						{
							int i;
							for (i = 0; i < removeIBs.size(); i++)
							{
								if (v == removeIBs.get(i))
								{
									break;
								}
							}
							if (i >= removeIBs.size())
							{
								return;
							}
							LinearLayout imagesLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(0);
							LinearLayout choiceLL = (LinearLayout)((LinearLayout)ansLL.getChildAt(1)).getChildAt(1);
							imagesLL.removeViewAt(i);
							choiceLL.removeViewAt(i);
							removeIBs.remove(i);
						}
						else
						{
							Toast.makeText(TestQEditActivity.this, R.string.at_least_two_ans, Toast.LENGTH_LONG).show();
						}
					}
				});

				EditText choiceET = new EditText(TestQEditActivity.this);
				choiceET.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
				choiceET.setHint(R.string.answer);
				choiceET.setHeight(75);

				imagesLL.addView(removeIB);
				answersLL.addView(choiceET);
			}

			choiceLL.addView(imagesLL);
			choiceLL.addView(answersLL);
			ansLL.addView(infoTV);
			ansLL.addView(choiceLL);
			ansLL.addView(addBT);
		}
		else if (view == modeRBs[3]) //Развёрнутый ответ
		{
			TextView infoTV = new TextView(TestQEditActivity.this);
			infoTV.setText(R.string.teacher_check);
			infoTV.setLayoutParams(new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			infoTV.setTextColor(getResources().getColor(R.color.colorText));
			infoTV.setTextSize(20);

			ansLL.addView(infoTV);
		}
	}

	//Удаление задания
	public void delete(View view)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(TestQEditActivity.this);
		builder.setMessage(R.string.delete_sure)
				.setIcon(R.drawable.info)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						returnDelete();
					}
				})
				.setNegativeButton(R.string.no, null)
				.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void returnDelete()
	{
		setResult(TestQListActivity.RESULT_DELETE);
		finish();
	}
}
