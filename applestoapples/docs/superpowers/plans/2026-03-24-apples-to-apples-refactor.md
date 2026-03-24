# Apples to Apples Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Refactor the "Apples to Apples" `HostGame` class into a State Pattern with a decoupled communication layer (`IGameNotifier`) to improve modifiability, extensibility, and testability.

**Architecture:** Use the State Pattern (Context-Controlled) for the game loop. Abstract all player communication (network, local, bots) behind an `IGameNotifier` interface. This allows for "arbitrary entry point" testing by injecting state into the `HostGame` context.

**Tech Stack:** Java, JUnit (for testing), existing networking/player classes.

---

### Task 1: Define Core Interfaces

**Files:**
- Create: `src/game/IGameState.java`
- Create: `src/game/IGameNotifier.java`

- [ ] **Step 1: Create `IGameState` interface**
```java
package game;
public interface IGameState {
    void handle(HostGame context);
    String getPhaseName();
}
```

- [ ] **Step 2: Create `IGameNotifier` interface**
```java
package game;
import player.Player;
import game.Card;
import java.util.List;
public interface IGameNotifier {
    void notifyDraw(Player player, List<Card> cards);
    void askForCard(Player player, Card greenApple);
    void broadcastWinner(int winnerId, Card winningApple);
    void notifyGameFinished(int winningPlayerId);
}
```

- [ ] **Step 3: Commit**
```bash
git add src/game/IGameState.java src/game/IGameNotifier.java
git commit -m "feat: add IGameState and IGameNotifier interfaces"
```

---

### Task 2: Refactor `HostGame` for State Injection

**Files:**
- Modify: `src/game/HostGame.java`

- [ ] **Step 1: Update `HostGame` fields and add getters/setters**
Modify `HostGame` to include:
- `private IGameState currentGameState;`
- `private IGameNotifier notifier;`
- Public getters/setters for `players`, `playedApples`, `redApples`, `greenApples`, `judge`, `pointsToWin`, `currentGreenApple`.

- [ ] **Step 2: Add `setGameState` and update the game loop**
Replace the `switch` statement in `nextPhase()` or create a new `runLoop()` method that calls `currentGameState.handle(this)`.

- [ ] **Step 3: Add constructor for state injection (Testability)**
Add a constructor that accepts `players`, `deck`, `notifier`, etc., to allow starting the game from any point.

- [ ] **Step 4: Commit**
```bash
git add src/game/HostGame.java
git commit -m "refactor: HostGame to support State Pattern and injection"
```

---

### Task 3: Implement `DrawState` and Test

**Files:**
- Create: `src/game/states/DrawState.java`
- Create: `src/game/states/DrawStateTest.java` (if JUnit is available)

- [ ] **Step 1: Implement `DrawState`**
Move the logic from `HostGame.DrawPhase()` into `DrawState.handle(HostGame context)`. Use `context.getNotifier().notifyDraw(...)`.

- [ ] **Step 2: Write isolation test for `DrawState`**
Create a test that uses a mock `HostGame` and `IGameNotifier` to verify that players receive the correct number of cards.

- [ ] **Step 3: Commit**
```bash
git add src/game/states/DrawState.java
git commit -m "feat: implement DrawState"
```

---

### Task 4: Implement `PlayState` and Test

**Files:**
- Create: `src/game/states/PlayState.java`

- [ ] **Step 1: Implement `PlayState`**
Move logic from `HostGame.PlayPhase()`. Ensure the `ExecutorService` and `CountDownLatch` logic is preserved but uses `IGameNotifier.askForCard(...)`.

- [ ] **Step 2: Commit**
```bash
git add src/game/states/PlayState.java
git commit -m "feat: implement PlayState"
```

---

### Task 5: Implement `JudgeState` and Test

**Files:**
- Create: `src/game/states/JudgeState.java`

- [ ] **Step 1: Implement `JudgeState`**
Move logic from `HostGame.JudgePhase()`. Use `notifier.broadcastWinner(...)`.

- [ ] **Step 2: Commit**
```bash
git add src/game/states/JudgeState.java
git commit -m "feat: implement JudgeState"
```

---

### Task 6: Implement `WinCheckState` and Finalize

**Files:**
- Create: `src/game/states/WinCheckState.java`
- Modify: `src/game/Main.java`

- [ ] **Step 1: Implement `WinCheckState`**
Move logic from `HostGame.WinCheck()` and `notifyGameFinished()`.

- [ ] **Step 2: Create `NetworkGameNotifier`**
Implement `IGameNotifier` using the existing `ServerNetworking` logic.

- [ ] **Step 3: Update `Main` or `HostGame` constructor**
Ensure the game starts by initializing the `NetworkGameNotifier` and setting the initial state to `DrawState`.

- [ ] **Step 4: Verify everything passes**
Run the game and ensure manual testing or automated tests pass.

- [ ] **Step 5: Commit**
```bash
git add src/game/states/WinCheckState.java src/game/Main.java
git commit -m "feat: finalize State Pattern refactor"
```
