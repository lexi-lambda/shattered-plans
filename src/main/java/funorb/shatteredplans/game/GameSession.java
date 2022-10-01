package funorb.shatteredplans.game;

import funorb.shatteredplans.StringConstants;
import funorb.shatteredplans.game.ai.AI;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class GameSession {
  public static final int[] TURN_DURATIONS = new int[]{15000, 9000, 6000, 4500, 3000, 2250, 1500};

  protected abstract @NotNull Optional<AI> getAI(@NotNull Player player);

  public void handlePactAccepted(final @NotNull Player offerer, final @NotNull Player offeree) {
    this.getAI(offerer).ifPresent(ai -> ai.handlePactAccepted(offeree));
  }

  public abstract void showAIChatMessage(@NotNull Player sender,
                                         @NotNull Player recipient,
                                         @MagicConstant(valuesFromClass = StringConstants.AIMessage.class) int which,
                                         int systemIndex);

  public void handleAIPactOffer(final @NotNull Player offerer, final @NotNull Player offeree) {
    if (offeree != offerer) {
      if (!offerer.isOfferingPactTo(offeree)) {
        offerer.outgoingPactOffersBitmap |= 1 << offeree.index;
        offeree.incomingPactOffersBitmap |= 1 << offerer.index;
        this.getAI(offeree).ifPresent(ai -> ai.handlePactAccepted(offerer));
        if (offerer.hasPactOfferFrom(offeree)) {
          Player.establishPact(offerer, offeree);
          this.handlePactAccepted(offeree, offerer);
        }
      }
    }
  }
}
