package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL_LoadSave {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    import spark.implicits._
    //TODO 执行逻辑
    //加载列式存储的格式文件
    //val frame: DataFrame = spark.read.load("input/users.parquet")
    val frame: DataFrame = spark.read.format("json").load("input/user.json")
    frame.show

    spark.stop()
  }
  case class User(
                 id:Int,
                 name:String,
                 age:Long
                 )

  case class AvgBuffer(
                      var totalage:Long,
                      var count:Long
                      )




//自定义聚合函数 强类型，平均年龄
class MyAvgAgeUDAFClass extends Aggregator[User,AvgBuffer,Long] {
  //缓冲区的初始值
  override def zero :AvgBuffer={
    AvgBuffer(0L,0L)
  }
  // TODO 聚合数据
  override def reduce(buffer: AvgBuffer, user: User): AvgBuffer = {
    buffer.totalage = buffer.totalage + user.age
    buffer.count = buffer.count + 1
    buffer
  }

  // TODO 合并缓冲区
  override def merge(buffer1: AvgBuffer, buffer2: AvgBuffer): AvgBuffer = {
    buffer1.totalage = buffer1.totalage + buffer2.totalage
    buffer1.count = buffer1.count + buffer2.count
    buffer1
  }

  // TODO 计算函数的结果
  override def finish(reduction: AvgBuffer): Long = {
    reduction.totalage / reduction.count
  }

  override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

  override def outputEncoder: Encoder[Long] = Encoders.scalaLong


 }
}