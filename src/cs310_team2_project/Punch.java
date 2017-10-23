package cs310_team2_project;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Punch {
    private int id, terminalID, punchTypeID, shiftID;
    private String badgeID, eventData;
    private Timestamp originalTimestamp, adjustedTimestamp;

    public Punch(int id, int terminalID, int eventTypeID, int shiftID, String badgeID, String eventData, Timestamp originalTimestamp, Timestamp adjustedTimestamp) {
        this.id = id;
        this.terminalID = terminalID;
        this.punchTypeID = eventTypeID;
        this.shiftID = shiftID;
        this.badgeID = badgeID;
        this.eventData = eventData;
        this.originalTimestamp = originalTimestamp;
        this.adjustedTimestamp = adjustedTimestamp;
    }
    
    public Punch(String badgeid, int terminalid, int punchtypeid)
    {
        this.badgeID = badgeid;
        this.terminalID = terminalid;
        this.punchTypeID = punchtypeid;
        this.id = 0;
        this.originalTimestamp = new Timestamp(System.currentTimeMillis());
        this.adjustedTimestamp = null;
        this.shiftID = 0;
        this.eventData = null;
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalID;
    }

    public int getPunchtypeid() {
        return punchTypeID;
    }

    public int getShiftid() {
        return shiftID;
    }

    public String getBadgeid() {
        return badgeID;
    }

    public String getEventdata() {
        return eventData;
    }

    public Timestamp getOriginaltimestamp() {
        return originalTimestamp;
    }

    public Timestamp getAdjustedtimestamp() {
        return adjustedTimestamp;
    }
    
    public String printOriginalTimestamp(){
        String original = (new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss")).format(originalTimestamp.getTime()).toUpperCase();
        String data = "";
        data += "#" + badgeID + " ";
        if(punchTypeID == 0){
            data += "CLOCKED OUT";
        }
        if(punchTypeID == 1){
            data += "CLOCKED IN";
        }
        if(punchTypeID == 2){
            data += "TIMED OUT";
        }
        data += ": " + original;
        return data;
    }
}
