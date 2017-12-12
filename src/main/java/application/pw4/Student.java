package application.pw4;

import framework.hibernate.annotations.Id;
import framework.hibernate.annotations.Table;
import framework.hibernate.annotations.Text;

@Table(tableName = "student")
public class Student {

	@Id(fieldName = "student_id")
	private int studentId;

	@Text(fieldName = "first_name", optional = false)
	private String firstName;

	@Text(fieldName = "last_name", optional = false)
	private String lastName;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}

}
