
public class TeacherWithScore implements Comparable<TeacherWithScore>
{
	private Teacher teacher;
	private int score;
	
	public TeacherWithScore(Teacher teacher, int score) {
		this.teacher = teacher;
		this.score = score;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "TeacherWithScore [teacher=" + teacher + ", score=" + score + "]";
	}

	@Override
	public int compareTo(TeacherWithScore other) {
		
		return other.score - this.score;
	}
	
	
}
