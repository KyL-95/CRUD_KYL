package com.vti.testing.bosung;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    private static final Logger logger =  LoggerFactory.getLogger(ScheduledTasks.class);
//    @Scheduled(fixedRate = 2000)
    public void scheduleTaskWithFixedRate() throws InterruptedException {
        Thread.sleep(1000);
        logger.info("FixRate ...");
    }
//    @Scheduled(fixedDelay = 2000)
    public void scheduleTaskWithFixedDelay() throws InterruptedException {
        Thread.sleep(4000);
       logger.info("FixDelay ...");
    }
//    @Scheduled(fixedRate = 2000, initialDelay = 10000)
    public void scheduleTaskWithInitialDelay() {
        logger.info("Initial Delay...");
    }
    @Scheduled(cron="0 0 0 25 12 ? ") // https://stackjava.com/uncategorized/cron-expression-la-gi-huong-dan-cu-phap-cron-expression.html
    public void scheduleTaskWithCronExpression() {
        logger.info("No-el");
    }
    // CRON : s  m  H  DoM  M  DoW

}
