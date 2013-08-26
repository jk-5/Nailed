/*
 * Copyright 2013 TeamNexus
 *
 * TeamNexus Licenses this file to you under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://opensource.org/licenses/mit-license.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License
 */

package com.nexus.data.json

import java.io.Reader
import java.util.Collections
import java.util
import java.lang.Iterable
import com.google.common.collect.Lists
import scala.collection.JavaConversions._

object JsonArray {
  def readFrom(reader: Reader) = JsonValue.readFrom(reader).asArray

  def readFrom(string: String) = JsonValue.readFrom(string).asArray

  implicit def asJsonArray(c: util.Collection[_]): JsonArray = {
    val a = new JsonArray
    for (o <- c) {
      o match {
        case i: Int => a.add(i)
        case l: Long => a.add(l)
        case f: Float => a.add(f)
        case d: Double => a.add(d)
        case b: Boolean => a.add(b)
        case s: String => a.add(s)
        case v: JsonValue => a.add(v)
        case e => throw new RuntimeException("Unsupported conversion, from %s to JsonValue".format(e.getClass.getSimpleName))
      }
    }
    a
  }
}

class JsonArray extends JsonValue with Iterable[JsonValue] {

  private final val values: util.List[JsonValue] = Lists.newArrayList()

  def add(value: Int): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: Long): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: Float): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: Double): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: Boolean): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: String): JsonArray = {
    this.values.add(JsonValue.valueOf(value))
    this
  }

  def add(value: JsonValue): JsonArray = {
    if (value == null) {
      throw new NullPointerException("value is null")
    }
    this.values.add(value)
    this
  }

  def set(index: Int, value: Long): JsonArray = {
    this.values.set(index, JsonValue.valueOf(value))
    this
  }

  def set(index: Int, value: Float): JsonArray = {
    this.values.set(index, JsonValue.valueOf(value))
    this
  }

  def set(index: Int, value: Double): JsonArray = {
    this.values.set(index, JsonValue.valueOf(value))
    this
  }

  def set(index: Int, value: Boolean): JsonArray = {
    this.values.set(index, JsonValue.valueOf(value))
    this
  }

  def set(index: Int, value: String): JsonArray = {
    this.values.set(index, JsonValue.valueOf(value))
    this
  }

  def set(index: Int, value: JsonValue): JsonArray = {
    if (value == null) {
      throw new NullPointerException("value is null")
    }
    this.values.set(index, value)
    this
  }

  def size: Int = this.values.size

  def isEmpty: Boolean = this.values.isEmpty

  def get(index: Int): JsonValue = this.values.get(index)

  def getValues = Collections.unmodifiableList(this.values)

  def iterator: util.Iterator[JsonValue] = {
    val iterator: util.Iterator[JsonValue] = this.values.iterator
    new util.Iterator[JsonValue] {
      def hasNext: Boolean = iterator.hasNext

      def next: JsonValue = iterator.next

      def remove() = throw new UnsupportedOperationException
    }
  }

  private[json] def write(writer: JsonWriter) = writer.writeArray(this)

  override def isArray = true

  override def asArray = this

  override def hashCode = this.values.hashCode

  override def equals(obj: Any): Boolean =
    if (obj == null) false
    else if (this.getClass ne obj.getClass) false
    else this.values == obj.asInstanceOf[JsonArray].values
}