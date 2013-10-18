package jk_5.nailed.map.gameloop

/**
 * No description given
 *
 * @author jk-5
 */
trait IInstruction {
  def injectArguments(args: String)
  def execute(controller: GameThread)
}
