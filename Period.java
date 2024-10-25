//Dylan Barrett
//Description
//10/21/24

public class Period implements Comparable {

    public static String LUNCH_STRING = "L";
    public static String AFTER_SCHOOL = "9";
    public static String P0 = "0";

    public static int AFTER_SCHOOL_NUM = 9;
    public static int P0_NUM = 0;
    public static int LUNCH_NUM = -2;

    public static Period LUNCH = new Period("L");


    private String period;

    public Period(String period) {
        this.period = period;
    }

    public Period(int period) {
        this.period = period +"";
    }

    /**
     * Makes a period based off the Monday schedule and the day the period is on
     * @param period the period on a monday schedule
     * @param day the day of the week the period is on
     */
    public Period(int period, String day) {

        if(day.equals("M")) this.period = period + "";
        else if(period == -2) this.period = "L";
        else if(day.length() > 1) this.period = period + "";
        else {

            String schedule = BigDuty.schedule.get(day+"_SCHEDULE");

            if(schedule.indexOf(period + "") == 8) this.period = AFTER_SCHOOL;
            else {
                System.out.println(schedule.indexOf(period + "") + "");
                this.period = schedule.indexOf(period + "") + "";
            }
        }


    }
    public Period(String period, String day) {

        this(period.equals("L") ? -2 : Integer.parseInt(period), day);

    }


    public String getPeriod() {
        return period;
    }

    public int getValue() {

        if(period.equals("L")) return -2;

        return Integer.parseInt(period);

    }

    public boolean isLunch() {

        if(period.equals("L")) return true;
        return false;

    }

    /**
     * Returns the value of what the increment would be when adding the current period and the specified number, does not modify the period
     * @param increment the amount to increment the period by, can be positive or negative
     * @return what the value of the period would be
     */
    public int increment(int increment) {
        if(this.isLunch()) {
            if(increment > 0) {
                if(4 + increment > Integer.parseInt(Assignment.AFTER_SCHOOL)) return AFTER_SCHOOL_NUM;
                return 4 + increment;
            }
            if(5 + increment < Integer.parseInt(Assignment.P0)) return P0_NUM;
            return 5 + increment;
        }

        if(getValue() + increment > AFTER_SCHOOL_NUM) return AFTER_SCHOOL_NUM;

        if(getValue() + increment < P0_NUM) return P0_NUM;

        return getValue() + increment;

    }

    public boolean greaterThan(Period period) {

        if(equals(period)) return false;

        if(period.isLunch()) return getValue() > 4;

        if(isLunch()) return period.getValue() < 5;

        return getValue() > period.getValue();

    }

    public boolean greaterThan(int period) {
        return greaterThan(new Period(period));
    }

    public boolean equals(Period period) {
        return getValue() == period.getValue();
    }

    public void setPeriod(int period) {
        this.period = period + "";
    }

    public void setPeriod(String period) {
        this.period = period;
    }


    @Override
    public int compareTo(Object o) {

        Period p = (Period) o;

        if(isLunch() && p.isLunch()) return 0;

        if(isLunch()) return (int) (4.5 - p.getValue());
        if(p.isLunch()) return (int) (p.getValue() - 4.5);

        return getValue() - p.getValue();

    }

    public String toString() {
        return getPeriod();
    }
}
