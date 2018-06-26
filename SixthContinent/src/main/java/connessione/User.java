package connessione;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String username;
	private String password;
	
	public User() {}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
		
	}

	
	
	public List<User> creaLista(User user1, User user2) {
	List<User> credentialsList = new ArrayList<User>();
	credentialsList.add(user1);
	credentialsList.add(user2);
	 return credentialsList;
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
	}
