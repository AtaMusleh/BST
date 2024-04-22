import java.time.LocalDate;

public class Martyr {
	private String name;
	private int age;
	private String location;
	private LocalDate date;
	private char gender;

	public Martyr(String name, int age, String location, LocalDate date, char gender) {
		super();
		this.name = name;
		this.age = age;
		this.location = location;
		this.date = date;
		this.gender = gender;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "Martyr [name=" + name + ", age=" + age + ", location=" + location + ", date=" + date + ", gender="
				+ gender + "]";
	}

}
