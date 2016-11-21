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
	String invoiceType;
	ArrayList<String> responseArray = null;
	String[][] options = null;
	Search searchObject;
	String errorMessage = null;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String button = req.getParameter("category");
		String office = req.getParameter("offices");
		String factuurType = req.getParameter("factuurType");
		String token = (String) req.getSession().getAttribute("softwareToken");
		String session = (String) req.getSession().getAttribute("session");
		if (button.equals("alles")) {
			getEmployees(office, session, token);
			getMaterials(office, session, token);
			getProjects(office, session, token);
			getRelations(office, session, token);
			getHourTypes(office, session, token);
		}
		else{
			switch (button) {
			case "getEmployees":
				getEmployees(office, session, token);
				break;
			case "getMaterials":
				getMaterials(office, session, token);
				break;
			case "getProjects":
				getProjects(office, session, token);
				break;
			case "getRelations":
				getRelations(office, session, token);
				break;
			case "getHourTypes":
				getHourTypes(office, session, token);
				break;
			case "start":
				setDelay(true);
				break;
			case "stop":
				setDelay(false);
				break;
			case "getWorkorder":
				ArrayList<WorkOrder> allData = RestHandler.getData(token, "GetWorkorders", factuurType, true);
				String hourString = "<teqs>";
				for (WorkOrder w : allData) {
					String string = null;
					if (w.getProjectNr().equals("")) {
						Address factuur = null;
						Address post = null;
						post = ObjectDAO.getAddressID(token, "postal", w.getCustomerDebtorNr());
						factuur = ObjectDAO.getAddressID(token, "invoice", w.getCustomerDebtorNr());
						if (post == null) {
							post = factuur;
						}
						invoiceType = "FACTUUR";
						string = "<salesinvoice>" + "<header>" + "<office>" + office + "</office>" + "<invoicetype>"
								+ invoiceType + "</invoicetype>" + "<invoicedate>" + w.getCreationDate()
								+ "</invoicedate>" + "<duedate>" + w.getWorkDate() + "</duedate>" + "<customer>"
								+ w.getCustomerDebtorNr() + "</customer>" + "<status>" + w.getStatus() + "</status>"
								+ "<paymentmethod>" + w.getPaymentMethod() + "</paymentmethod>"
								+ "<invoiceaddressnumber>" + factuur.getAddressId() + "</invoiceaddressnumber>"
								+ "<deliveraddressnumber>" + post.getAddressId() + "</deliveraddressnumber>"
								+ "</header>" + "<lines>";
						int i = 0;
						for (Material m : w.getMaterials()) {
							i++;
							// subCode is empty for now because werkbonapp
							// doesnt provide this function
							String subCode = "";
							string += "<line id=\"" + i + "\">" + "<article>" + m.getCode() + "</article>"
									+ "<subarticle>" + subCode + "</subarticle>" + "<quantity>" + m.getQuantity()
									+ "</quantity>" + "<units>" + m.getUnit() + "</units>" + "</line>";
						}
						string += "</lines></salesinvoice>";
						System.out.println("string " + string);
						SoapHandler.createSOAPXML(session, string, "workorder");
					} else {
						// String readString =
						// "<read><type>dimensions</type><office>" + office +
						// "</office><dimtype>PRJ</dimtype><code>" +
						// w.getProjectNr() + "</code></read>";
						// Object obj = SoapHandler.createSOAPXML(session,
						// readString, "project");
						// Project p = null;
						String code = "test";
						// if (obj != null) {
						// p = (Project) obj;
						// code = p.getAuthoriser();
						// if(code.equals(w.getEmployeeNr())){
						// code = "DIRECT";
						// }
						// }
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
						System.out.println("project type " + code + " nr " + projectNr + " startswith "
								+ projectNr.startsWith("FP"));

						invoiceType = "UREN";
						hourString += "<teq>" + "<header>" + "<office>" + office + "</office>"
						// Check this later
								+ "<code>" + "PERSONAL" + "</code>" + "<user>" + w.getEmployeeNr() + "</user>"
								+ "<date>" + w.getWorkDate() + "</date>" + "<prj1>" + w.getProjectNr() + "</prj1>"
								+ "<prj2>" + w.getHourType() + "</prj2>" + "</header>" + "<lines>"
								+ "<line type= \"TIME\">" + "<duration>" + w.getDuration() + "</duration>"
								+ "<description>" + w.getDescription() + "</description>" + "</line>"
								+ "<line type=\"QUANTITY\">" + "</line>" + "</lines></teq>";
					}

				}
				hourString += "</teqs>";
				System.out.println("string " + hourString);
				// SoapHandler.createSOAPXML(session, hourString, "workorder");
				break;
			}
		}
		RequestDispatcher rd = req.getRequestDispatcher("adapter.jsp");
		req.getSession().setAttribute("error", errorMessage);
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
			RestHandler.addData(token, emp, "employees");
		} else {
			errorMessage = "No Employees found";
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
			RestHandler.addData(token, projects, "projects");
		} else {
			errorMessage = "Office " + office + " heeft geen projecten";
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
			RestHandler.addData(token, materials, "materials");
		} else {
			errorMessage = "Office " + office + " heeft geen materialen";
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
			RestHandler.addData(token, relations, "relations");
		} else {
			errorMessage = "Office " + office + " heeft geen relations";
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
			RestHandler.addData(token, hourtypes, "hourtypes");
		} else {
			errorMessage = "Office " + office + " heeft geen hourtypes";
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

}
