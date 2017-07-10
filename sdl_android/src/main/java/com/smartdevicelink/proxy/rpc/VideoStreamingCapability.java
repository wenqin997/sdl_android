package com.smartdevicelink.proxy.rpc;

import com.smartdevicelink.proxy.RPCStruct;

import java.util.Hashtable;

/**
 * Contains information about this system's video streaming capabilities.
 */

public class VideoStreamingCapability extends RPCStruct {
	public static final String KEY_PREFERRED_RESOLUTION = "preferredResolution";
	public static final String KEY_MAX_BITRATE = "maxBitrate";
	public static final String KEY_SUPPORTED_FORMATS = "supportedFormats";

	public VideoStreamingCapability(){}
	public VideoStreamingCapability(Hashtable<String, Object> hash){super(hash);}

	public void setPreferredResolution(ImageResolution res){
		setValue(KEY_PREFERRED_RESOLUTION, res);
	}

	public ImageResolution getPreferredResolution(){
		return (ImageResolution) getObject(ImageResolution.class, KEY_PREFERRED_RESOLUTION);
	}

	public void setMaxBitrate(Integer maxBitrate){
		setValue(KEY_MAX_BITRATE, maxBitrate);
	}

	public Integer getMaxBitrate(){
		return getInteger(KEY_MAX_BITRATE);
	}

	public void setSupportedFormats(VideoStreamingFormat formats){
		setValue(KEY_SUPPORTED_FORMATS, formats);
	}

	public VideoStreamingFormat getSupportedFormats(){
		return (VideoStreamingFormat) getObject(VideoStreamingFormat.class, KEY_SUPPORTED_FORMATS);
	}
}
