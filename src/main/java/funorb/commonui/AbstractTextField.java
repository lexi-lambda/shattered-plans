package funorb.commonui;

import funorb.awt.KeyState;
import funorb.awt.MouseState;
import funorb.commonui.listener.ComponentListener;
import funorb.commonui.listener.TextFieldListener;
import funorb.commonui.renderer.ComponentRenderer;
import funorb.commonui.renderer.ITextRenderer;
import funorb.shatteredplans.client.JagexApplet;
import funorb.util.PseudoMonotonicClock;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

public class AbstractTextField extends Button {
  private final int _O;
  private final boolean _M;
  private int _H;
  private int _R = -1;
  private int _N;
  private long _F = 0L;
  private boolean _P = false;
  private long _Q;

  protected AbstractTextField(final String text, final ComponentRenderer renderer, final ComponentListener listener, final int var3) {
    super(text, renderer, listener);
    this._O = var3;
    this.a676(text, true);
    this._M = true;
    this._Q = PseudoMonotonicClock.currentTimeMillis();
  }

  private void k423() {
    final int var2;
    if (this._H != this._N) {
      var2 = Math.min(this._N, this._H);
      final int var3 = Math.max(this._H, this._N);
      this._N = var2;
      this._H = var2;
      this.text = this.text.substring(0, var2) + this.text.substring(var3);
      this.i150();
    }
  }

  private void h423() {
    this.h150();
    this.k423();
  }

  private void a407(final String var2) {
    if (this._O != -1) {
      assert this._O - this.text.length() >= 0;
      return;
    }

    if (this._H == this.text.length()) {
      this.text = this.text + var2;
    } else {
      this.text = this.text.substring(0, this._H) + var2 + this.text.substring(this._H);
    }

    this._H += var2.length();
    this._N = this._H;
    this.i150();
  }

  public final void e487() {
    this._N = 0;
    this._H = 0;
    this.text = "";
    this.i150();
  }

  @Override
  public final boolean a446(final int var1, final int var2, final int var4, final int var5, final int var6, final Component var7) {
    if (super.a446(var1, var2, var4, var5, var6, var7) && this.renderer instanceof ITextRenderer) {
      final int var8 = ((ITextRenderer) this.renderer).a242(var5, JagexApplet.mouseX, var6, this, JagexApplet.mouseY);
      this.a093(var8 != -1 ? var8 : 0);
      final long var10 = PseudoMonotonicClock.currentTimeMillis();
      this._P = -this._F + var10 < 250L;
      if (this._P) {
        this._N = this.g410();
        this._H = this.a137();
        if (this._H > 0 && this.text.charAt(this._H - 1) == ' ') {
          --this._H;
        }

        this._R = this._H;
      }

      this._F = var10;
      return true;
    } else {
      return false;
    }
  }

  private void j423() {
    if (this._M) {
      if (this.renderer instanceof final ITextRenderer var2) {
        final AbstractTextLayout var3 = var2.updateLayout(this);
        final int var4 = var3.getWidth();
        final int var5 = var2.getAvailableWidth(this);
        final int var6 = var2.getLineHeight() >> 1;
        if (var4 < -var6 + var5) {
          this._l = 0;
          this._h = 0;
        } else {
          final int var7 = this._h + var3.getCharacterX(this._H);
          if (-var6 + var5 >= var7) {
            if (var6 > var7) {
              this._h += -var7 + var6;
            }
          } else {
            this._h = this._h - var6 + (var5 - var7);
          }

          if (this._h <= 0) {
            if (this._h < -var5 + var6) {
              this._h = -var5 + var6;
            }
          } else {
            this._h = 0;
          }

        }
      }
    } else {
      this._l = 0;
      this._h = 0;
    }
  }

  @Override
  public final void draw(final int x, final int y) {
    if (this.renderer != null) {
      this.renderer.draw(this, x, y, this.enabled);
      if (this.renderer instanceof final ITextRenderer var5) {
        if (this._H != this._N) {
          var5.a132(this._N, y, x, this._H, this);
        }

        final long var6 = PseudoMonotonicClock.currentTimeMillis();
        if ((var6 - this._Q) % 1000L < 500L) {
          var5.a403(this._H, x, y, this);
        }
      }
    }
  }

  private void a093(final int var2) {
    this._H = var2;
    if (!JagexApplet.keysDown[KeyState.Code.SHIFT]) {
      this._N = this._H;
    }
  }

  @Override
  public final boolean keyTyped(@MagicConstant(valuesFromClass = KeyState.Code.class) final int keyCode, final char keyChar, final Component focusRoot) {

    this._Q = PseudoMonotonicClock.currentTimeMillis();
    if (keyChar == '<' || keyChar == '>') {
      return false;
    } else if (keyChar >= ' ' && keyChar <= '~') {
      if (this._H != this._N) {
        this.k423();
      }

      if (this._O == -1 || this.text.length() < this._O) {
        if (this._H >= this.text.length()) {
          this.text = this.text + keyChar;
          this._N = this._H = this.text.length();
        } else {
          this.text = this.text.substring(0, this._H) + keyChar + this.text.substring(this._H);
          ++this._H;
          this._N = this._H;
        }

        this.i150();
      }

      return true;
    } else {
      if (keyCode == KeyState.Code.BACKSPACE) {
        if (this._N != this._H) {
          this.k423();
          return true;
        }

        if (this._H > 0) {
          this._N = this._H - 1;
          this.k423();
          return true;
        }
      } else if (keyCode == KeyState.Code.DELETE) {
        if (this._N != this._H) {
          this.k423();
          return true;
        }

        if (this._H < this.text.length()) {
          this._N = 1 + this._H;
          this.k423();
          return true;
        }
      } else {
        if (keyCode == KeyState.Code.ESCAPE) {
          this.e487();
          return true;
        }

        if (keyCode != KeyState.Code.LEFT) {
          if (keyCode != KeyState.Code.RIGHT) {
            if (keyCode == KeyState.Code.HOME) {
              this.a093(0);
              return true;
            }

            if (keyCode == KeyState.Code.END) {
              this.a093(this.text.length());
              return true;
            }

            if (keyCode == KeyState.Code.ENTER) {
              this.handleEnterKey();
              return true;
            }

            if (JagexApplet.keysDown[KeyState.Code.CONTROL] && keyCode == KeyState.Code.LETTER_X) {
              this.h423();
              return true;
            }

            if (JagexApplet.keysDown[KeyState.Code.CONTROL] && keyCode == KeyState.Code.LETTER_C) {
              this.h150();
              return true;
            }

            if (JagexApplet.keysDown[KeyState.Code.CONTROL] && keyCode == KeyState.Code.LETTER_V) {
              this.m423();
              return true;
            }
          } else if (this._H < this.text.length()) {
            this.a093(JagexApplet.keysDown[KeyState.Code.CONTROL] ? this.a137() : 1 + this._H);
            return true;
          }
        } else if (this._H > 0) {
          this.a093(!JagexApplet.keysDown[KeyState.Code.CONTROL] ? this._H - 1 : this.g410());
          return true;
        }
      }

      return false;
    }
  }

  private int a137() {
    final int var2 = this.text.length();
    if (var2 == this._H) {
      return this._H;
    } else {
      int var3;
      var3 = 1 + this._H;
      while (var2 > var3 && this.text.charAt(var3 - 1) != ' ') {
        ++var3;
      }
      return var3;
    }
  }

  @Override
  public void tick(final int x, final int y, final Component root) {
    super.tick(x, y, root);
    this.j423();
    if (this.mouseButtonClicked == MouseState.Button.LEFT) {
      if (this.renderer instanceof final ITextRenderer var5) {
        int var6 = var5.a242(x, JagexApplet.mouseX, y, this, JagexApplet.mouseY);
        if (var6 != -1) {
          if (this._P && this._R > var6 && var6 > this._N) {
            var6 = this._R;
          }

          this._H = var6;
        }
      }

      this._Q = PseudoMonotonicClock.currentTimeMillis();
    }

  }

  protected void i150() {
    if (this.listener instanceof TextFieldListener) {
      ((TextFieldListener) this.listener).handleTextFieldChanged();
    }
  }

  public final void a676(String var2, final boolean var3) {
    if (var2 == null) {
      var2 = "";
    }

    this.text = var2;
    final int var5 = var2.length();
    if (this._O != -1 && this._O < var5) {
      this.text = this.text.substring(0, this._O);
    }

    this._H = this._N = this.text.length();
    if (!var3) {
      this.i150();
    }

  }

  private String l983() {
    final int var2 = Math.min(this._H, this._N);
    final int var3 = Math.max(this._N, this._H);
    return this.text.substring(var2, var3);
  }

  private void m423() {
    try {
      final String var2 = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
      this.k423();
      this.a407(var2);
    } catch (final Exception var3) {
    }

  }

  private void h150() {
    final String var2 = this.l983();
    if (var2.length() > 0) {
      Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(this.l983()), null);
    }

  }

  private int g410() {
    if (this._H == 0) {
      return this._H;
    } else {
      int var2 = this._H - 1;
      while (var2 > 0 && this.text.charAt(var2 - 1) != ' ') {
        --var2;
      }

      return var2;
    }
  }

  private void handleEnterKey() {
    if (this.listener instanceof final TextFieldListener l) {
      l.handleTextFieldEnterPressed(this);
    }
  }
}
