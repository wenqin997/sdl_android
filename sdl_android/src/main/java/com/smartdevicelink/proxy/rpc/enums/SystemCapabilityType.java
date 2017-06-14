package com.smartdevicelink.proxy.rpc.enums;

import java.util.EnumSet;

/**
 * The SystemCapabilityType indicates which type of capability information exists in a SystemCapability struct.
 */

public enum SystemCapabilityType {
    NAVIGATION("NAVIGATION", true),

    PHONE_CALL("PHONE_CALL", true),

    VIDEO_STREAMING("VIDEO_STREAMING", true),

    AUDIO_STREAMING("AUDIO_STREAMING", true),

    DISPLAY("DISPLAY", false);

    private final String INTERNAL_NAME;
    private final boolean CAN_REQUEST_FOR;

    private SystemCapabilityType(String internalName, boolean requestable){
        this.INTERNAL_NAME = internalName;
        this.CAN_REQUEST_FOR = requestable;
    }

    public String toString() {
        return this.INTERNAL_NAME;
    }

    public Boolean canRequestFor() {return this.CAN_REQUEST_FOR;}

    public static SystemCapabilityType valueForString(String value) {
        if(value == null){
            return null;
        }

        for (SystemCapabilityType anEnum : EnumSet.allOf(SystemCapabilityType.class)) {
            if (anEnum.toString().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
