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

import java.io.IOException
import java.io.Reader
import java.io.Serializable
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import com.nexus.util.PrimitiveChecks

object JsonValue {
  final val TRUE = new JsonLiteral("true")
  final val FALSE = new JsonLiteral("false")
  final val NULL = new JsonLiteral("null")

  def readFrom(reader: Reader) = new JsonParser(reader).parse
  def readFrom(text: String): JsonValue =
    try {
      new JsonParser(new StringReader(text)).parse
    }catch{
      case e: IOException => throw new RuntimeException(e)
    }
  def valueOf(value: Int) = new JsonNumber(value.toString)
  def valueOf(value: Long) = new JsonNumber(value.toString)
  def valueOf(f: Float) = {
    if(PrimitiveChecks.isInfinite(f) || PrimitiveChecks.isNaN(f)) throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON")
    new JsonNumber(JsonValue.cutOffPointZero(f.toString))
  }
  def valueOf(d: Double) = {
    if(PrimitiveChecks.isInfinite(d) || PrimitiveChecks.isNaN(d)) throw new IllegalArgumentException("Infinite and NaN values not permitted in JSON")
    new JsonNumber(JsonValue.cutOffPointZero(d.toString))
  }
  def valueOf(string: String) = if (string == null) JsonValue.NULL else new JsonString(string)
  def valueOf(value: Boolean) = if (value) JsonValue.TRUE else JsonValue.FALSE
  private def cutOffPointZero(string: String): String =
    if(string.endsWith(".0")) string.substring(0, string.length - 2)
    else string
}

abstract class JsonValue extends Serializable {

  def isObject = false
  def isArray = false
  def isNumber = false
  def isString = false
  def isBoolean = false
  def isTrue = false
  def isFalse = false
  def isNull = false

  def asObject: JsonObject = throw new UnsupportedOperationException("Not an object: " + this.stringify)
  def asArray: JsonArray = throw new UnsupportedOperationException("Not an array: " + this.stringify)
  def asInt: Int = throw new UnsupportedOperationException("Not a number: " + this.stringify)
  def asLong: Long = throw new UnsupportedOperationException("Not a number: " + this.stringify)
  def asFloat: Float = throw new UnsupportedOperationException("Not a number: " + this.stringify)
  def asDouble: Double = throw new UnsupportedOperationException("Not a number: " + this.stringify)
  def asString: String = throw new UnsupportedOperationException("Not a string: " + this.stringify)
  def asBoolean: Boolean = throw new UnsupportedOperationException("Not a boolean: " + this.stringify)

  def writeTo(writer: Writer) = this.write(new JsonWriter(writer))
  def stringify: String = {
    val stringWriter: StringWriter = new StringWriter
    val jsonWriter: JsonWriter = new JsonWriter(stringWriter)
    try{
      write(jsonWriter)
    }catch{
      case e: IOException => throw new RuntimeException(e)
    }
    stringWriter.toString
  }

  override def hashCode = super.hashCode
  private [json] def write(writer: JsonWriter)

  def getMimeType = "application/json"
  def getResponseData: String = this.stringify
}