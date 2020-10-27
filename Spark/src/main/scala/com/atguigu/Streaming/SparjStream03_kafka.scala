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
object SparkStream03_kafka {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑
    //3.定义Kafka参数
    val kafkaPara: Map[String, Object] = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop102:9092,hadoop103:9092,hadoop104:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "atguigu",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",//key反序列化
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer"//value反序列化
    )
    //4.读取Kafka数据创建DStream
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,//位置策略
      ConsumerStrategies.Subscribe[String, String](Set("atguigu111"), kafkaPara))  //主题和配置对象

     val value: DStream[String] = kafkaDStream.map(record => record.value())

    value.flatMap(_.split(" "))
        .map((_,1))
        .reduceByKey(_+_)
        .print()



    ssc.start()
    ssc.awaitTermination()
  }




}
