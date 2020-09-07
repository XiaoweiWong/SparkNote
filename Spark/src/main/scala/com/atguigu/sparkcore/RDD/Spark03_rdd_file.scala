package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark03_rdd_file {
  def main(args: Array[String]): Unit = {
    val wordcount: SparkConf = new SparkConf().setMaster("local").setAppName("file_rdd")
    val sc: SparkContext = new SparkContext(wordcount)
    val value: RDD[String] = sc.textFile("input1",3)
    value.saveAsTextFile("output5")
    //println(value.collect().mkString(","))
    sc.stop()
  }
}
