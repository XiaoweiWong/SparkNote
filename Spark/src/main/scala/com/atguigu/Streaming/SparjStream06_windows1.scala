package com.atguigu.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream06_windows1 {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑
    ssc.sparkContext.setCheckpointDir("cp")
    val ds = ssc.socketTextStream("hadoop102",9999)
    val wordToOneDS: DStream[(String, Int)] = ds.map(num =>("key",num.toInt))
    val value: DStream[(String, Int)] = wordToOneDS.reduceByKeyAndWindow(
      (x, y) => {
        println(s"x=${x},y=${y}")
        x + y
      },
      (a, b) => {
        println(s"a=${a},b=${b}")
        a - b
      },
      Seconds(9)
    )
    value.foreachRDD(rdd => rdd.foreach(println))
    ssc.start()
    ssc.awaitTermination()
  }

}
