package services;

import java.util.Random;

public class Exhibition {
	String title;
	String templateid;
	String [] media;
	String creator;
	String dateCreated;
	String description;
	
	public Exhibition()
	{
		this.title="title";
		this.templateid=null;
		this.media=null;
		this.creator=null;
		this.dateCreated=null;
		this.description=null;
		
	}
	public String getTitle()
	{
		return (this.title);
	}
	
	public String [] getMedia()
	{
		return this.media;
	}
	public void AutoExhibitionGenerator()
	{
		Exhibition exhibition = new Exhibition();
		
		this.media = new String[10];
		Random random = new Random();
		int randomNum;
		
		for (int i=0;i<10;i++)
		{
			randomNum= random.nextInt(15-1);
			
			this.media[i]="Images/"+String.valueOf(randomNum)+".jpg";
			
			
			
		}
		
	}
	
	
	

}
