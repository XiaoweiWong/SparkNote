package com.atguigu.MVC.service

import com.atguigu.MVC.DAO.HotCategorySessionDAO
import com.atguigu.sparkcore.bean
import com.atguigu.sparkcore.bean.HotCategory
import com.atguigu.summer.framework.core.TService
import com.atguigu.summer.framework.util.EnvUtil
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD


/**
 * @author david 
 * @create 2020-09-01 下午 4:32 
 */
class HotCategorySessionService extends TService {
  private val sessiondao = new HotCategorySessionDAO


  /**
   * 对参数数据分析
   * @param data
   * @return
   */
  override def analysis(data: Any) = {
    val hotCategoryTop10: List[HotCategory] = data.asInstanceOf[List[HotCategory]]
     val actionRDD: RDD[bean.UserVisitAction] = sessiondao.getUserVisitAction("input/user_visit_action.txt")

    //TODO 获取用户行为数据

    //TODO 对数据进行过滤,对点击的行为过滤
    val filterRDD: RDD[bean.UserVisitAction] = actionRDD.filter(
      action => {
        if (action.click_category_id != "-1") {

          var flag = false
          hotCategoryTop10.foreach(
            hc => {
              if (hc.categoryid.toLong == action.click_category_id) {
                flag = true
              }
            }
          )
          flag
        } else {
          false
        }
      }
    )

    //TODO 对过滤后的数据进行分析处理 key（品类_会话，1）=> (品类_会话，sum)
    val value: RDD[(String, Int)] = filterRDD.map(
      action => {
        (action.click_category_id + "_" + action.session_id, 1)
      }
    )
    val reduceRDD: RDD[(String, Int)] = value.reduceByKey(_+_)
    //TODO 将统计后的结果，进行结构转换(品类_会话，sum)=>(品类，（会话，sum）)
    val mapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case (key, count) => {
        val strings: Array[String] = key.split("_")
        (strings(0), (strings(1), count))
      }
    }
    //TODO 将结构转换的数据进行对品类进行分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.groupByKey()

    //TODO 将分组后的数据进行排序前10名
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      iter => {
        iter.toList.sortWith(
          (left, right) => {
            left._2 > right._2
          }
        )
      }
    )
    resultRDD.collect()
  }

  def analysis1(data: Any) = {
    val hotCategoryTop10: List[HotCategory] = data.asInstanceOf[List[HotCategory]]
    val actionRDD: RDD[bean.UserVisitAction] = sessiondao.getUserVisitAction("input/user_visit_action.txt")
    val ids: List[String] = hotCategoryTop10.map(_.categoryid)
    //TODO 获取用户行为数据
    // println(actionRDD.count())
    //TODO 对数据进行过滤,对点击的行为过滤
    val filterRDD: RDD[bean.UserVisitAction] = actionRDD.filter(
      action => {
        if (action.click_category_id != "-1") {
          ids.contains(action.click_category_id.toString)

        } else {
          false
        }
      }
    )

    //TODO 对过滤后的数据进行分析处理 key（品类_会话，1）=> (品类_会话，sum)
    val value: RDD[(String, Int)] = filterRDD.map(
      action => {
        (action.click_category_id + "_" + action.session_id, 1)
      }
    )
    val reduceRDD: RDD[(String, Int)] = value.reduceByKey(_+_)
    //TODO 将统计后的结果，进行结构转换(品类_会话，sum)=>(品类，（会话，sum）)
    val mapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case (key, count) => {
        val strings: Array[String] = key.split("_")
        (strings(0), (strings(1), count))
      }
    }
    //TODO 将结构转换的数据进行对品类进行分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.groupByKey()

    //TODO 将分组后的数据进行排序前10名
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      iter => {
        iter.toList.sortWith(
          (left, right) => {
            left._2 > right._2
          }
        )
      }
    )
    resultRDD.collect()
  }
  def analysis2(data: Any) = {
    val hotCategoryTop10: List[HotCategory] = data.asInstanceOf[List[HotCategory]]
    val actionRDD: RDD[bean.UserVisitAction] = sessiondao.getUserVisitAction("input/user_visit_action.txt")
    val ids: List[String] = hotCategoryTop10.map(_.categoryid)
    //TODO 使用广播变量实现数据的传输
    val BClist: Broadcast[List[String]] = EnvUtil.getEnv().broadcast(ids)


    //TODO 获取用户行为数据
    // println(actionRDD.count())
    //TODO 对数据进行过滤,对点击的行为过滤
    val filterRDD: RDD[bean.UserVisitAction] = actionRDD.filter(
      action => {
        if (action.click_category_id != "-1") {
          BClist.value.contains(action.click_category_id.toString)
        } else {
          false
        }
      }
    )
    //TODO 对过滤后的数据进行分析处理 key（品类_会话，1）=> (品类_会话，sum)
    val value: RDD[(String, Int)] = filterRDD.map(
      action => {
        (action.click_category_id + "_" + action.session_id, 1)
      }
    )
    val reduceRDD: RDD[(String, Int)] = value.reduceByKey(_+_)
    //TODO 将统计后的结果，进行结构转换(品类_会话，sum)=>(品类，（会话，sum）)
    val mapRDD: RDD[(String, (String, Int))] = reduceRDD.map {
      case (key, count) => {
        val strings: Array[String] = key.split("_")
        (strings(0), (strings(1), count))
      }
    }
    //TODO 将结构转换的数据进行对品类进行分组
    val groupRDD: RDD[(String, Iterable[(String, Int)])] = mapRDD.groupByKey()

    //TODO 将分组后的数据进行排序前10名
    val resultRDD: RDD[(String, List[(String, Int)])] = groupRDD.mapValues(
      iter => {
        iter.toList.sortWith(
          (left, right) => {
            left._2 > right._2
          }
        )
      }
    )
    resultRDD.collect()
  }

}
