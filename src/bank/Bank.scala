package bank


object HOL {

trait equal[A] {
  val `HOL.equal`: (A, A) => Boolean
}
def equal[A](a: A, b: A)(implicit A: equal[A]): Boolean = A.`HOL.equal`(a, b)

def eq[A : equal](a: A, b: A): Boolean = equal[A](a, b)

} /* object HOL */

/* message type coming from Isabelle Theories */

abstract sealed class message
final case class Pay(a: (Nat.nat, (Nat.nat, Nat.nat)), b: Nat.nat) extends message
final case class Ack(a: (Nat.nat, (Nat.nat, Nat.nat)), b: Nat.nat) extends message
final case class Cancel(a: (Nat.nat, (Nat.nat, Nat.nat))) extends message

/* Nat Types coming from Isabelle Theories */

object Code_Numeral {

def integer_of_nat(x0: Nat.nat): BigInt = x0 match {
  case Nat.Nata(x) => x
}

} /* object Code_Numeral */

object Nat {

abstract sealed class nat
final case class Nata(a: BigInt) extends nat

def equal_nata(m: nat, n: nat): Boolean =
  Code_Numeral.integer_of_nat(m) == Code_Numeral.integer_of_nat(n)

implicit def equal_nat: HOL.equal[nat] = new HOL.equal[nat] {
  val `HOL.equal` = (a: nat, b: nat) => equal_nata(a, b)
}

def less_nat(m: nat, n: nat): Boolean =
  Code_Numeral.integer_of_nat(m) < Code_Numeral.integer_of_nat(n)

def zero_nat: nat = Nata(BigInt(0))

def less_eq_nat(m: nat, n: nat): Boolean =
  Code_Numeral.integer_of_nat(m) <= Code_Numeral.integer_of_nat(n)

} /* object Nat */
object Natural {

  def apply(numeral: BigInt): Natural = new Natural(numeral max 0)
  def apply(numeral: Int): Natural = Natural(BigInt(numeral))
  def apply(numeral: String): Natural = Natural(BigInt(numeral))

}

class Natural private (private val value: BigInt) {

  override def hashCode(): Int = this.value.hashCode()

  override def equals(that: Any): Boolean = that match {
    case that: Natural => this equals that
    case _ => false
  }

  override def toString(): String = this.value.toString

  def equals(that: Natural): Boolean = this.value == that.value

  def as_BigInt: BigInt = this.value
  def as_Int: Int = if (this.value >= scala.Int.MinValue && this.value <= scala.Int.MaxValue)
    this.value.intValue
  else sys.error("Int value out of range: " + this.value.toString)

  def +(that: Natural): Natural = new Natural(this.value + that.value)
  def -(that: Natural): Natural = Natural(this.value - that.value)
  def *(that: Natural): Natural = new Natural(this.value * that.value)

  def /%(that: Natural): (Natural, Natural) = if (that.value == 0) (new Natural(0), this)
  else {
    val (k, l) = this.value /% that.value
    (new Natural(k), new Natural(l))
  }

  def <=(that: Natural): Boolean = this.value <= that.value

  def <(that: Natural): Boolean = this.value < that.value

}

