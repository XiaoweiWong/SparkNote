package com.atguigu.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream01_WordCount {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑

    val socketDS: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102",9999)
    val wordDS: DStream[String] = socketDS.flatMap(_.split(" "))
    val wordToOneDS: DStream[(String, Int)] = wordDS.map((_,1))
    val wordToSum: DStream[(String, Int)] = wordToOneDS.reduceByKey(_+_)
    wordToSum.print()
    //关闭

    ssc.start()
    ssc.awaitTermination()
  }

}
