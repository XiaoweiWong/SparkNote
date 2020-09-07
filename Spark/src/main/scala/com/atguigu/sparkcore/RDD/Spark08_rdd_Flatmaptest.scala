package com.atguigu.sparkcore.RDD

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark08_rdd_Flatmaptest {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
    val rdd = context.makeRDD(List(List(1,2),3,List(4,5)),1)
      val rdd2 = rdd.flatMap(
        data => data match {
          case list:List[_]=>list
          case d=> List(d)
        }
        )
       println(rdd2.collect.mkString(","))
       context.stop()
  }
}
