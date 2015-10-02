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
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public User() {
		 
	};
	
	public User(String login) {
		this.login = login;
 
	};
 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
 
	private String login;
 
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public String getLogin() {
		return login;
	}
 
	public void setLogin(String login) {
		this.login = login;
	}
}