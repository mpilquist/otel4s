/*
 * Copyright 2022 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.otel4s
package java
package metrics

import cats.effect.kernel.Sync
import io.opentelemetry.api.metrics.{Meter => JMeter}
import org.typelevel.otel4s.metrics._

private[java] case class UpDownCounterBuilderImpl[F[_]](
    jMeter: JMeter,
    name: String,
    unit: Option[String] = None,
    description: Option[String] = None
)(implicit F: Sync[F])
    extends SyncInstrumentBuilder[F, UpDownCounter[F, Long]] {
  type Self = UpDownCounterBuilderImpl[F]

  def withUnit(unit: String): UpDownCounterBuilderImpl[F] =
    copy(unit = Option(unit))

  def withDescription(description: String): UpDownCounterBuilderImpl[F] =
    copy(description = Option(description))

  def create: F[UpDownCounter[F, Long]] = F.delay {
    val b = jMeter.upDownCounterBuilder(name)
    unit.foreach(b.setUnit)
    description.foreach(b.setDescription)
    new UpDownCounterImpl(b.build)
  }
}
