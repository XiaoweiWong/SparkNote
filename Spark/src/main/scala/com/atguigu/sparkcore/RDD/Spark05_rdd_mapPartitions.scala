package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark05_rdd_mapPartitions {
  def main(args: Array[String]): Unit = {
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("file_rdd")
    val sc: SparkContext = new SparkContext(sparkconf)
    val rdd1 = sc.makeRDD(List(1, 9, 11, 4,23),2)
    val value1 = rdd1.mapPartitionsWithIndex(
      (index, iter) => {
        List(index, iter.max).iterator
      }
    )
    println(value1.collect.mkString(","))
    //println(array.mkString(" "))
    sc.stop()
  }
}
