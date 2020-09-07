package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark14_rdd_coalesce {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
      val rdd1: RDD[Int] = context.makeRDD(List(1,1,1,4,5,6,6,6),6)
  // val value: RDD[Int] = rdd1.filter(num => num%2==0)
    val value1: RDD[Int] = rdd1.coalesce(2)
    val value: RDD[Int] = rdd1.repartition(8)
    //println(value.collect.mkString(","))
   // println(value1.collect.mkString(","))
    value1.saveAsTextFile("output_coalesce2")
    value.saveAsTextFile("output_coalesce3")

      
      context.stop()
  }
}
