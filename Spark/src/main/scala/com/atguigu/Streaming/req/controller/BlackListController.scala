package com.atguigu.Streaming.req.controller

import com.atguigu.Streaming.req.service.BlackListService
import com.atguigu.summer.framework.core.TController


/**
 * @author david 
 * @create 2020-10-29 上午 11:45 
 */

class BlackListController extends TController{

  private val blackListService: BlackListService = new BlackListService

  override def execute(): Unit = {
    val result: Any = blackListService.analysis()


  }
}
