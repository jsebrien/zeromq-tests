package blog.hashmade.zeromq.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

public class CommonThreadFactory implements ThreadFactory {

  /** The Constant LOGGER. */
  private static final Logger LOGGER = Logger.getLogger(CommonThreadFactory.class);
  
  /** The thread name. */
  private String threadName = null;
  
  /** The thread number. */
  private final AtomicInteger threadNumber = new AtomicInteger(1);
  
  private int defautPriority = Thread.NORM_PRIORITY;
  
  /**
   * Instantiates a new common thread factory.
   * 
   * @param threadName the thread name
   */
  protected CommonThreadFactory(String threadName){
    this(threadName, Thread.NORM_PRIORITY);
  }
  
  protected CommonThreadFactory(String threadName, int defautPriority){
    this.threadName = threadName;
    this.defautPriority = defautPriority;
  }

  /* (non-Javadoc)
   * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
   */
  @Override
  public Thread newThread(Runnable r) {
    LOGGER.debug("Thread with "+this.defautPriority +" instancied");
    return newThread(r, this.defautPriority, threadName+StringHelper.UNDERSCORE+threadNumber.getAndIncrement());
  }

  /**
   * New thread max priority.
   * 
   * @param r the r
   * @param name the name
   * 
   * @return the thread
   */
  /*private static Thread newThreadMaxPriority(Runnable r, String name) {
    return newThread(r, Thread.MAX_PRIORITY, name);
  }*/

  /**
   * New thread min priority.
   * 
   * @param r the r
   * @param name the name
   * 
   * @return the thread
   */
  /*private static Thread newThreadMinPriority(Runnable r, String name) {
    return newThread(r, Thread.MIN_PRIORITY, name);
  }*/

  /**
   * New thread normal priority.
   * 
   * @param r the r
   * @param name the name
   * 
   * @return the thread
   */
  private static Thread newThreadNormalPriority(Runnable r, String name) {
    return newThread(r, Thread.NORM_PRIORITY, name);
  }
  
  private static ReentrantLock lock = new ReentrantLock();
  
  // pourrait utiliser une concurrenthashmap avec putifabsent mais bon...
  private static Map<String, AtomicLong> threadNumbers = new HashMap<String, AtomicLong>();
  
  private static String getNumberedThreadName(final String threadName){
    lock.lock();
    try{
      AtomicLong longAtomic = threadNumbers.get(threadName);
      if(longAtomic==null){
        longAtomic = new AtomicLong(0);
        threadNumbers.put(threadName, longAtomic);
      }
      return StringHelper.buildStringNoSep(threadName,StringHelper.UNDERSCORE,longAtomic.incrementAndGet());
      
    }finally{
      lock.unlock();
    }
  }
  
  /**
   * New thread.
   * 
   * @param r the r
   * @param priority the priority
   * @param threadName the thread name
   * 
   * @return the thread
   */
  private static Thread newThread(Runnable r, int priority, String threadName) {
    Thread thread = new Thread(r);
    thread.setPriority(priority);
    if(threadName!=null){
      thread.setName(getNumberedThreadName(threadName));
    }
    return thread;
  }
  
  
  public static void executeThread(String threadName, Runnable r){
    executeThread(threadName, Thread.NORM_PRIORITY, r);
  }
  
  public static void executeThread(String threadName, int priority, Runnable r){
    Thread thread = newThreadNormalPriority(r, threadName);
    thread.start();
  }

}
