//Dylan Barrett
//Description
//1/25/25

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class SemesterConfigPanel extends CustomPanel {


    private BigDuty bigDuty;
    private Border border;
    private JPanel weekPanel;
    private JPanel sideBar;
    private static final String[] DAY_NAMES = {"M", "T", "W", "R", "F"};
    private ArrayList<SemesterConfigSpinner> spinners = new ArrayList<>();


    public SemesterConfigPanel(String panelName, Dimension d, BigDuty bd) {
        super(panelName, d);

        this.bigDuty = bd;

        this.border = BorderFactory.createLineBorder(Color.BLACK);

        this.setLayout(new BorderLayout());

        this.weekPanel = new JPanel(new GridLayout(1, 5));


        this.add(weekPanel, BorderLayout.CENTER);

        this.sideBar = new JPanel();
        this.add(sideBar, BorderLayout.NORTH);

        refreshPanel();

    }

    public void refreshPanel() {

        weekPanel.removeAll();

//        make the grid for schedule
        CustomPanel[] weekPanels = new CustomPanel[5];

        for(int i = 0; i < 5; i++) {
            weekPanels[i] = new CustomPanel(DAY_NAMES[i], new Dimension(100, 50));
            weekPanels[i].setLayout(new GridLayout(8, 1));
            weekPanels[i].setBorder(border);

            weekPanel.add(weekPanels[i]);

        }

        int o = 0;

        for(int i = 0; i < DAY_NAMES.length; i++) {

            for(int j = 0; j < ((GridLayout) weekPanels[i].getLayout()).getRows(); j++) {

                JPanel dayPanel = new JPanel(new GridLayout(4, 1));
                dayPanel.setBorder(border);

//                check for period 4 && period 8 on every day that isnt monday
                if((j == 3 || j == 7) && i != 0) {
                    weekPanels[i].add(dayPanel);
                    continue;
                }

                for(int k = 0; k < DutyType.values().length-1; k++) {

                    JPanel p = new JPanel(new GridLayout(1, 2));
                    p.add(new JLabel(DutyType.values()[k] + ": "));
                    SemesterConfigSpinner old = getPreviousSpinner(DutyType.values()[k], j+1, DAY_NAMES[i]);
                    int num = 0;

                    if(bigDuty.getDutySlot(new Period(j+1), DAY_NAMES[i], DutyType.values()[k]) == null) {
                        switch (DutyType.values()[k]) {
                            case COVERAGE: {
                                num = 3;
                                break;
                            }
                            case HALL: {
                                num = 1;
                                break;
                            }
                            case PASCACK: {
                                num = 2;
                                break;
                            }
                            case FROSH_PASCACK: {
                                num = 0;
                                break;
                            }
                        }
                    } else {
                        num = bigDuty.getDutySlot(new Period(j+1), DAY_NAMES[i], DutyType.values()[k]).getAmount();
                    }

                    SemesterConfigSpinner input = new SemesterConfigSpinner(new SpinnerNumberModel(num, 0, 99, 1), DutyType.values()[k], new Period(j+1), DAY_NAMES[i], bigDuty);
                    spinners.add(input);
                    spinners.remove(old);
                    p.add(input);
                    dayPanel.add(p);

                }

                weekPanels[i].add(dayPanel);

            }

        }

        sideBar.removeAll();

        JLabel filledDuties = new JLabel();

        String text = "<html>There are <b>" + bigDuty.getNumberOfDutySlots() + "</b> of <b>" + bigDuty.getUnlockedTeachers().size()*3 + "</b> needed available duty slots. <br/>";

        if(bigDuty.getNumberOfDutySlots() > bigDuty.getUnlockedTeachers().size()*3) {

            text += "There are <b><font color=#ff0000>" + (bigDuty.getNumberOfDutySlots() - bigDuty.getUnlockedTeachers().size()*3) + " extra </font></b> duty slots. </html>";

        } else if(bigDuty.getNumberOfDutySlots() < bigDuty.getUnlockedTeachers().size()*3) {

            text += "There are <b><font color=#ff0000>" + (bigDuty.getUnlockedTeachers().size()*3 - bigDuty.getNumberOfDutySlots()) + " missing </font></b> duty slots. </html>";

        } else {
            text += "All <b><font color=#00ff00> " + bigDuty.getNumberOfDutySlots() + "</font></b> duty slots filled! </html>";
        }

        filledDuties.setText(text);

        sideBar.add(filledDuties);


        super.refreshPanel();
    }

    public SemesterConfigSpinner getPreviousSpinner(DutyType type, int period, String day) {

        for(SemesterConfigSpinner s : spinners) {

            if(s.getDay().equals(day) && s.getPeriod().equals(new Period(period)) && s.getType().equals(type)) {
                return s;
            }

        }
        return null;

    }
}
