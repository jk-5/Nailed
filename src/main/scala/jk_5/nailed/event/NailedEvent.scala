package jk_5.nailed.event

/**
 * No description given
 *
 * @author jk-5
 */
class NailedEvent {
  private var canceled = false
  @inline def isCancelable = false
  @inline final def isCanceled = this.canceled
  @inline final def setCanceled(cancel: Boolean) = this.canceled = cancel
}
