package com.atguigu.sql

import org.apache.spark.SparkConf
import org.apache.spark.sql._

/**
 * @author david
 * @create 2020-09-04 下午 7:48
 */
object SparkSQL07_Req_Mock {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "atguigu")
    //TODO 创建环境对象
    val sparkconf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSQL")
    //启动hive支持
    val spark: SparkSession = SparkSession.builder().enableHiveSupport().config(sparkconf).getOrCreate()
    //导入隐式转换，导入的spark是环境变量的名称
    //要求这个变量必须使用val声明
  import spark.implicits._
    spark.sql(" use atguigu")
//    spark.sql(
//      """
//        |CREATE TABLE `user_visit_action`(
//        |  `date` string,
//        |  `user_id` bigint,
//        |  `session_id` string,
//        |  `page_id` bigint,
//        |  `action_time` string,
//        |  `search_keyword` string,
//        |  `click_category_id` bigint,
//        |  `click_product_id` bigint,
//        |  `order_category_ids` string,
//        |  `order_product_ids` string,
//        |  `pay_category_ids` string,
//        |  `pay_product_ids` string,
//        |  `city_id` bigint)
//        |row format delimited fields terminated by '\t'
//        |
//        |
//        |""".stripMargin)
//    spark.sql(
//      """
//        |load data local inpath 'input/user_visit_action.txt' into table atguigu.user_visit_action
//        |""".stripMargin)
//
//    spark.sql(
//      """
//        |CREATE TABLE `product_info`(
//        |  `product_id` bigint,
//        |  `product_name` string,
//        |  `extend_info` string)
//        |row format delimited fields terminated by '\t'
//        |
//        |""".stripMargin
//    )
//    spark.sql(
//      """
//        |load data local inpath 'input/product_info.txt' into table product_info
//        |""".stripMargin)
//
//
//
//    spark.sql(
//      """
//        |CREATE TABLE `city_info`(
//        |  `city_id` bigint,
//        |  `city_name` string,
//        |  `area` string)
//        |row format delimited fields terminated by '\t'
//        |
//        |""".stripMargin)
//
//    spark.sql(
//      """
//        |load data local inpath 'input/city_info.txt' into table city_info
//        |""".stripMargin)

    spark.sql(
      """
        |select * from  city_info
        |""".stripMargin).show(10)
    spark.stop()
  }
}