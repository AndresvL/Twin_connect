package object.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WorkOrder {
	// Header
	// ProjectNr, performancedate, invoiceaddressnumber,
	// deliveraddressnumber, customercode, status, paymentmethod(cash, bank,
	// cheque, cashondelivery, da)
	private String projectNr, workDate, customerEmailInvoice, customerEmail, customerDebtorNr, status, paymentMethod,
			creationDate;
	// line
	private ArrayList<Material> materials;

	public WorkOrder(String projectNr, String workDate, String customerEmailInvoice, String customerEmail,
			String customerDebtorNr, String status, String paymentMethod, ArrayList<Material> m, String creationDate) {
		this.projectNr = projectNr;
		setWorkDate(workDate);
		this.customerEmailInvoice = customerEmailInvoice;
		this.customerEmail = customerEmail;
		this.customerDebtorNr = customerDebtorNr;
		this.status = status;
		this.paymentMethod = paymentMethod;
		this.materials = m;
		setCreationDate(creationDate);
	}

	public String getProjectNr() {
		return projectNr;
	}

	public void setProjectNr(String projectNr) {
		this.projectNr = projectNr;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		try {
			SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
			Date date = dt.parse(workDate);
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
			System.out.println("date " + dt1.format(date));
			this.workDate = dt1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getCustomerEmailInvoice() {
		return customerEmailInvoice;
	}

	public void setCustomerEmailInvoice(String customerEmailInvoice) {
		this.customerEmailInvoice = customerEmailInvoice;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerDebtorNr() {
		return customerDebtorNr;
	}

	public void setCustomerDebtorNr(String customerDebtorNr) {
		this.customerDebtorNr = customerDebtorNr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public ArrayList<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(ArrayList<Material> materials) {
		this.materials = materials;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			Date date = dt.parse(creationDate);
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyyMMdd");
			System.out.println("date " + dt1.format(date));
			this.creationDate = dt1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
