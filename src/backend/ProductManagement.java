package backend;

import java.util.List;

import beans.Product;
import db.DataAccessObject;
import utility.ProjectUtils;

public class ProductManagement {

	private ProjectUtils pu;
	
	public String backController(String jobCode, String parameters) {
		
		String serverData = null;
		
		this.pu = new ProjectUtils();
		switch (jobCode) {
		case "getNewProductCode":
			serverData = this.getNewProductCode();
			break;
		case "addProduct":
			serverData = this.addProduct(parameters);
			break;
		case "getAllProductInfo":
			serverData = this.getAllProductInfo();
		default:
			break;
		}
		return serverData;
	}
	private String getAllProductInfo() {
		StringBuilder sb = new StringBuilder();
		List<Product> productList = new Sales().getAllProductInfo();
//		0005,루피장난감,완구,10000,5
		int index = 0;
		for (Product p : productList) {
			
			sb.append("productCode=");
			sb.append(p.getProductCode());
			sb.append("&name=");
			sb.append(p.getProductName());
			sb.append("&category=");
			sb.append(p.getCategory());
			sb.append("&price=");
			sb.append(p.getPrice());
			sb.append("&stock=");
			sb.append(p.getStock());
			sb.append(index < productList.size() - 1 ? "&" : "");
			index++;
		}
		return sb.toString();
	}
	private String addProduct(String parameters) {
//		productCode=0004&
//		name=testName&
//		category=testCategory&
//		price=1000&
//		stock=20
		String serverData = "fail";
		String[][] productInfo = this.pu.extractData(parameters);
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "products.txt", true)) {
//			String productCode, String productName, String category, int price, int stock
			serverData = dao.addProduct(new Product(
					productInfo[0][1], 
					productInfo[1][1],
					productInfo[2][1],
					Integer.parseInt(productInfo[3][1]),
					Integer.parseInt(productInfo[4][1]))) ? "success" : "fail";
		}
		dao.fileClose(false);
		return serverData;
	}
	private String getNewProductCode() {
		String newProductCode = "fail";
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			newProductCode = dao.getNewProductCode();
		}
		dao.fileClose(true);
		return newProductCode;
	}
}
