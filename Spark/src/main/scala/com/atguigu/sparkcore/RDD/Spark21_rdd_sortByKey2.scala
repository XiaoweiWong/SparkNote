package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark21_rdd_sortByKey2 {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val value: RDD[(User, Int)] = context.makeRDD(List(
      (new User(), 1),
      (new User(), 2),
      (new User(), 3),
      (new User(), 4)
    ))
    val value1: RDD[(User, Int)] = value.sortByKey(true)
    value1.collect.foreach(println)
      context.stop()
  }
}
class User1 extends Ordered[User1] with Serializable {
  override def compare(that: User1): Int = {
    1
  }
}