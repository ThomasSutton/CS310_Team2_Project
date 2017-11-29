package cs310_team2_project;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    
    public void adjust (Shift s){
        //Creates Shift Start
        int punchMinute = originalTimestamp.toLocalDateTime().getMinute();
        int adjustedMinute = 0;
        
        GregorianCalendar shift_start = new GregorianCalendar();
        shift_start.setTimeInMillis(originalTimestamp.getTime());
        int interval = s.getInterval();
        int h = s.getStart().toLocalDateTime().getHour();
        int m = s.getStart().toLocalDateTime().getMinute();
        shift_start.set(Calendar.HOUR_OF_DAY, h);
        shift_start.set(Calendar.MINUTE, m);
        shift_start.set(Calendar.SECOND, 0);
        long shift_start1 = shift_start.getTimeInMillis();
        
        //Creates Shift End
        GregorianCalendar shift_stop = new GregorianCalendar();
        shift_stop.setTimeInMillis(originalTimestamp.getTime());
        int hs = s.getStop().toLocalDateTime().getHour();
        int ms = s.getStop().toLocalDateTime().getMinute();;
        shift_stop.set(Calendar.HOUR_OF_DAY, hs);
        shift_stop.set(Calendar.MINUTE, ms);
        shift_stop.set(Calendar.SECOND, 0);
        long shift_stop1 = shift_stop.getTimeInMillis();
        
        //Creates Interval 1
        GregorianCalendar Interval_1 = new GregorianCalendar();
        Interval_1.setTimeInMillis(shift_start.getTimeInMillis());
        Interval_1.add(Calendar.MINUTE, -(s.getInterval()));
        long first_interval = Interval_1.getTimeInMillis();
        
        //Creates Interval 2
        GregorianCalendar Interval_2 = new GregorianCalendar();
        Interval_2.setTimeInMillis(shift_stop.getTimeInMillis());
        Interval_2.add(Calendar.MINUTE, (s.getInterval()));
        long second_interval = Interval_2.getTimeInMillis();
        
        //creates first grace period
        GregorianCalendar First_grace = new GregorianCalendar();
        First_grace.setTimeInMillis(shift_start.getTimeInMillis());
        First_grace.add(Calendar.MINUTE, (s.getGracePeriod()));
        long grace_1 = First_grace.getTimeInMillis();
        
        //creates second grace period
        GregorianCalendar Second_grace = new GregorianCalendar();
        Second_grace.setTimeInMillis(shift_stop.getTimeInMillis());
        Second_grace.add(Calendar.MINUTE, -(s.getGracePeriod()));
        long grace_2 = Second_grace.getTimeInMillis();
        
        //Creates first dock
        GregorianCalendar dock_1 = new GregorianCalendar();
        dock_1.setTimeInMillis(shift_start.getTimeInMillis());
        dock_1.add(Calendar.MINUTE, (s.getDock()));
        long first_dock = dock_1.getTimeInMillis();
        
        //Creates second dock
        GregorianCalendar dock_2 = new GregorianCalendar();
        dock_2.setTimeInMillis(shift_stop.getTimeInMillis());
        dock_2.add(Calendar.MINUTE, -(s.getDock()));
        long second_dock = dock_2.getTimeInMillis();
        
        //Creates lunch start
        GregorianCalendar lunch_start = new GregorianCalendar();
        lunch_start.setTimeInMillis(originalTimestamp.getTime());
        int lh = s.getLunchStart().toLocalDateTime().getHour();
        int lm = s.getLunchStart().toLocalDateTime().getMinute();
        lunch_start.set(Calendar.HOUR_OF_DAY, lh);
        lunch_start.set(Calendar.MINUTE, lm);
        lunch_start.set(Calendar.SECOND, 0);
        long start_lunch = lunch_start.getTimeInMillis();
        
        //Creates lunch stop
        GregorianCalendar lunch_stop = new GregorianCalendar();
        lunch_stop.setTimeInMillis(originalTimestamp.getTime());
        int lh2 = s.getLunchStop().toLocalDateTime().getHour();
        int lm2 = s.getLunchStop().toLocalDateTime().getMinute();
        lunch_stop.set(Calendar.HOUR_OF_DAY, lh2);
        lunch_stop.set(Calendar.MINUTE, lm2);
        lunch_stop.set(Calendar.SECOND, 0);
        long stop_lunch = lunch_stop.getTimeInMillis();
        
        long p1 = originalTimestamp.getTime();
        GregorianCalendar newAdjustedTimeStamp = new GregorianCalendar();
        newAdjustedTimeStamp.setTimeInMillis(p1);
        
        if(shift_start.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && shift_start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
            if(punchTypeID == 1){
                if(p1 >= first_interval && p1 <= shift_start1){
                    p1 = shift_start1;
                    eventData = "Shift Start";
                }
                else if(p1 <= grace_1 && p1 >= shift_start1){
                    p1 = shift_start1;
                    eventData = "Shift Start";
                }
                else if(p1 > grace_1 && p1 <= first_dock){
                    p1 = first_dock; 
                    eventData = "Shift Dock";
                }
                else if(p1 >= start_lunch && p1 <= stop_lunch){
                    p1 = stop_lunch;
                    eventData = "Lunch Stop";
                }
                else{
                    if(punchMinute%interval == 0){
                        eventData = "None";
                    }
                    else{
                        if(punchMinute % interval !=0){
                            if((punchMinute % interval) < (interval /2)){
                                adjustedMinute = (Math.round(punchMinute / interval) * interval);
                            }
                            else{
                                adjustedMinute = (Math.round(punchMinute / interval) * interval) + interval;
                            }
                            newAdjustedTimeStamp.add(Calendar.MINUTE, (adjustedMinute-punchMinute));
                            newAdjustedTimeStamp.set(Calendar.SECOND, 0);
                            p1 = newAdjustedTimeStamp.getTimeInMillis();
                            eventData = "Interval Round";
                        }
                    }
                }
            }
            else{
                if(p1 <= second_interval && p1 >= shift_stop1){
                    p1 = shift_stop1;
                    eventData = "Shift Stop";
                }
                else if(p1 >= grace_2 && p1 <= shift_stop1){
                    p1 = shift_stop1; 
                    eventData = "Shift Stop";
                }
                else if(p1 < grace_2 && p1 >= second_dock){
                    p1 = second_dock; 
                    eventData = "Shift Dock";
                }
                else if(p1 >= start_lunch && p1 <= stop_lunch){
                    p1 = start_lunch;
                    eventData = "Lunch Start";
                }
                else{
                    if(punchMinute%interval == 0){
                        eventData = "None";
                    }
                    else{
                        if(punchMinute % interval !=0){
                            if((punchMinute % interval) < (interval /2)){
                                adjustedMinute = (Math.round(punchMinute / interval) * interval);
                            }
                            else{
                                adjustedMinute = (Math.round(punchMinute / interval) * interval) + interval;
                            }
                            newAdjustedTimeStamp.add(Calendar.MINUTE, (adjustedMinute-punchMinute));
                            newAdjustedTimeStamp.set(Calendar.SECOND, 0);
                            p1 = newAdjustedTimeStamp.getTimeInMillis();
                            eventData = "Interval Round";
                        }
                    }
                }
            }
        }
        else{
            if(punchMinute%interval == 0){
                eventData = "None";
            }
            else{
                if(punchMinute % interval !=0){
                    if((punchMinute % interval) < (interval /2)){
                        adjustedMinute = (Math.round(punchMinute / interval) * interval);
                    }
                    else{
                        adjustedMinute = (Math.round(punchMinute / interval) * interval) + interval;
                    }
                    newAdjustedTimeStamp.add(Calendar.MINUTE, (adjustedMinute-punchMinute));
                    newAdjustedTimeStamp.set(Calendar.SECOND, 0);
                    p1 = newAdjustedTimeStamp.getTimeInMillis();
                    eventData = "Interval Round";
                }
            }
        }
        adjustedTimestamp = new Timestamp(p1);
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
    
    public String printAdjustedTimestamp(){
        String adjusted = (new SimpleDateFormat("EEE MM/dd/yyyy HH:mm:ss")).format(adjustedTimestamp.getTime()).toUpperCase();
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
        data += ": " + adjusted;
        data += " (" + eventData + ")";
        return data;
    }
}
