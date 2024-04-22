import java.time.LocalDate;

public class DateStack {
	private LocalDate date;
	private Stack stack;

	public DateStack(LocalDate date) {

		this.date = date;
		this.stack = new Stack();

	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Stack getStack() {
		return stack;
	}

	public void setStack(Stack stack) {
		this.stack = stack;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(").append(date.getMonthValue()).append("/").append(date.getDayOfMonth()).append("/")
				.append(date.getYear()).append("):\n");
		int counter = 1;
		Stack tempStack = new Stack();
		while (!stack.isEmpty()) {
			Martyr martyr = stack.pop();
			sb.append(counter).append(". ").append(martyr.toString()).append("\n");
			tempStack.push(martyr);
			counter++;
		}

		// Restore the original stack
		while (!tempStack.isEmpty()) {
			stack.push(tempStack.pop());
		}

		return sb.toString();
	}

}
