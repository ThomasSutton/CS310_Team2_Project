package cs310_team2_project;

import java.sql.*;
import java.text.SimpleDateFormat;

public class Punch {
    private int id, terminalID, eventTypeID, shiftID;
    private String badgeID, eventData;
    private Timestamp originalTimestamp, adjustedTimestamp;

    public Punch(int id, int terminalID, int eventTypeID, int shiftID, String badgeID, String eventData, Timestamp originalTimestamp, Timestamp adjustedTimestamp) {
        this.id = id;
        this.terminalID = terminalID;
        this.eventTypeID = eventTypeID;
        this.shiftID = shiftID;
        this.badgeID = badgeID;
        this.eventData = eventData;
        this.originalTimestamp = originalTimestamp;
        this.adjustedTimestamp = adjustedTimestamp;
    }

    public int getId() {
        return id;
    }

    public int getTerminalID() {
        return terminalID;
    }

    public int getEventTypeID() {
        return eventTypeID;
    }

    public int getShiftID() {
        return shiftID;
    }

    public String getBadgeID() {
        return badgeID;
    }

    public String getEventData() {
        return eventData;
    }

    public Timestamp getOriginalTimestamp() {
        return originalTimestamp;
    }

    public Timestamp getAdjustedTimestamp() {
        return adjustedTimestamp;
    }
    
    public String printOriginalTimestamp(){
        String original = (new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss")).format(originalTimestamp.getTime()).toUpperCase();
        String data = "";
        data += "#" + badgeID + " ";
        if(eventTypeID == 0){
            data += "CLOCKED OUT";
        }
        if(eventTypeID == 1){
            data += "CLOCKED IN";
        }
        if(eventTypeID == 2){
            data += "TIMED OUT";
        }
        data += ": " + original;
        return data;
    }
}
