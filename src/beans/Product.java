package beans;

public class Product {
	String productCode;
	String productName;
	String category;
	int price;
	int stock;
	
	int quantity;
	
	public Product() {}
	public Product(String productCode, String productName, String category, int price, int stock) {
		this.productCode = productCode;
		this.productName = productName;
		this.category = category;
		this.price = price;
		this.stock = stock;
		
		this.quantity = 1;
	}
	
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
}
