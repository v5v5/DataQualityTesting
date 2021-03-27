package org.example

import org.apache.spark.sql.SparkSession

/**
 * @author ${user.name}
 */
object App {
  
  def main(args : Array[String]) {
    println( "Hello World!" )

    val spark: SparkSession = SparkSession.builder()
      .appName("Deequ test")
      .master("local[*]")
      .getOrCreate()

    val dataset = spark.read.csv("file:///C:\\Users\\Vitalii_Balitckii\\Downloads\\amazon_reviews_us_Camera_v1_00.tsv\\amazon_reviews_us_Camera_v1_00.tsv")


    //    println("concat arguments = " + foo(args))
  }

}
