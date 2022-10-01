package funorb.client;

import java.io.IOException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class pa_ extends li_ {
  private final ProxySelector _g = ProxySelector.getDefault();

  private pa_() {}

  public static li_ a287ho(final int var0, final String var1) {
    final pa_ var2 = new pa_();
    var2._a = var0;
    var2._f = var1;
    return var2;
  }

  private Socket a837(final Proxy var2) throws IOException {
    if (var2.type() == Type.DIRECT) {
      return this.a693();
    } else {
      return null;
    }
  }

  @Override
  public Socket b693() throws IOException {
    final boolean var2 = Boolean.parseBoolean(System.getProperty("java.net.useSystemProxies"));
    if (!var2) {
      System.setProperty("java.net.useSystemProxies", "true");
    }

    final boolean var5 = this._a == 443;

    final List<Proxy> var3;
    final List<Proxy> var4;
    try {
      var3 = this._g.select(new URI((var5 ? "https" : "http") + "://" + this._f));
      var4 = this._g.select(new URI((!var5 ? "https" : "http") + "://" + this._f));
    } catch (final URISyntaxException var15) {
      return this.a693();
    }

    var3.addAll(var4);
    final Object[] var6 = var3.toArray();
    int var9 = 0;

    for (; var6.length > var9; ++var9) {
      final Object var10 = var6[var9];
      final Proxy var11 = (Proxy) var10;

      try {
        final Socket var12 = this.a837(var11);
        if (var12 != null) {
          return var12;
        }
      } catch (final IOException var14) {
      }
    }

    return this.a693();
  }
}
