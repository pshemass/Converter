package com.olvind.tso
package phases

import com.olvind.logging.{Formatter, Logger}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

/**
  * Runs a computation given a sequence of input ids.
  */
object PhaseRunner {
  def apply[_Id: Formatter, T](phase: RecPhase[_Id, T], getLogger: _Id => Logger[Unit], listener: PhaseListener[_Id])(
      initial: phase._Id
  ): PhaseRes[phase._Id, phase._T] =
    go(phase, initial, Nil, getLogger, listener)

  def go[Id: Formatter, TT](phase: RecPhase[Id, TT],
                            id:             Id,
                            circuitBreaker: List[Id],
                            getLogger:      Id => Logger[Unit],
                            listener:       PhaseListener[Id]): PhaseRes[Id, TT] =
    phase match {
      case _:    RecPhase.Initial[Id]     => PhaseRes.Ok[Id, TT](id)
      case next: RecPhase.Next[Id, t, TT] => doNext[Id, t, TT](next, id, circuitBreaker, getLogger, listener)
    }

  def doNext[Id: Formatter, T, TT](next: RecPhase.Next[Id, T, TT],
                                   id:             Id,
                                   circuitBreaker: List[Id],
                                   getLogger:      Id => Logger[Unit],
                                   listener:       PhaseListener[Id]): PhaseRes[Id, TT] = {

    val isCircular = circuitBreaker contains id
    if (isCircular) {
      getLogger(id).warn(("circular", circuitBreaker))
    }

    val res = Await.ready(
      next.cache.getOrElse((id, isCircular)) { p =>
        listener.on(next.name, id, PhaseListener.Started(next.name))

        val resLastPhase: PhaseRes[Id, T] =
          go(next.prev, id, Nil, getLogger, listener)

        val logger = getLogger(id)
          .withContext(id)
          .withContext("thread", Thread.currentThread().getId)
          .withContext("phase", next.name)

        def calculateDeps(newRequestedIds: Set[Id]): PhaseRes[Id, Map[Id, TT]] = {
          listener.on(next.name, id, PhaseListener.Blocked(next.name, newRequestedIds.map(Formatter.apply[Id])))

          val ret: PhaseRes[Id, Map[Id, TT]] =
            PhaseRes.sequenceMap(
              newRequestedIds
                .to[Seq]
                .map(thisId => thisId -> go(next, thisId, id :: circuitBreaker, getLogger, listener))
                .toMap
            )

          listener.on(next.name, id, PhaseListener.Started(next.name))

          ret match {
            case PhaseRes.Failure(errors) => logger.warn((s"Failed because of", errors))
            case _                        => ()
          }
          ret
        }

        val result: PhaseRes[Id, TT] =
          resLastPhase.flatMap(
            lastValue => PhaseRes.attempt(id, logger, next.trans(id, lastValue, calculateDeps, isCircular, logger))
          )

        result match {
          case res @ PhaseRes.Ok(_) =>
            listener.on(next.name, id, PhaseListener.Success(next.name))
            logger.debug("Success")
            p.success(res)
          case res @ PhaseRes.Failure(errors) =>
            listener.on(next.name, id, PhaseListener.Failure(next.name))
            logger.debug(("Failure", errors))
            p.success(res)
          case res @ PhaseRes.Ignore() =>
            listener.on(next.name, id, PhaseListener.Ignored)
            logger.debug("Ignored")
            p.success(res)
        }
      },
      Duration.Inf
    )

    res.value match {
      case Some(Success(ok)) => ok
      case Some(Failure(th)) =>
        listener.on(next.name, id, PhaseListener.Failure(next.name))
        System.err.println("Error while running phase")
        th.printStackTrace()
        PhaseRes.Failure(Map(id -> Left(th)))

      case None =>
        listener.on(next.name, id, PhaseListener.Failure(next.name))
        PhaseRes.Failure(Map(id -> Right("timed out")))
    }
  }
}
