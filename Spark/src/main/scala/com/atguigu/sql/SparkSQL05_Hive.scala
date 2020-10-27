package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL05_Hive {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    //启动hive支持
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    //spark.sql("create table test(id int)")
   //spark.sql("show tables").show()
    spark.sql("load data local inpath 'input/id.txt' into table test")
    spark.sql("select * from test").show
    spark.stop()
  }
}