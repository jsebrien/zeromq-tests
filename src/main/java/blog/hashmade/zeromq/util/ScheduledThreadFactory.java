package blog.hashmade.zeromq.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class ScheduledThreadFactory {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = Logger.getLogger(ScheduledThreadFactory.class);
  
  private static final int DEFAULT_TIMEOUT = 5000;
  
  private static final Map<String, ScheduledThreadPoolExecutor> EXECUTORS = new ConcurrentHashMap<String, ScheduledThreadPoolExecutor>();

  /**
   * Instantiates a new scheduled thread factory.
   */
  private ScheduledThreadFactory() {
  }

  /**
   * Creates a new ScheduledThread object.
   * 
   * @param taskName the task name
   * 
   * @return the scheduled thread pool executor
   */
  public static ScheduledThreadPoolExecutor createScheduledExecutor(String taskName, boolean addHook) {
    return createScheduledExecutor(1, taskName, addHook);
  }

  /**
   * Creates a new ScheduledThread object.
   * 
   * @param nbThreads the nb threads
   * @param taskName the task name
   * 
   * @return the scheduled thread pool executor
   */
  public static ScheduledThreadPoolExecutor createScheduledExecutor(int nbThreads, String taskName, boolean addHook) {
    return createScheduledExecutor(nbThreads, taskName, Thread.NORM_PRIORITY, addHook);
  }
  
  /**
   * Creates a new ScheduledThread object.
   * 
   * @param nbThreads the nb threads
   * @param taskName the task name
   * 
   * @return the scheduled thread pool executor
   */
  public static ScheduledThreadPoolExecutor createScheduledExecutor(int nbThreads, final String taskName, int defaultPriority, boolean addHook) {
    LOGGER.debug("Creating ScheduledThreadPoolExecutor with : " + nbThreads + " with name : " + taskName + " and priority : "+defaultPriority);
    final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(nbThreads, new CommonThreadFactory(taskName, defaultPriority));
    executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
    if(addHook){
      Runtime.getRuntime().addShutdownHook(new Thread(){
        @Override
        public void run(){
          if(!executor.isShutdown()){
            executor.shutdownNow();
            LOGGER.info(taskName + " stopped");
  
          }
        }
      });
    }
    EXECUTORS.put(taskName, executor);
    return executor;
  }
  
  public static boolean stopExecutor(ScheduledThreadPoolExecutor executor){
    return stopExecutor(executor, DEFAULT_TIMEOUT);
  }
  
  public static boolean stopExecutor(ScheduledThreadPoolExecutor executor, long timeout){
    try {
      if(executor!=null) {
        executor.shutdown();
        boolean success = executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        if(!success){
          executor.shutdownNow();
          success = executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        }
        return success;
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return false;
  }
}
