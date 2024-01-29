package Model;
 
public class User {
  //  private String fullname;
    private String username;
    private String password;
   // private String userId;
    
	public User() {
		super();
	}
	public User(String username, String password, String fullname , String userId) {
		super();
		//this.fullname = fullname;
		this.username = username;
		this.password = password;
     //   this.userId =userId;
	}

    public String getUsername() {
        return username;
    }

     

    
    // public String getUserId() {
    //     return userId;
    // }
    // public void setUserId(String userId) {
    //     this.userId = userId;
    // }
    public void setUsername(String username) {
        this.username = username;
    }

    // public String getFullname() {
    //     return fullname;
    // }

    // public void setFullname(String fullname) {
    //     this.fullname = fullname;
    // }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }     
     
}