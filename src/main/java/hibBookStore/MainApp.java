package hibBookStore;


import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class MainApp {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Configuration con = new Configuration().configure().addAnnotatedClass(HibBook.class).addAnnotatedClass(HibBuyer.class).addAnnotatedClass(HibSeller.class);
		ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry();
		SessionFactory sf = con.buildSessionFactory(reg);
		Session s = sf.openSession();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Do you want to sell or buy? S/B");
			char ch = br.readLine().toUpperCase().charAt(0);
			if(ch=='S') {
				System.out.println("**********Welcome Seller**********");
				System.out.println("Books Trending");
				HibBuyer.trending(s);
				HibBuyer.trending(s);
				System.out.println("Please enter your seller id");
				int sellerId = Integer.parseInt(br.readLine());
				System.out.println("Please enter your name");
				String sellerName = br.readLine().trim();
				System.out.println("Please enter your email");
				String sellerEmail = br.readLine().trim();
				System.out.println("Please enter your phone number");
				long sellerPhone = Long.parseLong(br.readLine());
				System.out.println("Please enter a book id");
				int bookId = Integer.parseInt(br.readLine());
				System.out.println("Enter bookname");
				String bName = br.readLine().trim();
				System.out.println("Enter author name");
				String aName = br.readLine().trim();
				System.out.println("Enter price of book");
				float bPrice = Float.parseFloat(br.readLine());
				System.out.println("Enter stock quantity");
				int bStock = Integer.parseInt(br.readLine());
				HibBook.addBookSeller(s, bookId, bName, aName, bPrice, bStock, sellerId, sellerName, sellerEmail, sellerPhone);
			}
			else if(ch=='B'){
				System.out.println("*********Welcome Buyer**********");
				System.out.println("Books Available");
				HibBook.display(s);
				System.out.println("Please enter name of the book you wanna buy");
				String bookName = br.readLine().trim();
				System.out.println("Please enter author name of the book you wanna buy");
				String authorName = br.readLine().trim();
				System.out.println("Please enter the number of copies you wanna buy");
				int copies = Integer.parseInt(br.readLine());
				int id = HibBook.checkAvailability(s, bookName, authorName, copies);
				if(id==0)
					System.out.println("Book not available in store");
				else {
					System.out.println("Please enter your buyer id");
					int buyerId = Integer.parseInt(br.readLine());
					System.out.println("Please enter your name");
					String buyerName = br.readLine().trim();
					System.out.println("Please enter your email");
					String buyerEmail = br.readLine().trim();
					System.out.println("Please enter your phone number");
					long buyerPhone = Long.parseLong(br.readLine());
					System.out.println("Please enter your address");
					String buyerAddress = br.readLine().trim();
					double price = HibBook.getPrice(s, id);
					System.out.println("Please pay the price of the book, Rs."+price);
					float pay = Float.parseFloat(br.readLine());
					if(pay==price) {
						HibBook.updateStock(s, id, copies);
						HibBuyer.placeOrder(s, buyerId, buyerName, buyerEmail, buyerPhone, buyerAddress, bookName, 
								authorName, copies);
						String em = HibBook.sellerInfo(s, id);
						System.out.println("You can follow up with seller at following mail: "+em);
					}
					else
						System.out.println("Please pay the required amount of the book");
				}
			}
			else {
				System.out.println("Please provide a proper input");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			s.close();
		}
	}

}
