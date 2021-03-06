package typings.node.NodeJS

import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation._

@js.native
trait WritableStream extends EventEmitter {
  var writable: Boolean = js.native
  def end(str: String): Unit = js.native
  def end(str: String, encoding: js.UndefOr[scala.Nothing], cb: js.Function): Unit = js.native
  def end(str: String, encoding: String): Unit = js.native
  def end(str: String, encoding: String, cb: js.Function): Unit = js.native
}

