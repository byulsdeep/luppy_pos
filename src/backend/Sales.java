package backend;

import java.util.List;

import beans.Product;
import db.DataAccessObject;
import utility.ProjectUtils;

public class Sales {

	private ProjectUtils pu;
	
//	insertSalesInfo
//	total=2000&date=2023-08-30&time=20:42
	public String backController(String jobCode, String parameters) {
		
		String serverData = null;
		
		this.pu = new ProjectUtils();
		switch (jobCode) {
		case "getProductInfo":
			serverData = this.getProductInfo(parameters);
			break;
		case "insertSalesInfo":
			serverData = this.insertSalesInfo(parameters);
			break;
		case "updateStock":
			serverData = this.updateStock(parameters);
		default:
			break;
		}
		return serverData;
	}
	private String updateStock(String parameters) {
		String serverData = null;
//		1. db에서 상품 정보 가져와서 List로 만들기
		List<Product> allProducts = this.getAllProductInfo();
//		2. List를 Front에서 가져온 데이터로 업데이트
//		productCode=0001
//		stock=5
//		productCode=0002
//		stock=10
//		productCode=0003
//		stock=5
		String[][] clientData = this.pu.extractData(parameters);
		for (int i = 0; i < clientData.length; i += 2) {
			for (int j = 0; j < allProducts.size(); j++) {
				if (clientData[i][1].equals(allProducts.get(j).getProductCode())) {
					allProducts.get(j).setStock(Integer.parseInt(clientData[i + 1][1]));
					break;
				}
			}
		}
		for (Product p : allProducts) {
			System.out.println(p.getProductCode() + ":" + p.getStock());
		}
//		3. List를 활용해서 db 업데이트
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "products.txt", false)) {
			serverData = dao.updateStock(allProducts) ? "success" : "fail";
			dao.fileClose(false);
		}
		return serverData;
	}
	List<Product> getAllProductInfo() {
		List<Product> allProducts = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			allProducts = dao.getAllProductInfo();
			dao.fileClose(true);
		}
		return allProducts;
	}
	private String insertSalesInfo(String parameters) {
		System.out.println("sales parameters" + parameters);
		String serverData = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(false, "sales.txt", true)) {
			this.pu.println("file connected");
//			date=2023-08-30&time=20:42&total=2000
			serverData = dao.insertSalesInfo(this.pu.extractData(parameters));
			dao.fileClose(false);
		}
		return serverData;
	}
	private String getProductInfo(String parameters) {
		String serverData = null;
		DataAccessObject dao = new DataAccessObject();
		if (dao.fileConnection(true, "products.txt", false)) {
			serverData = dao.getProductInfo(this.pu.extractData(parameters));
			dao.fileClose(true);
		}
		return serverData;
	}
	
}
