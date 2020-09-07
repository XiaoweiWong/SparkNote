package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark09_rdd_groupby {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List(1,2,4,6,5,9),6)
    val rdd2: RDD[(Int, Iterable[Int])] = rdd.groupBy(
      num => {
        num % 2
      },2
    )
    println(rdd2.glom.collect.length)
    rdd2.collect.foreach {
      case (key, list) => {
        println("key:" + key + " list:" + list.mkString(" "))
      }
    }

      context.stop()
  }
}
