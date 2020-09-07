package com.atguigu.MVC.DAO

import com.atguigu.sparkcore.bean.UserVisitAction
import com.atguigu.summer.framework.core.TDao
import org.apache.spark.rdd.RDD

/**
 * @author david 
 * @create 2020-09-02 下午 8:52 
 */
class PageFlowDAO extends TDao {
  def getUserVisitAction(path:String) ={
    val rdd: RDD[String] = readFile(path)
    rdd.map(
      //把行数据封装成对象
      line=>{
        val datas = line.split("_")
        UserVisitAction(
          datas(0),
          datas(1).toLong,
          datas(2),
          datas(3).toLong,
          datas(4),
          datas(5),
          datas(6).toLong,
          datas(7).toLong,
          datas(8),
          datas(9),
          datas(10),
          datas(11),
          datas(12).toLong,
        )
      }

    )
  }
}
