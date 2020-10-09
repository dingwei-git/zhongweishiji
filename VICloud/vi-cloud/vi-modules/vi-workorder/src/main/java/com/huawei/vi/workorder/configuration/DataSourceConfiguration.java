package com.huawei.vi.workorder.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.huawei.vi.workorder.properties.GuassViDsProperties;
import com.huawei.vi.workorder.properties.GuassVicDsProperties;
import com.huawei.vi.workorder.properties.MysqlViDsProperties;
import com.huawei.vi.workorder.properties.MysqlVicDsProperties;
import com.jovision.jaws.common.datasource.DynamicDataSource;
import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfiguration {

    @Value("${dbtype}")
    private String dbtype;

    @Autowired
    private MysqlViDsProperties mysqlViDsProperties;

    @Autowired
    private MysqlVicDsProperties mysqlVicDsProperties;

    @Autowired
    private GuassViDsProperties guassViDsProperties;

    @Autowired
    private GuassVicDsProperties guassVicDsProperties;

    private DSProperties viProperties;

    private DSProperties vicProperties;



    public DataSource videoInsight(){

        if("gauss".equals(dbtype)){
            viProperties = guassViDsProperties;
        }

        if("mysql".equals(dbtype)){
            viProperties = mysqlViDsProperties;
        }

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(viProperties.getUrl());
        datasource.setUsername(viProperties.getUsername());
        datasource.setPassword(viProperties.getPassword());
        datasource.setDriverClassName(viProperties.getDriverClassName());
        datasource.setInitialSize(viProperties.getInitialSize());
        datasource.setMinIdle(viProperties.getMinIdle());
//        datasource.setMaxActive(properties.getMaxActive());
//        datasource.setMaxWait(properties.getMaxWait());
//        datasource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
//        datasource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
//        datasource.setValidationQuery(properties.getValidationQuery());
//        datasource.setTestWhileIdle(properties.getTestWhileIdle());
//        datasource.setTestOnBorrow(properties.getTestOnBorrow());
//        datasource.setTestOnReturn(properties.getTestOnReturn());
//        datasource.setPoolPreparedStatements(properties.getPoolPreparedStatements());
//        datasource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
//        try {
//            datasource.setFilters(properties.getFilters());
//        } catch (SQLException e) {
//        }
        return datasource;
    }

    public DataSource videoinsightcollecter(){

        if("gauss".equals(dbtype)){
            vicProperties = guassVicDsProperties;
        }

        if("mysql".equals(dbtype)){
            vicProperties = mysqlVicDsProperties;
        }

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(vicProperties.getUrl());
        datasource.setUsername(vicProperties.getUsername());
        datasource.setPassword(vicProperties.getPassword());
        datasource.setDriverClassName(vicProperties.getDriverClassName());
        datasource.setInitialSize(vicProperties.getInitialSize());
        datasource.setMinIdle(vicProperties.getMinIdle());
//        datasource.setMaxActive(properties.getMaxActive());
//        datasource.setMaxWait(properties.getMaxWait());
//        datasource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
//        datasource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
//        datasource.setValidationQuery(properties.getValidationQuery());
//        datasource.setTestWhileIdle(properties.getTestWhileIdle());
//        datasource.setTestOnBorrow(properties.getTestOnBorrow());
//        datasource.setTestOnReturn(properties.getTestOnReturn());
//        datasource.setPoolPreparedStatements(properties.getPoolPreparedStatements());
//        datasource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
//        datasource.setMaxPoolPreparedStatementPerConnectionSize(properties.getMaxPoolPreparedStatementPerConnectionSize());
//        try {
//            datasource.setFilters(properties.getFilters());
//        } catch (SQLException e) {
//        }
        return datasource;
    }


    @Primary
    @Bean("dynamicDataSource")
    public DataSource dynamicDataSource(){
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(videoInsight());
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("videoInsight",videoInsight());
        dsMap.put("videoinsightcollecter",videoinsightcollecter());
        dataSource.setTargetDataSources(dsMap);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager videoinsightcollecterTransactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }
}
