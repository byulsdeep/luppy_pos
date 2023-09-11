package backend;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import db.DataAccessObject;
import utility.ProjectUtils;

public class StoreManagement {
	
	private ProjectUtils pu;
	
	public String backController(String jobCode, String parameters) {
		
		String serverData = null;
		
		this.pu = new ProjectUtils();
		switch (jobCode) {
		case "getMonthlySales":
			serverData = this.getMonthlySales(parameters);
			break;
		case "viewSalesByPeriod":
			serverData = this.viewSalesByPeriod(parameters);
			break;
		default:
			break;
		}
		return serverData;
	}
//	viewSalesByPeriod?
//	startDate=2023-08-30&endDate=2023-09-01
	private String viewSalesByPeriod(String parameters) {
		
		String[][] clientData = null;
		int salesByPeriod = 0;
		
//		String 2023-08-30 -> Date
		clientData = this.pu.extractData(parameters);
//		startDate	2023-08-30
//		endDate		2023-09-01
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		sdf -> 변환하는 기준
		Date[] dates = new Date[2];
		try {
			dates[0] = sdf.parse(clientData[0][1]);
			dates[1] = sdf.parse(clientData[1][1]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "sales.txt", false)) {
			salesByPeriod = dao.viewSalesByPeriod(dates, sdf);
		}
		dao.fileClose(true);
//		Date[] 
//		reader
//		기간별 매출 총액 string
		return String.valueOf(salesByPeriod);
	}
	
	private String getMonthlySales(String parameters) {
//	getMonthlySales?month=9
		String month = null;
		String monthlySales = "0";
		
		month = parameters.substring(parameters.indexOf("=") + 1);
		
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "sales.txt", false)) {
			monthlySales = dao.getMonthlySales(month);
		}
		dao.fileClose(true);
		return monthlySales;
	}
}
