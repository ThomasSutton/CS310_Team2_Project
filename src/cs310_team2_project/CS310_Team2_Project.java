package cs310_team2_project;

public class CS310_Team2_Project {
    public static void main(String[] args){
        TASDatabase db = new TASDatabase();
        
        Shift s1 = db.getShift(1);
        System.out.println("\n" + s1.toString());
        Punch p3 = db.getPunch(4119);
        p3.adjust(s1);
        System.out.println(p3.printAdjustedTimestamp());
    }
}
