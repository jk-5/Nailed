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
import scala.collection.mutable.ListBuffer

object JsonObject {
  def readFrom(reader: Reader) = JsonValue.readFrom(reader).asObject
  def readFrom(string: String) = JsonValue.readFrom(string).asObject

  class Member(private final val name: String, private final val value: JsonValue) {
    def getName = name
    def getValue = value

    override def hashCode: Int = {
      var result: Int = 1
      result = 31 * result + name.hashCode
      result = 31 * result + value.hashCode
      result
    }

    override def equals(obj: Any): Boolean =
      if(this.eq(obj.asInstanceOf[AnyRef])) true
      else if(obj== null) false
      else if(getClass ne obj.getClass) false
      else name.equals(obj.asInstanceOf[Member].name) && value == obj.asInstanceOf[Member].value
  }

  private[json] class HashIndexTable(private final val original: HashIndexTable = null) {
    private final val hashTable: Array[Byte] = new Array[Byte](32)

    if(this.original != null) System.arraycopy(original.hashTable, 0, this.hashTable, 0, this.hashTable.length)

    private[json] def add(name: String, index: Int) {
      val slot: Int = hashSlotFor(name)
      if(index < 0xff) hashTable(slot) = (index + 1).asInstanceOf[Byte]
      else hashTable(slot) = 0.toByte
    }

    private[json] def remove(name: String) {
      val slot: Int = hashSlotFor(name)
      hashTable(slot) = 0.toByte
    }

    private[json] def get(name: AnyRef): Int = {
      val slot: Int = hashSlotFor(name)
      (hashTable(slot) & 0xff) - 1
    }

    private def hashSlotFor(element: AnyRef) = element.hashCode & hashTable.length - 1
  }
}

class JsonObject extends JsonValue with Iterable[JsonObject.Member]{
  private final val names = ListBuffer[String]()
  private final val values = ListBuffer[JsonValue]()
  @transient private final val table: JsonObject.HashIndexTable = new JsonObject.HashIndexTable

  def add(name: String, value: Int): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: Long): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: Float): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: Double): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: Boolean): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: String): JsonObject = {
    this.add(name, JsonValue.valueOf(value))
    this
  }

  def add(name: String, value: JsonValue): JsonObject = {
    if(name == null) throw new NullPointerException("Name is null")
    if(value == null) throw new NullPointerException("Value is null")
    table.add(name, this.names.size)
    names += name
    values += value
    this
  }

  def set(name: String, value: Int): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: Long): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: Float): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: Double): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: Boolean): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: String): JsonObject = {
    this.set(name, JsonValue.valueOf(value))
    this
  }

  def set(name: String, value: JsonValue): JsonObject = {
    if(name == null) throw new NullPointerException("Name is null")
    if(value == null) throw new NullPointerException("Value is null")
    val index: Int = this.indexOf(name)
    if(index != -1) values.update(index, value)
    else{
      table.add(name, names.size)
      names += name
      values += value
    }
    this
  }

  def remove(name: String): JsonObject = {
    if(name == null) throw new NullPointerException("Name is null")
    val index: Int = this.indexOf(name)
    if(index != -1) {
      table.remove(name)
      names.remove(index)
      values.remove(index)
    }
    this
  }

  def get(name: String): JsonValue = {
    if(name == null) throw new NullPointerException("Name is null")
    val index: Int = this.indexOf(name)
    if (index != -1) values(index) else null
  }

  override def size = names.size
  override def isEmpty = names.isEmpty
  def getNames = this.names

  def iterator: Iterator[JsonObject.Member] = {
    val namesIterator = this.names.iterator
    val valuesIterator = this.values.iterator
    new Iterator[JsonObject.Member] {
      def hasNext = namesIterator.hasNext
      def next(): JsonObject.Member = {
        val name: String = namesIterator.next()
        val value: JsonValue = valuesIterator.next()
        new JsonObject.Member(name, value)
      }
      def remove = throw new UnsupportedOperationException
    }
  }

  private [json] def write(writer: JsonWriter) = writer.writeObject(this)
  override def isObject = true
  override def asObject = this
  override def hashCode = 31 * (31 * 1 + names.hashCode) + values.hashCode
  override def equals(obj: Any) =
    if(obj == null) false
    else if(this.getClass.ne(obj.getClass)) false
    else this.names == obj.asInstanceOf[JsonObject].names && this.values == obj.asInstanceOf[JsonObject].values

  private[json] def indexOf(name: String): Int = {
    val index: Int = table.get(name)
    if (index != -1 && (name == names(index))) {
      return index
    }
    names.lastIndexOf(name)
  }

  def addError(desc: String): JsonObject = {
    this.add("error", desc)
    this
  }
}