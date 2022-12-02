package funorb.commonui.frame;

import funorb.awt.KeyState;
import funorb.commonui.Component;
import funorb.commonui.TransparentContainer;
import org.jetbrains.annotations.NotNull;

public class ContentFrame extends Frame {
  private static final int PADDING = 6;
  private static final int PADDING_2 = PADDING * 2;

  private final int paddingTop;
  private final int ticksPerState;
  private final int animateBoundsTicks;
  private Component nextContent;
  private TransparentContainer transparentContent;
  private @NotNull ContentFrame.AnimationState animationState = AnimationState.NONE;
  private int currentTick;

  public ContentFrame(final RootFrame root, final Component content, final int paddingTop, final int ticksPerState, final int animateBoundsTicks) {
    super(root, content.width + PADDING_2, content.height + paddingTop + PADDING_2);
    this.paddingTop = paddingTop;
    this.ticksPerState = ticksPerState;
    this.animateBoundsTicks = animateBoundsTicks;
    this.setContent(content);
  }

  private void setContent(final Component content) {
    if (this.transparentContent != null) {
      this.removeChild(this.transparentContent);
    }

    if (content == null) {
      this.transparentContent = new TransparentContainer();
    } else {
      content.setBounds(PADDING, this.paddingTop + PADDING, content.width, content.height);
      this.transparentContent = new TransparentContainer(content);
    }

    this.addChild(this.transparentContent);
    this.nextContent = null;
  }

  @Override
  public boolean keyTyped(final int keyCode, final char keyChar, final Component focusRoot) {
    if (super.keyTyped(keyCode, keyChar, focusRoot)) {
      return true;
    }

    if (this.transparentContent != null) {
      if (keyCode == KeyState.Code.UP || keyCode == KeyState.Code.DOWN) {
        this.transparentContent.focus(focusRoot);
      }
    }

    return false;
  }

  @Override
  protected final void onAnimateBoundsComplete() {
    if (this.animationState != AnimationState.FADE_OUT) {
      this.currentTick = 0;
      this.animationState = AnimationState.FADE_IN;
      this.setContent(this.nextContent);
      this.transparentContent.alpha = 0;
      this.nextContent = null;
    }
  }

  @Override
  public void tickAnimations() {
    if (this.animationState == AnimationState.FADE_OUT) {
      if (++this.currentTick == this.ticksPerState) {
        this.animationState = AnimationState.BOUNDS;
        this.animateBounds(this.animateBoundsTicks, this.nextContent.width + PADDING_2, this.nextContent.height + this.paddingTop + PADDING_2);
        this.currentTick = 0;
        this.transparentContent.alpha = 0;
      } else {
        this.transparentContent.alpha = 256 - ((this.currentTick << 8) / this.ticksPerState);
      }
    } else if (this.animationState == AnimationState.FADE_IN) {
      if (++this.currentTick == this.ticksPerState) {
        this.transparentContent.alpha = 256;
        this.animationState = AnimationState.NONE;
      } else {
        this.transparentContent.alpha = (this.currentTick << 8) / this.ticksPerState;
      }
    }

    super.tickAnimations();
  }

  public final void setNextContent(final Component content) {
    this.nextContent = content;
    if (this.animationState == AnimationState.BOUNDS) {
      this.animateBounds(this.animateBoundsTicks, this.nextContent.width + PADDING_2, this.nextContent.height + this.paddingTop + PADDING_2);
      this.currentTick = 0;
    } else if (this.animationState != AnimationState.FADE_OUT) {
      this.animationState = AnimationState.FADE_OUT;
      this.currentTick = 0;
    }
  }

  @Override
  protected final void skipAnimations() {
    if (this.animationState != AnimationState.NONE) {
      if (this.animationState != AnimationState.FADE_IN) {
        this.setBoundsCentered(this.nextContent.width + PADDING_2, this.nextContent.height + this.paddingTop + PADDING_2);
        this.setContent(this.nextContent);
      }

      this.transparentContent.alpha = 256;
      this.animationState = AnimationState.NONE;
    }

    super.skipAnimations();
  }

  private enum AnimationState {
    NONE, FADE_OUT, BOUNDS, FADE_IN
  }
}
