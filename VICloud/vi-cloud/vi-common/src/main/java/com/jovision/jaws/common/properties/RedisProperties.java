package com.jovision.jaws.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public class RedisProperties {

    private String redisip;
    private String redisport;
    private String redispass;
    private String redismaxactive;
    private String redismaxidle;
    private String redismaxwait;
    private String redistimeout;
    private String redistestonborrow;

    public String getRedisip() {
        return redisip;
    }

    public void setRedisip(String redisip) {
        this.redisip = redisip;
    }

    public String getRedisport() {
        return redisport;
    }

    public void setRedisport(String redisport) {
        this.redisport = redisport;
    }

    public String getRedispass() {
        return redispass;
    }

    public void setRedispass(String redispass) {
        this.redispass = redispass;
    }

    public String getRedismaxactive() {
        return redismaxactive;
    }

    public void setRedismaxactive(String redismaxactive) {
        this.redismaxactive = redismaxactive;
    }

    public String getRedismaxidle() {
        return redismaxidle;
    }

    public void setRedismaxidle(String redismaxidle) {
        this.redismaxidle = redismaxidle;
    }

    public String getRedismaxwait() {
        return redismaxwait;
    }

    public void setRedismaxwait(String redismaxwait) {
        this.redismaxwait = redismaxwait;
    }

    public String getRedistimeout() {
        return redistimeout;
    }

    public void setRedistimeout(String redistimeout) {
        this.redistimeout = redistimeout;
    }

    public String getRedistestonborrow() {
        return redistestonborrow;
    }

    public void setRedistestonborrow(String redistestonborrow) {
        this.redistestonborrow = redistestonborrow;
    }
}
