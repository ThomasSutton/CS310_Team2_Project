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
 * @author Brian
 */
public class Feature4 {
    private TASDatabase db;
    public Feature4() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        db = new TASDatabase();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testMinutesAccruedShift1Weekday() {
		
		/* Get Punch */
        
        Punch p = db.getPunch(3634);
		
		/* Compute Pay Period Total */
        
        int m = db.getMinutesAccrued(p);
		
		/* Compare to Expected Value */
        
        assertEquals(m, 480);
        
    }
    
    @Test
    public void testMinutesAccruedShift1Weekend() {
		
		/* Get Punch */
        
        Punch p = db.getPunch(1087);
		
		/* Compute Pay Period Total */
        
        int m = db.getMinutesAccrued(p);
		
		/* Compare to Expected Value */
        
        assertEquals(m, 330);
        
    }

    @Test
    public void testMinutesAccruedShift2Weekday() {
		
		/* Get Punch */
        
        Punch p = db.getPunch(4943);
		
		/* Compute Pay Period Total */
        
        int m = db.getMinutesAccrued(p);
		
		/* Compare to Expected Value */
        
        assertEquals(m, 540);
        
    }
}
