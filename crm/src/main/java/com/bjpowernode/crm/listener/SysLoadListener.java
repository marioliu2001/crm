package com.bjpowernode.crm.listener;

import com.bjpowernode.crm.settings.domain.DictionaryValue;
import com.bjpowernode.crm.settings.service.DictionaryTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2022/1/24 11:25
 * @Version 1.0
 */
public class SysLoadListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //为什么要手动初始化spring容器，就是因为再服务器启动的过程中，Spring容器没有初始化完成
        //手动加载spring容器
        ApplicationContext app = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-service.xml");

        //从容器中获取字典的service对象
        DictionaryTypeService bean = app.getBean(DictionaryTypeService.class);

        //调用service方法，进行查询
        Map<String, List<DictionaryValue>> cacheData = bean.findCacheData();

        /*

            缓存的结构：


         */

        //遍历数据，添加到缓存中
        for (Map.Entry<String, List<DictionaryValue>> entry : cacheData.entrySet()) {
            String key = entry.getKey();
            List<DictionaryValue> value = entry.getValue();

            //System.out.println("key "+key);
            //System.out.println("value " + value);

            //只要服务器启动成功，数据就存到了服务器缓存中
            //存放到服务器缓存中
            sce.getServletContext().setAttribute(key,value);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
