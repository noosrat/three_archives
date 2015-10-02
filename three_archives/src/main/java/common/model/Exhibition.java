package common.model;

import java.io.Serializable;
//import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EXHIBITION")
public class Exhibition implements Serializable  {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	String title;
	String description;
	int templateid;
	String creator;
	String media;
	String captions;
	
	public Exhibition() {

	}
	public Exhibition(String title, String description,String media) {
		this.title=title;
		this.description=description;
		this.media=media;
	}
	public Exhibition(String title, String description, int templateid, String creator,String media,String captions) {
		this.title=title;
		this.description=description;
		this.templateid=templateid;
		this.creator=creator;
		this.media=media;
		this.captions=captions;
	}
	
	
	public Exhibition(String title,int exID,String tempID,String media,String creator,String description, String captions)
	{
		this.title=title;
		this.id=exID;
		this.templateid=Integer.parseInt(tempID);
		this.media=media;
		this.creator=creator;
		this.description=description;
		this.captions= captions;
	}
	public String getCaptions(){
		return this.captions;
	}
	public void setCaptions(String captions){
		this.captions=captions;
	}
	public int getId() {
	      return this.id;
	 }
	 public void setId( int id ) {
	     this.id = id;
	  }
	 public String getTitle() {
	      return title;
	 }
	 public void setTitle( String title ) {
	     this.title = title;
	  }
	 
	public void setTemplateid(String template)
	{
		this.templateid=Integer.parseInt(template);
	}
	public int getTemplateid()
	{
		return(templateid);
	}
	
	
	public void setDescription(String description)
	{
		this.description=description;
	}
	
	public String getDescription()
	{
		return(this.description);
	}
	public void setCreator(String creator)
	{
		this.creator=creator;
	}
	
	public String getCreator()
	{
		return(this.creator);
	}

	public String getMedia() {
		return this.media;
	}
	public void setMedia(String media) {
		this.media=media;
	}
	
	
	

	/*public void AutoExhibitionGenerator() {//remove this method
		Exhibition exhibition = new Exhibition();

		this.media = new String[10];
		Random random = new Random();
		int randomNum;

		for (int i = 0; i < 10; i++) {
			randomNum = random.nextInt(15 - 1);

			this.media[i] = "/images/" + String.valueOf(randomNum) + ".jpg";

		}

	}*/
}