import static scala.collection.JavaConverters.asScalaBuffer;

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

public class Main3 {

  public static void main(String[] args) {

    var spark = SparkSession.builder()
        .master("local[*]")
        .appName("Deequ_task")
        .getOrCreate();

    var conf = Arrays.stream(spark.sparkContext().getConf().getAll())
        .collect(Collectors.toList());

    System.out.println(Instant.now() + ": " + conf);

    var pathToFile = "/Downloads/amazon_reviews_us_Camera_v1_00.tsv";
//    var pathToFile = "/Downloads/amazon_reviews_us_Camera_v1_00.tsv.gz";

    var df = spark.read()
        .option("sep", "\t")
//        .option("mode", "FAILFAST")
        .option("multiline", "true")
        .option("inferSchema", "true")
        .option("header", "true")
        .csv(System.getProperty("user.home") + pathToFile);
    df.printSchema();

    System.out.println("fsdfsdfd");

    df.show(5, false);

    var analysisResult = AnalysisRunner
        .onData(df)
        .addAnalyzer((Analyzer) new Size(Option.empty()))
        .addAnalyzer((Analyzer) new Completeness("star_rating", Option.empty()))
        .addAnalyzer((Analyzer) new Completeness("product_title", Option.empty()))
        .addAnalyzer((Analyzer) new ApproxCountDistinct("total_votes", Option.empty()))
        .addAnalyzer((Analyzer) new Mean("star_rating", Option.empty()))
        .addAnalyzer(
            (Analyzer) new Compliance("top star_rating", "star_rating >= 4.0", Option.empty()))
        .addAnalyzer((Analyzer) new Correlation("star_rating", "helpful_votes", Option.empty()))
        .run();

    var metrics = AnalyzerContext.successMetricsAsDataFrame(
        spark,
        analysisResult,
        asScalaBuffer(Collections.emptyList()));
    metrics.show(false);

    spark.stop();
  }
}