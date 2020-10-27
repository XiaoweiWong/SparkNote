package com.atguigu.Streaming

import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}


/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream02_DIY {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑

    val lineStream: ReceiverInputDStream[String] = ssc.receiverStream(new MyReceiver("hadoop102",9999))
    val wordStream: DStream[String] = lineStream.flatMap(_.split("\t"))

    val wordAndOneStream: DStream[(String, Int)] = wordStream.map((_,1))
    wordAndOneStream.reduceByKey(_+_).print()
    ssc.start()
    ssc.awaitTermination()
  }

  /**
   * 自定义数据源
   * @param host
   * @param port
   */
  //父类是有参构造，子类继承必须传值
  class MyReceiver(host:String,port:Int) extends Receiver[String](StorageLevel.MEMORY_ONLY){
   // private var socket:Socket =_
  /**
   * 读取数据并发送到spark
   */
  def receiver():Unit ={
      val socket1: Socket = new Socket(host,port)

    //定义一个变量，用来接收端口传来的数据
      var input:String = null
    //创建一个bufferedReader流读取端口传的数据
      val reader: BufferedReader = new BufferedReader(new InputStreamReader(socket1.getInputStream,"UTF-8"))
      //
      input = reader.readLine()
    //当receiver没有关闭并且输入数据不为空，则循环发送数据给Spark
    while(!isStopped() && input != null){
      store(input)
      input = reader.readLine()
    }
    //跳出循环则关闭资源
    reader.close()
    socket1.close()

    restart("restart")

    }

  /**
   * 最初启动的时候，调用该方法，作用为：读数据并将数据发送给Spark
   */
    override def onStart(): Unit = {
      new Thread("Socket Receiver"){
        override def run(): Unit ={
          receiver()
        }
      }.start()
    }

    override def onStop(): Unit = {}
}

}
