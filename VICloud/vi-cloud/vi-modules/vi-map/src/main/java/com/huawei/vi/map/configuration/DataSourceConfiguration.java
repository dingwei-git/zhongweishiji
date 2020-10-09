package com.huawei.vi.map.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.huawei.vi.map.properties.ViDsProperties;
import com.huawei.vi.map.properties.VicDsProperties;
import com.jovision.jaws.common.datasource.DynamicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Bean("videoInsight")
    public DataSource videoInsight(ViDsProperties properties){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(properties.getUrl());
        datasource.setUsername(properties.getUsername());
        datasource.setPassword(properties.getPassword());
        datasource.setDriverClassName(properties.getDriverClassName());
        datasource.setInitialSize(properties.getInitialSize());
        datasource.setMinIdle(properties.getMinIdle());
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

    @Bean("videoinsightcollecter")
    public DataSource videoinsightcollecter(VicDsProperties properties){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(properties.getUrl());
        datasource.setUsername(properties.getUsername());
        datasource.setPassword(properties.getPassword());
        datasource.setDriverClassName(properties.getDriverClassName());
        datasource.setInitialSize(properties.getInitialSize());
        datasource.setMinIdle(properties.getMinIdle());
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
    public DataSource dynamicDataSource(@Qualifier("videoInsight")DataSource videoInsight,
                                        @Qualifier("videoinsightcollecter")DataSource videoinsightcollecter){
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(videoInsight);
        Map<Object, Object> dsMap = new HashMap<>();
        dsMap.put("videoInsight",videoInsight);
        dsMap.put("videoinsightcollecter",videoinsightcollecter);
        dataSource.setTargetDataSources(dsMap);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("videoInsight")DataSource videoInsight,
                                                         @Qualifier("videoinsightcollecter")DataSource videoinsightcollecter){
        return new DataSourceTransactionManager(dynamicDataSource(videoInsight,videoinsightcollecter));
    }
}
