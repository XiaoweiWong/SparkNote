package com.atguigu.sparkcore.RDD_Action

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 2:20 
 */
object scala_spark1_action_take{
  def main(args: Array[String]): Unit = {
    val action: SparkConf = new SparkConf().setMaster("local[*]").setAppName("action")
    val sc: SparkContext = new SparkContext(action)
    val rdd: RDD[Int] = sc.makeRDD(List(1,2,4,3),2)
    //val i: Int = rdd.reduce(_+_)
    // println(rdd.count())
    //println(i)
    rdd.takeOrdered(3).foreach(println)


    sc.stop()
  }
}
