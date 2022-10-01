package funorb.net;

public final class ProtocolException extends RuntimeException {
  @SuppressWarnings("unused")
  public ProtocolException() {
    super();
  }

  @SuppressWarnings("unused")
  public ProtocolException(final String message) {
    super(message);
  }

  @SuppressWarnings("unused")
  public ProtocolException(final String message, final Throwable cause) {
    super(message, cause);
  }

  @SuppressWarnings("unused")
  public ProtocolException(final Throwable cause) {
    super(cause);
  }
}
