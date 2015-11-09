/*Author: Nicole Petersen
Description: User class
*/
package common.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ArchiveUser")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//USer variables
	private int id;
	private String username;
	private String password;
	private String role;//can be admin or privileged
	
	//Constructors
	public User() {	 
	}
	
	public User(String username,String password,String role) {
		this.username=username;
		this.password=password;
		this.role=role;
 
	}
 
	//getters and setters
	public int getId() {
		return id;
	}
 
	public void setId(int id) {
		this.id = id;
	}
 
	public String getUsername() {
		return username;
	}
 
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRole(String role)
	{
		this.role = role;
	}
	public String getRole()
	{
		return role;
	}
	

}
