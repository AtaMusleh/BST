
public class LocationNode {
	private Location location;
	private LocationNode next;
	private LocationNode pre;

	public LocationNode(Location location) {
		next = pre = null;
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public LocationNode getNext() {
		return next;
	}

	public void setNext(LocationNode next) {
		this.next = next;
	}

	public LocationNode getPre() {
		return pre;
	}

	public void setPre(LocationNode pre) {
		this.pre = pre;
	}

	@Override
	public String toString() {
		return "LocationNode [location=" + location + "]";
	}

}
