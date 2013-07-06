package blog.hashmade.zeromq.main;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import blog.hashmade.zeromq.TransportLayerMessage;
import blog.hashmade.zeromq.reqrep.TransportLayerReceiver;
import blog.hashmade.zeromq.reqrep.TransportLayerRequestReplyListener;
import blog.hashmade.zeromq.util.StringHelper;

public class RequestReplyServer {

  private static final Logger LOGGER = Logger.getLogger(RequestReplyServer.class);
  private static final String URL = "tcp://0.0.0.0:5555";
  private static final int NB_REPLY_THREADS = 5;
  
  public static void main(String[] args) {
    Callable<TransportLayerRequestReplyListener[]> listenersBuilder = new Callable<TransportLayerRequestReplyListener[]>() {
      @Override
      public TransportLayerRequestReplyListener[] call() throws Exception {
        TransportLayerRequestReplyListener[] listeners = new TransportLayerRequestReplyListener[NB_REPLY_THREADS];
        for(int i=0 ; i < NB_REPLY_THREADS ; i++){
          final int listenerIndex = i;
          listeners[i] = new TransportLayerRequestReplyListener() {
            @Override
            public TransportLayerMessage onMessage(TransportLayerMessage inputMessage) {
              String result = StringHelper.buildString(StringHelper.UNDERSCORE, listenerIndex, "OK");
              LOGGER.info("Receiving message: " + inputMessage+ " in listener: "+listenerIndex+" - returning: " + result);
              return new TransportLayerMessage(inputMessage.getSubject(), inputMessage.getCommand(),
                inputMessage.getDescription(), result);
            }
            
            @Override
            public String toString(){
              return "Listener "+listenerIndex;
            }
          };
        }
        return listeners;
      }
    };
    
    TransportLayerReceiver receiver = new TransportLayerReceiver(URL, listenersBuilder);
    receiver.start();
  }

}
