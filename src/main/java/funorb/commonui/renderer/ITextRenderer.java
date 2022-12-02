package funorb.commonui.renderer;

import funorb.commonui.Component;
import funorb.commonui.AbstractTextLayout;

public interface ITextRenderer extends ComponentRenderer {
  AbstractTextLayout updateLayout(Component component);

  int a242(int var1, int var2, int var3, Component var4, int var6);

  int getPreferredHeight(Component component);

  int getAvailableWidth(Component component);

  int getLineHeight();

  void a132(int var1, int var2, int var3, int var4, Component var6);

  int b754(Component var2, int var3);

  int getPreferredWidth(Component component);

  void a403(int var1, int var2, int var4, Component var5);

  int a754(int var1, Component var2);
}
