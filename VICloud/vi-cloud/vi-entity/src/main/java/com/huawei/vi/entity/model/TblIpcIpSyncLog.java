package com.huawei.vi.entity.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tbl_ipc_ip_sync_log
 * @author 
 */
@Data
public class TblIpcIpSyncLog implements Serializable {

    private String id;

    /**
     * record表中的taskid
     */
    private String taskid;

    /**
     * 设备编码
     */
    private String cameraid;

    /**
     * 记录的状态：add新增，updata更新
     */
    private String status;

    /**
     * 设备的详细信息
     */
    private String details;

    /**
     * 操作时间
     */
    private Date createtime;

    /**
     * 操作人员
     */
    private String operator;

    private static final long serialVersionUID = 1L;
}