package com.atguigu.MVC.controller

import com.atguigu.MVC.service.PageFlowService
import com.atguigu.summer.framework.core.TController

/**
 * @author david 
 * @create 2020-09-02 下午 8:57 
 */
class PageFlowController extends TController{

  private val pageFlowService: PageFlowService = new PageFlowService

  override def execute(): Unit = {

    val result: Unit = pageFlowService.analysis()
  }
}
