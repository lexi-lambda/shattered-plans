package funorb.commonui.listener;

import funorb.commonui.Button;

public non-sealed interface ButtonListener extends ComponentListener {
  void handleButtonClicked(Button button);
}
