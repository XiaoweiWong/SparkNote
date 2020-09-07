package com.atguigu.MVC.controller

import com.atguigu.MVC.service.{HotCategoryAnalysisService, HotCategorySessionService}
import com.atguigu.sparkcore.bean
import com.atguigu.summer.framework.core.TController

/**
 * @author david 
 * @create 2020-09-01 下午 4:31 
 */
class HotCategorySessionController extends TController{

  private val hotCategoryService: HotCategoryAnalysisService = new HotCategoryAnalysisService
  private val sessionService: HotCategorySessionService = new HotCategorySessionService

  override def execute() ={

    val categories: List[bean.HotCategory] = hotCategoryService.analysis3()
      //把结果传递给sessionService进行分析
    val result: Array[(String, List[(String, Int)])] = sessionService.analysis2(categories)

    result.foreach(println)

  }
}
