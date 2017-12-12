package application.pw4;

import framework.hibernate.annotations.Id;
import framework.hibernate.annotations.Int;
import framework.hibernate.annotations.Table;
import framework.hibernate.annotations.Text;

@Table(tableName = "subject")
public class Subject {

	@Id(fieldName = "subject_id")
	private long subjectId;

	@Text(fieldName = "name", optional = false)
	private String name;

	@Int(fieldName = "credits", optional = false)
	private Integer credits;

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCredits() {
		return credits;
	}

	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	@Override
	public String toString() {
		return "Subject [subjectId=" + subjectId + ", name=" + name + ", credits=" + credits + "]";
	}
}
