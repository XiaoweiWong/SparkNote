package com.atguigu.Streaming.req.util

import java.io.InputStreamReader
import java.util.Properties


/**
 * @author david 
 * @create 2020-10-29 上午 8:40 
 */
object propertiesUtil {
    def load(propertiesName: String)={
      val prop = new Properties()
      prop.load(new InputStreamReader(Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName),"UTF-8"))
      prop
    }
}
