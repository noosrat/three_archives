package common.model;

import java.util.Random;

public class Exhibition {
	String title;
	int exhibitionid;
	int templateid;
	String[] media;
	String creator;
	String description;

	public Exhibition() {

	}
	public Exhibition(String title,int id)
	{
		this.title=title;
		this.exhibitionid=id;
		this.AutoExhibitionGenerator();
	}
	
	public int getExhibitionId()
	{
		return(this.exhibitionid);
	}

	public String getTitle() {
		return (this.title);
	}

	public String[] getMedia() {
		return this.media;
	}
	public String getTemplateid()
	{
		return (String.valueOf(this.templateid));
	}
	
	public void setTemplate(String template)
	{
		this.templateid=Integer.parseInt(template);
	}

	public void AutoExhibitionGenerator() {
		Exhibition exhibition = new Exhibition();

		this.media = new String[10];
		Random random = new Random();
		int randomNum;

		for (int i = 0; i < 10; i++) {
			randomNum = random.nextInt(15 - 1);

			this.media[i] = "/images/" + String.valueOf(randomNum) + ".jpg";

		}

	}
}