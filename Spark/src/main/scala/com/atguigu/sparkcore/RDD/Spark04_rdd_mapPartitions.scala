package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark04_rdd_mapPartitions {
  def main(args: Array[String]): Unit = {
    val wordcount: SparkConf = new SparkConf().setMaster("local").setAppName("file_rdd")
    val sc: SparkContext = new SparkContext(wordcount)
    val rdd1 = sc.makeRDD(List(1, 9, 11, 4,23),2)
    val value: RDD[Int] = rdd1.mapPartitions(
      iter => {
        List(iter.max).iterator
      }
    )
    println(value.collect.mkString(","))
    //println(array.mkString(" "))
    sc.stop()
  }
}
