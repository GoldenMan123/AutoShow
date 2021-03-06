package test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

import org.hibernate.HibernateException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.sql.SQLException;

import logic.*;
import DAO.*;

public class OrderDAOTest extends Assert {
	private OrderDAO tD = Factory.getInstance().getOrderDAO();
	private ClientDAO lD = Factory.getInstance().getClientDAO();
	private CarDAO cD = Factory.getInstance().getCarDAO();
	private StatusDAO sD = Factory.getInstance().getStatusDAO();
	
	@DataProvider
	public Object[][] OrderData() {
		return new Object[][] {
				{ (long)1, (long)1, "21.12.2014 12:30:00", Boolean.FALSE, 1, true },
				{ null, (long)1, "21.12.2014 12:30:00", Boolean.FALSE, 1, false },
				{ (long)1, null, "21.12.2014 12:30:00", Boolean.FALSE, 1, false },
				{ (long)1, (long)1, null, Boolean.FALSE, 1, false },
				{ (long)1, (long)1, "21.12.2014 12:30:00", Boolean.FALSE, null, false },
				{ (long)1, (long)1, "21.12.2014 12:30:00", null, 1, true },
				{ null, null, null, null, null, false }
		};
	}
	
	@Test(dataProvider="OrderData")
	public void DataTest(Long c, Long l, String sd, Boolean t, Integer s, boolean expect) {
		try {
			String fdate = "dd.MM.yyyy HH:mm:ss";
			SimpleDateFormat ff = new SimpleDateFormat(fdate);
			Date d = null;
			if (sd != null)
				d = ff.parse(sd);
			
			Integer count = tD.getAll().size();
			Order obj = new Order(count.longValue() + 1, c, l, d, t, s);

			tD.insert(obj);
			Order temp = tD.getOrderByNumber(obj.getNumber());
			assertNotNull(temp);
			assertEquals(temp.getNumber(), obj.getNumber());
			assertEquals(temp.getCarId(), obj.getCarId());
			assertEquals(temp.getClientId(), obj.getClientId());
			assertEquals(temp.getDate(), obj.getDate());
			assertEquals(temp.getTestdrive(), obj.getTestdrive());
			assertEquals(temp.getStatus(), obj.getStatus());
			
			obj.setCarId(Long.valueOf(2));
			obj.setClientId(Long.valueOf(2));
			obj.setStatus(2);
			obj.setTestdrive(true);
			tD.update(obj);
			temp = tD.getOrderByNumber(obj.getNumber());
			assertNotNull(temp);
			assertEquals(temp.getNumber(), obj.getNumber());
			assertEquals(temp.getCarId(), obj.getCarId());
			assertEquals(temp.getClientId(), obj.getClientId());
			assertEquals(temp.getDate(), obj.getDate());
			assertEquals(temp.getTestdrive(), obj.getTestdrive());
			assertEquals(temp.getStatus(), obj.getStatus());
			
			tD.delete(obj);
			temp = tD.getOrderByNumber(obj.getNumber());
			assertNull(temp);
			
			assertEquals(tD.getAll().size(), count.intValue());
			assertTrue(expect);
		} catch (HibernateException ex) {
			assertFalse(expect);
		} catch (ParseException ex) {
			System.out.print(ex);
			assertFalse(expect);
		} catch (SQLException ex) {
			assertFalse(expect);
		}
	}

	@Test
	public void getByCar() {
		try {
			Collection<Car> cs = cD.getAll();
			assertNotNull(cs);
			if (cs.size() != 0) {
				for (Car i : cs) {
					Collection<Order> temp = tD.getOrdersByCar(i);
					assertNotNull(temp);
					for (Order ii: temp) {
						assertEquals(ii.getCarId(), i.getRegNumber());
					}
				}
			} else {
				Collection<Order> os = tD.getAll();
				assertNotNull(os);
				assertFalse(os.size() != 0);
			}
		} catch (SQLException ex) {
			assertFalse(true);
		}
	}
	
	@Test
	public void getByClient() {
		try {
			Collection<Client> cs = lD.getAll();
			assertNotNull(cs);
			if (cs.size() != 0) {
				for (Client i : cs) {
					Collection<Order> temp = tD.getOrdersByClient(i);
					assertNotNull(temp);
					for (Order ii: temp) {
						assertEquals(ii.getClientId(), i.getId());
					}
				}
			} else {
				Collection<Order> os = tD.getAll();
				assertNotNull(os);
				assertFalse(os.size() != 0);
			}
		} catch (SQLException ex) {
			assertFalse(true);
		}
	}

	@Test
	public void getByStatus() {
		try {
			Collection<Status> cs = sD.getAll();
			assertNotNull(cs);
			if (cs.size() != 0) {
				for (Status i : cs) {
					Collection<Order> temp = tD.getOrdersByStatus(i);
					assertNotNull(temp);
					for (Order ii: temp) {
						assertEquals(ii.getStatus(), i.getId());
					}
				}
			} else {
				Collection<Order> os = tD.getAll();
				assertNotNull(os);
				assertFalse(os.size() != 0);
			}
		} catch (SQLException ex) {
			assertFalse(true);
		}
	}
}