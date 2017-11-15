package cs310_team2_project;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class TASDatabase {
    private static TASDatabase globalSQL;
    private Connection conn; 
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/";
    
    public TASDatabase(){
        try{
            Class.forName(DRIVER);
            System.out.print("Connecting to database: " + "tas");
            conn = DriverManager.getConnection(URL + "tas", "CS310TeamTwo", "cs3102017");
        }
        catch(Exception se){
            se.printStackTrace();
        }
    }
    
    public boolean isConnected(){
        return conn != null;
    }
    
    public Badge getBadge(String idNum){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM badge WHERE id='" + idNum + "'");
            if(result != null){
                result.next();
                String id = result.getString("id");
                String description = result.getString("description");
                Badge b = new Badge(id, description);
                return b;
            }
            result.close();
            statement.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }
    
    public Punch getPunch(int idNum){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM event WHERE id='" + Integer.toString(idNum) + "'");
            if(result != null){
                result.next();
                
                int id = result.getInt("id");
                int terminalID = result.getInt("terminalid");
                String badgeID = result.getString("badgeid");
                Timestamp originalTimestamp = result.getTimestamp("originaltimestamp");
                int eventTypeID = result.getInt("eventtypeid");
                
                String eventData = result.getString("eventdata");
                Timestamp adjustedTimestamp = result.getTimestamp("adjustedtimestamp");
                
                int shiftID = -1;
                ResultSet badge = statement.executeQuery("SELECT shiftid from employee WHERE badgeid='" + badgeID + "'");
                if(badge != null){
                    badge.next();
                    shiftID = badge.getInt("shiftid");
                }
                badge.close();
                
                String description = "";
                ResultSet desc = statement.executeQuery("SELECT description from eventtype WHERE id='" + Integer.toString(eventTypeID) + "'");
                if(desc != null){
                    desc.next();
                    description = desc.getString("description");
                }
                desc.close();
                
                Punch p = new Punch(id, terminalID, eventTypeID, shiftID, badgeID, eventData,
                                    originalTimestamp, adjustedTimestamp);
                return p;
            }
            result.close();
            statement.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }
    
    public int insertPunch(Punch p){
        String query = "INSERT INTO `event` (`id`,`terminalid`,`badgeid`,`originaltimestamp`,`eventtypeid`,`eventdata`,`adjustedtimestamp`) VALUES (";
        query += Integer.toString(p.getId());
        query += "," + Integer.toString(p.getTerminalid());
        query += ",'" + p.getBadgeid() + "',";
        query += "'" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(p.getOriginaltimestamp()) + "',";
        query += Integer.toString(p.getPunchtypeid());
        query += ",NULL,NULL);";
        
        int newID = -1;
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet result = statement.getGeneratedKeys();
            if(result != null){
                result.next();
                newID = result.getInt(1);
            }
            result.close();
            statement.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return newID;
    }
    
    public Shift getShift(int idNum){
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM shift WHERE id='" + Integer.toString(idNum) + "'");
            if(result != null){
                result.next();
                
                int id = result.getInt("id");
                String description = result.getString("description");
                Timestamp start = result.getTimestamp("start");
                Timestamp stop = result.getTimestamp("stop");
                int interval = result.getInt("interval");
                int gracePeriod = result.getInt("graceperiod");
                
                int dock = result.getInt("dock");
                Timestamp lunchStart = result.getTimestamp("lunchstart");
                Timestamp lunchStop = result.getTimestamp("lunchstop");
                int lunchDeduct = result.getInt("lunchdeduct");
                int maxTime = result.getInt("maxtime");
                int overtimeThreshold = result.getInt("overtimethreshold");
                
                Shift s = new Shift(id, interval, gracePeriod, dock, lunchDeduct, maxTime, overtimeThreshold,
                                    description, start, stop, lunchStart, lunchStop);
                return s;
            }
            result.close();
            statement.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return null;
    }
    
    public int getMinutesAccrued(Punch p){
        int minutes = 0;
        String badge = p.getBadgeid().toUpperCase();
        String date = (new SimpleDateFormat("yyyy-MM-dd")).format(p.getOriginaltimestamp());
        try{
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM event WHERE badgeid='" + badge + "' AND originaltimestamp LIKE '" + date + "%'");
            if(result != null){
                ArrayList<Punch> punches = new ArrayList();
                
                int shiftID = getShiftID(badge);
                Shift s = getShift(shiftID);
                
                while(result.next()){
                    int id = result.getInt("id");
                    int terminalID = result.getInt("terminalid");
                    String badgeID = result.getString("badgeid");
                    Timestamp originalTimestamp = result.getTimestamp("originaltimestamp");
                    int eventTypeID = result.getInt("eventtypeid");

                    String eventData = result.getString("eventdata");
                    Timestamp adjustedTimestamp = result.getTimestamp("adjustedtimestamp");

                    Punch newPunch = new Punch(id, terminalID, eventTypeID, shiftID, badgeID, eventData,
                                        originalTimestamp, adjustedTimestamp);

                    punches.add(newPunch);
                }
                int numPunches = punches.size();
                ArrayList<Long> times = new ArrayList();
                for(int i=0; i<numPunches; i++){
                    punches.get(i).adjust(s);
                    times.add(punches.get(i).getAdjustedtimestamp().getTime());
                }
                long timeInterval = 0;
                if(numPunches == 2){
                    if(!punches.get(1).getEventdata().equals("TIMED OUT")){
                        timeInterval = (times.get(1) - times.get(0))/60000;
                        if(timeInterval >= s.getLunchDeduct()-30){
                            timeInterval -= 30;
                        }
                    }
                    else{
                        timeInterval = 0;
                    }
                }
                else{
                    timeInterval += (times.get(1) - times.get(0));
                    if(!punches.get(3).getEventdata().equals("TIMED OUT")){
                        timeInterval += (times.get(3) - times.get(2));
                    }
                    timeInterval /= 60000;
                }
                minutes = (int) timeInterval;
                result.close();
                return minutes; 
            }
            statement.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return -1;
    }
    
    private  int getShiftID(String badge){
        try{
            int shiftID = -1;
            Statement statement = conn.createStatement();
            ResultSet b = statement.executeQuery("SELECT shiftid from employee WHERE badgeid='" + badge + "'");
            if(b != null){
                b.next();
                shiftID = b.getInt("shiftid");
                b.close();
            }
            return shiftID;
        }
        catch(SQLException se){
            se.printStackTrace();
        }
        return -1;
    }
    
    public void close(){
        try{
         conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}