package arch.sm213.machine.student;

import machine.AbstractMainMemory;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainMemoryTest {
    MainMemory dram = new MainMemory(256);
    @Test
    public void TestIsAccessAligned() {
        // see if isAccessAligned() returns true in case of aligned access
        assertTrue(dram.isAccessAligned(0,4));
        assertTrue(dram.isAccessAligned(4,4));
        // see if isAccessAligned() returns false in case of unaligned access
        assertFalse(dram.isAccessAligned(1,4));
        assertFalse(dram.isAccessAligned(2,4));
        assertFalse(dram.isAccessAligned(3,4));
    }

    @Test
    public void TestBytesToInteger() {
        // zero, positive and negative numbers
        assertEquals(0,             dram.bytesToInteger((byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00));
        assertEquals(32768,         dram.bytesToInteger((byte)0x00, (byte)0x00, (byte)0x80, (byte)0x00));
        assertEquals(-32768,        dram.bytesToInteger((byte)0xff, (byte)0xff, (byte)0x80, (byte)0x00));
        // edge case
        assertEquals(-1,            dram.bytesToInteger((byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff));
        assertEquals(-2147483648,   dram.bytesToInteger((byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00));
        // see if wrong result pass the test
        assertNotEquals(88,        dram.bytesToInteger((byte)0xde, (byte)0xad, (byte)0xbe, (byte)0xff));
    }

    @Test
    public void TestIntegerToBytes() {
        //zeros
        byte[] expected = {0,0,0,0};
        assertArrayEquals(expected, dram.integerToBytes(0));

        //positive number
        expected = new byte[] {0,0,(byte)0x80,0};
        assertArrayEquals(expected, dram.integerToBytes(32768));

        //negative number
        expected = new byte[] {(byte)0xff,(byte)0xff,(byte)0x80,0};
        assertArrayEquals(expected, dram.integerToBytes(-32768));

        // 0xffffffff
        expected = new byte[] {(byte)0xff,(byte)0xff,(byte)0xff, (byte)0xff};
        assertArrayEquals(expected, dram.integerToBytes(-1));

        // 0x7fffffff
        expected = new byte[] {(byte)0x7f,(byte)0xff,(byte)0xff, (byte)0xff};
        assertArrayEquals(expected, dram.integerToBytes(2147483647));

        // 0x000000ff
        expected = new byte[] {(byte)0,(byte)0,(byte)0, (byte)0xff};
        assertArrayEquals(expected, dram.integerToBytes(255));

    }


    @Test
    public void testGetAndSet() throws AbstractMainMemory.InvalidAddressException {
        MainMemory mem = new MainMemory(256);

        byte [] intArr = {2,3,5,7};
        byte [] intArr2 = {2,3,3,3};

        try{
            mem.set(-1, intArr);
            fail("exception not thrown when address < 0");
        }
        catch (Exception ignored){
        }

        try{
            mem.set(2048, intArr);
            fail("exception not thrown when address > MainMemory.length");
        }
        catch (Exception ignored){
        }

        //setter / getter test
        mem.set(0,  intArr);
        assertArrayEquals(intArr, mem.get(0,4));
        mem.set(128,  intArr);
        assertArrayEquals(intArr, mem.get(128,4));
        mem.set(128,  intArr2);
        assertArrayEquals(intArr2, mem.get(128,4));
    }

}