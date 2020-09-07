package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark23_rdd_cogroup {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd1 = context.makeRDD(List(
        ("nba",1),("cba",2),("wnba",1)))
    val rdd2 = context.makeRDD(List(
      ("nba",1),("cba",2),("wnba",1),("nba",3)))
    //val value: RDD[(String, (Int, Int))] = rdd1.join(rdd2)

   val value1: RDD[(String, (Iterable[Int], Iterable[Int]))] = rdd1.cogroup(rdd2)
    println(value1.collect.mkString(","))
      context.stop()
  }
}
