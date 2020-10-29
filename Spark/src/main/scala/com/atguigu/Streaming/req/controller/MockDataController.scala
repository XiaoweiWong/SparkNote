package com.atguigu.Streaming.req.controller

import com.atguigu.Streaming.req.service.MockDataService
import com.atguigu.summer.framework.core.TController

/**
 * @author david 
 * @create 2020-10-29 上午 9:25 
 */
class MockDataController extends TController{
  private val service: MockDataService = new MockDataService

  override def execute(): Unit = {
    val result: Any = service.analysis()

  }
}
