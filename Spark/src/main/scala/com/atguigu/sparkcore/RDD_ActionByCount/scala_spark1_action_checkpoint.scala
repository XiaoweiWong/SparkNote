package com.atguigu.sparkcore.RDD_ActionByCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 2:20 
 */
object scala_spark1_action_checkpoint{
  def main(args: Array[String]): Unit = {
    val action: SparkConf = new SparkConf().setMaster("local[*]").setAppName("action")
    val sc: SparkContext = new SparkContext(action)
    sc.setCheckpointDir("outpoint")
    val rdd = sc.makeRDD(List(1,2,3,4))
    val mapRDD: RDD[(Int, Int)] = rdd.map(
      num => {
        println("map........")
        (num, 1)
      }
    )


    mapRDD.checkpoint()
    println(mapRDD.toDebugString)
    mapRDD.collect.foreach(println)
    println(mapRDD.toDebugString)
    sc.stop()
  }
}
