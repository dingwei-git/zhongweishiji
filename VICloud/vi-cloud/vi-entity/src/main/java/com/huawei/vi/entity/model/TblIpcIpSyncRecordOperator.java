package com.huawei.vi.entity.model;

import java.util.Date;

public class TblIpcIpSyncRecordOperator {

    private String id;

    private String taskid;

    private String copolecode;

    private String ip;

    private String cameranum;

    private String cameraName;

    private String cameraid;

    private String checkflag;

    private String iptype;

    private String longitude;

    private String latitude;

    private String platformip;

    private String cameraSn;

    private String groupName;

    private String lastonlinetime;

    private String refertime;

    private Date createtime;

    private String operator;

    private String status;

    private String currentlevels;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    private String hd;

    private String rawlevels;

    public String getCurrentlevels() {
        return currentlevels;
    }

    public void setCurrentlevels(String currentlevels) {
        this.currentlevels = currentlevels == null ? null : currentlevels.trim();
    }

    public String getRawlevels() {
        return rawlevels;
    }

    public void setRawlevels(String rawlevels) {
        this.rawlevels = rawlevels == null ? null : rawlevels.trim();
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid == null ? null : taskid.trim();
    }

    public String getCopolecode() {
        return copolecode;
    }

    public void setCopolecode(String copolecode) {
        this.copolecode = copolecode == null ? null : copolecode.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getCameranum() {
        return cameranum;
    }

    public void setCameranum(String cameranum) {
        this.cameranum = cameranum == null ? null : cameranum.trim();
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName == null ? null : cameraName.trim();
    }

    public String getCameraid() {
        return cameraid;
    }

    public void setCameraid(String cameraid) {
        this.cameraid = cameraid == null ? null : cameraid.trim();
    }

    public String getCheckflag() {
        return checkflag;
    }

    public void setCheckflag(String checkflag) {
        this.checkflag = checkflag == null ? null : checkflag.trim();
    }

    public String getIptype() {
        return iptype;
    }

    public void setIptype(String iptype) {
        this.iptype = iptype == null ? null : iptype.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }

    public String getPlatformip() {
        return platformip;
    }

    public void setPlatformip(String platformip) {
        this.platformip = platformip == null ? null : platformip.trim();
    }

    public String getCameraSn() {
        return cameraSn;
    }

    public void setCameraSn(String cameraSn) {
        this.cameraSn = cameraSn == null ? null : cameraSn.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getLastonlinetime() {
        return lastonlinetime;
    }

    public void setLastonlinetime(String lastonlinetime) {
        this.lastonlinetime = lastonlinetime == null ? null : lastonlinetime.trim();
    }

    public String getRefertime() {
        return refertime;
    }

    public void setRefertime(String refertime) {
        this.refertime = refertime == null ? null : refertime.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }
}