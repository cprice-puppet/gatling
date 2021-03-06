/**
 * Copyright 2011-2013 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.core.validation

sealed trait Validation[+T] {
	def map[A](f: T => A): Validation[A]
	def flatMap[A](f: T => Validation[A]): Validation[A]
	def mapError(f: String => String): Validation[T]
	def foreach[A](f: T => A) { map(f) }
}

case class Success[+T](value: T) extends Validation[T] {
	def map[A](f: T => A): Validation[A] = Success(f(value))
	def flatMap[A](f: T => Validation[A]): Validation[A] = f(value)
	def mapError(f: String => String): Validation[T] = this
}

case class Failure(message: String) extends Validation[Nothing] {
	def map[A](f: Nothing => A): Validation[A] = this
	def flatMap[A](f: Nothing => Validation[A]): Validation[A] = this
	def mapError(f: String => String): Validation[Nothing] = Failure(f(message))
}