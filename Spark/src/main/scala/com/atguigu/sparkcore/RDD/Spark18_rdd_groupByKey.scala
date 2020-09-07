package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark18_rdd_groupByKey {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd1 = context.makeRDD(List(
        ("nba",1),("cba",2),("wnba",1),("cba",3),("nba",5),("cba",2)))
    val value: RDD[(String, Iterable[Int])] = rdd1.groupByKey()
    val value1: RDD[(String, Int)] = value.map {
      case (key, iter) => {
        (key, iter.size)
      }
    }
    println(value1.collect.mkString(","))
      context.stop()
  }
}
