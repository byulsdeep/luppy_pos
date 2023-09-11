package frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import backend.Controller;
import beans.Product;
import beans.UserInfo;
import utility.ProjectUtils;

/*
	 * 	 등급 노예 < 매니저 < 사장님
	 A. 시작 - 로고 홈화면 -> 계속하려면 Enter
	 B. 로그인 사번, 비번
	 C. 메뉴 
	 	 1. 판매 - 노예 v
	 	 	a. 상품조회 v
	 	 	b. 장바구니 추가 v
	 	 	c. 결제 v
	 	 	d. 영수증 출력 v 
	     2. 상품관리
	     	a. 상품추가 v
	     	b. 상품수정 v
	     	c. 상품삭제 - 마무리 숙제 v
		 3. 매장관리 - 사장님
	 	   	a. 직원등록
	 	   		- 초기비번 등록일 + 노예순서
	 	   	b. 직원수정 v
	 	   	c. 직원삭제 v
	 	   	d. 매출조회 
	 	 4. 마이페이지 v
	 	 	a. 내정보 확인 / 수정 v
	 D. 종료
	 */
public class FrontEnd {

	private Scanner sc;
	private ProjectUtils pu;
	private UserInfo ui;
	private List<Product> order;

	public FrontEnd() {
		this.sc = new Scanner(System.in);
		this.pu = new ProjectUtils();
		this.run();
		this.sc.close();
	}
	
	private void run() {
		
		this.pu.println(this.getTitle());
		this.sc.nextLine();	
		while (true) {
			if (this.login()) {
				this.main();
				break;
			} else {
				this.pu.println("알 수 없는 오류");
			}			
		}
	}

	private boolean login() {
		
		String employeeCode;
		String password;
		String[] names = { "employeeCode", "password" }; 
		String serverData = null;
		
		this.pu.println(this.getTitle("로그인"));
		this.pu.println("사번을 입력해주세요: (종료: alt + F4)");
		employeeCode = this.input();
		this.pu.println("비밀번호를 입력해주세요: ");
		password = this.input();
		
		// login?employeeCode=looppy&password=1234
		serverData = new Controller().entrance(this.pu.makeTransferData("login", names, new String[] { employeeCode, password }));
		if (!serverData.equals("fail")) {
			// employeeCode, name, grade
			String[][] extractedData = this.pu.extractData(serverData);
			this.ui = new UserInfo(extractedData[0][1], extractedData[1][1], extractedData[2][1]);
			return true;
		}
		return false;
	}
	
	private void main() {
		String select = null;
		String serverData = null;
		
		this.pu.println(this.getTitle(this.ui.getName() + "님 (" + this.ui.getGrade() + ") 환영합니다!"));

		boolean run = true;
		while (run) {
			this.pu.println(this.getMenu(new String[] { "상품판매", "상품관리", "매장관리", "마이페이지" }));
			select = this.input();
			switch (select) {
			case "1": // 상품판매
				this.sales();
				break;
			case "2": // 상품관리
				this.productManagement();
				break;
			case "3": // 매장관리
				this.storeManagement();
				break;
			case "4": // 마이페이지
				this.myPage();
				break;
			case "0": // 종료
				run = false;
				this.pu.println("종료");
				break;
			default:
				this.pu.println("이상한짓 하지마세요");
				break;
			}
		}
	}
	private void productManagement() {
		boolean run = true;
		String select = null;
		
		this.pu.println(getTitle("상품관리"));
		while (run) {
			this.pu.println(this.getMenu(new String[] { "상품추가", "상품수정", "상품삭제" }));
			select = this.input();
			switch (select) {
			case "1":
				this.addProduct();
				break;
			case "2":
				break;
			case "3":
				this.deleteProduct();
				break;
			case "0":
				run = false;
				break;
			default: 
				break;
			}
		}
	}
	private void deleteProduct() {
		String productCode = null;
		String serverData = null;
		List<Product> productList = new ArrayList<>();
		
		// UserInfo에서 등급확인 (매니저 이상부터 가능)
		
		if (this.ui.getGrade().equals("노예")) {
			this.pu.println("권한이 없습니다.");
			return;
		} 
		// 전체 상품 리스트 가져오기
		serverData = new Controller().entrance("getAllProductInfo");
		StringTokenizer st = new StringTokenizer(serverData, "=&");
		Product product;
		while (st.hasMoreTokens()) {
			product = new Product(); st.nextToken();
			product.setProductCode(st.nextToken()); st.nextToken();	
			product.setProductName(st.nextToken()); st.nextToken();
			product.setCategory(st.nextToken()); st.nextToken();
			product.setPrice(Integer.parseInt(st.nextToken())); st.nextToken();
			product.setStock(Integer.parseInt(st.nextToken()));
			productList.add(product);
		}
//		for (Product p : proudctList) {}
//		extends Object
//		Set List Queue Map
		// 숙제1 : 상품수정 잡 완성
		// 숙제2 : 위의 코드가 정상작동하는지 테스트하고 list 내용 출력해보기
		// 지울 상품 상품코드 서버로 보내기
		// 메모장에서 해당상품 제거하기
		// 성공여부 리턴하기
	}
	private void addProduct() {
		String[] labels = { "상품코드", "상품이름", "카테고리", "가격", "재고" };
		String[] values = new String[5];
		String serverData = null;
		// 상품코드 + 1 가져오기
		// getNewProductCode
		values[0] = new Controller().entrance("getNewProductCode");
		
		// 추가할 상품정보 요청 makeTransferData로 작성
//		0001,루피가죽,가공식품,200000,17
		for (int i = 1; i < 5; i++) {
			this.pu.println(labels[i] + ": ");
			values[i] = this.input();
		}
		this.pu.println("상품을 등록합니다 (Enter)");
		this.input();
//		addProduct?productCode=0004&name=testName&category=testCategory&price=1000&stock=20
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"addProduct", 
				new String[] { "productCode", "name", "category", "price", "stock" }, 
				values));
		System.out.println(serverData);
		// 성공실패 여부 받아오고 출력하기
		String[] names = { };
		
//		0001,루피가죽,가공식품,200000,12
	}
	private void storeManagement() {
		boolean run = true;
		String select = null;
		
		this.pu.println(getTitle("매장관리"));
		while (run) {
			this.pu.println(this.getMenu(new String[] { "직원등록", "직원수정", "직원삭제", "매출조회" }));
			select = this.input();
			switch (select) {
			case "1":
				break;
			case "2":
				break;
			case "3":
				break;
			case "4":
				this.viewSales();
				break;
			case "0":
				run = false;
				break;
			default: 
				break;
			}
		}
	}
	private void viewSales() {
		boolean run = true;
		String select = null;
		
		this.pu.println(getTitle("매출조회"));
		while (run) {
			this.pu.print(this.getMenu(new String[] { "월별조회", "기간별조회", "일별조회" }));
			select = this.input();
			switch (select) {
			case "1":
				this.viewSalesMonthly();
				break;
			case "2":
				this.viewSalesByPeriod();
				break;
			case "3":
				break;
			case "0":
				run = false;
				break;
			default: 
				break;
			}
		}
	}
	private void viewSalesByPeriod() {
		String serverData = null;
		String[] dates = new String[2];
		
		if (!this.ui.getGrade().equals("사장님")) {
			this.pu.println("권한이 없습니다.");
			return;
		}
		this.pu.println("시작일을 입력해주세요: (yyyy-MM-dd)");
		dates[0] = this.input();
		this.pu.println("종료일을 입력해주세요: (yyyy-MM-dd)");
		dates[1] = this.input();
		
//		viewSalesByPeriod?startDate=2023-08-30&endDate=2023-09-01
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"viewSalesByPeriod", 
				new String[] { "startDate", "endDate" },
				dates
				));
		System.out.println(dates[0] + " ~ " + dates[1] + "의 매출: " + serverData);
	}
	private void viewSalesMonthly() {
//		0. 권한 확인
//		1. 월 선택 입력받기
//		2. 해당 월 정보 가져오기 (메모장에서 컬렉션 객체로 변환)
//		3. 매출 계산 (해당월 매출 전부 합치기)
//		4. 출력
		String serverData = null;
		String month = null;
		
		if (!this.ui.getGrade().equals("사장님")) {
			this.pu.println("권한이 없습니다.");
			return;
		}
		this.pu.println("조회 할 월을 선택해주세요: ");
		month = this.input(); 
		month = month.length() < 2 ? "0" + month : month;
//		getMonthlySales?month=9
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"getMonthlySales", 
				"month", 
				month)
				);
		if (!serverData.equals("fail")) {
			this.pu.println(month + "월 매출은 ￥" + serverData + "입니다.");			
		} else {
			this.pu.println("매출정보를 가져오지 못했습니다.");
		}
	}
	private void myPage() {
		boolean run = true;
		String select = null;
		
		this.pu.println(getTitle("마이페이지"));
		this.printUserInfo();
		while (run) {
			this.pu.print(this.getMenu(new String[] { "정보수정" }));
			select = this.input();
			switch (select) {
			case "1":
				if (this.updateUserInfo()) this.printUserInfo();
				break;
			case "0":
				run = false;
				break;
			default: 
				break;
			}
		}
	}
	private void printUserInfo() {
//		4444,4444,beaver,사장님
//		사번,비번,이름,등급 (노예,매니저,사장님)
//		이름: beaver
//		등급: 사장님
//		사번: 4444
		StringBuilder sb = new StringBuilder();
		sb.append("이름: ");
		sb.append(this.ui.getName());
		sb.append("\n");
		sb.append("등급: ");
		sb.append(this.ui.getGrade());
		sb.append("\n");
		sb.append("사번: ");
		sb.append(this.ui.getEmployeeCode());
		sb.append("\n");
		this.pu.print(sb.toString());
	}
	private boolean updateUserInfo() {
		String serverData = null;
		
		this.pu.println("변경할 이름을 입력해주세요: ");
		String name = this.input();
		this.pu.println("새로운 비밀번호를 입력해주세요: ");
		String password = this.input();
//		입력: updateUserInfo?employeeCode=4444&name=루피&password=2222
//		전체 List<UserInfo> 가져오고, 해당 사원 정보 수정, 수정된 List를 메모장에 덮어쓰기
//		출력: 사번,이름,등급
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"updateUserInfo", 
				new String[] { "employeeCode", "name", "password" }, 
				new String[] { this.ui.getEmployeeCode(), name, password }
				));
		if (!serverData.equals("fail") && serverData != null) {
			this.ui.setName(this.pu.extractData(serverData)[1][1]);
			this.pu.println("수정이 완료됐습니다.");
			return true;
		} else {
			this.pu.println("네트워크 오류.");
		}
		return false;
	}
	private void sales() {
		order = new ArrayList<>();
		String select = null;
		String serverData = null;
		int total = 0;
		
		this.pu.println(this.getTitle("상품판매"));
		boolean run = true;
		while (run) {
			this.pu.println(this.getMenu(new String[] { "상품결제", "수량변경" }));
			this.pu.println("상품코드를 입력해주세요:");
			
			select = this.input();
			
			switch (select) {
			case "1":
				if (!this.checkOut(total)) this.printOrder();
				break;
			case "2":
				this.modifyOrder();
				total = this.printOrder();
				break;
			case "0":
				run = false;
				break;
			default: // 상품코드
				if (this.addProductToCart(select)) total = this.printOrder();
				break;
			}
		}
		
		// 상품코드 입력시 
		// 상품정보 상품코드/상품이름/카테고리
		// 바코드 -> 상품추가 / 1. -> 주문완료 / 2. -> 주문수정 / 0. 돌아가기
		// 상품 갱신될 때 마다 전체 주문 재출력
		// 2. -> 주문수정 : 상품 앞에 번호로 상품을 선택해서 수량 변경 가능. 
	}
	private boolean checkOut(int total) {

		int JCB = 0;
		String serverData = null;
		String[] names = new String[this.order.size() * 2];
		String[] values = new String[names.length];
		
		for (Product pro : this.order) {
			if (pro.getQuantity() > pro.getStock()) {
				this.pu.println("재고가 부족합니다.");
				return false;
			}
		}
		this.pu.println("지불할 금액을 입력해주세요: ");
		JCB = Integer.parseInt(this.input());
		if (total > JCB) {
			this.pu.println("잔액이 부족합니다.");
			return false;
		}
		this.pu.println("거스름돈 ₩" + (JCB - total) + "입니다.");
//		insertSalesInfo?date=2023-08-30&time=20:42&total=2000
//		매출 입력
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"insertSalesInfo", 
				new String[] { "date", "time", "total" }, 
				new String[] { this.pu.getDate(), this.pu.getTime(), String.valueOf(total) }
				));
		if (serverData.equals("fail")) return false;
//		재고 수정
//		updateStock?productCode=0001&stock=5&productCode=0002&stock=10&productCode=0003&stock=5
		for (int i = 0; i < names.length; i++) {
			names[i] = "productCode";
			i++;
			names[i] = "stock";
		}
		for (int i = 0, j = 0; i < this.order.size(); i++, j++) {
			values[j] = String.valueOf(this.order.get(i).getProductCode());
			j++;
			values[j] = String.valueOf(this.order.get(i).getStock() - this.order.get(i).getQuantity());
		}
		serverData = new Controller().entrance(this.pu.makeTransferData(
				"updateStock", 
				names,
				values
				));
		if (serverData.equals("fail")) return false;
		this.pu.println("결제가 완료되었습니다.");
		
		this.printReceipt(JCB);
		this.order = new ArrayList<>();

		return true;
	}
	
	private void printReceipt(int JCB) {
		StringBuilder sb = new StringBuilder();
		sb.append("NO. 상품이름\t상품코드\t\t카테고리\t단가\t수량\t금액\n");
		int total = 0;
		for (int i = 0; i < this.order.size(); i++) {
			sb.append(String.valueOf(i + 3));
			sb.append(". ");
			String name = this.order.get(i).getProductName();
			sb.append(name + (name.length() < 4 ? " ".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(this.order.get(i).getProductCode());
			sb.append("\t");
			sb.append(this.order.get(i).getCategory());
			sb.append("\t₩");
			sb.append(this.order.get(i).getPrice());
			sb.append("\t");
			sb.append("x");
			sb.append(this.order.get(i).getQuantity());
			sb.append("\t₩");
			int amount = this.order.get(i).getPrice() * this.order.get(i).getQuantity();
			sb.append(amount);
			total += amount;
			sb.append("\n");
		}
		sb.append("-------------------------------------------\n");
		sb.append("합계 금액:\t₩");
		sb.append(total);
		sb.append("\n-------------------------------------------\n");
		sb.append("받은 금액:\t₩");
		sb.append(JCB);
		sb.append("\n-------------------------------------------\n");
		sb.append("거스름돈:\t₩");
		sb.append(JCB - total);
		sb.append("\n*************************************");
		this.pu.println(sb.toString());
	}
	private void modifyOrder() {
		this.pu.println("수정할 상품 NO.를 선택해 주세요: ");
		String select = this.input();
		Product selectedProduct = this.order.get(Integer.parseInt(select) - 3);
		this.pu.println("변경할 수량: ");
		select = this.input();
		selectedProduct.setQuantity(Integer.parseInt(select));
	}
	private boolean addProductToCart(String productCode) {
		String serverData;
//		 getProductInfo?productCode=snack0001
		serverData = new Controller().entrance(this.pu.makeTransferData("getProductInfo", "productCode", productCode));
		if (!serverData.equals("fail")) {
			String[][] extractedData = this.pu.extractData(serverData);
			// List<Product> order
			boolean exists = false;
			for (int i = 0; i < this.order.size(); i++) {
				if (this.order.get(i).getProductCode().equals(extractedData[0][1])) {
					this.order.get(i).setQuantity(this.order.get(i).getQuantity() + 1);
					exists = true;
					break;
				}
			}
			if (!exists) {
				this.order.add(new Product(extractedData[0][1], extractedData[1][1], extractedData[2][1], Integer.parseInt(extractedData[3][1]), Integer.parseInt(extractedData[4][1])));								
			}
			return true;
		} else {
			return false; 
		}
	}
	
	private int printOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append("NO. 상품이름\t상품코드\t\t카테고리\t단가\t재고\t수량\t금액\n");
		int total = 0;
		for (int i = 0; i < this.order.size(); i++) {
			sb.append(String.valueOf(i + 3));
			sb.append(". ");
			String name = this.order.get(i).getProductName();
			sb.append(name + (name.length() < 4 ? " ".repeat(4 - name.length()) : ""));
			sb.append("\t");
			sb.append(this.order.get(i).getProductCode());
			sb.append("\t");
			sb.append(this.order.get(i).getCategory());
			sb.append("\t₩");
			sb.append(this.order.get(i).getPrice());
			sb.append("\t");
			sb.append(this.order.get(i).getStock());
			sb.append("\t");
			sb.append("x");
			sb.append(this.order.get(i).getQuantity());
			sb.append("\t₩");
			int amount = this.order.get(i).getPrice() * this.order.get(i).getQuantity();
			sb.append(amount);
			total += amount;
			sb.append("\n");
		}
		sb.append("-------------------------------------------\n");
		sb.append("합계 금액:\t\n");
		sb.append("₩");
		sb.append(total);
		sb.append("\n*************************************");
		this.pu.println(sb.toString());
		return total;
	}
	
	private String getMenu(String[] options) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < options.length; i++)
			sb.append(i + 1 + ". " + options[i] + (i % 2 == 0 ? "\t" : "\n"));
		sb.append("0. 종료");
		return sb.toString();
	}

	
	private String getTitle() {
		StringBuilder sb = new StringBuilder(); 
		sb.append("*************************************\n\n\n");
		sb.append("	LOOPY POS V1.0\n\n\n");
		sb.append("		developed by Byul\n");
		sb.append("	https://github.com/byulsdeep\n\n");
		sb.append("	Press any key to continue...\n\n");
		sb.append("*************************************");
		return sb.toString();
	}	
	
	private String getTitle(String title) {
		StringBuilder sb = new StringBuilder(); 
		sb.append("*************************************\n\n\n");
		sb.append("	");
		sb.append(title);
		sb.append("\n\n\n");
		sb.append("*************************************");
		return sb.toString();
	}
	
	private String input() {
		return this.sc.nextLine();
	}
}

