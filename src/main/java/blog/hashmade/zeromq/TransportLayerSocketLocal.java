package blog.hashmade.zeromq;

import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class TransportLayerSocketLocal extends ThreadLocal<Socket> {
  
  private int socketType = -1;
 
  private Context context = null;
  
  public TransportLayerSocketLocal(int socketType, Context context) {
    super();
    this.socketType = socketType;
    this.context = context;
  }
  
  @Override
  protected Socket initialValue() {
    return context.socket(socketType);
  }

}
