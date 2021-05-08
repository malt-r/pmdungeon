package traps;

/**
 * Factory for creation of traps
 * <p>
 *     Creates all types of monster that can appear in the game.
 * </p>
 */
public class TrapFactory {
  /**
   * Creates a trap of the specific type
   *
   * @param   trapType Type of the trap
   * @throws  Exception   if monsterType is not supported
   * @return  A trap, of the specified type.
   */
  public static Trap CreateTrap(TrapType trapType) throws Exception{
    if(trapType == TrapType.HOLE) return new HoleTrap();
    if(trapType == TrapType.SPIKES) return new SpikesTrap();
    if(trapType == TrapType.ACTIVATOR) return new ActivatorTrap();
    throw new Exception("TrapType no supported");
  }
}