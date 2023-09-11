package utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.StringTokenizer;

public class ProjectUtils {
	
	public void print(String s) { System.out.print(s); } 
	public void println(String s) { System.out.println(s); } 

	public String makeTransferData(String jobCode, String name, String value) {
		return jobCode + "?" + name + "=" + value;
	}
	public String makeTransferData(String jobCode, String[] names, String[] values) {
		// login?employeeCode=looppy&password=1234&password=1234&password=1234
		StringBuilder sb = new StringBuilder();
		sb.append(jobCode);
		sb.append("?");
		for (int i = 0; i < values.length; i++) {
			sb.append(names[i]);			
			sb.append("=");			
			sb.append(values[i]);			
			sb.append(i < values.length - 1 ? "&" : "");			
		}
		return sb.toString();
	}
	public String makeTransferData(String[] names, String[] values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			sb.append(names[i]);			
			sb.append("=");			
			sb.append(values[i]);			
			sb.append(i < values.length - 1 ? "&" : "");			
		}
		return sb.toString();
	}
	
	public String[][] extractData(String data) {
		
		//	employeeCode=looppy&password=1234
		//  0,0          0,1    1,0      1,1
		StringTokenizer st = new StringTokenizer(data,"=&");
		String[][] extractedData = new String[ st.countTokens() / 2 ][2];
		if (st.countTokens() < 2) return null;
		
		for (int i = 0; st.hasMoreTokens(); i++) {
			extractedData[i][0] = st.nextToken(); // 0 0 employeeCode // 1 0password
			extractedData[i][1] = st.nextToken(); // 0 1 looppy		  // 1 1 1234
		}
		return extractedData;
	}
	
	public String getDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}
	public String getTime() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
	}
}
