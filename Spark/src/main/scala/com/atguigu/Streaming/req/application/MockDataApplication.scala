package com.atguigu.Streaming.req.application

import com.atguigu.Streaming.req.controller.MockDataController
import com.atguigu.summer.framework.core.TApplication

/**
 * @author david 
 * @create 2020-10-27 下午 6:21 
 */
object  MockDataApplication  extends App with TApplication{
  start("sparkStreaming"){
    val controller: MockDataController = new MockDataController
    controller.execute()
  }
}
