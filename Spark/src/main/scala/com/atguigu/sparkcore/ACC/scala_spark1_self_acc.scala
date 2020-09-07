package com.atguigu.sparkcore.ACC

import org.apache.spark.rdd.RDD
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * @author david 
 * @create 2020-08-31 下午 8:52 
 */
object scala_spark1_self_acc {
  def main(args: Array[String]): Unit = {
    val acc: SparkConf = new SparkConf().setMaster("local[*]").setAppName("acc")
    val sc: SparkContext = new SparkContext(acc)
     val rdd: RDD[String] = sc.makeRDD(List("hello scala","hadoop spark","hello world"))
    //声明累加器变量
    val accumulator: MyAccumulator = new MyAccumulator
    sc.register(accumulator)

    rdd.flatMap(_.split(" ")).foreach(
      word => accumulator.add(word)
    )

    println(accumulator.value)
    sc.stop()
  }
}
class MyAccumulator extends AccumulatorV2[String,mutable.Map[String,Int]]{
  //
   private var wordcountMap: mutable.Map[String, Int] = mutable.Map[String,Int]()

  /**
   * 判断累加器是否为初始化的
   * @return
   */
  override def isZero: Boolean = {
    wordcountMap.isEmpty
  }

  /**
   * 复制累加器
   * @return
   */
  override def copy(): AccumulatorV2[String, mutable.Map[String, Int]] = {
    new MyAccumulator
  }

  /**
   * 重置累加器
   */
  override def reset(): Unit = {
    wordcountMap.clear()
  }

  /**
   * 像累加器添加值
   * @param word
   */
  override def add(word: String): Unit = {
   wordcountMap(word) = wordcountMap.getOrElse(word,0)+1
  }

  /**
   * 合并当前累加器和其他累加器
   * 合并累加器
   * @param other
   */
  override def merge(other: AccumulatorV2[String, mutable.Map[String, Int]]): Unit = {
    val map1: mutable.Map[String, Int] = other.value
    val map2:mutable.Map[String,Int] = wordcountMap
      wordcountMap = map1.foldLeft(map2){
        (map,kv)=>{
          map(kv._1) = map.getOrElse(kv._1,0)+kv._2
          map
        }
      }

  }

  /**
   * 返回累加器的值
   * @return
   */
  override def value: mutable.Map[String, Int] = {
    wordcountMap
  }
}