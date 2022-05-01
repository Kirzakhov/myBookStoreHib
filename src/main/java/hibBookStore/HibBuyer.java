package hibBookStore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class HibBuyer {
	@Id
	private int id;
	private String name;
	private long phone;
	private String email;
	private String address;
	private String bname;
	private String aname;
	private int copies;
	private String orderdate;
	public int getId() {
		return id;
	}
	public void setId(int buyerId) {
		this.id = buyerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public int getCopies() {
		return copies;
	}
	public void setCopies(int copies) {
		this.copies = copies;
	}
	public String getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}
	@Override
	public String toString() {
		return "HibBuyer [id=" + id + ", name=" + name + ", phone=" + phone + ", email=" + email + ", address="
				+ address + ", bname=" + bname + ", aname=" + aname + ", copies=" + copies + ", orderdate=" + orderdate
				+ "]";
	}
	public static void trending(Session s) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("select distinct bname, aname from HibBuyer order by orderdate desc limit 3");
		q.setCacheable(true);
		List<Object[]> list = q.list();
		System.out.println("Book Name\tAuthor Name");
		for(Object[] obj : list)
			System.out.println(obj[0] + "\t\t" + obj[1]);
		s.getTransaction().commit();
	}
	public static void placeOrder(Session s, int buyerId, String buyerName, String buyerEmail, long buyerPhone, String buyerAddress,
			String bookName, String authorName, int copies2) {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(c.getTime());
		s.beginTransaction();
		HibBuyer hb = new HibBuyer();
		hb.setId(buyerId);
		hb.setName(buyerName);
		hb.setEmail(buyerEmail);
		hb.setPhone(buyerPhone);
		hb.setAddress(buyerAddress);
		hb.setBname(bookName);
		hb.setAname(authorName);
		hb.setCopies(copies2);
		hb.setOrderdate(date);
		s.save(hb);
		s.getTransaction().commit();
		
	}
	
}
