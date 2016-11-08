package object;

import org.w3c.dom.Text;

public class Material {
	private String code, unit;
	private Text description;
	private double price;
	
	public Material(String c, String u, Text des, double p){
		this.code = c;
		this.unit = u;
		this.description = des;
		this.price = p;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Text getDescription() {
		return description;
	}
	public void setDescription(Text description) {
		this.description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
