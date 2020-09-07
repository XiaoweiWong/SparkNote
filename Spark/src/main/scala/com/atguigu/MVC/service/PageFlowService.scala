package com.atguigu.MVC.service

import com.atguigu.MVC.DAO.PageFlowDAO
import com.atguigu.sparkcore.bean
import com.atguigu.summer.framework.core.TService
import org.apache.spark.rdd.RDD

/**
 * @author david 
 * @create 2020-09-02 下午 8:57 
 */
class PageFlowService extends TService {
  private val pageFlowdao: PageFlowDAO = new PageFlowDAO

  override def analysis() = {
    //TODO 对指定页面流程进行页面单挑转换率的统计
    //1,2,3,4,5
    //(1,2)(2,3)(3,4)(4,5)
    //统计指定页面
    val flowIds = List(1, 2, 3, 4, 5, 6, 7)
    val tuples: List[(Int, Int)] = flowIds.zip(flowIds.tail)
    //(1,2)(2,3)(3,4)(4,5)
    val okflowIds: List[String] = tuples.map(t => (t._1 + "-" + t._2))


    //TODO 获取数据源
    val actionRDD: RDD[bean.UserVisitAction] =
      pageFlowdao.getUserVisitAction("input/user_visit_action.txt")
    //TODO 计算分母
    //将数据过滤后再统计,看看action对象的pageid属性是否在指定页面集合里
    val filterRDD: RDD[bean.UserVisitAction] = actionRDD.filter(
      action => {
        //统计页面的分母数不需要考虑尾部页面，最后一页跳转到哪不考虑flowIds.init
        flowIds.init.contains(action.page_id.toInt)
      }
    )
    val pageToOneRDD: RDD[(Long, Int)] = filterRDD.map(
      action => {
        (action.page_id, 1)
      }
    )
    val pageToSumRDD: RDD[(Long, Int)] = pageToOneRDD.reduceByKey(_ + _)
    val pageCountArray: Array[(Long, Int)] = pageToSumRDD.collect()


    //TODO 计算分子
    //将数据根据用户session分组
    val sessionRDD: RDD[(String, Iterable[bean.UserVisitAction])] =
    actionRDD.groupBy(_.session_id)
    val pageFlowRDD: RDD[(String, List[(String, Int)])] = sessionRDD.mapValues(
      iter => {
        //将分组后的数据根据时间进行排序
        //对迭代类型转换为集合排序
        val actions: List[bean.UserVisitAction] = iter.toList.sortWith(
          (left, right) => {
            //左右分别是迭代类型的两个数据
            left.action_time < right.action_time
          }
        )
        //TODO 将排序后的数据进行结构转换 action=> pageid
        val pageids: List[Long] = actions.map(_.page_id)

        //TODO 将转换结构的数据后的数据再进行转换
        // (1,2,3,4) 取尾部，再和原来数据进行拉链
        // （1,2,3,4）
        // (2,3,4)=>(1-2)(2-3)(3-4)
        val zipids = pageids.zip(pageids.tail)
        zipids.map {
          case (pageids1, pageids2) => {
            (pageids1 + "-" + pageids2, 1)
          }
        }.filter {
          //对页面对的集合结果过滤
          case (ids, one) => {
            okflowIds.contains(ids)
          }
        }
      }
    )
    // 将分组后的数据进行结构转换  ((1-2),sum)
    val sumListRDD: RDD[List[(String, Int)]] = pageFlowRDD.map(_._2)
    val flatmapRDD: RDD[(String, Int)] = sumListRDD.flatMap(list => list)
    val pageSumRDD: RDD[(String, Int)] = flatmapRDD.reduceByKey(_ + _)

    //TODO 计算页面单挑转换率 1-2 /1
    pageSumRDD.foreach {
      case (pageflow, sum) => {
        val pageid: String = pageflow.split("-")(0)
        //计算页面为1的分母数
        val value: Int = pageCountArray.toMap.getOrElse(pageid.toLong, 1)
        println("页面跳转" + pageflow + "转换率为：" + sum.toDouble / value)
      }
    }

  }

  def analysis1() = {

    //TODO 先计算分母
    val actionRDD: RDD[bean.UserVisitAction] =
      pageFlowdao.getUserVisitAction("input/user_visit_action.txt")
    val pageToOneRDD: RDD[(Long, Int)] = actionRDD.map(
      //action的内容是UserVisitAction对象
      action => {
        (action.page_id, 1)
      }
    )
    val pageToSumRDD: RDD[(Long, Int)] = pageToOneRDD.reduceByKey(_ + _)
    val pageCountArray: Array[(Long, Int)] = pageToSumRDD.collect()


    //TODO 计算分子
    //将数据根据用户session分组
    val sessionRDD: RDD[(String, Iterable[bean.UserVisitAction])] =
    actionRDD.groupBy(_.session_id)
    val pageFlowRDD: RDD[(String, List[(String, Int)])] = sessionRDD.mapValues(
      iter => {
        //将分组后的数据根据时间进行排序
        //对迭代类型转换为集合排序
        val actions: List[bean.UserVisitAction] = iter.toList.sortWith(
          (left, right) => {
            //左右分别是迭代类型的两个数据
            left.action_time < right.action_time
          }
        )
        //TODO 将排序后的数据进行结构转换 action=> pageid
        val pageids: List[Long] = actions.map(_.page_id)
        //TODO 将转换结构的数据后的数据再进行转换
        // (1,2,3,4) 取尾部，再和原来数据进行拉链
        // （1,2,3,4）
        // (2,3,4)=>(1-2)(2-3)(3-4)
        val zipids = pageids.zip(pageids.tail)
        zipids.map {
          case (pageids1, pageids2) => {
            (pageids1 + "-" + pageids2, 1)
          }
        }
      }
    )
    // 将分组后的数据进行结构转换  ((1-2),sum)
    val sumListRDD: RDD[List[(String, Int)]] = pageFlowRDD.map(_._2)
    val flatmapRDD: RDD[(String, Int)] = sumListRDD.flatMap(list => list)
    val pageSumRDD: RDD[(String, Int)] = flatmapRDD.reduceByKey(_ + _)

    //TODO 计算页面单挑转换率 1-2 /1
    pageSumRDD.foreach {
      case (pageflow, sum) => {
        val pageid: String = pageflow.split("-")(0)
        //计算页面为1的分母数
        val value: Int = pageCountArray.toMap.getOrElse(pageid.toLong, 1)
        println("页面跳转" + pageflow + "转换率为：" + sum.toDouble / value)
      }
    }

  }
}
