import static com.amazon.deequ.VerificationResult.checkResultsAsDataFrame;
import static scala.collection.JavaConverters.asScalaBuffer;

import com.amazon.deequ.VerificationSuite;
import com.amazon.deequ.checks.Check;
import com.amazon.deequ.checks.CheckLevel;
import com.amazon.deequ.constraints.ConstrainableDataTypes;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import org.apache.spark.sql.SparkSession;
import scala.Option;
import scala.util.matching.Regex;

public class Main1 {

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
//        .option("inferSchema", "true")
        .option("header", "true")
        .csv(System.getProperty("user.home") + pathToFile);
    df.printSchema();

    df.show(5, false);

    var verificationResult = new VerificationSuite()
        .onData(df)
        .addCheck(
            new Check(
                CheckLevel.Error(),
//                "Check that column 'verified_purchase' contains only “N” and “Y” as values",
                "verified_purchase",
                asScalaBuffer(Collections.emptyList())
            )
                .isContainedIn("verified_purchase", new String[]{"Y", "N"})
        )
        .addCheck(
            new Check(
                CheckLevel.Error(),
//                "Check that column 'review_date' contains only dates (use regex)",
                "review_date",
                asScalaBuffer(Collections.emptyList())
            )
                .hasPattern("review_date",
                    new Regex("\\d{4}-\\d{2}-\\d{2}",
                        asScalaBuffer(Collections.emptyList())),
                    Check.IsOne(), Option.empty(), Option.empty())
        )
        .addCheck(
            new Check(
                CheckLevel.Error(),
//                "Check that column 'review_id' contains unique not null values",
                "review_id",
                asScalaBuffer(Collections.emptyList())
            )
                .isUnique("review_id", Option.empty())
                .isComplete("review_id", Option.empty())
        )
        .addCheck(
            new Check(
                CheckLevel.Error(),
//                "Check that column 'total_votes' contains only integers",
                "total_votes",
                asScalaBuffer(Collections.emptyList())
            )
                .hasDataType("total_votes", ConstrainableDataTypes.Numeric(), Check.IsOne(),
                    Option.empty())
        )
        .run();
    var checkResultAsDataframe = checkResultsAsDataFrame(
        spark, verificationResult, asScalaBuffer(Collections.emptyList()));
    checkResultAsDataframe.show(false);

    spark.stop();
  }
}