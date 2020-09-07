package com.atguigu.sparkcore.RDD_ActionByCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 2:20 
 */
object scala_spark1_action_foreach{
  def main(args: Array[String]): Unit = {
    val action: SparkConf = new SparkConf().setMaster("local[*]").setAppName("action")
    val sc: SparkContext = new SparkContext(action)

    val value: RDD[String] = sc.makeRDD(List("a", "a",  "a", "b", "c" , "c"))
    value.foreach(println)
    //println(wc)
    //println(wordToCount)
    sc.stop()
  }
}
