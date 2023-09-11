package beans;

public class UserInfo {
	
	private String employeeCode;
	private String name;
	private String grade;
	private String password;
	
	public UserInfo(String employeeCode, String name, String grade) {
		this.employeeCode = employeeCode;
		this.name = name;
		this.grade = grade;
	}
	public UserInfo(String employeeCode, String password, String name, String grade) {
		this.employeeCode = employeeCode;
		this.password = password;
		this.name = name;
		this.grade = grade;
	}	
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getEmployeeCode() {
		return this.employeeCode;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return this.name;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGrade() {
		return this.grade;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return this.password;
	}
}