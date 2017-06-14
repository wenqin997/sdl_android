package com.smartdevicelink.proxy;

import com.smartdevicelink.exception.SdlException;
import com.smartdevicelink.proxy.rpc.GetSystemCapability;
import com.smartdevicelink.proxy.rpc.GetSystemCapabilityResponse;
import com.smartdevicelink.proxy.rpc.RegisterAppInterfaceResponse;
import com.smartdevicelink.proxy.rpc.SystemCapability;
import com.smartdevicelink.proxy.rpc.enums.SystemCapabilityType;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCResponseListener;
import com.smartdevicelink.proxy.rpc.listeners.SystemCapabilityListener;
import com.smartdevicelink.util.CorrelationIdGenerator;

import java.util.HashMap;

public class SystemCapabilityManager {
    HashMap<SystemCapabilityType, Object> cachedSystemCapabilities = new HashMap<>();
    RegisterAppInterfaceResponse raiResponse;

    public SystemCapabilityManager(RegisterAppInterfaceResponse raiResponse){
        this.raiResponse = raiResponse;
    }

    public void getSystemCapability(SdlProxyBase proxy, final SystemCapabilityType systemCapabilityType, final SystemCapabilityListener scListener){
        Object obj = cachedSystemCapabilities.get(systemCapabilityType);
        if(obj != null){
            scListener.onCapabilityRetrieved(obj);
        }

        if(systemCapabilityType.equals(SystemCapabilityType.NAVIGATION)){
            if(!raiResponse.getHmiCapabilities().isNavigationAvailable()){
                scListener.onCapabilityRetrieved(null);
                return;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.PHONE_CALL)){
            if(!raiResponse.getHmiCapabilities().isPhoneCallAvailable()){
                scListener.onCapabilityRetrieved(null);
                return;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.VIDEO_STREAMING)){
            if(!raiResponse.getHmiCapabilities().isVideoStreamingAvailable()){
                scListener.onCapabilityRetrieved(null);
                return;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.AUDIO_STREAMING)){
            if(!raiResponse.getHmiCapabilities().isAudioStreamingAvailable()){
                scListener.onCapabilityRetrieved(null);
                return;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.DISPLAY)){
            scListener.onCapabilityRetrieved(raiResponse.getDisplayCapabilities());
            return;
        }

        if(systemCapabilityType.canRequestFor()){
            final GetSystemCapability request = new GetSystemCapability();
            request.setSystemCapabilityType(systemCapabilityType);
            request.setOnRPCResponseListener(new OnRPCResponseListener() {
                @Override
                public void onResponse(int correlationId, RPCResponse response) {
                    if(response.getSuccess()){
                        SystemCapability systemCapability = ((GetSystemCapabilityResponse) response).getSystemCapability();
                        Object capability = systemCapability.getCapabilityForType(systemCapabilityType);
                        cachedSystemCapabilities.put(systemCapabilityType, capability);
                        scListener.onCapabilityRetrieved(capability);
                    }
                }
            });
            request.setCorrelationID(CorrelationIdGenerator.generateId());
            try {
                proxy.sendRPCRequest(request);
            } catch (SdlException e) {
                e.printStackTrace();
            }
        }else{
            scListener.onCapabilityRetrieved(null);
        }
    }

}

