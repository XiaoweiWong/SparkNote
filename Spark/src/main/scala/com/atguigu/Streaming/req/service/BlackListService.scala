package com.atguigu.Streaming.req.service

import com.atguigu.Streaming.req.dao.BlackListDao
import com.atguigu.summer.framework.core.TService
import org.apache.spark.streaming.dstream.DStream

/**
 * @author david 
 * @create 2020-10-29 上午 11:30 
 */
class BlackListService extends TService{
  private val blackListDao: BlackListDao = new BlackListDao

  /**
   * 数据分析
   *
   * @return
   */
  override def analysis() {
    val ds: DStream[String] = blackListDao.readKafka()
    //todo 将数据转换为样例类来使用
    ds.map(
      data=>{
        val datas = data.split(" ")
      }
    )
    //todo 周期性的获取黑名单的信息，判断当前用户的点击数据是否在黑名单中
    //todo 如果用户在黑名单中，那么将数据过滤掉，不进行统计
    //todo 将正常的数据进行点击量的统计
    //（key,1）     (day-userid-adid,1)=>(day-userid-adid,sum)

    //todo 将统计的结果中超过阈值的用户信息拉人到黑名单中

    ds.print()
  }
}
