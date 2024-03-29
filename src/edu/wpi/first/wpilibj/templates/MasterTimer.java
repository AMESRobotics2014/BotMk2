package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import java.util.Vector;

public class MasterTimer extends Timer {

    Vector actindex;
    accessdata use;

    public void MasterTimer() {
    }

    public void Init() {
        actindex = new Vector();
        addEventTimer("LimitDEBUG");
        addEventTimer("printsDEBUG");
        addEventTimer("potential");
        addEventTimer("TableTimer");
        addEventTimer("Retract");
        addEventTimer("AtonTimer");
        addEventTimer("ShortThrow");
        addEventTimer("ModeCycler");
        addEventTimer("Pass");
    }

    public void addEventTimer(String tid) {
        use = new accessdata(tid);
        use.Init(tid);
        actindex.addElement(use);
    }

    public void listIndicesDEBUG() {
        try {
            int i;
            System.out.println("Test" + actindex.size());
            for (i = 0; i < actindex.size(); i++) {
                use = (accessdata) actindex.elementAt(i);
                System.out.println("Name: " + use.id + " at index location:[" + i + "]");
                System.out.println("Has gdt val of: " + use.gdt());
            }
        } catch (NullPointerException ex) {
            System.out.println("This index doesn't exist D:");
        }
    }

    public double gdt(int loc) {
        //  System.out.println("Getting gdt at location:" +loc);
        use = (accessdata) actindex.elementAt(loc);
        // System.out.println("GDT is :" + use.gdt());
        return use.gdt();
    }

    public void sc(int loc) {
        use = (accessdata) actindex.elementAt(loc);
        //actindex.removeElementAt(loc);
       // System.out.println(actindex.elementAt(loc));
        use.sc();
        actindex.setElementAt(use, loc);
    }

    public void Freset() {//Full reset!!!
        this.reset();
        int i;
        for (i = 0; i < actindex.size(); i++) {
            use = (accessdata) actindex.elementAt(i);
            use.wipe();
            actindex.setElementAt(use, i);
        }
    }

    public void Ereset(int[] exclude) {//Exclusion reset!!!
        int i;
        for (i = 0; i < actindex.size() - 1; i++) {
            use = (accessdata) actindex.elementAt(i);
            for (int k = 0; k < exclude.length; k++) {
                if (i == exclude[k]) {
                    break;
                } else {
                    use.wipe();
                    actindex.setElementAt(use, i);
                }
            }
        }
    }

    public void Sreset(int[] include) {//Selective reset
        int i;
        for (i = 0; i < include.length; i++) {
            use = (accessdata) actindex.elementAt(i);
            use.wipe();
            actindex.setElementAt(use, i);
        }
    }

    public class accessdata {

        double ot;
        double dt;
        String id;

        accessdata(String tid) {
        }

        public void Init(String tid) {
            this.id = tid;
            this.ot = get();
            this.dt = -10;//Just Cause 2  
        }

        public double gdt() {
            this.dt = get() - ot;
            //ot = get();
            return this.dt;
        }

        public void sc() {
            this.ot = get();
        }

        public void wipe() {
            this.ot = 0;
            this.dt = 0;
            this.id = null;
        }
    }
}
