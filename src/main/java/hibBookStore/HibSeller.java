package hibBookStore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class HibSeller {
	@Id
	private int id;
	private String name;
	private String email;
	private long phone;
	@OneToMany(mappedBy="seller")
	private List<HibBook> books = new ArrayList<HibBook>();
	public int getId() {
		return id;
	}
	public void setId(int sellerId) {
		this.id = sellerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhone() {
		return phone;
	}
	public void setPhone(long phone) {
		this.phone = phone;
	}
	public List<HibBook> getBooks() {
		return books;
	}
	public void setBooks(List<HibBook> books) {
		this.books = books;
	}
	@Override
	public String toString() {
		return "HibSeller [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + "]";
	}
	public static boolean checkSeller(Session s, String sellerEmail) {
		// TODO Auto-generated method stub
		s.beginTransaction();
		Query q = s.createQuery("from HibSeller where email= :e");
		q.setCacheable(true);
		q.setParameter("e", sellerEmail);
		HibSeller hs = (HibSeller) q.uniqueResult();
		s.getTransaction().commit();
		if(hs != null)
			return true;
		return false;
	}
	
}
