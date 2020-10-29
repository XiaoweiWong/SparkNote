package com.atguigu.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

/**
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream09_continue {
  def main(args: Array[String]): Unit = {
    val ssc = StreamingContext.getActiveOrCreate("cp",getStreamingContext)
    //执行逻辑
    ssc.start()


    //优雅的关闭
    new Thread(
      new Runnable {
      override def run(): Unit = {
        while(true){
          Thread.sleep(10000)//本次测试是线程睡眠10秒后关闭资源，可根据业务可以采用三方程序更改
          val state: StreamingContextState = ssc.getState()
          if(state == StreamingContextState.ACTIVE){
            ssc.stop(true,true)
            System.exit(0)
          }
        }
      }
    }).start()

    ssc.awaitTermination()
  }


  //设置检查点
  def getStreamingContext():StreamingContext = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc: StreamingContext = new StreamingContext(sparkConf,Seconds(3))
    ssc.checkpoint("cp")
    val ds = ssc.socketTextStream("hadoop102",9999)
    val wordToOneDS = ds.map(num =>("key",num))
    wordToOneDS.print()
    ssc
  }

}
