package funorb.shatteredplans.game.ai;

import funorb.shatteredplans.game.Player;
import org.intellij.lang.annotations.MagicConstant;

public interface AI {
  Player getPlayer();

  void planTurnOrders();

  void initialize();

  void initialize(boolean isActive);

  void handlePactAccepted(Player ally);

  void handlePactOffer(Player offerer);

  @MagicConstant(valuesFromClass = Type.class)
  int getType();

  void makeDesiredPactOffers();

  @SuppressWarnings({"unused", "WeakerAccess"})
  final class Type {
    public static final int OLD_STANDARD = 0;
    public static final int OLD_CAUTIOUS = 1;
    public static final int TUTORIAL = 2;
    public static final int STANDARD = 3;
    public static final int AGGRESSIVE = 4;
    public static final int RECKLESS = 5;
    public static final int ISOLATIONIST = 6;
    public static final int DEFENSIVE = 7;
    public static final int TASK = 8;
  }
}
