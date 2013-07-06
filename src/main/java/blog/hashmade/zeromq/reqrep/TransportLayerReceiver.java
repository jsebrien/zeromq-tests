package blog.hashmade.zeromq.reqrep;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQQueue;

import blog.hashmade.zeromq.TransportLayerSocketLocal;
import blog.hashmade.zeromq.util.GsonGsonMessageBuilder;
import blog.hashmade.zeromq.util.ScheduledThreadFactory;

public class TransportLayerReceiver {

  private static final Logger LOGGER = Logger.getLogger(TransportLayerReceiver.class);
  private static final String INPROC_WORKERS = "inproc://workers";
  
  private ScheduledThreadPoolExecutor replyExecutor = null;
  private final TransportLayerSocketLocal xReqSocketLocal;
  private final TransportLayerSocketLocal repSocketLocal;
  private final String url;
  public static final long TIMEOUT = 500;

  private final ScheduledThreadPoolExecutor executor;
  private final AtomicBoolean shouldStop = new AtomicBoolean(false);

  private final Context context;
  private final TransportLayerSocketLocal socketLocal;
  private final Callable<TransportLayerRequestReplyListener[]> listenersBuilder;

  public TransportLayerReceiver(final String url, final Callable<TransportLayerRequestReplyListener[]> listenersBuilder) {
    this.url = url;
    this.listenersBuilder = listenersBuilder;
    this.context = ZMQ.context(1);
    this.socketLocal = new TransportLayerSocketLocal(ZMQ.XREP, this.context);
    this.xReqSocketLocal = new TransportLayerSocketLocal(ZMQ.XREQ, this.context);
    this.repSocketLocal = new TransportLayerSocketLocal(ZMQ.REP, this.context);
    this.executor = ScheduledThreadFactory.createScheduledExecutor(1, "AbstractTransportLayerReceiver", true);
  }

  public boolean start() {
    try {
      LOGGER.info("Starting receiver...");
      final Socket socket = socketLocal.get();
      final Socket xReqSocket = xReqSocketLocal.get();

      final TransportLayerRequestReplyListener[] listeners = listenersBuilder.call();
      this.replyExecutor = ScheduledThreadFactory.createScheduledExecutor(
        listeners.length,
        "ReplyThread Listeners Task",
        false);
      socket.bind(url);
      LOGGER.info("Successfully bind: "+url);
      xReqSocket.bind(INPROC_WORKERS);
      for (TransportLayerRequestReplyListener listener : listeners) {
        LOGGER.info("Starting "+listener);
        replyExecutor.execute(new TransportLayerReplyThread(
          INPROC_WORKERS,
          repSocketLocal,
          listener,
          shouldStop,
          new GsonGsonMessageBuilder()));
      }
      this.executor.execute(new ZMQQueue(context, socket, xReqSocket));
      LOGGER.info("Receiver started!");
      return true;
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
    return false;
  }

  public boolean stop() {
    ScheduledThreadFactory.stopExecutor(replyExecutor, TIMEOUT);
    final Socket xReqSocket = xReqSocketLocal.get();
    xReqSocket.close();
    
    shouldStop.set(true);
    Socket socket = this.socketLocal.get();
    this.socketLocal.remove();
    socket.close();
    ScheduledThreadFactory.stopExecutor(executor, TIMEOUT);
    return true;
  }

}
