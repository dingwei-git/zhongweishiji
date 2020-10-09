package com.huawei.vi.entity.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class OrderEntity {

        private int id;

        /**
         * 创建时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        private Date creatTime;

        /**
         * 完成时长（h）
         */
        private int durationTime;

        /**
         * 工单标题
         */
        private String orderTitle;

        /**
         * 摄像机编码
         */
        private String cameraId;


        /**
         * 工单类型
         */
        private String orderType;

        /**
         * 设备类型
         */
        private String deviceType;

        /**
         * 工单状态
         */
        private String orderStatus;

        /**
         * 创建者
         */
        private String creator;

        /**
         * 创建方式
         */
        private String creationType;

        /**
         * 故障类型
         */
        private String faultTypes;

        /**
         * 工单描述
         */
        private String orderDescription;

        /**
         * 建单审核意见
         */
        private String examineMessage;

        /**
         * 工单编码
         */
        private String orderCode;
}
