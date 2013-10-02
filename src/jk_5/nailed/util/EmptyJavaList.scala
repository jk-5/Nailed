package jk_5.nailed.util

import java.util

/**
 * No description given
 *
 * @author jk-5
 */
class EmptyJavaList[T] extends util.List[T] {
  def size = 0
  def isEmpty = true
  def contains(o: AnyRef) = false
  def iterator = new util.Iterator[T] {
    def next(): T = null.asInstanceOf[T]
    def remove() {}
    def hasNext: Boolean = false
  }
  def toArray = new Array[AnyRef](0)
  def toArray[T](t: Array[T]): Array[T] = t
  def add(e: T) = throw new UnsupportedOperationException("Cannot add elements to an EmptyList")
  def remove(e: AnyRef) = false
  def containsAll(c: util.Collection[_]) = false
  def addAll(c: util.Collection[_ <: T]) = throw new UnsupportedOperationException("Cannot add elements to an EmptyList")
  def addAll(i: Int, c: util.Collection[_ <: T]) = throw new UnsupportedOperationException("Cannot add elements to an EmptyList")
  def removeAll(c: util.Collection[_]) = false
  def retainAll(c: util.Collection[_]) = false
  def clear(){}
  override def hashCode = 0
  override def equals(o: AnyRef) = o.isInstanceOf[EmptyJavaList]
  def get(i: Int): T = null.asInstanceOf[T]
  def set(i: Int, e: T): T = throw new UnsupportedOperationException("Cannot add elements to an EmptyList")
  def add(i: Int, e: T) = throw new UnsupportedOperationException("Cannot add elements to an EmptyList")
  def remove(i: Int) = null.asInstanceOf[T]
  def indexOf(o: AnyRef) = -1
  def lastIndexOf(o: AnyRef) = -1
  def listIterator = null
  def listIterator(i: Int) = null
  def subList(i: Int, i1: Int) = null
}
