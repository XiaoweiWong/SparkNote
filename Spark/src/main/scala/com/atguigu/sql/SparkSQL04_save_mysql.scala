package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL04_save_mysql {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    //TODO 执行逻辑
    //加载列式存储的格式文件
    //val frame: DataFrame = spark.read.load("input/users.parquet")

    val frame: DataFrame = spark.read.format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/spark-sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "root")
      .option("dbtable", "user")
      .load()
    frame.write.format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/spark-sql")
      .option("driver", "com.mysql.jdbc.Driver")
      .option("user", "root")
      .option("password", "root")
      .option("dbtable", "user1")
      .mode(SaveMode.Append)
      .save()
    spark.stop()
  }
}