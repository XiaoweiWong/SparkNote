package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark07_rdd_Flatmap {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List(List(1,24),List(63,24),List(32,23)),1)
      val rdd2 = rdd.flatMap(list=>list)
       println(rdd2.collect.mkString(","))
       context.stop()
  }
}
