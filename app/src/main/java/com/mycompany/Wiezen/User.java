package com.mycompany.Wiezen;

public class User
{
	public String choice;
    public String help;
	public String toolTip;

    public User(String choice, String help)
	{
		this.choice = choice;
		this.help = help;
    }

	public User(String choice, String help, String toolTip)
	{
		this.choice = choice;
		this.help = help;
		this.toolTip = toolTip;
    }

	public String getChoice()
	{
		return choice;
	}

	public String getToolTip()
	{
		return toolTip;
	}
}
