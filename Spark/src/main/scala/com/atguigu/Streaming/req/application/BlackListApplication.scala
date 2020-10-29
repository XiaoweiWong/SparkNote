package com.atguigu.Streaming.req.application

import com.atguigu.Streaming.req.controller.BlackListController
import com.atguigu.Streaming.req.service.BlackListService
import com.atguigu.summer.framework.core.TApplication

/**
 * @author david 
 * @create 2020-10-29 上午 11:18 
 */
object BlackListApplication extends App with TApplication{
  start("sparkStreaming"){
    val blackListController: BlackListController = new BlackListController
    blackListController.execute()
  }

}
