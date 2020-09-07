package com.atguigu.sparkcore.RDD

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark06_rdd_mapPartitionsWithIndx {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd1: RDD[Int] = context.makeRDD(List(1,24,63,24,32,23),2)
    val rdd2 = rdd1.mapPartitionsWithIndex(
      (index, iter) => {
        //List((index, iter.max)).iterator
        if(index==1){
          iter
        }else{
          Nil.iterator
        }
      }
    )
    println(rdd2.collect.mkString(","))
    context.stop()
  }
}
