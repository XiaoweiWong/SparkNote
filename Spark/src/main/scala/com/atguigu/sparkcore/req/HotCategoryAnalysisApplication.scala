package com.atguigu.sparkcore.req

import com.atguigu.MVC.controller.HotCategoryAnalysisController
import com.atguigu.summer.framework.core.TApplication

/**
 * @author david 
 * @create 2020-09-01 下午 4:29 
 */
object HotCategoryAnalysisApplication extends App with TApplication{

  start("spark") {
     val controller = new HotCategoryAnalysisController
      controller.execute()
  }
}
