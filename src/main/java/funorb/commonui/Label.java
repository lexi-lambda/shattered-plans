package funorb.commonui;

import funorb.commonui.listener.LinkedTextListener;
import funorb.commonui.renderer.ComponentRenderer;
import funorb.commonui.renderer.ITextRenderer;
import funorb.shatteredplans.client.JagexApplet;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class Label extends Button {
  private final List<Hotspot> hotspots = new ArrayList<>();
  private @Nullable Hotspot hoveredHotspot = null;
  private @Nullable String[] hotspotTooltips;

  public Label(final String text, final ComponentRenderer renderer) {
    super(text, renderer, null);
  }

  @Override
  public void draw(final int x, final int y) {
    super.draw(x, y);
    final ITextRenderer renderer = (ITextRenderer) this.renderer;
    Hotspot var6 = this.hoveredHotspot;
    if (var6 != null) {
      final int var7 = renderer.b754(this, x);
      final int var8 = renderer.a754(y, this);

      do {
        Button.drawFocusRect(var6.x + var7 - 2, var8 + var6.y - 2, var6.width + 2, var6.height + 2);
        var6 = var6.next;
      } while (var6 != null);
    }
  }

  public final void setHotspotTooltip(final int index, final String tooltip) {
    if (this.hotspotTooltips == null || this.hotspotTooltips.length <= index) {
      final String[] var4 = new String[index + 1];
      if (this.hotspotTooltips != null) {
        System.arraycopy(this.hotspotTooltips, 0, var4, 0, this.hotspotTooltips.length);
      }
      this.hotspotTooltips = var4;
    }
    this.hotspotTooltips[index] = tooltip;
  }

  @Override
  public final void setBounds(final int x, final int y, final int width, final int height) {
    super.setBounds(x, y, width, height);
    this.recalculateHotspots();
  }

  @Override
  public boolean focus(final Component focusRoot) {
    return false;
  }

  public final void setBounds(final int x, final int y, final int width) {
    this.setBounds(x, y, width, ((ITextRenderer) this.renderer).getPreferredHeight(this));
  }

  protected final void recalculateHotspots() {
    this.hotspots.clear();
    final ITextRenderer renderer = (ITextRenderer) this.renderer;
    final AbstractTextLayout layout = renderer.updateLayout(this);

    int pos = 0;
    while (true) {
      final int hotspotOpenStart = this.text.indexOf("<hotspot=", pos);
      if (hotspotOpenStart == -1) {
        return;
      }

      final int hotspotOpenEnd = this.text.indexOf(">", hotspotOpenStart);
      final int hotspotNum = Integer.parseInt(this.text.substring(hotspotOpenStart + 9, hotspotOpenEnd));
      pos = this.text.indexOf("</hotspot>", hotspotOpenStart);
      final int hotspotStartLine = layout.getCharacterLine(hotspotOpenStart);
      final int hotspotEndLine = layout.getCharacterLine(pos);

      Hotspot prevHotspot = null;
      for (int i = hotspotStartLine; i <= hotspotEndLine; ++i) {
        final TextLineMetrics metrics = layout.lineMetrics[i];
        final int startX = i == hotspotStartLine ? layout.getCharacterX(hotspotOpenStart) : metrics.charXs[0];
        final int endX = i == hotspotEndLine ? layout.getCharacterX(pos) : metrics.charXs[metrics.getCharCount()];
        final Hotspot hotspot = new Hotspot(hotspotNum, startX, metrics.top, endX - startX, Math.max(renderer.getLineHeight(), metrics.bottom - metrics.top));
        if (prevHotspot != null) {
          prevHotspot.next = hotspot;
        }

        prevHotspot = hotspot;
        this.hotspots.add(hotspot);
      }
    }
  }

  @Override
  public String getCurrentTooltip() {
    if (this.hoveredHotspot == null || this.hotspotTooltips == null || this.hotspotTooltips.length <= this.hoveredHotspot.index) {
      return null;
    } else {
      return this.hotspotTooltips[this.hoveredHotspot.index];
    }
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this.hoveredHotspot = null;
    if (this.isMouseOver) {
      this.hoveredHotspot = this.hotspotAt(JagexApplet.mouseX - x - this.x, JagexApplet.mouseY - y - this.y).orElse(null);
    }
  }

  @Override
  public final void handleClicked(final int button, final int x, final int y) {
    super.handleClicked(button, x, y);
    if (this.listener != null) {
      this.hotspotAt(x - this.x, y - this.y).ifPresent(hotspot -> {
        ((LinkedTextListener) this.listener).handleLinkClicked(hotspot.index);
      });
    }
  }

  private Optional<Hotspot> hotspotAt(final int mouseX, final int mouseY) {
    return this.hotspots.stream()
        .filter(hotspot -> hotspot.stream().anyMatch(hotspot1 -> hotspot1.hitTest(mouseX, mouseY)))
        .findFirst();
  }

  private static final class Hotspot {
    public final int index;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public @Nullable Hotspot next;

    public Hotspot(final int index, final int x, final int y, final int width, final int height) {
      this.index = index;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }

    public boolean hitTest(final int x, final int y) {
      return x >= this.x
          && y >= this.y
          && x < this.x + this.width
          && y <= this.y + this.height;
    }

    public Stream<Hotspot> stream() {
      return Stream.iterate(this, Objects::nonNull, hotspot -> hotspot.next);
    }
  }
}
