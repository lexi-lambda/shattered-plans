package funorb.awt;

import funorb.Strings;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import static java.awt.event.InputEvent.ALT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;

public final class KeyState implements KeyListener, FocusListener {
  public static final KeyState instance = new KeyState();

  @SuppressWarnings("WeakerAccess")
  public static final class Code {
    public static final int F1 = 1;
    public static final int F2 = 2;
    public static final int F3 = 3;
    public static final int F4 = 4;
    public static final int F5 = 5;
    public static final int F6 = 6;
    public static final int F7 = 7;
    public static final int F8 = 8;
    public static final int F9 = 9;
    public static final int F10 = 10;
    public static final int F11 = 11;
    public static final int F12 = 12;
    public static final int ESCAPE = 13;
    public static final int NUMBER_1 = 16;
    public static final int NUMBER_2 = 17;
    public static final int NUMBER_3 = 18;
    public static final int NUMBER_4 = 19;
    public static final int NUMBER_5 = 20;
    public static final int NUMBER_6 = 21;
    public static final int NUMBER_7 = 22;
    public static final int NUMBER_8 = 23;
    public static final int NUMBER_9 = 24;
    public static final int NUMBER_0 = 25;
    public static final int LETTER_Q = 32;
    public static final int LETTER_W = 33;
    public static final int LETTER_E = 34;
    public static final int LETTER_R = 35;
    public static final int LETTER_T = 36;
    public static final int LETTER_Y = 37;
    public static final int LETTER_U = 38;
    public static final int LETTER_I = 39;
    public static final int LETTER_O = 40;
    public static final int LETTER_P = 41;
    public static final int LETTER_A = 48;
    public static final int LETTER_S = 49;
    public static final int LETTER_D = 50;
    public static final int LETTER_F = 51;
    public static final int LETTER_G = 52;
    public static final int LETTER_H = 53;
    public static final int LETTER_B = 68;
    public static final int LETTER_J = 54;
    public static final int LETTER_K = 55;
    public static final int LETTER_L = 56;
    public static final int LETTER_Z = 64;
    public static final int LETTER_X = 65;
    public static final int LETTER_C = 66;
    public static final int LETTER_V = 67;
    public static final int LETTER_N = 69;
    public static final int LETTER_M = 70;
    public static final int FORWARD_SLASH = 73;
    public static final int TAB = 80;
    public static final int SHIFT = 81;
    public static final int CONTROL = 82;
    public static final int SPACE = 83;
    public static final int ENTER = 84;
    public static final int BACKSPACE = 85;
    public static final int ALT = 86;
    public static final int CLEAR = 91;
    public static final int LEFT = 96;
    public static final int RIGHT = 97;
    public static final int UP = 98;
    public static final int DOWN = 99;
    public static final int INSERT = 100;
    public static final int DELETE = 101;
    public static final int HOME = 102;
    public static final int END = 103;
    public static final int PAGE_UP = 104;
    public static final int PAGE_DOWN = 105;
  }

  private static final int[] codeMapping = new int[521];

  static {
    Arrays.fill(codeMapping, -1);
    codeMapping[  8] = Code.BACKSPACE;
    codeMapping[  9] = Code.TAB;
    codeMapping[ 10] = Code.ENTER;
    codeMapping[ 12] = Code.CLEAR;
    codeMapping[ 16] = Code.SHIFT;
    codeMapping[ 17] = Code.CONTROL;
    codeMapping[ 18] = Code.ALT;
    codeMapping[ 27] = Code.ESCAPE;
    codeMapping[ 32] = Code.SPACE;
    codeMapping[ 33] = Code.PAGE_UP;
    codeMapping[ 34] = Code.PAGE_DOWN;
    codeMapping[ 35] = Code.END;
    codeMapping[ 36] = Code.HOME;
    codeMapping[ 37] = Code.LEFT;
    codeMapping[ 38] = Code.UP;
    codeMapping[ 39] = Code.RIGHT;
    codeMapping[ 40] = Code.DOWN;
    codeMapping[ 48] = Code.NUMBER_0;
    codeMapping[ 49] = Code.NUMBER_1;
    codeMapping[ 50] = Code.NUMBER_2;
    codeMapping[ 51] = Code.NUMBER_3;
    codeMapping[ 52] = Code.NUMBER_4;
    codeMapping[ 53] = Code.NUMBER_5;
    codeMapping[ 54] = Code.NUMBER_6;
    codeMapping[ 55] = Code.NUMBER_7;
    codeMapping[ 56] = Code.NUMBER_8;
    codeMapping[ 57] = Code.NUMBER_9;
    codeMapping[ 65] = Code.LETTER_A;
    codeMapping[ 66] = Code.LETTER_B;
    codeMapping[ 67] = Code.LETTER_C;
    codeMapping[ 68] = Code.LETTER_D;
    codeMapping[ 69] = Code.LETTER_E;
    codeMapping[ 70] = Code.LETTER_F;
    codeMapping[ 71] = Code.LETTER_G;
    codeMapping[ 72] = Code.LETTER_H;
    codeMapping[ 73] = Code.LETTER_I;
    codeMapping[ 74] = Code.LETTER_J;
    codeMapping[ 75] = Code.LETTER_K;
    codeMapping[ 76] = Code.LETTER_L;
    codeMapping[ 77] = Code.LETTER_M;
    codeMapping[ 78] = Code.LETTER_N;
    codeMapping[ 79] = Code.LETTER_O;
    codeMapping[ 80] = Code.LETTER_P;
    codeMapping[ 81] = Code.LETTER_Q;
    codeMapping[ 82] = Code.LETTER_R;
    codeMapping[ 83] = Code.LETTER_S;
    codeMapping[ 84] = Code.LETTER_T;
    codeMapping[ 85] = Code.LETTER_U;
    codeMapping[ 86] = Code.LETTER_V;
    codeMapping[ 87] = Code.LETTER_W;
    codeMapping[ 88] = Code.LETTER_X;
    codeMapping[ 89] = Code.LETTER_Y;
    codeMapping[ 90] = Code.LETTER_Z;
    codeMapping[ 96] = 228;
    codeMapping[ 97] = 231;
    codeMapping[ 98] = 227;
    codeMapping[ 99] = 233;
    codeMapping[100] = 224;
    codeMapping[101] = 219;
    codeMapping[102] = 225;
    codeMapping[103] = 230;
    codeMapping[104] = 226;
    codeMapping[105] = 232;
    codeMapping[106] = 89;
    codeMapping[107] = 87;
    codeMapping[109] = 88;
    codeMapping[110] = 229;
    codeMapping[111] = 90;
    codeMapping[112] = Code.F1;
    codeMapping[113] = Code.F2;
    codeMapping[114] = Code.F3;
    codeMapping[115] = Code.F4;
    codeMapping[116] = Code.F5;
    codeMapping[117] = Code.F6;
    codeMapping[118] = Code.F7;
    codeMapping[119] = Code.F8;
    codeMapping[120] = Code.F9;
    codeMapping[121] = Code.F10;
    codeMapping[122] = Code.F11;
    codeMapping[123] = Code.F12;
    codeMapping[127] = Code.DELETE;
    codeMapping[155] = Code.INSERT;
  }

  public static void initializeAdditionalCodeMappings() {
    codeMapping[ 44] = 71;
    codeMapping[ 45] = 26;
    codeMapping[ 46] = 72;
    codeMapping[ 47] = Code.FORWARD_SLASH;
    codeMapping[ 59] = 57;
    codeMapping[ 61] = 27;
    codeMapping[ 91] = 42;
    codeMapping[ 92] = 74;
    codeMapping[ 93] = 43;
    codeMapping[192] = 28;
    codeMapping[222] = 58;
    codeMapping[520] = 59;
  }


  public static volatile int ticksSinceLastKeyEvent = 0;

  public static final int[] keyPressQueue = new int[128];

  public static int keyPressQueueFront = 0;
  public static int keyPressQueueBack = 0;
  public static final int[] keyTypeCodeQueue = new int[128];

  public static final char[] keyTypeCharQueue = new char[128];
  public static int keyTypeQueueFront = 0;
  public static int keyTypeQueueBack = 0;

  private static boolean isCharAllowed(final char c) {
    if (c == 0) {
      return false;
    } else if (c >= 128 && (c < 160 || c > 255)) {
      for (final char knownChar : Strings.WINDOWS_1252_CHARS) {
        if (c == knownChar) {
          return true;
        }
      }

      return false;
    } else {
      return true;
    }
  }

  @Override
  public synchronized void keyPressed(final KeyEvent var1) {
    if (instance != null) {
      ticksSinceLastKeyEvent = 0;
      final int rawCode = var1.getKeyCode();
      final int mappedCode;
      if (rawCode >= 0 && rawCode < codeMapping.length) {
        final int var21 = codeMapping[rawCode];
        if ((var21 & 128) == 0) {
          mappedCode = var21;
        } else {
          mappedCode = -1;
        }
      } else {
        mappedCode = -1;
      }

      if (mappedCode >= 0) {
        if (keyPressQueueBack >= 0) {
          keyPressQueue[keyPressQueueBack] = mappedCode;
          keyPressQueueBack = (keyPressQueueBack + 1) & 127;
          if (keyPressQueueFront == keyPressQueueBack) {
            keyPressQueueBack = -1;
          }
        }

        final int i = (keyTypeQueueBack + 1) & 127;
        if (i != keyTypeQueueFront) {
          keyTypeCodeQueue[keyTypeQueueBack] = mappedCode;
          keyTypeCharQueue[keyTypeQueueBack] = 0;
          keyTypeQueueBack = i;
        }
      }

      final int modifiers = var1.getModifiers();
      if ((modifiers & (CTRL_MASK | ALT_MASK)) != 0 || mappedCode == 10 || mappedCode == 85) {
        var1.consume();
      }
    }
  }

  @Override
  public synchronized void keyReleased(final KeyEvent event) {
    if (instance != null) {
      ticksSinceLastKeyEvent = 0;
      final int rawCode = event.getKeyCode();
      final int mappedCode;
      if (rawCode >= 0 && rawCode < codeMapping.length) {
        mappedCode = codeMapping[rawCode] & -129;
      } else {
        mappedCode = -1;
      }

      if (keyPressQueueBack >= 0 && mappedCode >= 0) {
        keyPressQueue[keyPressQueueBack] = ~mappedCode;
        keyPressQueueBack = 1 + keyPressQueueBack & 127;
        if (keyPressQueueBack == keyPressQueueFront) {
          keyPressQueueBack = -1;
        }
      }
    }

    event.consume();
  }

  @Override
  public void keyTyped(final KeyEvent event) {
    if (instance != null) {
      final char c = event.getKeyChar();
      if (c != 0 && c != '\uffff' && isCharAllowed(c)) {
        final int i = (keyTypeQueueBack + 1) & 127;
        if (i != keyTypeQueueFront) {
          keyTypeCodeQueue[keyTypeQueueBack] = -1;
          keyTypeCharQueue[keyTypeQueueBack] = c;
          keyTypeQueueBack = i;
        }
      }
    }

    event.consume();
  }

  @Override
  public synchronized void focusLost(final FocusEvent var1) {
    if (instance != null) {
      keyPressQueueBack = -1;
    }
  }

  @Override
  public void focusGained(final FocusEvent var1) {
  }

  public void attach(final Component c) {
    c.setFocusTraversalKeysEnabled(false);
    c.addKeyListener(this);
    c.addFocusListener(this);
  }

  public void detach(final Component c) {
    c.removeKeyListener(this);
    c.removeFocusListener(this);
    keyPressQueueBack = -1;
  }
}
