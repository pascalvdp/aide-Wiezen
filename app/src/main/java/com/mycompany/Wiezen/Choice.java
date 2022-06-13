package com.mycompany.Wiezen;

public class Choice
{
	public int arrayLength;
	public	String[] str;
	public	boolean[] sameLength; // *=false
	public  boolean[] plus; //+=true
	public	boolean[] min;   //-=true             
	public	boolean[] trump;   //!=true       

	public Choice(String string)
	{
		String[] st=string.split(",");
		arrayLength = st.length;
		str = new String[st.length];
		sameLength = new boolean[st.length];
		plus = new boolean[st.length];
		min = new boolean[st.length];
		trump = new boolean[st.length];
		StringBuilder[] strB=new StringBuilder[st.length];
		for (int i=0;i < strB.length;i++)
		{
			sameLength[i] = true;
			plus[i] = false;
			min[i] = false;
			trump[i] = false;
			strB[i] = new StringBuilder(st[i]);
			for (int z=0;z < strB[i].length();z++)
			{
				switch (strB[i].charAt(z))
				{
					case '*':
						sameLength[i] = false;
						strB[i].deleteCharAt(z);
						z--;
						break;
					case '+':
						plus[i] = true;
						strB[i].deleteCharAt(z);
						z--;
						break;
					case '-':
						min[i] = true;
						strB[i].deleteCharAt(z);
						z--;
						break;
					case '!':
						trump[i] = true;
						strB[i].deleteCharAt(z);
						z--;
						break;
				}
			}
			str[i] = strB[i].toString();
		}
	}
}
