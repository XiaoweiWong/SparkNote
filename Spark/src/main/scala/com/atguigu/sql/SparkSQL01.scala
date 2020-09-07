package com.atguigu.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL01 {

  def main(args: Array[String]): Unit = {
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark")
    val spark: SparkSession = SparkSession.builder().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
    import spark.implicits._
    //TODO 执行逻辑


    val df: DataFrame = spark.read.json("input/user.json")
    df.createTempView("user")
    spark.sql("select * from user").show()

    df.select("name","age").show()
    df.select('name,'age).show()
        //TODO RDD <=> DF
    val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(List(
      (1, "zhansan", 30),
      (2, "lisi", 35),
      (3, "wangwu", 10),
      (4, "liqi", 20)
    ))
    val df1=rdd.toDF("id", "name", "age")
    df1.show()
    val dfToRDD: RDD[Row] = df1.rdd
    //TODO RDD < = >  DS
    val mapRDD: RDD[User] = rdd.map(
      t => User(t._1, t._2, t._3)
    )
    val ds1: Dataset[User] = mapRDD.toDS()
    ds1.show()
    val dsToRDD: RDD[User] = ds1.rdd

    //TODO datafram <=> dataset
    val userDS: Dataset[User] = df1.as[User]
    val userdf: DataFrame = userDS.toDF()

    //TODO 关闭连接释放对象
    spark.stop()
  }


}
case class User(
  id:Int,
  name:String,
  age:Int)

