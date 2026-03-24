# Design Doc: Apples to Apples Refactor

**Date:** 2026-03-24  
**Topic:** Refactoring `HostGame` using the State Pattern and SOLID principles.

## 1. Overview
The current `HostGame` class is a "God Object" that manages game logic, networking, player types, and the game loop using a large `switch` statement. This makes it difficult to test, modify, and extend. This refactor aims to decompose `HostGame` into smaller, cohesive units that adhere to SOLID principles and Booch metrics.

## 2. Goals (Success Criteria)
*   **Modifiability:** Changes to one game phase (e.g., `JUDGE_PHASE`) should not affect others.
*   **Extensibility:** Adding new game phases or player types should be possible without modifying core game logic.
*   **Testability:** Each game phase must be testable in isolation without running the entire game.
*   **SOLID Compliance:** 
    *   **SRP:** `HostGame` focuses on state management; individual classes handle phase logic.
    *   **OCP:** New phases can be added by implementing the `IGameState` interface.
    *   **ISP/DIP:** Networking and communication are abstracted behind an `IGameNotifier` interface.

## 3. Architecture

### 3.1 State Pattern (Option B - Context-Controlled)
The game loop will be refactored into a State Pattern where the `HostGame` acts as the context.

*   **`IGameState` (Interface):**
    ```java
    public interface IGameState {
        void handle(HostGame context);
    }
    ```
*   **Concrete States:**
    *   `DrawState`: Handles card distribution.
    *   `PlayState`: Manages player card submissions (including thread pool logic).
    *   `JudgeState`: Handles the judge's selection and winner notification.
    *   `WinCheckState`: Checks for the final winner and transitions or ends the game.

*   **`HostGame` (Context):**
    *   Holds references to `Deck`, `List<Player>`, `playedCards`.
    *   Maintains the `currentGameState`.
    *   Method `setGameState(IGameState state)` to transition.

### 3.2 Communication Layer (IGameNotifier)
To decouple game logic from networking and player types:

*   **`IGameNotifier` (Interface):**
    ```java
    public interface IGameNotifier {
        void notifyDraw(Player player, List<Card> cards);
        void askForCard(Player player, Card greenApple);
        void broadcastWinner(Player winner, Card winningApple);
        void notifyGameFinished(Player winner);
    }
    ```
*   **`NetworkGameNotifier` (Implementation):**
    *   Wraps `ServerNetworking`.
    *   Handles the logic: "If player is online, send message; if bot, do nothing; if local, print to console."

## 4. Components & Data Flow
1.  `Main` initializes `HostGame`.
2.  `HostGame` initializes `NetworkGameNotifier`, `Deck`, and `Players`.
3.  `HostGame` starts the loop with `DrawState`.
4.  Each state executes its logic using the `IGameNotifier` for all communication.
5.  `HostGame` (the context) transitions to the next state after each phase completion.

## 5. Testing & Testability

### 5.1 Arbitrary Entry Points (State Restoration)
To meet the requirement of being "launchable from any initialization," the system will support:
*   **Context Injection:** `HostGame` will provide a way to inject pre-configured players (with specific hands and scores), decks, and currently played cards.
*   **Direct State Entry:** The `HostGame` loop will start from whatever `IGameState` is currently set, allowing a test to skip straight to `JudgeState` or `WinCheckState`.
*   **Example Test Scenario:** 
    1. Instantiate `HostGame`.
    2. Inject 4 players, each with 0 points.
    3. Manually add 3 `Card` objects to the `playedCards` list.
    4. `setGameState(new JudgeState())`.
    5. Run `execute()`.
    6. Verify that the winner is chosen and points are awarded without needing to simulate the `Draw` or `Play` phases.

### 5.2 Isolation
*   **Unit Tests:** Each `IGameState` implementation will be tested by passing a mock `HostGame` context and a mock `IGameNotifier`.
*   **Communication Mocking:** `IGameNotifier` can be swapped with a `TestNotifier` to verify communication logic (e.g., "was the winner notified?") without actual networking.

## 6. Booch Metrics Alignment
*   **Coupling (CBO):** Reduced by using interfaces (`IGameState`, `IGameNotifier`) instead of concrete implementations.
*   **Cohesion (LCOM):** Increased by moving phase-specific logic into dedicated classes.
*   **Complexity (WMC):** Reduced in `HostGame` by eliminating the large `switch` statement and nested logic.
