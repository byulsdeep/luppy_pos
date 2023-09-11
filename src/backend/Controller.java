package backend;

public class Controller {

	// login?employeeCode=looppy&password=1234
	public String entrance(String clientData) {
		String[] clientDataSplit = clientData.split("\\?");
		String jobCode = clientDataSplit[0];
		String parameters = clientDataSplit.length > 1 ? clientDataSplit[1] : null;
		
		String serverData = null;
		switch (jobCode) {
		case "login":
		case "moveMyPage":
		case "updateUserInfo":
			serverData = new Authentication().backController(jobCode, parameters);
		case "logout":
			break;
		case "getProductInfo":
		case "insertSalesInfo":
		case "updateStock":
			serverData = new Sales().backController(jobCode, parameters);
			break;
		case "getNewProductCode":
		case "addProduct":
		case "getAllProductInfo":
			serverData = new ProductManagement().backController(jobCode, parameters);
			break;
		case "getMonthlySales":
		case "viewSalesByPeriod":
			serverData = new StoreManagement().backController(jobCode, parameters);
			break;
		default:
			break;
		}
		return serverData;
	}
}
