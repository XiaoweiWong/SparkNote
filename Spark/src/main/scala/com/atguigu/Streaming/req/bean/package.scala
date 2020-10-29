package com.atguigu.Streaming.req

/**
 * @author david 
 * @create 2020-10-29 下午 3:20 
 */
package object bean {
   case class Ad_Click_Log(
                            ts:String,
                            area: String,
                            city:String,
                            userId:String,
                            adId:String
                          )
}
