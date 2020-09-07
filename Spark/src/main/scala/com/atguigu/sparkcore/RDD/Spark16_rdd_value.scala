package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark16_rdd_value {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
      val rdd1: RDD[Int] = context.makeRDD(List(1,3,4,6,7),2)
      val rdd2: RDD[Int] = context.makeRDD(List(1,2,4,5,2,6),2)
    //并集
      val value1: RDD[Int] = rdd1.union(rdd2)
    //println(value1.collect.mkString(","))
    //value1.saveAsTextFile("output1")
    //交集
      val value2: RDD[Int] = rdd1.intersection(rdd2)
    //println(value2.collect.mkString(","))
    //value2.saveAsTextFile("output2")
    //差集
      val value3: RDD[Int] = rdd1.subtract(rdd2)
    value3.saveAsTextFile("output3")
    //println(value3.collect.mkString(","))
    //拉链
    val value4: RDD[(Int, Int)] = rdd1.zip(rdd2)
    value4.saveAsTextFile("output4")
    //println(value4.collect.mkString(","))
      context.stop()
  }
}
