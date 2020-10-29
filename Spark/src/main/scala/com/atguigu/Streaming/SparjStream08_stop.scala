package com.atguigu.Streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.{Seconds, StreamingContext, StreamingContextState}

/**
 *
 *
 * 优雅的关闭
 * @author david 
 * @create 2020-10-26 下午 2:59 
 */
object SparjStream08_stop {
  def main(args: Array[String]): Unit = {
    //spark环境
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("wordCountStreaming")
    val ssc = new StreamingContext(sparkConf,Seconds(3))
    //执行逻辑
    ssc.sparkContext.setCheckpointDir("cp")
    val ds = ssc.socketTextStream("hadoop102",9999)
    val wordToOneDS: DStream[(String, Int)] = ds.map(num =>("key",num.toInt))
   wordToOneDS.print()

    ssc.start()

    /**
     * 优雅的关闭
     *
     */
    new Thread(new Runnable {
      override def run(): Unit = {
        while(true){
          Thread.sleep(10000)
          //stop时机判断一般不会采用业务操作，一般采用第三方程序或者存储进行判断
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

}
