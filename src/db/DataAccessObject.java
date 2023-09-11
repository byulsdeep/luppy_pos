package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import beans.Product;
import beans.UserInfo;
import utility.ProjectUtils;

public class DataAccessObject {
	File file;
	FileReader reader;
	FileWriter writer;
	BufferedReader bReader;
	BufferedWriter bWriter;
	ProjectUtils pu;
	
	public DataAccessObject() {
		this.pu = new ProjectUtils();
	}
	
	public int viewSalesByPeriod(Date[] inputDates, SimpleDateFormat sdf) {
		String currentLine = null;
		int salesByPeriod = 0;
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				2023-08-30,21:41,200000
//				날짜가 그 기간에 있다는것 
//				2023-08-30 ~ 2023-09-01 
//				2023-08-30 <= 날짜 <= 2023-09-01 
				String[] split = currentLine.split(",");
				Date currentDate = sdf.parse(split[0]);
				if (inputDates[0].compareTo(currentDate) <= 0
					&&
					currentDate.compareTo(inputDates[1]) <= 0
						) {
					salesByPeriod += Integer.parseInt(split[2]);
				}
			}
		} catch (Exception e) {}
		return salesByPeriod;
	}
	
	public String getMonthlySales(String month) {
		String currentLine = null;
		BigInteger monthlySales = BigInteger.ZERO;
		BigInteger dailySales;
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				2023-08-30,21:41,200000
				dailySales = BigInteger.valueOf(Integer.parseInt(currentLine.split(",")[2]));
				
				if (currentLine.split("-")[1].equals(month)) {
					monthlySales = monthlySales.add(dailySales);
				}
			}
		} catch (IOException e) {
			return "fail";
		}
		return String.valueOf(monthlySales);
	}
	
	public boolean addProduct(Product productInfo) {
		StringBuilder sb = new StringBuilder();
//		0001,루피가죽,가공식품,200000,12
		sb.append(productInfo.getProductCode());
		sb.append(",");
		sb.append(productInfo.getProductName());
		sb.append(",");
		sb.append(productInfo.getCategory());
		sb.append(",");
		sb.append(productInfo.getPrice());
		sb.append(",");
		sb.append(productInfo.getStock());
		try {
			this.bWriter.write(sb.toString());
			this.bWriter.write("\n");
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public String getNewProductCode() {
		String currentLine = null;
		String newProductCode = "0000";
		try {
			while ((currentLine = this.bReader.readLine()) != null) {
//				0001,루피가죽,가공식품,200000,12
				newProductCode = currentLine.substring(0, currentLine.indexOf(",")); 
			}
		} catch (IOException e) {
			return "fail";
		}
		int intValue = Integer.parseInt(newProductCode) + 1;
		newProductCode = String.valueOf(intValue);
		return intValue < 10 ? "000" + newProductCode : intValue < 100 ? "00" + newProductCode : intValue < 1000 ? "0" + newProductCode : intValue < 10000 ? newProductCode : "full";		
	}
	
	public boolean updateStock(List<Product> allProducts) {
		StringBuilder sb = null;
		for (Product p : allProducts) {
			sb = new StringBuilder();
//			processed0001,루피가죽,가공식품,200000,15
			sb.append(p.getProductCode());
			sb.append(",");
			sb.append(p.getProductName());
			sb.append(",");
			sb.append(p.getCategory());
			sb.append(",");
			sb.append(p.getPrice());
			sb.append(",");
			sb.append(p.getStock());
			try {
				this.bWriter.write(sb.toString());
				this.bWriter.write("\n");
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	public List<Product> getAllProductInfo() {
		List<Product> allProducts = new ArrayList<>();
		String record = null;
		try {
			while ((record = this.bReader.readLine()) != null) {
//				processed0001,루피가죽,가공식품,200000,15
				String[] splitRecord = record.split(",");
//				public Product(String productCode, String productName, String category, int price, int stock) {
				allProducts.add(new Product(
						splitRecord[0],
						splitRecord[1],
						splitRecord[2],
						Integer.parseInt(splitRecord[3]),
						Integer.parseInt(splitRecord[4])
						));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return allProducts;
	}
	public String insertSalesInfo(String[][] extractedData) {
		this.pu.println("dao executed");
//		insertSalesInfo?date=2023-08-30&time=20:42&total=2000
//		2023-08-30,20:56,200000
		StringBuilder sb = new StringBuilder();
		sb.append(extractedData[0][1]);
		sb.append(",");
		sb.append(extractedData[1][1]);
		sb.append(",");
		sb.append(extractedData[2][1]);
		this.pu.println("sb.toString(): " + sb.toString());
		try {
			this.bWriter.write(sb.toString());
			this.bWriter.newLine();
			return "success";
		} catch (Exception e) {
			this.pu.println("쓰기실패");
		}
		return "fail";
	}
	public String getProductInfo(String[][] extractedData) {
		String record = null;
		String[] recordSplit = null;
		
		try {
			while ((record = this.bReader.readLine()) != null) {
				recordSplit = record.split(",");
//				getProductInfo?productCode=snack0001
//				루피가죽,processed0001	,가공식품
				if (recordSplit[0].equals(extractedData[0][1])) {
//					"productCode=processed0001&productName=루피가죽&category=가공식품";
					return this.pu.makeTransferData(new String[] { "productCode", "productName", "category", "price", "stock" }, recordSplit);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}
	public String updateUserInfo(List<UserInfo> userList) {
		StringBuilder sb = null;
		for (UserInfo ui : userList) {
			sb = new StringBuilder();
			sb.append(ui.getEmployeeCode());
			sb.append(",");
			sb.append(ui.getPassword());
			sb.append(",");
			sb.append(ui.getName());
			sb.append(",");
			sb.append(ui.getGrade());
			try {
				this.bWriter.write(sb.toString());
				this.bWriter.write("\n");
			} catch (Exception e) {
				return "fail";
			}
		}
		return "success";
	}
	
	public List<UserInfo> getUserList() {
		List<UserInfo> userList = new ArrayList<>();
		String record = null;
		String[] recordSplit = null;
		try {
			while ((record = this.bReader.readLine()) != null) {
				recordSplit = record.split(",");
//				9797,9797,looppy,노예
				userList.add(new UserInfo(recordSplit[0], recordSplit[1], recordSplit[2], recordSplit[3]));
			}
		} catch (Exception e) {}
		return userList;
	}
	
	public String login(String[][] extractedData) {
		String record = null;
		String[] recordSplit = null;
		try {
			while ((record = this.bReader.readLine()) != null) {
				// 9797,9797,looppy,노예
				// employeeCode, password, name, grade?
				recordSplit = record.split(",");
				if (recordSplit[0].equals(extractedData[0][1]) && recordSplit[1].equals(extractedData[1][1])) 
					return "employeeCode=" + recordSplit[0] + "&name=" + recordSplit[2] + "&grade=" + recordSplit[3]; 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "fail";
	}
	public boolean fileConnection(boolean readOrWrite, String fileName, boolean append) {
		boolean result;
		String ap = new File("").getAbsolutePath();
		this.file = new File(ap + "\\src\\db\\" + fileName);
		try {
			if (readOrWrite) {
				this.bReader = new BufferedReader(new FileReader(this.file));
			} else {
				if (append) {
					this.writer = new FileWriter(this.file, true); // 파일 내용 추가
				} else {
					this.writer = new FileWriter(this.file); // 파일 내용 덮어쓰기
				}
				this.bWriter = new BufferedWriter(this.writer);
			}
			result = true;
		} catch (Exception e) {
			System.out.println("fileConnection: ");
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	public void fileClose(boolean readOrWrite) {
		if (readOrWrite) {
			try {
				if (this.bReader != null) {
					this.bReader.close();
				}
			} catch (Exception e2) {
			}
			try {
				if (this.reader != null) {
					this.reader.close();
				}
			} catch (Exception e) {
			}
		} else {
			try {
				if (this.bWriter != null) {
					this.bWriter.close();
				}
			} catch (Exception e2) {
			}
			try {
				if (this.writer != null) {
					this.writer.close();
				}
			} catch (Exception e) {
			}
		}
	}
}
