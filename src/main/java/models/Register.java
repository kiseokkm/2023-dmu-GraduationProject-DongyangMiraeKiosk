package models;

public class Register {
	  private int Id;
	  private String Username;
	  private String  password ;
	  private String  major;
	  private String studentId;
	  private String  email;
	  private String phoneNumber;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	@Override
	public String toString() {
		return "Register [Id=" + Id + ", Username=" + Username + ", password=" + password + ", major=" + major
				+ ", studentId=" + studentId + ", email=" + email + ", phoneNumber=" + phoneNumber + "]";
	}
	public Register(int id, String username, String password, String major, String studentId, String email,
			String phoneNumber) {
		super();
		Id = id;
		Username = username;
		this.password = password;
		this.major = major;
		this.studentId = studentId;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	public Register() {
		super();
		// TODO Auto-generated constructor stub
	}
	  
	  
	  

	  
	

	
};