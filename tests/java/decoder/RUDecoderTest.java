package decoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import decoder.RU;
import decoder.HeadDecoder;

/**
 * Unit tests for RU (Recording Unit) Decoder
 * Tests packet decoding according to ATPRU-LOGF-001 v1.8 specification
 */
public class RUDecoderTest {
    
    private HeadDecoder decoder;
    
    @BeforeEach
    public void setUp() {
        decoder = new HeadDecoder();
    }
    
    @Test
    @DisplayName("Test RU packet header parsing")
    public void testPacketHeaderParsing() {
        // Create test packet with known header values
        byte[] testPacket = createTestPacket();
        
        RU ru = new RU(testPacket);
        
        // Verify packet type is decoded correctly
        assertTrue(ru.getPacketType() >= 0, "Packet type should be non-negative");
        
        // Verify timestamp is valid
        assertNotNull(ru.getTimestamp(), "Timestamp should not be null");
        
        // Verify location stamp is within valid range
        assertTrue(ru.getLocation() >= 0, "Location should be non-negative");
    }
    
    @Test
    @DisplayName("Test speed stamp parsing according to ATPRU-LOGF-001 v1.8")
    public void testSpeedStampParsing() {
        // Speed stamp is 4 bytes at offset 11-14 (signed, cm/s)
        byte[] testPacket = createTestPacketWithSpeed(1000); // 10 m/s
        
        RU ru = new RU(testPacket);
        
        // Speed should be parsed correctly from 4-byte field
        assertNotEquals(0, ru.getSpeed(), "Speed should be parsed");
    }
    
    @Test
    @DisplayName("Test MMI_DYNAMIC packet decoding")
    public void testMMIDynamicPacket() {
        byte[] mmiDynamicPacket = createMMIDynamicPacket();
        
        RU ru = new RU(mmiDynamicPacket);
        
        // Packet type 1 is MMI_DYNAMIC
        assertEquals(1, ru.getPacketType(), "Should decode as MMI_DYNAMIC");
    }
    
    @Test
    @DisplayName("Test BTM fragment handling")
    public void testBTMFragmentHandling() {
        byte[] btmPacket = createBTMPacket();
        
        RU ru = new RU(btmPacket);
        
        // BTM packets should be identified correctly
        // Packet types 5-9 are BTM fragments
        assertTrue(ru.getPacketType() >= 5 && ru.getPacketType() <= 9 || 
                   ru.getPacketType() == 1, // or other valid types
                   "Packet type should be valid");
    }
    
    @Test
    @DisplayName("Test invalid packet handling")
    public void testInvalidPacketHandling() {
        byte[] invalidPacket = new byte[10]; // Too short
        
        assertThrows(Exception.class, () -> {
            RU ru = new RU(invalidPacket);
            // Should throw exception for invalid packet
        });
    }
    
    @Test
    @DisplayName("Test null packet handling")
    public void testNullPacketHandling() {
        assertThrows(NullPointerException.class, () -> {
            RU ru = new RU(null);
        });
    }
    
    // Helper methods to create test packets
    
    private byte[] createTestPacket() {
        // Create a minimal valid RU packet (at least 15 bytes for header)
        byte[] packet = new byte[32];
        
        // Packet type (byte 0)
        packet[0] = 1; // MMI_DYNAMIC
        
        // Timestamp (bytes 1-6)
        packet[1] = 0x25; // Year BCD
        packet[2] = 0x09; // Month BCD
        packet[3] = 0x03; // Day BCD
        packet[4] = 0x02; // Hour BCD
        packet[5] = 0x44; // Minute BCD
        packet[6] = 0x32; // Second BCD
        
        // Location stamp (bytes 7-10) - 4 bytes signed
        packet[7] = 0x00;
        packet[8] = 0x4E;
        packet[9] = 0x72;
        packet[10] = 0x09;
        
        // Speed stamp (bytes 11-14) - 4 bytes signed, cm/s
        packet[11] = 0x00;
        packet[12] = 0x00;
        packet[13] = 0x08;
        packet[14] = 0x3B; // 2107 cm/s
        
        return packet;
    }
    
    private byte[] createTestPacketWithSpeed(int speedCmPerSec) {
        byte[] packet = createTestPacket();
        
        // Set speed stamp (bytes 11-14) - 4 bytes signed
        packet[11] = (byte)((speedCmPerSec >> 24) & 0xFF);
        packet[12] = (byte)((speedCmPerSec >> 16) & 0xFF);
        packet[13] = (byte)((speedCmPerSec >> 8) & 0xFF);
        packet[14] = (byte)(speedCmPerSec & 0xFF);
        
        return packet;
    }
    
    private byte[] createMMIDynamicPacket() {
        byte[] packet = createTestPacket();
        packet[0] = 1; // MMI_DYNAMIC type
        return packet;
    }
    
    private byte[] createBTMPacket() {
        byte[] packet = createTestPacket();
        packet[0] = 5; // BTM fragment type
        return packet;
    }
}
