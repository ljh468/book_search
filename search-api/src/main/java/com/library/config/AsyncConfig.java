package com.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * #### Async 설정 ####<p>
 * - 쓰레드는 앞단에서 대고객 트래픽을 받을 때 사용함 (오래 걸리는 작업 X)<p>
 * - cpu 연산이 많다면, CorePoolSize와 MaxPoolSize를 동일하게 가져감<p>
 * - io 작업이 많다면, MaxPoolSize를 CorePoolSize보다 2,3배이상 가져감 (대기시간이 많은 특성)<p>
 * - 요청량이 갑자기 많다면, 작업큐가 없는게 나은 선택일 수도 있음 (바로 쓰레드가 생성되도록)<p>
 */
@Slf4j
@Configuration
public class AsyncConfig implements AsyncConfigurer {

  @Bean("bsExecutor")
  @Override
  public Executor getAsyncExecutor() {
    // 설정값은 상황에 따라 설정
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    int cpuCoreCount = Runtime.getRuntime().availableProcessors();
    executor.setCorePoolSize(cpuCoreCount); // 기본적으로 유지되는 쓰레드 수
    executor.setMaxPoolSize(cpuCoreCount * 2); // 최대 늘어나는 쓰레드 수 - "최대(MAX_VALUE)로 설정하는게 장애예방에 도움이 될 수 도 있음"
    executor.setQueueCapacity(10); // 작업큐에 용량
    executor.setKeepAliveSeconds(60); // 늘어난 스레드가 사용되지 않을때 유지되는 시간

    executor.setWaitForTasksToCompleteOnShutdown(true); // 프로그램이 종료될때 작업중이 쓰레드를 기다릴지 여부
    executor.setAwaitTerminationSeconds(60); // 얼마까지 기다릴지 설정

    // 작업큐가 꽉차서 요청을 거부할 때 처리하는 Handler
    // 설정을 하지않으면 디폴트로 그냥 요청을 거절하게 됨 (AbortPolicy.Class)
    // executor.setRejectedExecutionHandler(new CustomRejectHandler());

    executor.setThreadNamePrefix("BS-");
    executor.initialize();
    return executor;
  }

  // 비동기 메서드를 실행할 때 발생하는 예외를 처리하는 Handler를 설정
  @Override
  public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
    return new CustomAsyncExceptionHandler();
  }

  // 비동기 메서드(@Async 어노테이션이 있는 메서드)를 실행할 때 발생하는 예외를 처리하는 Handler
  private static class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(Throwable throwable, Method method, Object... params) {
      log.error("Failed to execute method={}, message={}", method.getName(), throwable.getMessage());
      Arrays.asList(params).forEach(param -> log.error("parameter value {}", param));

    }
  }

  // RejectedExecutionHandler를 커스텀할 수 도 있음, 그러나 main 쓰레드에 영향을 미치지 않게 하는게 중요
  // 일반적으로 요청을 거절하는게 좋은 선택일 수 있음
  private static class CustomRejectHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
      log.warn("[{}] rejected from executor. maxPoolSize: {}, activeCount: {}, Queue size: {}",
               r.getClass().getSimpleName(),
               executor.getMaximumPoolSize(),
               executor.getActiveCount(),
               executor.getQueue().size());
    }
  }


}
