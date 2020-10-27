package com.atguigu.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream05_windows {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑
    ssc.sparkContext.setCheckpointDir("cp")
    val ds = ssc.socketTextStream("hadoop102",9999)

    val wordToOneDS: DStream[(String, Long)] = ds.flatMap(_.split(" ")).map((_,1L))
    val windowDS: DStream[(String, Long)] = wordToOneDS.window(Seconds(9))
    val result: DStream[(String, Long)] = windowDS.reduceByKey(_+_)
    result.print()
    ssc.start()
    ssc.awaitTermination()
  }




}
