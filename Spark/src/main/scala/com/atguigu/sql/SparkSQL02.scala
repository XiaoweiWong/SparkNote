package com.atguigu.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, IntegerType, LongType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL02 {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark")
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    import spark.implicits._
    //TODO 执行逻辑


    //TODO RDD
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List(
      (1, "zhansan", 30),
      (2, "lisi", 35),
      (3, "wangwu", 10),
      (4, "liqi", 20)
    ))

 //   rdd => df => ds 此路不通
//    val df: DataFrame = rdd.toDF("id","name","age")
//    val ds: Dataset[Any] = df.map(row => {
//      val id = row(0)
//      val name = row(1)
//      val age = row(2)
//      row(id, "name:" + name, age)
//    })
//    ds.show()



//    //TODO RDD < = >  DS
//    val mapRDD: RDD[User] = rdd.map(
//      t => User(t._1, t._2, t._3)
//    )
//    val ds1: Dataset[User] = mapRDD.toDS()
//    val newDS: Dataset[User] = ds1.map(map => {
//      User(map.id, "name:" + map.name, map.age)
//    })
//    newDS.show()
//
    val df: DataFrame = rdd.toDF("id","name","age")
    df.createOrReplaceTempView("user")
//    spark.udf.register("addName",(x:String)=>"name:"+x)
//    spark.udf.register("changeAge",(x:Int)=>18)
//    spark.sql("select addName(name),changeAge(age) from user").show()
      val udaf: MyAvgUDAF = new MyAvgUDAF
      spark.udf.register("avgAge",udaf)
      spark.sql("select avgAge(age) from user").show


     spark.stop()
  }


}




//自定义聚合函数
class MyAvgUDAF extends UserDefinedAggregateFunction {
  /**
   * 输入数据结构信息,年龄的信息
   * @return
   */
  override def inputSchema: StructType = {
    StructType(Array(StructField("age",IntegerType)))
  }

  /**
   * 缓冲区的数据结构信息年龄的总和，人的数量
   * @return
   */
  override def bufferSchema: StructType = {
    StructType(Array(StructField("totalage",IntegerType)))
    StructType(Array(StructField("count",IntegerType)))
  }

  /**
   * 聚合函数返回值的类型
   * @return
   */
  override def dataType: DataType = LongType

  /**
   * 函数的稳定性
   * @return
   */
  override def deterministic: Boolean = true

  /**
   * 函数的缓冲区初始化
   * @param buffer
   */
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0)=0L
    buffer(1)=0L
  }

  /**
   * 更新缓冲区数据
   * @param buffer
   * @param input
   */
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0)=buffer.getLong(0)+input.getLong(0)
    buffer(1)=buffer.getLong(1)+1
  }

  /**
   * 合并缓冲区
   * @param buffer1
   * @param buffer2
   */
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0)=buffer1.getLong(0)+buffer2.getLong(0)
    buffer1(1)=buffer1.getLong(1)+buffer2.getLong(1)
  }

  /**
   * 函数的计算
   * @param buffer
   * @return
   */
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0)/buffer.getLong(1)
  }
}