package com.huawei.vi.thirddata.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.huawei.vi.thirddata.properties.GuassViDsProperties;
import com.huawei.vi.thirddata.properties.GuassVicDsProperties;
import com.huawei.vi.thirddata.properties.MysqlViDsProperties;
import com.huawei.vi.thirddata.properties.MysqlVicDsProperties;
import com.jovision.jaws.common.datasource.DynamicDataSource;
import com.jovision.jaws.common.properties.DSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
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
        datasource.setMaxActive(viProperties.getMaxActive());
        datasource.setMaxWait(viProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(viProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(viProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(viProperties.getValidationQuery());
        datasource.setTestWhileIdle(viProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(viProperties.getTestOnBorrow());
        datasource.setTestOnReturn(viProperties.getTestOnReturn());
        datasource.setPoolPreparedStatements(viProperties.getPoolPreparedStatements());
        datasource.setMaxOpenPreparedStatements(viProperties.getMaxOpenPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(viProperties.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setBreakAfterAcquireFailure(viProperties.getBreakAfterAcquireFailure());
        datasource.setConnectionErrorRetryAttempts(viProperties.getConnectionErrorRetryAttempts());
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
        datasource.setMaxActive(vicProperties.getMaxActive());
        datasource.setMaxWait(vicProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(vicProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(vicProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(vicProperties.getValidationQuery());
        datasource.setTestWhileIdle(vicProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(vicProperties.getTestOnBorrow());
        datasource.setTestOnReturn(vicProperties.getTestOnReturn());
        datasource.setPoolPreparedStatements(vicProperties.getPoolPreparedStatements());
        datasource.setMaxOpenPreparedStatements(vicProperties.getMaxOpenPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(vicProperties.getMaxPoolPreparedStatementPerConnectionSize());
        datasource.setBreakAfterAcquireFailure(vicProperties.getBreakAfterAcquireFailure());
        datasource.setConnectionErrorRetryAttempts(vicProperties.getConnectionErrorRetryAttempts());
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
