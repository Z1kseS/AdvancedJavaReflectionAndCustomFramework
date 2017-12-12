package application.pw4;

import framework.hibernate.annotations.Id;
import framework.hibernate.annotations.Int;
import framework.hibernate.annotations.Table;
import framework.hibernate.annotations.Text;
import framework.hibernate.annotations.relation.ManyToOne;

@Table(tableName = "mark")
public class Mark {

	@Id(fieldName = "mark_id")
	private long markId;

	@Int(fieldName = "mark", optional = false)
	private Integer mark;

	@Text(fieldName = "scale", optional = false)
	private String scale;

	@ManyToOne(fieldName = "subject_id")
	private Subject subject;

	public long getMarkId() {
		return markId;
	}

	public void setMarkId(long markId) {
		this.markId = markId;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	@Override
	public String toString() {
		return "Mark [markId=" + markId + ", mark=" + mark + ", scale=" + scale + ", subject=" + subject + "]";
	}
}
