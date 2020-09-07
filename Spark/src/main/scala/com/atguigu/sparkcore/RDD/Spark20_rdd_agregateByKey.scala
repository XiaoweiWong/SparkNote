package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark20_rdd_agregateByKey {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd1 = context.makeRDD(List(
        ("nba",1),("cba",2),("wnba",1),("cba",3),("nba",5),("cba",2)))
    val value: RDD[(String, (Int, Int))] = rdd1.combineByKey(
      v => (v, 1),
      (t: (Int, Int), v: Int) => {
        (t._1 + v, t._2 + 1)
      },
      (t1: (Int, Int), t2: (Int, Int)) => {
        (t1._1 + t2._1, t1._2 + t2._2)
      }
    )
    value.map{
      case (key,(total,count))=>(key,total/count)
    }.collect.foreach(println)

      context.stop()
  }
}
