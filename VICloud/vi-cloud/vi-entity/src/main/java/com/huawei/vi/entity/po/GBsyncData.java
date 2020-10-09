package com.huawei.vi.entity.po;

import lombok.Data;

@Data
public class GBsyncData {

    private String ip;

    private int port;

    private String platformId;

    public GBsyncData(String ip, int port, String platformId) {
        this.ip = ip;
        this.port = port;
        this.platformId = platformId;
    }
}
