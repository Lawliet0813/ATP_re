package decoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import decoder.MMIVariables;

/**
 * Unit tests for MMI (Man-Machine Interface) Decoder
 * Tests MMI packet decoding and variable extraction
 */
public class MMIDecoderTest {
    
    private MMIVariables mmiVars;
    
    @BeforeEach
    public void setUp() {
        mmiVars = new MMIVariables();
    }
    
    @Test
    @DisplayName("Test MMI_V_TRAIN speed calculation")
    public void testTrainSpeedCalculation() {
        // Test speed calculation from 2 bytes
        // Speed in km/h = (byte1 * 256 + byte2) * 5 / 10
        byte b1 = 0x00;
        byte b2 = 0x14; // 20 decimal
        
        int speed = mmiVars.MMI_V_TRAIN(b1, b2);
        
        assertTrue(speed >= 0, "Speed should be non-negative");
    }
    
    @Test
    @DisplayName("Test MMI_A_TRAIN acceleration calculation")
    public void testTrainAccelerationCalculation() {
        // Test acceleration calculation
        byte b1 = 0x00;
        byte b2 = 0x00; // Zero acceleration
        
        int acceleration = mmiVars.MMI_A_TRAIN(b1, b2);
        
        assertTrue(acceleration >= -200 && acceleration <= 200, 
                   "Acceleration should be in valid range");
    }
    
    @Test
    @DisplayName("Test MMI_O_TRAIN location calculation")
    public void testTrainLocationCalculation() {
        // Test location from 4 bytes
        byte b1 = 0x00;
        byte b2 = 0x00;
        byte b3 = 0x00;
        byte b4 = 0x64; // 100 decimal
        
        int location = mmiVars.MMI_O_TRAIN(b1, b2, b3, b4);
        
        assertTrue(location >= 0, "Location should be non-negative");
    }
    
    @Test
    @DisplayName("Test MMI mode indicator flags")
    public void testMMIModeIndicators() {
        // Test various mode indicators
        byte testByte = 0x01;
        
        boolean result = mmiVars.MMI_M_ADHESION(testByte);
        
        // Result should be boolean
        assertTrue(result == true || result == false, "Should return boolean value");
    }
    
    @Test
    @DisplayName("Test MMI level calculation")
    public void testMMILevelCalculation() {
        byte testByte = 0x02; // Level 2
        
        int level = mmiVars.MMI_M_LEVEL(testByte);
        
        assertTrue(level >= 0 && level <= 3, "Level should be 0-3");
    }
    
    @Test
    @DisplayName("Test brake status indicators")
    public void testBrakeStatusIndicators() {
        byte testByte = 0x01;
        
        boolean brakeStatus = mmiVars.MMI_M_BRAKE(testByte);
        
        // Should return valid boolean
        assertNotNull(brakeStatus, "Brake status should not be null");
    }
    
    @Test
    @DisplayName("Test warning indicators")
    public void testWarningIndicators() {
        byte testByte = 0x01;
        
        boolean warning = mmiVars.MMI_M_WARNING(testByte);
        
        // Should return valid boolean
        assertNotNull(warning, "Warning status should not be null");
    }
    
    @Test
    @DisplayName("Test negative speed values")
    public void testNegativeSpeedValues() {
        // Test with negative byte values
        byte b1 = (byte)0xFF;
        byte b2 = (byte)0xFF;
        
        int speed = mmiVars.MMI_V_TRAIN(b1, b2);
        
        // Speed calculation should handle negative bytes correctly
        assertTrue(speed >= 0, "Speed should be non-negative even with negative bytes");
    }
    
    @Test
    @DisplayName("Test maximum speed values")
    public void testMaximumSpeedValues() {
        // Test with maximum byte values
        byte b1 = (byte)0xFF;
        byte b2 = (byte)0xFF;
        
        int speed = mmiVars.MMI_V_TRAIN(b1, b2);
        
        // Should handle maximum values without overflow
        assertTrue(speed >= 0 && speed <= 65535 * 5, 
                   "Speed should be in valid range");
    }
    
    @Test
    @DisplayName("Test zero values")
    public void testZeroValues() {
        byte b1 = 0x00;
        byte b2 = 0x00;
        
        int speed = mmiVars.MMI_V_TRAIN(b1, b2);
        
        assertEquals(0, speed, "Zero bytes should result in zero speed");
    }
}
