package com.mycompany.databaseOnUpgrade;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity 
{
    Database mDb;
	Button but,but2;
	int player, color;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		mDb=new Database(this);
		but=(Button)findViewById(R.id.button);
		but2=(Button)findViewById(R.id.button2);
		but.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					mDb.setPlayer(50);//(mDb.getPlayer()+1);
					player=mDb.getPlayer();
					toast("player="+ String.valueOf(player));
				}
			});
		but2.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					player=mDb.getPlayer();
					toast("player="+ String.valueOf(player));
					color=mDb.getColor();
					toast("color="+ String.valueOf(color));
				}
			});

		mDb.open();

		
		//player=mDb.getPlayer();
		//toast("player="+ String.valueOf(player));
		

    }
	
	public void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
