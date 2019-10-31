package logic;

import java.text.ParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.sql.SQLException;

import logic.*;
import DAO.*;
import DAO.Impl.*;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/")
public class AutoShowController {

	private static CarDAO car = new CarDAOImpl(Car.class);
	private static CityDAO city = new CityDAOImpl(City.class);
	private static ClientDAO client = new ClientDAOImpl(Client.class);
	private static ColourDAO col = new ColourDAOImpl(Colour.class);
	private static CountryDAO country = new CountryDAOImpl(Country.class);
	private static LabelDAO label = new LabelDAOImpl(Label.class);
	private static ManufacturerDAO fact = new ManufacturerDAOImpl(Manufacturer.class);
	private static MaterialDAO mater = new MaterialDAOImpl(Material.class);
	private static OrderDAO order = new OrderDAOImpl(Order.class);
	private static SpecificationsDAO spec = new SpecificationsDAOImpl(Specifications.class);
	private static StatusDAO st = new StatusDAOImpl(Status.class);
	private static TestDriveDAO td = new TestDriveDAOImpl(TestDrive.class);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String getIndex(Model model) {
		List<NewOrd> orders = new ArrayList<NewOrd>();
		Collection<Status> sts = st.getAll();
		try {
			Collection<Order> ords = order.getAll();
			for (Order i : ords) {
				Client cl = client.getClientById(i.getClientId());
				String name = "reatshrdytfuy";
				if (cl != null)
					name = cl.getFirstName() + " " + cl.getLastName();
				Status stat = st.getStatusById(i.getStatus());
				if (stat == null)
					stat = new Status(34, "fgsdg");
				NewOrd temp = new NewOrd(i.getNumber(), i.getCarId(), i.getClientId(), name, 
							i.getDate(), i.getTestdrive(), stat.getStatus());
				orders.add(temp);
				if (temp == null)
					break;
			}
		} catch(SQLException ex) {
			
		}
		
		model.addAttribute("orders", orders);
		model.addAttribute("statuses", sts);
		model.addAttribute("OrderStatus", new Status());
		return "index";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public String getIndex(@RequestParam(value="id", required=true) Long id, 
			@ModelAttribute("OrderStatus") Status new_st, Model model) {
		try {
			Order ord = order.getOrderByNumber(id);
			ord.setStatus(new_st.getId());
			order.update(ord);
		} catch(SQLException ex) {
			
		}
	
		return "redirect:index";
	}
	
	@RequestMapping(value = "/clients", method = RequestMethod.GET)
	public String getClients(Model model) {
		List<NewClient> clients = new ArrayList<NewClient>();
		try {
			Collection<Client> cls = client.getAll();
			for (Client i : cls) {
				City c = city.getCityByClient(i);
				String cit = "reatshrdytfuy";
				if (c != null)
					cit = c.getCity();
				NewClient temp = new NewClient(i.getId(), i.getFirstName(), i.getLastName(), cit, 
							i.getAddress(), i.getEmail(), i.getPhone());
				clients.add(temp);
				if (temp == null)
					break;
			}
		} catch(SQLException ex) {
			
		}
		
		model.addAttribute("clients", clients);
		return "clients";
	}
	
	 @RequestMapping(value = "/client", method = RequestMethod.GET)
	 public String getStudent(@RequestParam(value="id", required=true) Long id, Model model) {
		NewClient cl = null;
		Set<Order> ords = new HashSet<Order>();
		try {
			Client ccl = client.getClientById(id);
			ords = ccl.getOrders();
			String cit = city.getCityById(ccl.getLocation()).getCity();
			cl =  new NewClient(ccl.getId(), ccl.getFirstName(), ccl.getLastName(), cit, 
					ccl.getAddress(), ccl.getEmail(), ccl.getPhone());
		} catch(SQLException ex) {
			
		}
		
		model.addAttribute("client", cl);
		model.addAttribute("orders", ords);
		return "client";
	}
}