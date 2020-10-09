package com.huawei.vi.entity.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * tbl_ipc_ip_sync_record
 * @author 
 */
public class TblIpcIpSyncRecord implements Serializable {

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 设备编码
     */
    private String deviceid;


    private String level1;

    private String level2;

    private String level3;

    private String level4;

    private String level5;

    private List<String> levels = new ArrayList<>();

    /**
     * 设备名称
     */
    private String name;

    /**
     * 区域父id
     */
    private String parentid;

    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备归属
     */
    private String owner;

    /**
     * 行政区域
     */
    private String civilcode;

    /**
     * 设备安装地址
     */
    private String address;

    /**
     * 加密签名同时采用的方式；4 数字摘要方式
     */
    private String safetyway;

    /**
     * 注册方式缺省是1
     */
    private String registerway;

    /**
     * 保密属性，缺省是0；0不涉密，1涉密
     */
    private String secrecy;

    /**
     * 为设备时是否有子设备，1有，0没有
     */
    private String parental;

    /**
     * 设备状态
     */
    private String status;

    /**
     * 设备端口号
     */
    private String port;

    /**
     * 设备IP地址
     */
    private String ipaddress;

    /**
     * 登陆用户
     */
    private String loginname;

    /**
     * 密码
     */
    private String password;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 分辨率
     */
    private String hd;

    private String channelno;

    /**
     * 摄像机类型，1球机，2半球，3固定枪机，4遥控枪机
     */
    private String info;

    /**
     * 同步时间
     */
    private Date operatetime;

    /**
     * 同步的设备，需要做什么操作 updata(设备已存在需要更新)，add（设备需要新增）
     */
    private String synctype;

    /**
     * 层级下的设备
     */
    List<TblIpcIpSyncRecord> child;

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getLevel3() {
        return level3;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public String getLevel4() {
        return level4;
    }

    public void setLevel4(String level4) {
        this.level4 = level4;
    }

    public String getLevel5() {
        return level5;
    }

    public void setLevel5(String level5) {
        this.level5 = level5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCivilcode() {
        return civilcode;
    }

    public void setCivilcode(String civilcode) {
        this.civilcode = civilcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSafetyway() {
        return safetyway;
    }

    public void setSafetyway(String safetyway) {
        this.safetyway = safetyway;
    }

    public String getRegisterway() {
        return registerway;
    }

    public void setRegisterway(String registerway) {
        this.registerway = registerway;
    }

    public String getSecrecy() {
        return secrecy;
    }

    public void setSecrecy(String secrecy) {
        this.secrecy = secrecy;
    }

    public String getParental() {
        return parental;
    }

    public void setParental(String parental) {
        this.parental = parental;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getChannelno() {
        return channelno;
    }

    public void setChannelno(String channelno) {
        this.channelno = channelno;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Date getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(Date operatetime) {
        this.operatetime = operatetime;
    }

    public List<TblIpcIpSyncRecord> getChild() {
        return child;
    }

    public void setChild(List<TblIpcIpSyncRecord> child) {
        this.child = child;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void setLevels(List<String> levels) {
        this.levels = levels;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getSynctype() {
        return synctype;
    }

    public void setSynctype(String synctype) {
        this.synctype = synctype;
    }

    public String getHd() {
        return hd;
    }

    public void setHd(String hd) {
        this.hd = hd;
    }
}