package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark10_rdd_fliter {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List(1,2,4,6,5,9),3)
    val value: RDD[Int] = rdd.filter(
      num => num % 2 == 0
    )
    println(value.collect.mkString(","))

      context.stop()
  }
}
