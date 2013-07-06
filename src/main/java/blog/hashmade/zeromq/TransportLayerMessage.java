package blog.hashmade.zeromq;

public class TransportLayerMessage {
  
  private String subject = null;
  
  private String command = null;
  
  private String description = null;
  
  private Object params = null;
  
  public TransportLayerMessage(){
  }
  
  public TransportLayerMessage(final String subject, final String command, final String description, final Object params) {
    super();
    this.subject = subject;
    this.command = command;
    this.description = description;
    this.params = params;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public String getDescription() {
    return description;
  }

  public Object getParams() {
    return params;
  }

  public void setParams(Object params) {
    this.params = params;
  }

  @Override
  public String toString() {
    return "TransportLayerMessage [subject="
        + subject
        + ", command="
        + command
        + ", description="
        + description
        + ", params="
        + params
        + "]";
  }

}
