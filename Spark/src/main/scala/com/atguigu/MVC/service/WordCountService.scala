package com.atguigu.MVC.service

import com.atguigu.MVC.DAO.WordCountDAO
import com.atguigu.summer.framework.core.{TDao, TService}
import org.apache.spark.rdd.RDD

/**
 * @author david 
 * @create 2020-09-01 下午 3:44 
 */
class WordCountService extends TService{
   var wordCountDao: TDao = new WordCountDAO


  override def analysis()={
    val fileRDD: RDD[String] = wordCountDao.readFile("input1/2.txt")
    val wordRDD: RDD[String] = fileRDD.flatMap(_.split(" "))
    val mapRDD: RDD[(String, Int)] = wordRDD.map(word=>(word,1))
    val wordCountRDD: RDD[(String, Int)] = mapRDD.reduceByKey(_+_)
    val wordCountArray: Array[(String, Int)] = wordCountRDD.collect
    wordCountArray
  }
}
