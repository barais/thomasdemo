package bank

import util.Try

// On doit traiter la liste des messages depuis le début car
// Le serveur est stateless: il ne se souvient pas de ce qu'on a fait
// au coup d'avant... sinon on devrait avoir 1 état différent pour chaque 
// utilisateur (de la page) se connectant... comment faire?

case class Solutions(ss: (() =>TransValidator)*) {
	def process(ms: List[message]): Seq[Tpassoc] =
			for (s <- ss.map(_.apply())) yield {
				try{
					Try {
						ms.foreach(s.process)
						val name= s.getClass().getPackage().getName().split("validator.")(1)
						val res= s.getValidTrans.map { case ((Nat.Nata(c), (Nat.Nata(m), Nat.Nata(t))), Nat.Nata(a)) => (c.toInt, m.toInt, t.toInt, a.toInt) }
						(new Tpassoc(name, res.toString))
					} getOrElse ({
						val name= s.getClass().getPackage().getName().split("validator.")(1)
						val res= List()
								(new Tpassoc (name, res.toString))
					})
					//ajouté ce try catch car pas capturé par Try!!
					// pas mis dans la version de test pour détecter ces comportements
					// vraiment bizarres mais ce try catch a été ajouté sur le site!
				} catch {case e:java.lang.NoSuchMethodError => (new Tpassoc(s.authors, List().toString))}    
			}
}

object Solutions {
  def init = Solutions(
    () => new validator.genetProved.ConcreteValidator,
    () => new validator.Adili.ConcreteValidator
  )
}

case class Tpassoc(name:String,resultat:String)

//case class Solutions(ss: (() => TransValidator)*) {
//	def process(m: message): Seq[(String, List[(Int,Int,Int,Int)])] =
//			for (s <- ss.map(_.apply())) yield {
//				try{
//					Try {
//						s.process(m)
//						val name= s.getClass().getPackage().getName().split("validator.")(1)
//						(name, s.getValidTrans.map { case ((Nat.Nata(c), (Nat.Nata(m), Nat.Nata(t))), Nat.Nata(a)) => (c.toInt, m.toInt, t.toInt, a.toInt) })
//					} getOrElse ({
//						val name= s.getClass().getPackage().getName().split("validator.")(1)
//								(name, List.empty)
//					})
//					//ajouté ce try catch car pas capturé par Try!!
//					// pas mis dans la version de test pour détecter ces comportements
//					// vraiment bizarres mais ce try catch a été ajouté sur le site!
//				} catch {case e:java.lang.NoSuchMethodError => ((s.authors, List.empty))}    
//			}
//}
//
//object Solutions {
//  def init = Solutions(
//    () => {println("recrée"); new validator.genetProved.ConcreteValidator},
//    () => new validator.Adili.ConcreteValidator
//  )
//}
