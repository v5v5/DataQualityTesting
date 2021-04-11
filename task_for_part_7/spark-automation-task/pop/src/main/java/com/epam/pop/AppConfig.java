package com.epam.pop;

import java.util.Properties;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.epam.pop")
@PropertySource({"classpath:user.properties", "classpath:log4j.properties"})
public class AppConfig {

    @Bean
    public JavaSparkContext cxt() {

        SparkConf conf = new SparkConf().setAppName("pop").setMaster("local[4]");
        return new JavaSparkContext(conf);
    }

    @Bean
    public SQLContext sql() {
        return new SQLContext(cxt());
    }
}
