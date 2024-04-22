
public class Location {
	private String location;
	private DateAVL dateAvl;
	private MartyrAVL martyrAvl;

	public Location(String location) {
		this.location = location;
		this.dateAvl = new DateAVL();
		this.martyrAvl = new MartyrAVL();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public DateAVL getDateAvl() {
		return dateAvl;
	}

	public void setDateAvl(DateAVL dateAvl) {
		this.dateAvl = dateAvl;
	}

	public MartyrAVL getMartyrAvl() {
		return martyrAvl;
	}

	public void setMartyrAvl(MartyrAVL martyrAvl) {
		this.martyrAvl = martyrAvl;
	}

	public int compareTo(Location o) {
		return location.compareToIgnoreCase(o.getLocation());
	}

	@Override
	public String toString() {
		return "Location [location=" + location + "]";
	}
	
}
