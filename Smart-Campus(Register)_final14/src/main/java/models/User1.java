package models;

public class User1 {
  private int user_id;
  private String username;
  private String password;
  private String major;
  private String studentId;
  private String name;
  private String phoneNumber;
  private String mostPreciousThing;

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
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

  public String getname() {
    return name;
  }

  public void setname (String name) {
    this.name = name;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  
  public String getMostPreciousThing() {
      return mostPreciousThing;
  }

  public void setMostPreciousThing(String mostPreciousThing) {
      this.mostPreciousThing = mostPreciousThing;
  }

  @Override
  public String toString() {
    return "User1 [user_id=" + user_id + ", username=" + username + ", password=" + password + ", major=" + major
        + ", studentId=" + studentId + ", name=" + name + ", phoneNumber=" + phoneNumber + "]";
  }

  public User1() { }

  public User1(String username, String password, String major, String studentId, String name, String phoneNumber, String mostPreciousThing) {
      this.username = username;
      this.password = password;
      this.major = major;
      this.studentId = studentId;
      this.name = name;
      this.phoneNumber = phoneNumber;
      this.mostPreciousThing = mostPreciousThing;  // 추가된 파라미터 초기화
  }
}
