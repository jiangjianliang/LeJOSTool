package com.wander.train.nxt;


import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import java.util.*;

/**
 * Supports for HiTechnic IRLink - see http://www.hitechnic.com/.
 * 
 * @author Lawrie Griffiths original class author
 * @author Pierre Archambault extended with all PF modes and functionalities
 */

public class IrLinkExt extends I2CSensor {
    /**
     * Variable enumeration
     */

    // Private variables
    private final static byte TX_BUFFER = 0x40; // 40 to 4C
    private final static byte TX_MAX_BUFFER_LEN = 13;

    // IR PF signal encoding parameters
    private final byte MAX_BITS = TX_MAX_BUFFER_LEN * 8;
    private final byte STOP_START_PAUSE = 7;
    private final byte LOW_BIT_PAUSE = 2;
    private final byte HIGH_BIT_PAUSE = 4;
    private byte toggle = 0;
    private BitSet bits = new BitSet(MAX_BITS);
    private int nextBit = 0;
    private final static byte TX_MODE_PF = 2;

    // PF Receiver channel selection
    public static final byte PF_Channel_1 = 0;
    public static final byte PF_Channel_2 = 1;
    public static final byte PF_Channel_3 = 2;
    public static final byte PF_Channel_4 = 3;

    // PF Modes
    private final static byte ESC_MODE_SELECT = 0;
    private final static byte ESC_PWM = 1;
    private final static byte PF_MODE_EXTENDED = 0;
    private final static byte PF_MODECOMBO_DIRECT = 1;

    // PF PWM motor operations for various modes
    public static final byte PF_PMW_FLOAT = 0;
    public static final byte PF_PMW_FWD_1 = 1;
    public static final byte PF_PMW_FWD_2 = 2;
    public static final byte PF_PMW_FWD_3 = 3;
    public static final byte PF_PMW_FWD_4 = 4;
    public static final byte PF_PMW_FWD_5 = 5;
    public static final byte PF_PMW_FWD_6 = 6;
    public static final byte PF_PMW_FWD_7 = 7;
    public static final byte PF_PMW_BRK_FLT = 8;
    public static final byte PF_PMW_REV_7 = 9;
    public static final byte PF_PMW_REV_6 = 10;
    public static final byte PF_PMW_REV_5 = 11;
    public static final byte PF_PMW_REV_4 = 12;
    public static final byte PF_PMW_REV_3 = 13;
    public static final byte PF_PMW_REV_2 = 14;
    public static final byte PF_PMW_REV_1 = 15;

    // PF Combo Direct motor operations
    public static final byte PF_COMBO_DIRECT_FLOAT = 0;
    public static final byte PF_COMBO_DIRECT_FORWARD = 1;
    public static final byte PF_COMBO_DIRECT_BACKWARD = 2;
    public static final byte PF_COMBO_DIRECT_BRAKE_FLOAT = 3;

    // PF Extended mode motor operations
    public static final byte PF_EXTENDED_BRAKE_FLOAT = 0;
    public static final byte PF_EXTENDED_INC_SPEED_A = 1;
    public static final byte PF_EXTENDED_DEC_SPEED_A = 2;
    public static final byte PF_EXTENDED_TOGGLE_FWD_FLT_B = 4;

    // PF Single Mode PWM & CST motor selection
    public static final byte PF_SINGLE_MODE_RED_PORT = 0;
    public static final byte PF_SINGLE_MODE_BLUE_PORT = 1;

    // PF PWM motor operations for various modes
    public static final byte PF_CST_TOGGLE_FULL_FW = 0;
    public static final byte PF_CST_TOGGLE_DIR = 1;
    public static final byte PF_CST_INC_PWM_NUM = 2;
    public static final byte PF_CST_DEC_PWM_NUM = 3;
    public static final byte PF_CST_INC_PWM = 4;
    public static final byte PF_CST_DEC_PWM = 5;
    public static final byte PF_CST__FULL_FW = 6;
    public static final byte PF_CST_FULL_BW = 7;
    public static final byte PF_CST_TOGGLE_FULL_FW_BW = 8;
    public static final byte PF_CST_CLR_C1 = 9;
    public static final byte PF_CST_SET_C1 = 10;
    public static final byte PF_CST_TOGGLE_C1 = 11;
    public static final byte PF_CST_CLR_C2 = 12;
    public static final byte PF_CST_SET_C2 = 13;
    public static final byte PF_CST_TOGGLE_C2 = 14;
    public static final byte PF_CST_TOGGLE_FL_BW = 15;

    // Public object
    public IrLinkExt(I2CPort port) {
        super(port);
    }

    /**
     * Send commands to both motors. Uses PF Combo PMW mode.
     * 
     * @param channel
     *            the channel number (0-3)
     * @param opA
     *            Motor A operation RED
     * @param opB
     *            Motor B operation BLUE
     */
    public void sendPFComboPmw(int channel, int opA, int opB)
    {
        sendPF_PWM(channel, opA, opB);
    }

    private void sendPF_PWM(int channel, int opA, int opB)
    {
        byte nibble1 = (byte) ((0 << 3) | (ESC_PWM << 2) | channel);// 0 is a place holder for a (address) for future use
        byte nibble2 = (byte) (opB);
        byte nibble3 = (byte) (opA);
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;
        completeSend(pfData);
    }

    /**
     * Send commands to both motors. Uses PF Combo DIRECT mode.
     * 
     * @param channel
     *            the channel number (0-3)
     * @param opA
     *            Motor A operation RED
     * @param opB
     *            Motor B operation BLUE
     */
    public void sendPFComboDirect(int channel, int opA, int opB)
    {
        sendPF_ComboDirect(channel, opA, opB);
    }

    private void sendPF_ComboDirect(int channel, int opA, int opB) 
    {
        byte nibble1 = (byte) ((toggle << 3) | (ESC_MODE_SELECT << 2) | channel);
        byte nibble2 = (byte) (0 << 3) | PF_MODECOMBO_DIRECT;// 0 is a place holder for a (address) for future use
        byte nibble3 = (byte) (opB << 2 | opA);
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;
        completeSend(pfData);
    }

    /**
     * Send commands to both motors. Uses PF Extended mode.
     * 
     * @param channel
     *            the channel number (0-3)
     * @param opA
     *            Refer to action list
     */

    public void sendPFExtended(int channel, int opA) {
        sendPF_ExtendedMode(channel, opA);
    }

    private void sendPF_ExtendedMode(int channel, int opA)
    {
        byte nibble1 = (byte) ((toggle << 3) | (ESC_MODE_SELECT << 2) | channel);
        byte nibble2 = (byte) (0 << 3) | PF_MODE_EXTENDED;// 0 is a place holder for a (address) for future use
        byte nibble3 = (byte) (opA);
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;
        completeSend(pfData);
    }

    /**
     * Send commands to a single motors. Uses PF Single mode PWM.
     *
     * @param channel
     *            the channel number (0-3)
     * @param opA
     *            Motor selection Red/Blue port
     * @param opB
     *            Use PF_PWM operation codes
     */

    public void sendPFSingleModePWM(int channel, int opA, int opB) // opA = output RED or BlUE, opB command code
    { 
        sendPF_SingleModePWM(channel, opA, opB);
    }

    private void sendPF_SingleModePWM(int channel, int opA, int opB) 
    {
        byte nibble1 = (byte) ((toggle << 3) | (ESC_MODE_SELECT << 2) | channel);
        byte nibble2 = (byte) ((0 << 3) | (1 << 2) | (0 << 1) | opA);// 0 is a place holder for a (address) for future use
        byte nibble3 = (byte) (opB);
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;
        completeSend(pfData);
    }

    /**
     * Send commands to a single port. Uses PF Single mode Clear/Set/Toggle
     * control PINS.
     * 
     * @param channel
     *            the channel number (0-3)
     * @param opA
     *            Motor selection Red/Blue port
     * @param opB
     *            Refer to action list
     */

    public void sendPFSingleModeCST(int channel, int opA, int opB)// opA = output RED or BlUE, opB command code
    { 
        sendPF_SingleModeCST(channel, opA, opB);
    }

    private void sendPF_SingleModeCST(int channel, int opA, int opB) 
    {
        byte nibble1 = (byte) ((toggle << 3) | (ESC_MODE_SELECT << 2) | channel);
        byte nibble2 = (byte) ((0 << 3) | (1 << 2) | (1 << 1) | opA);// 0 is a place holder for a (address) for future use 
        byte nibble3 = (byte) (opB);
        byte lrc = (byte) (0xF ^ nibble1 ^ nibble2 ^ nibble3);
        int pfData = (nibble1 << 12) | (nibble2 << 8) | (nibble3 << 4) | lrc;
        completeSend(pfData);
    }

    // All mode share the same complete and send methods that follows
    private void completeSend(int pfData)
    {
        clearBits();
        nextBit = 0;
        setBit(STOP_START_PAUSE); // Start
        for (int i = 15; i >= 0; i--) {
            setBit(((pfData >> i) & 1) == 0 ? LOW_BIT_PAUSE : HIGH_BIT_PAUSE);
        }
        setBit(STOP_START_PAUSE); // Stop
        toggle ^= 1;
        byte[] pfCommand = new byte[16];

        for (int i = 0; i < MAX_BITS; i++) {
            boolean bit = bits.get(i);
            int byteIndex = i / 8;
            int bitVal = (bit ? 1 : 0);
            pfCommand[byteIndex] |= (bitVal << (7 - i % 8)); // X-OR byteIndex with bitVal left shifted (7 - remainder i /8)
        }

        pfCommand[13] = TX_MAX_BUFFER_LEN;
        pfCommand[14] = TX_MODE_PF;
        pfCommand[15] = 1;

        sendData(TX_BUFFER, pfCommand, TX_MAX_BUFFER_LEN + 3);
    }

    private void setBit(int pause)
    {
        bits.set(nextBit++);
        nextBit += pause;
    }

    private void clearBits() 
    {
        for (int i = 0; i < MAX_BITS; i++)
            bits.clear(i);
    }

}



































