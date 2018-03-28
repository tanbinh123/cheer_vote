package Listener;

import Imp.GetLuckUserServiceImp;
import service.GetLuckUserService;
import util.Const;
import util.JedisUtil;
import util.Time;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: REN
 * @Description:
 * @Date: Created in 17:11 2018/3/26
 */
public class getLuckUser  implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("tomcat listener start");
        //设置run方法的延迟启动时间
        final Time time = new Time();
        final long diff= time.getTimeDiff();
        System.out.println(diff);
        //设置自启方法 晚上八点获取幸运用户
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
        GetLuckUserService getLuckUserService = new GetLuckUserServiceImp();
            public void run() {
                 //得到幸运用户选取的时间段
                String str = getLuckUserService.getLuckUser();
                //写入redis
                JedisUtil.setString(Const.LuckUser,str);
                //写入mysql
                getLuckUserService.insertLuckUser(str);
            }

    }, diff, 24*60*60*1000);
}


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
