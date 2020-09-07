package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark11_rdd_test1 {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List("Hello", "hive", "hbase", "Hadoop"),3)
    val value: RDD[(Char, Iterable[String])] = rdd.groupBy(key => key.charAt(0))
   // val maxvalue: RDD[Int] = rdd1.map(array => array.max)
   // println(maxvalue.collect().sum)
    value.collect.foreach{
      case (key,list) =>{
        println("key:" + key + ",list:" + list.mkString(","))
      }
    }



      
      context.stop()
  }
}
