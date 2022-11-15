package com.ldg.baoli;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling   // 2.开启定时任务,可以加在启动类上
public class Application {
    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = null;
        try {
            ctx = new SpringApplicationBuilder(Application.class).run(args);
            waitForExecution(ctx);
        } catch (Exception e) {
            log.error("[APP] Error: ", e);
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
    }

    private static void waitForExecution(ConfigurableApplicationContext ctx) {
        synchronized (Application.class) {
            //服务器启动后进行线程等待，服务一直运行着
            while (ctx != null) {
                try {
                    Application.class.wait();
                } catch (InterruptedException ie) {
                    log.error("[APP] Interrupted: ", ie);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}