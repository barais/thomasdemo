package bank

import util.Try

case class Solutions(ss: (() => TransValidator)*) {
	def process(m: message): Seq[(String, List[(Int,Int,Int,Int)])] =
			for (s <- ss.map(_.apply())) yield {
				try{
					Try {
						s.process(m)
						val name= s.getClass().getPackage().getName().split("validator.")(1)
						(name, s.getValidTrans.map { case ((Nat.Nata(c), (Nat.Nata(m), Nat.Nata(t))), Nat.Nata(a)) => (c.toInt, m.toInt, t.toInt, a.toInt) })
					} getOrElse ({
						val name= s.getClass().getPackage().getName().split("validator.")(1)
								(name, List.empty)
					})
					//ajouté ce try catch car pas capturé par Try!!
					// pas mis dans la version de test pour détecter ces comportements
					// vraiment bizarres mais ce try catch a été ajouté sur le site!
				} catch {case e:java.lang.NoSuchMethodError => ((s.authors, List.empty))}    
			}
}

object Solutions {
  def init = Solutions(
    () => {println("recrée"); new validator.genetProved.ConcreteValidator},
    () => new validator.Adili.ConcreteValidator
  )
}

