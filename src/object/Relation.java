package object;

public class Relation {
	private String name, debtorNumber, contact, phoneNumber, email, emailWorkorder, street, houseNumber, postalCode, city, remark;

	public Relation(String name, String debtorNumber, String contact, String phoneNumber, String email, String emailWorkorder, String street, String houseNumber, String postalCode, String city, String remark){
		this.name = name;
		this.debtorNumber = debtorNumber;
		this.contact = contact;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.emailWorkorder = emailWorkorder;
		this.street = street;
		this.houseNumber = houseNumber;
		this.postalCode = postalCode;
		this.city = city;
		this.remark = remark;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDebtorNumber() {
		return debtorNumber;
	}

	public void setDebtorNumber(String debtorNumber) {
		this.debtorNumber = debtorNumber;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailWorkorder() {
		return emailWorkorder;
	}

	public void setEmailWorkorder(String emailWorkorder) {
		this.emailWorkorder = emailWorkorder;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
