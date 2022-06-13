package com.mycompany.Wiezen;
import android.widget.*;
import android.content.*;
import java.util.*;
import android.view.*;
import android.view.View.*;
import android.os.*;
import android.graphics.*;
import android.util.*;

public class UsersAdapter extends ArrayAdapter<User>
{
	Context context;

	public UsersAdapter(Context context, ArrayList<User> users)
	{
		super(context, 0, users);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// Get the data item for this position
		User user = getItem(position);  
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.choice, parent, false);//false
		}
		// Lookup view for data population
		TextViewPlayer tvChoice = convertView.findViewById(R.id.choice);//TextView
		tvChoice.setBackgroundColor(Color.LTGRAY);//GREEN LTGRAY
		tvChoice.setTextColor(Color.BLACK);
		tvChoice.setTextSize(TypedValue.COMPLEX_UNIT_PX  , 24);
		//tvChoice.setTooltipText(user.toolTip);
		//setTooltipText => werkt niet bij oudere tablets

		TextViewPlayer tvHelp = convertView.findViewById(R.id.choiceHelp);
		tvHelp.setTextSize(TypedValue.COMPLEX_UNIT_PX  , 18);//18
		//tvHelp.setBackgroundColor(Color.RED);
		tvHelp.setPaddingLeft(20);//20
		// Populate the data into the template view using the data object
		tvChoice.setText(user.choice);
		tvHelp.setText(user.help);
		// Return the completed view to render on screen
		return convertView;
	}

	private void toast(String pp)
	{
		Toast.makeText(context, pp, Toast.LENGTH_SHORT).show();
	}
}
