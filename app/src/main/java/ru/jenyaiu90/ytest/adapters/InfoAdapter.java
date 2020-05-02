package ru.jenyaiu90.ytest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ru.jenyaiu90.ytest.R;

public class InfoAdapter extends ArrayAdapter<String[]>
{
	public InfoAdapter(Context context, String[][] arr)
	{
		super(context, R.layout.list_item_info, arr);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		String[] str = getItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_info, null);
		}
		((TextView)convertView.findViewById(R.id.hintTV)).setText(str[0]);
		((TextView)convertView.findViewById(R.id.dataTV)).setText(str[1]);
		return convertView;
	}
}
