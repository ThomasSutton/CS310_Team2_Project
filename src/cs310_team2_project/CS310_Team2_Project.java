package cs310_team2_project;

import java.text.SimpleDateFormat;

public class CS310_Team2_Project {
    public static void main(String[] args){
        TASDatabase db = new TASDatabase();
        
        //Shift s1 = db.getShift(1);
        //System.out.println("\n" + s1.toString());
        Punch p = db.getPunch(4943);
        //String date = (new SimpleDateFormat("yyyy-MM-dd")).format(p.getOriginaltimestamp());
        //int min = db.getMinutesAccrued(p);
        //System.out.println(Integer.toString(min));
        //System.out.println(date);
        System.out.println(db.getPunchListAsJSON(p));
    }
}
