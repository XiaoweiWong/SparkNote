package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark16_rdd_kv {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
      val rdd1= context.makeRDD(
        List(("a",1),("b",2)))
      val rdd2: RDD[(String, Int)] = rdd1.partitionBy(new HashPartitioner(2))
      //rdd2.saveAsTextFile("output5")
    // rdd2.sortBy()


      context.stop()
  }
}
