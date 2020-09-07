package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark04_rdd_operater {
  def main(args: Array[String]): Unit = {
    val wordcount: SparkConf = new SparkConf().setMaster("local").setAppName("file_rdd")
    val sc: SparkContext = new SparkContext(wordcount)
    val rdd1 = sc.makeRDD(List(1, 2, 3, 4),2)
   val rdd2: RDD[Int] = rdd1.map((i:Int)=>{i *2})
    val rdd3: RDD[Int] = rdd1.map(_*2)
    rdd3.saveAsTextFile("output6")
    //println(array.mkString(" "))
    sc.stop()
  }
}
