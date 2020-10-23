package bank

object tp3 {
    // Sets as list equality proven in Isabelle/HOL
  def member[A](uu: A, x1: List[A]): Boolean = (uu, x1) match {
	case (uu, Nil) => false
	case (e, x :: xs) => (if (x==e) true else member[A](e, xs))
	}
  
  def inclus[A](x0: List[A], l2: List[A]): Boolean = (x0, l2) match {
	case (Nil, l2) => true
	case (e :: l, l2) => member[A](e, l2) && inclus[A](l, l2)
	}

	def egal[A](l1: List[A], l2: List[A]): Boolean =
			inclus[A](l1, l2) && inclus[A](l2, l1)
  
}