package com.atguigu.sparkcore.ACC

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 8:52 
 */
object scala_spark2_acc1{
  def main(args: Array[String]): Unit = {
    val acc: SparkConf = new SparkConf().setMaster("local[*]").setAppName("acc")
    val sc: SparkContext = new SparkContext(acc)
    val rdd= sc.makeRDD(List(("a",2),("b",1),("c",1),("d",2)))
    val list: List[(String, Int)] = List(("a",2),("b",1),("c",1),("d",2))
    //声明广播变量
    val list1: Broadcast[List[(String, Int)]] = sc.broadcast(list)
    val value: RDD[(String, (Int, Int))] = rdd.map {
      case (word, count1) => {
        var count2 = 0
        //使用广播变量
        for (kv <- list1.value) {
          var v = kv._2
          var w = kv._1
          if (w == word) {
            count2 = v
          }
        }
        (word, (count1, count2))
      }
    }
    value.collect.foreach(println)
    sc.stop()
  }
}
