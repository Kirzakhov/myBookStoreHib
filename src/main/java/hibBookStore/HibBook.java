package hibBookStore;

import java.util.Iterator;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class HibBook {
	@Id
	private int id;
	private String bname;
	private String aname;
	private double price;
	private int stock;
	private int sold;
	@ManyToOne
	private HibSeller seller;
	public int getId() {
		return id;
	}
	public void setId(int bookId) {
		this.id = bookId;
	}
	public String getBname() {
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAname() {
		return aname;
	}
	public void setAname(String aname) {
		this.aname = aname;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getSold() {
		return sold;
	}
	public void setSold(int sold) {
		this.sold = sold;
	}
	public HibSeller getSeller() {
		return seller;
	}
	public void setSeller(HibSeller seller) {
		this.seller = seller;
	}
	@Override
	public String toString() {
		return "HibBook [id=" + id + ", bname=" + bname + ", aname=" + aname + ", price=" + price + ", stock=" + stock
				+ ", sold=" + sold + "]";
	}
	public static void addBookSeller(Session s, int bookId, String bName2, String aName2, float bPrice, int bStock, int sellerId, String sellerName, String sellerEmail, long sellerPhone) {
		// TODO Auto-generated method stub
		
		HibBook hb = new HibBook();
		hb.setId(bookId);
		hb.setBname(bName2);
		hb.setAname(aName2);
		hb.setPrice(bPrice);
		hb.setStock(bStock);
		HibSeller hs = new HibSeller();
		if(!hs.checkSeller(s, sellerEmail)) {
			hs.setId(sellerId);
			hs.setName(sellerName);
			hs.setEmail(sellerEmail);
			hs.setPhone(sellerPhone);
		}
		else {
			Query q = s.createQuery("from HibSeller where email=?");
			q.setParameter(0, sellerEmail);
			hs = (HibSeller) q.uniqueResult();
		}
		hs.getBooks().add(hb);
		hb.setSeller(hs);
		s.beginTransaction();
		s.save(hs);
		s.save(hb);
		s.getTransaction().commit();
		System.out.println("Seller added successfully");
		System.out.println("Book added successfully");
	}
	public static void display(Session s) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("select bname, aname, price, stock from HibBook");
		q.setCacheable(true);
		List<Object[]> list = q.list();
		s.getTransaction().commit();
		System.out.println("Book Name\tAuthor Name\tPrice in Rs.\tStock");
		for(Object[] obj : list) {
			System.out.println(obj[0]+"\t\t"+obj[1]+"\t\t"+obj[2]+"\t\t"+obj[3]);
		}
		
	}
	public static int checkAvailability(Session s, String bookName, String authorName, int copies) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("select id, price, stock from HibBook where bname= :bn and aname= :an");
		q.setCacheable(true);
		q.setParameter("bn", bookName);
		q.setParameter("an", authorName);
		List<Object[]> hb = q.list();
		s.getTransaction().commit();
		double minPrice = Double.MAX_VALUE;
		int id;
		int tId = 0;
		double price;
		int stock;
		Iterator<Object[]> itr = hb.iterator();
		while(itr.hasNext()) {
			Object[] obj = itr.next();
			id = (Integer) obj[0];
			price = (Double) obj[1];
			stock = (Integer) obj[2];
			if(price<minPrice && copies<=stock) {
				minPrice = price;
				tId = id;
			}
		}
		return tId;
	}
	public static double getPrice(Session s, int id2) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("select price from HibBook where id=:i");
		q.setCacheable(true);
		q.setParameter("i", id2);
		double p = (Double) q.uniqueResult();
		s.getTransaction().commit();
		return p;
	}
	public static void updateStock(Session s, int id2, int copies) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("select stock, sold from HibBook where id= :i");
		q.setCacheable(true);
		q.setParameter("i", id2);
		Object[] obj = (Object[]) q.uniqueResult();
		int st = (Integer) obj[0];
		int so = (Integer) obj[1];
		Query q1 = s.createQuery("update HibBook set stock= :st, sold= :so where id= :id");
		q1.setParameter("st", st-copies);
		q1.setParameter("so", so+copies);
		q1.setParameter("id", id2);
		int i = q1.executeUpdate();
		s.getTransaction().commit();
		if(i>0)
			System.out.println("Data updated successfully");
		else
			System.out.println("Not updated");
	}
	public static String sellerInfo(Session s, int id2) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("from HibBook where id= :i");
		q.setCacheable(true);
		q.setParameter("i", id2);
		HibBook hb =  (HibBook) q.uniqueResult();
		HibSeller hs = hb.getSeller();
		String str = hs.getEmail();
//		Query q1 = s.createQuery("select email from HibSeller where id= :id");
//		q1.setParameter("id", i);
//		String str = (String) q1.uniqueResult();
		s.getTransaction().commit();
		return str;
	}
	
}
