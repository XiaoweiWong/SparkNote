package com.atguigu.sparkcore.demo
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-31 下午 12:49
 */
object Demo1 {
  def main(args: Array[String]): Unit = {
    val demotest: SparkConf = new SparkConf().setMaster("local[*]").setAppName("demotest")
    val sc: SparkContext = new SparkContext(demotest)
    //获取原始数据
    val rdd: RDD[String] = sc.textFile("input/agent.log")
    //将原始数据进行转换（（省份——广告），1）
    val value1: RDD[(String, Int)] = rdd.map(
      line => {
        val strings: Array[String] = line.split(" ")
        (strings(1) + "-" + strings(4), 1)
      }
    )

    //将相同key 的数据进行分组转换(省份-广告),sum)
    val value2: RDD[(String, Int)] = value1.reduceByKey(_+_)
    //将聚合后的数据转换为（省份，（广告，sum））
    val value3: RDD[(String, (String, Int))] = value2.map(
      key => {
        val strings: Array[String] = key._1.split("-")
        (strings(0), (strings(1), key._2))

      }
    )

    //按照省份进行分组，（省份，iter[(广告1，sum1),(广告2，sum2)]）
    val value4: RDD[(String, Iterable[(String, Int)])] = value3.groupByKey(3)
    //分组后的数据进行排序（降序），top3
    val sortRDD: RDD[(String, List[(String, Int)])] = value4.mapValues(iter => {
      iter.toList.sortWith(
        (left, right) => {
          left._2 > right._2
        }
      ).take(3)
    }
    )
    //打印结果
    sortRDD.collect.foreach(println)
  }
}
