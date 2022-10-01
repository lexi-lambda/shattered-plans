package funorb.commonui.listener;

import funorb.commonui.AbstractTextField;

public non-sealed interface TextFieldListener extends ComponentListener {
  void handleTextFieldChanged();
  void handleTextFieldEnterPressed(AbstractTextField textField);
}
