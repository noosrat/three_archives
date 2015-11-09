/*Author: Nicole Petersen
Description: Exhibition class that defines an exhibition object
*/
package common.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EXHIBITION") //specifies the name of the exhibition table in the database
public class Exhibition implements Serializable  {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	//Exhibition variables
	int id;
	String title;
	String description;
	int templateid;
	String creator;
	String media;
	String captions;
	String cover;
	String border;
	
	//constructors
	public Exhibition() {

	}
	public Exhibition(String title, String description,String media) {
		this.title=title;
		this.description=description;
		this.media=media;
	}
	public Exhibition(String title, String description, int templateid, String creator,String media,String captions, String cover, String border) {
		this.title=title;
		this.description=description;
		this.templateid=templateid;
		this.creator=creator;
		this.media=media;
		this.captions=captions;
		this.cover=cover;
		this.border=border;
	}
	
	//getters and setters
	public String getBorder(){
		return this.border;
	}
	public void setBorder(String border){
		this.border=border;
	}
	public String getCover(){
		return this.cover;
	}
	public void setCover(String cover){
		this.cover=cover;
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
}