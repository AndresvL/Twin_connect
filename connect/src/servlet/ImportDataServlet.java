package servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import DAO.ObjectDAO;
import controller.RestHandler;
import controller.SoapHandler;
import object.Search;
import object.rest.Address;
import object.rest.Employee;
import object.rest.HourType;
import object.rest.Material;
import object.rest.Project;
import object.rest.Relation;
import object.rest.WorkOrder;

public class ImportDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String invoiceType;
	private ArrayList<String> responseArray = null;
	private String[][] options = null;
	private Search searchObject;
	private String errorMessage = "", errorMessageExport = "";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String office = req.getParameter("offices");
		String factuurType = req.getParameter("factuurType");
		String token = (String) req.getSession().getAttribute("softwareToken");
		String session = (String) req.getSession().getAttribute("session");
		errorMessage = "";
		errorMessageExport = "";
		if (button.equals("Import")) {
			getEmployees(office, session, token);
			getMaterials(office, session, token);
			getProjects(office, session, token);
			getRelations(office, session, token);
			getHourTypes(office, session, token);
		} else {
			getWorkOrders(office, session, token, factuurType);
		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.setAttribute("error", errorMessage);
		req.setAttribute("errorExport", errorMessageExport);
		req.getSession().setAttribute("soap", "");
		rd.forward(req, resp);
	}

	public void setDelay(Boolean b) {
		Timer t = new Timer();
		if (b) {
			MyTask mTask = new MyTask();
			// This task is scheduled to run every 10 seconds

			t.scheduleAtFixedRate(mTask, 0, 10000);
		} else {
			t.cancel();
		}
	}

	class MyTask extends TimerTask {
		private int i = 0;

		@Override
		public void run() {
			i++;
			System.out.println("nummer " + i);
		}

	}

	public void getEmployees(String office, String session, String token) throws ServletException, IOException {
		// Create search object
		// Parameters: type, pattern, field, firstRow, maxRows, options
		options = new String[][] { { "ArrayOfString", "string", "office", office } };
		searchObject = new Search("USR", "*", 0, 1, 100, options);
		responseArray = SoapHandler.createSOAPFinder(session, searchObject);
		ArrayList<Employee> emp = new ArrayList<Employee>();
		// Split data from ArrayList
		for (int i = 0; i < responseArray.size(); i++) {
			String[] parts = responseArray.get(i).split(",");
			// firstName and Lastname are identical
			Employee e = new Employee(parts[1], parts[1], parts[0]);
			emp.add(e);
		}
		if (!emp.isEmpty()) {
			ObjectDAO.saveEmployees(emp, token);
			// Post data to WorkorderApp
			Boolean b = RestHandler.addData(token, emp, "employees");
			if (b) {
				errorMessage += responseArray.size() + " Employees imported<br />";
			} else {
				errorMessage += "Something went wrong with Employees<br />";
			}
		} else {
			errorMessage += "No Employees found<br />";
		}
	}

	public void getProjects(String office, String session, String token) throws ServletException, IOException {
		// Create search object
		// Parameters: type, pattern, field, firstRow, maxRows, options
		options = new String[][] { { "ArrayOfString", "string", "dimtype", "PRJ" } };
		searchObject = new Search("DIM", "*", 0, 1, 100, options);
		responseArray = SoapHandler.createSOAPFinder(session, searchObject);
		ArrayList<Project> projects = new ArrayList<Project>();
		for (int i = 0; i < responseArray.size(); i++) {
			String[] parts = responseArray.get(i).split(",");
			// XMLString
			String string = "<read><type>dimensions</type><office>" + office + "</office><dimtype>PRJ</dimtype><code>"
					+ parts[0] + "</code></read>";
			Object obj = SoapHandler.createSOAPXML(session, string, "project");
			if (obj != null) {
				Project p = (Project) obj;
				projects.add(p);
			}
		}
		if (!projects.isEmpty()) {
			ObjectDAO.saveProjects(projects, token);
			Boolean b = RestHandler.addData(token, projects, "projects");
			if (b) {
				errorMessage += responseArray.size() + " Projects imported<br />";
			} else {
				errorMessage += "Something went wrong with Projects<br />";
			}
		} else {
			errorMessage += "Office " + office + " doesn't have any projects<br />";
		}
	}

	public void getMaterials(String office, String session, String token) throws ServletException, IOException {
		// Create search object
		// Parameters: type, pattern, field, firstRow, maxRows, options
		options = new String[][] { { "ArrayOfString", "string", "office", office } };
		searchObject = new Search("ART", "*", 0, 1, 100, options);
		responseArray = SoapHandler.createSOAPFinder(session, searchObject);
		ArrayList<Material> materials = new ArrayList<Material>();
		for (int i = 0; i < responseArray.size(); i++) {
			String[] parts = responseArray.get(i).split(",");
			String string = "<read><type>article</type><office>" + office + "</office><code>" + parts[0]
					+ "</code></read>";
			Object obj = SoapHandler.createSOAPXML(session, string, "material");
			if (obj != null) {
				Material m = (Material) obj;
				materials.add(m);
			}

		}
		if (!materials.isEmpty()) {
			ObjectDAO.saveMaterials(materials, token);
			Boolean b = RestHandler.addData(token, materials, "materials");
			if (b) {
				errorMessage += responseArray.size() + " Materials imported<br />";
			} else {
				errorMessage += "Something went wrong with Materials<br />";
			}
		} else {
			errorMessage += "Office " + office + " doesn't have any materials<br />";
		}
	}

	public void getRelations(String office, String session, String token) throws ServletException, IOException {
		// Create search object
		// Parameters: type, pattern, field, firstRow, maxRows, options
		options = new String[][] { { "ArrayOfString", "string", "dimtype", "DEB" } };
		searchObject = new Search("DIM", "*", 0, 1, 100, options);
		responseArray = SoapHandler.createSOAPFinder(session, searchObject);
		ArrayList<Relation> relations = new ArrayList<Relation>();
		for (int i = 0; i < responseArray.size(); i++) {
			String[] parts = responseArray.get(i).split(",");
			String string = "<read><type>dimensions</type><office>" + office + "</office><dimtype>DEB</dimtype><code>"
					+ parts[0] + "</code></read>";
			Object obj = SoapHandler.createSOAPXML(session, string, "relation");
			if (obj != null) {
				Relation r = (Relation) obj;
				relations.add(r);
			}
		}
		if (!relations.isEmpty()) {
			ObjectDAO.saveRelations(relations, token);
			Boolean b = RestHandler.addData(token, relations, "relations");
			if (b) {
				errorMessage += responseArray.size() + " Relations imported<br />";
			} else {
				errorMessage += "Something went wrong with Relations<br />";
			}
		} else {
			errorMessage += "Office " + office + " doesn't have any relations<br />";
		}
	}

	public void getHourTypes(String office, String session, String token) throws ServletException, IOException {
		// Create search object
		// Parameters: type, pattern, field, firstRow, maxRows, options
		options = new String[][] { { "ArrayOfString", "string", "office", office },
				{ "ArrayOfString", "string", "dimtype", "ACT" } };
		searchObject = new Search("DIM", "*", 0, 1, 100, options);
		responseArray = SoapHandler.createSOAPFinder(session, searchObject);
		ArrayList<HourType> hourtypes = new ArrayList<HourType>();
		for (int i = 0; i < responseArray.size(); i++) {
			String[] parts = responseArray.get(i).split(",");
			String string = "<read><type>dimensions</type><office>" + office + "</office><dimtype>ACT</dimtype><code>"
					+ parts[0] + "</code></read>";
			Object obj = SoapHandler.createSOAPXML(session, string, "hourtype");
			if (obj != null) {
				HourType h = (HourType) obj;
				hourtypes.add(h);
			}
		}
		if (!hourtypes.isEmpty()) {
			ObjectDAO.saveHourTypes(hourtypes, token);
			Boolean b = RestHandler.addData(token, hourtypes, "hourtypes");
			if (b) {
				errorMessage += responseArray.size() + " Hourtypes imported<br />";
			} else {
				errorMessage += "Something went wrong with Hourtypes<br />";
			}
		} else {
			errorMessage += "Office " + office + " doesn't have any hourtypes<br />";
		}
	}

	@SuppressWarnings("unchecked")
	public void getWorkOrders(String office, String session, String token, String factuurType) {
		ArrayList<WorkOrder> allData = RestHandler.getData(token, "GetWorkorders", factuurType, true);
		// ArrayList<WorkOrder> temp = new ArrayList<WorkOrder>();
		int factuurAmount = 0, urenAmount = 0;
		if (allData.isEmpty()) {
			System.out.println("test");
		}
		String hourString = "<teqs>";
		for (WorkOrder w : allData) {
			String string = null;
			if (w.getProjectNr().equals("")) {
				factuurAmount++;
				Address factuur = null;
				Address post = null;
				post = ObjectDAO.getAddressID(token, "postal", w.getCustomerDebtorNr());
				factuur = ObjectDAO.getAddressID(token, "invoice", w.getCustomerDebtorNr());
				if (post == null) {
					post = factuur;
				}
				invoiceType = "FACTUUR";
				string = "<salesinvoice>" + "<header>" + "<office>" + office + "</office>" + "<invoicetype>"
						+ invoiceType + "</invoicetype>" + "<invoicedate>" + w.getCreationDate() + "</invoicedate>"
						+ "<performancedate>" + w.getWorkDate() + "</performancedate>" + "<customer>"
						+ w.getCustomerDebtorNr() + "</customer>" + "<status>" + w.getStatus() + "</status>"
						+ "<paymentmethod>" + w.getPaymentMethod() + "</paymentmethod>" + "<invoiceaddressnumber>"
						+ factuur.getAddressId() + "</invoiceaddressnumber>" + "<deliveraddressnumber>"
						+ post.getAddressId() + "</deliveraddressnumber>" + "</header>" + "<lines>";
				int i = 0;
				for (Material m : w.getMaterials()) {
					i++;
					// subCode is empty for now because werkbonapp
					// doesnt provide this function
					String subCode = "";
					string += "<line id=\"" + i + "\">" + "<article>" + m.getCode() + "</article>" + "<subarticle>"
							+ subCode + "</subarticle>" + "<quantity>" + m.getQuantity() + "</quantity>" + "<units>"
							+ m.getUnit() + "</units>" + "</line>";
				}
				string += "</lines></salesinvoice>";
				SoapHandler.createSOAPXML(session, string, "workorder");
				errorMessageExport += factuurAmount + " Invoices created";
			} else {
				String code = "PERSONAL";
				String projectNr = w.getProjectNr();
				if (projectNr.startsWith("FP")) {
					code = "DIRECT";
				}
				if (projectNr.startsWith("NF")) {
					code = "INDIRECT";
				}
				if (projectNr.startsWith("IP1000")) {
					code = "PERSONAL";
				}
				invoiceType = "UREN";

				if (!w.getHourType().equals("")) {
					// temp.add(w);
					urenAmount++;
					hourString += "<teq>" + "<header>" + "<office>" + office + "</office>"
					// Check this later
							+ "<code>" + code + "</code>" + "<user>" + w.getEmployeeNr() + "</user>" + "<date>"
							+ w.getWorkDate() + "</date>" + "<prj1>" + w.getProjectNr() + "</prj1>" + "<prj2>"
							+ w.getHourType() + "</prj2>" + "</header>" + "<lines>" + "<line type= \"TIME\">"
							+ "<duration>" + w.getDuration() + "</duration>" + "<description>" + w.getDescription()
							+ "</description>" + "</line>" + "<line type=\"QUANTITY\">" + "</line>" + "</lines></teq>";
				}
			}
		}
		hourString += "</teqs>";
		// SoapHandler.createSOAPXML(session, hourString, "workorder");
		ArrayList<Boolean> results = new ArrayList<Boolean>();
		results = (ArrayList<Boolean>) SoapHandler.createSOAPXML(session, hourString, "workorder");
		int i = 0;
		for (Boolean b : results) {
			if (b) {
				i++;
			}
		}
		if (i != 0) {
			errorMessageExport = i + " of " + urenAmount + " Uurboekingen created\n";
		} else {
			errorMessageExport = "Something went wrong " + i + " uurboekingen created\n";
		}
		/*
		 * ArrayList<Boolean> results = new ArrayList<Boolean>(); results =
		 * (ArrayList<Boolean>) SoapHandler.createSOAPXML(session, hourString,
		 * "workorder"); int i = 0; for(Boolean b : results){ if(b){ WorkOrder o
		 * = temp.get(i); RestHandler.setWorkorderStatus(o.getId(),
		 * o.getWorkorderNr(), b, "GetWorkorders", token); } i++; }
		 */
	}
}
