package game.states;

import java.util.ArrayList;
import java.util.List;
import game.HostGame;
import game.IGameState;
import player.Player;
import game.Card;

/**
 * DrawState is responsible for the phase where players draw cards to fill their hand up to 7 cards.
 */
public class DrawState implements IGameState {
    
    @Override
    public void handle(HostGame context) {
        for (Player player : context.getPlayers()) {
            int currentHandSize = player.numberOfCards();
            int toDraw = 7 - currentHandSize;
            
            if (toDraw > 0) {
                List<Card> cardsDrawn = new ArrayList<>();
                for (int i = 0; i < toDraw; i++) {
                    Card card = context.getRedApples().drawCard();
                    player.drawCard(card);
                    cardsDrawn.add(card);
                }
                
                if (context.getNotifier() != null) {
                    context.getNotifier().notifyDraw(player, cardsDrawn);
                }
            }
        }
        
        // Transition to next state
        context.setGameState(new PlayState()); 
    }

    @Override
    public String getPhaseName() {
        return "DRAW_PHASE";
    }
}
