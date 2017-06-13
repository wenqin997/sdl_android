package com.smartdevicelink.proxy;

import com.smartdevicelink.exception.SdlException;
import com.smartdevicelink.proxy.rpc.GetSystemCapability;
import com.smartdevicelink.proxy.rpc.GetSystemCapabilityResponse;
import com.smartdevicelink.proxy.rpc.SystemCapability;
import com.smartdevicelink.proxy.rpc.enums.SystemCapabilityType;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCResponseListener;
import com.smartdevicelink.util.CorrelationIdGenerator;

import java.util.HashMap;

/**
 * Created by austinkirk on 6/12/17.
 */

public class SystemCapabilityManager {
    HashMap<String, Object> cachedSystemCapabilities = new HashMap<>();
    SdlProxyALM proxy = null;
    final Object SYS_CAP_LOCK = new Object();

    public SystemCapabilityManager(SdlProxyALM proxyALM){
        proxy = proxyALM;
    }

    public Object getSystemCapability(final SystemCapabilityType systemCapabilityType) throws SdlException {
        Object obj = cachedSystemCapabilities.get(systemCapabilityType.toString());
        if(obj != null){
            return obj;
        }

        if(systemCapabilityType.equals(SystemCapabilityType.NAVIGATION)){
            if(!proxy.getHmiCapabilities().isNavigationAvailable()){
                return null;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.PHONE_CALL)){
            if(!proxy.getHmiCapabilities().isPhoneCallAvailable()){
                return null;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.VIDEO_STREAMING)){
            if(!proxy.getHmiCapabilities().isVideoStreamingAvailable()){
                return null;
            }
        }else if(systemCapabilityType.equals(SystemCapabilityType.AUDIO_STREAMING)){
            if(!proxy.getHmiCapabilities().isAudioStreamingAvailable()){
                return null;
            }
        }

        final GetSystemCapability request = new GetSystemCapability();
        request.setSystemCapabilityType(systemCapabilityType);
        request.setOnRPCResponseListener(new OnRPCResponseListener() {
            @Override
            public void onResponse(int correlationId, RPCResponse response) {
                if(response.getSuccess()){
                    SystemCapability systemCapability = ((GetSystemCapabilityResponse) response).getSystemCapability();
                    Object capability = systemCapability.getCapabilityForType(systemCapabilityType);
                    if(capability != null) {
                        synchronized(SYS_CAP_LOCK){
                            SYS_CAP_LOCK.notify();
                        }
                        cachedSystemCapabilities.put(systemCapabilityType.toString(), capability);
                    }
                }
            }
        });
        request.setCorrelationID(CorrelationIdGenerator.generateId());
        proxy.sendRPCRequest(request);

        synchronized(SYS_CAP_LOCK) {
            try {
                SYS_CAP_LOCK.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        obj = cachedSystemCapabilities.get(systemCapabilityType.toString());
        return obj;
    }

}

