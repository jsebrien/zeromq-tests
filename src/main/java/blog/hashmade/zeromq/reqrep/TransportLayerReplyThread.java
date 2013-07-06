package blog.hashmade.zeromq.reqrep;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;
import org.zeromq.ZMQ.Socket;

import blog.hashmade.zeromq.TransportLayerMessage;
import blog.hashmade.zeromq.TransportLayerSocketLocal;
import blog.hashmade.zeromq.util.GsonGsonMessageBuilder;

class TransportLayerReplyThread implements Runnable {

  private static final Logger LOGGER = Logger.getLogger(TransportLayerReplyThread.class);
  
  private final TransportLayerRequestReplyListener listener;
  private final TransportLayerSocketLocal socketLocal;
  private final String url;
  private final AtomicBoolean shouldStop;
  private final GsonGsonMessageBuilder messageBuilder;

  protected TransportLayerReplyThread(
    final String url,
    final TransportLayerSocketLocal socketLocal,
    final TransportLayerRequestReplyListener listener,
    final AtomicBoolean shouldStop,
    final GsonGsonMessageBuilder messageBuilder) {
    this.url = url;
    this.listener = listener;
    this.socketLocal = socketLocal;
    this.shouldStop = shouldStop;
    this.messageBuilder = messageBuilder;
  }

  @Override
  public void run() {
    Socket socket = this.socketLocal.get();
    socket.connect(url);

    while (!shouldStop.get()) {
      try {
        byte[] receivedBytes = socket.recv(0);
        TransportLayerMessage message = this.messageBuilder.unserialize(receivedBytes);
        TransportLayerMessage resultMessage = listener.onMessage(message);
        byte[] bytes = this.messageBuilder.serialize(resultMessage);
        socket.send(bytes, 0);
      } catch (Exception e) {
        LOGGER.error(e.getMessage(), e);
      }
    }
    socket.close();
  }

}