//import com.amazon.deequ.analyzers.Analyzer;
//import com.amazon.deequ.analyzers.Completeness;
//import com.amazon.deequ.analyzers.Size;
//import com.amazon.deequ.analyzers.runners.AnalysisRunner;
//import com.amazon.deequ.metrics.Metric;
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.stream.Collectors;
//import org.apache.spark.sql.SparkSession;
//import scala.Option;

import com.amazon.deequ.analyzers.Analyzer;
import com.amazon.deequ.analyzers.ApproxCountDistinct;
import com.amazon.deequ.analyzers.Completeness;
import com.amazon.deequ.analyzers.Compliance;
import com.amazon.deequ.analyzers.Correlation;
import com.amazon.deequ.analyzers.Mean;
import com.amazon.deequ.analyzers.Size;
import com.amazon.deequ.analyzers.runners.AnalysisRunner;
import com.amazon.deequ.analyzers.runners.AnalyzerContext;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.spark.sql.SparkSession;
import scala.Option;
import scala.collection.JavaConverters;

public class Main {

  public static void main(String[] args) {

    var spark = SparkSession.builder()
        .master("local[*]")
        .appName("Deequ_task")
        .getOrCreate();
//    var spark = SparkSession.builder()
//        .master("spark")
//        .appName("Deequ_task")
//        .getOrCreate();

//    var spark = SparkSession.builder().appName("Deequ_task")
//        .config("spark.master", "local").getOrCreate();

    var conf = Arrays.stream(spark.sparkContext().getConf().getAll())
        .collect(Collectors.toList());

    System.out.println(Instant.now() + ": " + conf);

    var df = spark.read()
//        .format("com.databricks.spark.csv")
        .format("org.apache.spark.sql.execution.datasources.csv.CSVFileFormat")
//        .format("org.apache.spark.sql.execution.datasources.v2.csv.CSVDataSourceV2")
//        .format("csv")
        .option("sep", "\t")
        .option("inferSchema", "true")
        .option("header", "true")
        .load(
            "file:///C:/Users/Vitalii_Balitckii/Downloads/amazon_reviews_us_Camera_v1_00.tsv/amazon_reviews_us_Camera_v1_00.tsv");

    df.printSchema();

    System.out.println("fsdfsdfd");

    df.show(5);

    var size = (Analyzer) new Size(Option.empty());

    var analysisResult = AnalysisRunner
        .onData(df)
        .addAnalyzer(size)
        .addAnalyzer((Analyzer) new Completeness("star_rating", Option.empty()))
        .addAnalyzer((Analyzer) new Completeness("product_title", Option.empty()))
//        .addAnalyzer((Analyzer) new ApproxCountDistinct("star_rating",  Option.empty()))
        .addAnalyzer((Analyzer) new Mean("star_rating", Option.empty()))
        .addAnalyzer(
            (Analyzer) new Compliance("top star_rating", "star_rating >= 4.0", Option.empty()))
//        .addAnalyzer((Analyzer) new Correlation("star_rating", "helpful_votes", Option.empty()))
        .run();

    var metrics = AnalyzerContext.successMetricsAsDataFrame(
        spark,
        analysisResult,
        JavaConverters.asScalaBuffer(Collections.emptyList()));

    metrics.show();

    System.out.println("hegdofd");
    spark.stop();
  }
}