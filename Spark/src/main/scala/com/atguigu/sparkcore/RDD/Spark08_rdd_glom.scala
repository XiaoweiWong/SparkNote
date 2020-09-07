package com.atguigu.sparkcore.RDD

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark08_rdd_glom {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List(1,2,4,6,5),2)
      val rdd2 = rdd.glom()
    rdd2.foreach(
      array =>{
        println(array.mkString(","))
      }
    )
     //  println(rdd2.collect.mkString(","))
       context.stop()
  }
}
