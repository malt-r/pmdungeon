package traps;

/**
 * Factory for creation of traps.
 *
 * <p>Creates all types of monster that can appear in the game.
 */
public class TrapFactory {
  /**
   * Creates a trap of the specific type.
   *
   * @param trapType Type of the trap
   * @return A trap, of the specified type.
   * @throws Exception if monsterType is not supported
   */
  public static Trap createTrap(TrapType trapType) throws Exception {
    if (trapType == TrapType.HOLE) {
      return new HoleTrap();
    }
    if (trapType == TrapType.SPIKES) {
      return new SpikesTrap();
    }
    if (trapType == TrapType.ACTIVATOR) {
      return new ActivatorTrap();
    }
    if (trapType == TrapType.TELEPORT) {
      return new TeleporterTrap();
    }
    throw new Exception("TrapType no supported");
  }
}
