package backend;

import java.util.ArrayList;
import java.util.List;

import beans.UserInfo;
import db.DataAccessObject;
import utility.ProjectUtils;

public class Authentication {

	private ProjectUtils pu;
	
	public String backController(String jobCode, String parameters) {
		
		String serverData = null;
		
		this.pu = new ProjectUtils();
		switch (jobCode) {
		case "login":
			serverData = this.login(parameters);
			break;
		case "logout":
			break;
		case "updateUserInfo":
			serverData = this.updateUserInfo(parameters);
			break;
		default:
			break;
		}
		return serverData;
	}
	private String updateUserInfo(String parameters) {
		String serverData = null;
		List<UserInfo> userList = null;
		String[][] clientData = this.pu.extractData(parameters);
		
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) {
			userList = dao.getUserList();
			dao.fileClose(true);
		}
//		employeeCode=4444&
//		name=루피&
//		password=2222
		int userIndex = 0;
		for (int i = 0; i < userList.size(); i++) {
			if (clientData[0][1].equals(userList.get(i).getEmployeeCode())) {
				userIndex = i;
				userList.get(i).setName(clientData[1][1]);
				userList.get(i).setPassword(clientData[2][1]);
				break;
			}
		}
		if (dao.fileConnection(false, "employees.txt", false)) {
			serverData = dao.updateUserInfo(userList);
			dao.fileClose(false);
		}
		if (serverData.equals("success")) {
			serverData = this.pu.makeTransferData(
					new String[] { "employeeCode", "name", "grade" }, 
					new String[] { 
							userList.get(userIndex).getEmployeeCode(), 
							userList.get(userIndex).getName(),
							userList.get(userIndex).getGrade()
							});
		}
		return serverData;
	}
	private String login(String parameters) {

//		employeeCode=looppy&password=1234
		
		String serverData = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "employees.txt", false)) {
			serverData = dao.login(this.pu.extractData(parameters));
			dao.fileClose(true);
		}
		return serverData;
	}
	// 백 데이터 가공 // dao 데이터접근
	
}
