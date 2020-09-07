package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark01_rdd_memory_par {
  def main(args: Array[String]): Unit = {
    val wordcount: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordcount")
    val context: SparkContext = new SparkContext(wordcount)
    val list1: List[Int] = List(1,2,3,4,5)
    val rdd: RDD[Int] = context.parallelize(list1)
    rdd.collect().foreach(println)

    val rdd1: RDD[Int] = context.makeRDD(list1,2)
    rdd1.saveAsTextFile("output1")
    //println(rdd1.collect().mkString(","))
    val rdd2: RDD[Int] = context.makeRDD(list1,3)
    rdd2.saveAsTextFile("output2")

    val rdd3: RDD[Int] = context.makeRDD(list1,4)
    rdd3.saveAsTextFile("output3")
    context.stop()
  }
}
