package org.vichtisproductions.focusting.model;

/**
 * Created by Renier on 2016/04/12.
 */
public class UserInfo {
    private String mInstanceId;
    private long mInitialStartTime;
    private String mManufacturer;
    private String mModel;
    private String mOSVersion;
    private String mTimezone;

    public UserInfo() {
    }

    public UserInfo(String mInstanceId, long mInitialStartTime, String mManufacturer, String mModel, String mOSVersion, String mTimezone) {
        this.mInstanceId = mInstanceId;
        this.mInitialStartTime = mInitialStartTime;
        this.mManufacturer = mManufacturer;
        this.mModel = mModel;
        this.mOSVersion = mOSVersion;
        this.mTimezone = mTimezone;
    }

    public String getInstanceId() {
        return mInstanceId;
    }

    public void setInstanceId(String mInstanceId) {
        this.mInstanceId = mInstanceId;
    }

    public long getInitialStartTime() {
        return mInitialStartTime;
    }

    public void setInitialStartTime(long mInitialStartTime) {
        this.mInitialStartTime = mInitialStartTime;
    }

    public String getManufacturer() {
        return mManufacturer;
    }

    public void setManufacturer(String mManufacturer) {
        this.mManufacturer = mManufacturer;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String mModel) {
        this.mModel = mModel;
    }

    public String getOSVersion() {
        return mOSVersion;
    }

    public void setOSVersion(String mOSVersion) {
        this.mOSVersion = mOSVersion;
    }

    public String getTimezone() { return mTimezone; }

    public void setTimezone(String mTimezone) { this.mTimezone = mTimezone; }

}
