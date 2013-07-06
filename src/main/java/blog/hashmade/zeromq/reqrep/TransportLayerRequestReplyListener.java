package blog.hashmade.zeromq.reqrep;

import blog.hashmade.zeromq.TransportLayerMessage;

public interface TransportLayerRequestReplyListener{
  
  TransportLayerMessage onMessage(TransportLayerMessage inputMessage);

}
