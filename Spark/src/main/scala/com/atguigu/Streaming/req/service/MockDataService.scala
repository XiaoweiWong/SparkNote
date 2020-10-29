package com.atguigu.Streaming.req.service

import com.atguigu.Streaming.req.dao.MockDataDao
import com.atguigu.summer.framework.core.TService

/**
 * @author david 
 * @create 2020-10-29 上午 9:25 
 */
class MockDataService extends TService{
   private val mockDataDao =  new MockDataDao

  /**
   * 数据分析
   *
   * @return
   */
  override def analysis()={
      //TODO 模拟生成数据
     // import mockDataDao._
      val datas = mockDataDao.genMockData()

      //TODO 向kafka发送数据

     mockDataDao.writeToKafka(datas)
  }
}
