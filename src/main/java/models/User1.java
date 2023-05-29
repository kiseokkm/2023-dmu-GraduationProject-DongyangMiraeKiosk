// User1.java
package models;

public class User1 {
  private int user_id;
  private String Username;
  private String password;
  private String major;
  private String studentId;
  private String email;
  private String phoneNumber;

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
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
    return "User1 [user_id=" + user_id + ", Username=" + Username + ", password=" + password + ", major=" + major
        + ", studentId=" + studentId + ", email=" + email + ", phoneNumber=" + phoneNumber + "]";
  }

  public User1() {
    // 기본 생성자
  }

  public User1(int user_id, String username, String password, String major, String studentId, String email,
      String phoneNumber) {
    this.user_id = user_id;
    Username = username;
    this.password = password;
    this.major = major;
    this.studentId = studentId;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }
}
