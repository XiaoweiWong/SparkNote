package com.atguigu.sparkcore.req

import com.atguigu.MVC.controller.PageFlowController
import com.atguigu.summer.framework.core.TApplication

/**
 * @author david 
 * @create 2020-09-02 下午 8:48 
 */
object PageFlowApplication extends App with TApplication{


  start("spark"){
    val pageFlowController = new PageFlowController
    pageFlowController.execute()
  }
}
