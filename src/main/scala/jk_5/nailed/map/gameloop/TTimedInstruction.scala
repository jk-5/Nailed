package jk_5.nailed.map.gameloop

/**
 * No description given
 *
 * @author jk-5
 */
trait TTimedInstruction extends IInstruction {
  def shouldContinue(controller: GameThread, ticks: Int): Boolean
  final def execute(controller: GameThread) = {}
}
