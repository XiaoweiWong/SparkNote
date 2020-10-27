package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL01_Load_CSV {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    //TODO 执行逻辑
    //加载列式存储的格式文件
    //val frame: DataFrame = spark.read.load("input/users.parquet")
    val frame: DataFrame = spark.read.format("csv")
        .option("sep",";")
        .option("inferSchema","true")
        .option("header","true")//把第一行作为字段名
        .load("input/user.csv")

    frame.show
    spark.stop()
  }
}