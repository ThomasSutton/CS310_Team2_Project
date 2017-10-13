package cs310_team2_project;

import java.sql.*;

public class Badge {
    private String id, description;
    
    public Badge(String id, String description){
        this.id = id;
        this.description = description;
    }
    
    public String getID(){
        return this.id;
    }
    
    public String getDescription(){
        return this.description;
    }
    
    public String toString(){
        String data = "#";
        data += getID();
        data += " (" + getDescription() + ")";
        return data;
    }
}
