package cs310_team2_project;

import java.sql.*;
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
        //The first thing we need to do is create the query using the data provided for the new punch.
        //This is pretty much exactly like the toString() methods in Badge, Punch, and Shift.
        String query = "INSERT INTO `event` (`id`,`terminalid`,`badgeid`,`originaltimestamp`,`eventtypeid`,`eventdata`,`adjustedtimestamp`) VALUES (";
        
        //Now we concatonate the query string with our table data.
        //Although ID is auto-incremented by MySQL, it is also flagged as "NOT NULL",
        //so we must give it some kind of value in the query (in our case, 0).
        query += Integer.toString(p.getId());
        
        query += "," + Integer.toString(p.getTerminalid());
        query += ",'" + p.getBadgeid() + "',";
        
        //Before we pass in the original timestamp, we need to put it in the proer format.
        query += "'" + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(p.getOriginaltimestamp()) + "',";
        
        query += Integer.toString(p.getPunchtypeid());
        
        //Finally, eventdata and adjustedtimestamp go in as NULLs.
        query += ",NULL,NULL);";
        //If the calling class gets a -1 from this method, then we know something went wrong.
        int newID = -1;
        try{
            Statement statement = conn.createStatement();
            
            //To add or remove entries from the table, we must use Statement.executeUpdate instead of Statement.executeQuery.
            //This call will place any auto-generated table data into statement for later use.
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            
            //Collect the auto-generated table data into a ResultSet.
            ResultSet result = statement.getGeneratedKeys();
            
            //Extract the ID from the above ResultSet.
            if(result != null){
                result.next();
                
                //It isn't listed as "id," so we have to get it by index value.
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
    
    public void close(){
        try{
         conn.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}