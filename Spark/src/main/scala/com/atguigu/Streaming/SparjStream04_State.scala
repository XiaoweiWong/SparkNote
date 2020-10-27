package com.atguigu.Streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream04_State {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑
    ssc.sparkContext.setCheckpointDir("cp")
    val ds = ssc.socketTextStream("hadoop102",9999)

    ds.flatMap(_.split(" ")).map((_,1L))
    .updateStateByKey[Long](
      (seq:Seq[Long],buffer:Option[Long]) =>{
          val newBufferValue = buffer.getOrElse(0L) + seq.sum
          Option(newBufferValue)
      }
    )
    .print()
    ssc.start()
    ssc.awaitTermination()
  }




}
