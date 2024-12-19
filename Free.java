//Dylan Barrett
//Description
//9/23/24

public class Free extends Assignment {

    private boolean isLocked;

    public Free(int period, String semester, String name, String day, Teacher teacher, boolean adjustPeriod) {
        super(period, semester, name, day, teacher, adjustPeriod);

    }

    public Free(Period period, String semester, String name, String day, Teacher teacher) {
        super(period.getValue(), semester, name, day, teacher, false);
    }


    @Override
    public String getRoom() {
        return "Any";
    }

    @Override
    public double getWeight() {
        return 0;
    }

    @Override
    public String getDepartment() {
        return "None";
    }

   public boolean isLocked()
   {
      return isLocked;
   }

   public void setLocked(boolean isLocked)
   {
      this.isLocked = isLocked;
   }
   
   public String toString()
   {
	   return "Free " + super.toString();
   }
}
