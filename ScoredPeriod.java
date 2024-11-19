//Dylan Barrett
//Description
//10/1/24

public class ScoredPeriod implements Comparable {

    private int score;
    private Assignment Assignment;

    public ScoredPeriod(int score, Assignment assignment) {
        this.score = score;
        Assignment = assignment;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Assignment getAssignment() {
        return Assignment;
    }

    public void setAssignment(Assignment assignment) {
        Assignment = assignment;
    }

    @Override
    public int compareTo(Object o) {

        ScoredPeriod sp = (ScoredPeriod) o;

        return sp.getScore() - score;
    }

    public String toString() {

        return score + ", " + getAssignment().toString();

    }

}
