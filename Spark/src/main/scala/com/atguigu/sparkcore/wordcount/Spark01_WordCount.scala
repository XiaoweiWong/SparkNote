package com.atguigu.sparkcore.wordcount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david 
 * @create 2020-08-26 上午 11:00
 */
object Spark01_WordCount {
  def main(args: Array[String]): Unit = {
    //1.准备Spark环境
      val sparkconf = new SparkConf().setMaster("local[*]").setAppName("wordcount")
    //2.建立和Spark的连接
    val context = new SparkContext(sparkconf)
    //3.读取文件数据内容
    val fileRDD: RDD[String] = context.textFile("input/word.txt")
    //对文件的读取的数据进行切分分词
    val word: RDD[String] = fileRDD.flatMap( _.split(" "))
    //转换数据结构
    //word.map(word =>(word,1))
    val wordInt: RDD[(String, Int)] = word.map((_,1))
    //分组聚合
    val wordCount: RDD[(String, Int)] = wordInt.reduceByKey(_+_)
    //将聚合结果采集到内存
    val wordcount: Array[(String, Int)] = wordCount.collect()
    //打印结果
    wordcount.foreach(println)
    //4.释放连接
    context.stop()
  }
}
