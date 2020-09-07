package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark13_rdd_distinct {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
      val rdd1: RDD[Int] = context.makeRDD(List(1,2,1,4,5,7,7,9),2)
     val value: RDD[Int] = rdd1.distinct()
    val value1: RDD[Int] = rdd1.distinct(2)
    println(value.collect.mkString(","))
    println(value1.collect.mkString(","))



      
      context.stop()
  }
}
