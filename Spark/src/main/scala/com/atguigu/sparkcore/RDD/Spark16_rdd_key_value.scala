package com.atguigu.sparkcore.RDD
import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}

/**
 * @author david
 * @create 2020-08-27 下午 3:08
 */
object Spark16_rdd_key_value {
  def main(args: Array[String]): Unit = {
   val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("spark-RDD")
    val context: SparkContext = new SparkContext(sparkConf)
      val rdd1= context.makeRDD(
        List(
          ("nba",1),("cba",2),("wnba",1),("cba",3),("nba",5),("cba",2)
        ),1)
      val rdd2: RDD[(String, Int)] = rdd1.partitionBy(new MyPartitioner(2))
      //rdd2.saveAsTextFile("output5")
      //rdd2.sortBy()
      //看Kv对所在的分区
    val value: RDD[(Int, (String, Int))] = rdd2.mapPartitionsWithIndex(
      (index, datas) => {
        datas.map(
          datas => (index, datas)
        )
      }
    )
    value.collect.foreach(println)
    context.stop()
  }
}
class MyPartitioner(num:Int) extends Partitioner{
  override def numPartitions: Int = {
    num
  }

  override def getPartition(key: Any): Int = {
    key match {
      case "nba"=>0
      case _ =>1

    }
  }
}