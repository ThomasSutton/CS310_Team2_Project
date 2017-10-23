/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.jsu.mcis;

import cs310_team2_project.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author briancwillis
 */
public class Feature2 {
    public TASDatabase db;
    
    public Feature2() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setup() {
        db = new TASDatabase();
    }
    
    @Test
    public void testInsertCheckPunch() {
		
		/* Create New Punch Object */

        Punch p1 = new Punch("021890C0", 101, 1);
		
		/* Get Punch Properties */
        
        String badgeid = p1.getBadgeid();
        String originaltimestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(p1.getOriginaltimestamp().getTime());
        int terminalid = p1.getTerminalid();
        int eventtypeid = p1.getPunchtypeid();
		
		/* Insert Punch Into Database */
        
        int punchid = db.insertPunch(p1);
		
		/* Retrieve New Punch */
        
        Punch p2 = db.getPunch(punchid);
		
		/* Compare Punches */

        assertEquals(badgeid, p2.getBadgeid());
        assertEquals(originaltimestamp, (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(p2.getOriginaltimestamp().getTime()));
        assertEquals(terminalid, p2.getTerminalid());
        assertEquals(eventtypeid, p2.getPunchtypeid());
        
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
