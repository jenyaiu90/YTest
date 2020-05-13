package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.R;

public class QuestionAdapter extends ArrayAdapter<String[]>
{
	public QuestionAdapter(Context context, String[][] arr)
	{
		super(context, R.layout.list_item_question, arr);
	}

	@Override @NonNull
	public View getView(int position, View convertView, @NonNull ViewGroup parent)
	{
		String[] str = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_question, null);
		}
		((TextView)convertView.findViewById(R.id.numTV)).setText(Integer.toString(position + 1));
		((TextView)convertView.findViewById(R.id.nameTV)).setText(str[0]);
		((TextView)convertView.findViewById(R.id.typeTV)).setText(str[1]);
		return convertView;
	}
}
