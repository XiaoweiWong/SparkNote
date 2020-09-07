package com.atguigu.sparkcore.ACC

import org.apache.spark.rdd.RDD
import org.apache.spark.util.LongAccumulator
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-31 下午 8:52 
 */
object scala_spark1_acc {
  def main(args: Array[String]): Unit = {
    val acc: SparkConf = new SparkConf().setMaster("local[*]").setAppName("acc")
    val sc: SparkContext = new SparkContext(acc)
    val rdd: RDD[Int] = sc.makeRDD(List(1,2,3,4))
    //声明累加器变量
    val sum: LongAccumulator = sc.longAccumulator("sum")
    rdd.foreach(
      //使用累加器
      num => sum.add(num)
    )
    //获取累加器结果
    println(sum.value)
    sc.stop()
  }
}
