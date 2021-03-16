package com.epam.pop;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordData {

    @Autowired
    private SQLContext sqlContext;


    @Autowired
    private JavaSparkContext sc;

    public Dataset<Row> create(String pathToDir) {
        JavaRDD<Row> rdd = sc.textFile(pathToDir)
                .flatMap(line -> Utils.getWords(line).iterator())
                .map(RowFactory::create);

        return sqlContext.createDataFrame(rdd, DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("words", DataTypes.StringType, true)
        }));
    }
}
