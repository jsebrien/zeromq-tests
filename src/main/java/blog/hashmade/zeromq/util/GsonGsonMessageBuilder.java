package blog.hashmade.zeromq.util;

import blog.hashmade.zeromq.TransportLayerMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonGsonMessageBuilder {
  
  private final Gson gson;
  
  public GsonGsonMessageBuilder(){
    gson = new GsonBuilder().create();
  }

  public byte[] serialize(TransportLayerMessage message) {
    String result = gson.toJson(message);
    return result.getBytes();
  }

  public TransportLayerMessage unserialize(byte[] bytes) {
    TransportLayerMessage message = gson.fromJson(new String(bytes), TransportLayerMessage.class);
    return message;
  }

}
