package funorb.client;

public final class SetProfileRequest {
  public final byte[] data;
  public int digest;

  public SetProfileRequest(final byte[] data) {
    this.data = data;
  }
}
