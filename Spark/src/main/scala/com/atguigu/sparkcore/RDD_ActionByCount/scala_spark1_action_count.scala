package com.atguigu.sparkcore.RDD_ActionByCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 2:20 
 */
object scala_spark1_action_count{
  def main(args: Array[String]): Unit = {
    val action: SparkConf = new SparkConf().setMaster("local[*]").setAppName("action")
    val sc: SparkContext = new SparkContext(action)
    val rdd: RDD[(Int, String)] = sc.makeRDD(List((1, "a"), (1, "a"), (1, "a"), (2, "b"), (3, "c"), (3, "c")))
    val wordToCount: collection.Map[Int, Long] = rdd.countByKey()
    val value: RDD[String] = sc.makeRDD(List("a", "a",  "a", "b", "c" , "c"))
    val wc: collection.Map[String, Long] = value.countByValue()
    println(wc)
    println(wordToCount)
    sc.stop()
  }
}
