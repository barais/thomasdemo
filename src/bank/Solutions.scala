package bank

import util.Try

// On doit traiter la liste des messages depuis le début car
// Le serveur est stateless: il ne se souvient pas de ce qu'on a fait
// au coup d'avant... sinon on devrait avoir 1 état différent pour chaque 
// utilisateur (de la page) se connectant

case class Solutions(
     reference:()=>TransValidator, 
     ss: (() =>TransValidator)*) {
	def process(ms: List[message]): Seq[Tpassoc] ={
	    // we get the reference implem
	    val referenceImplem= reference.apply()
	    ms.foreach(referenceImplem.process)
	    // we get the reference solution
	    val referenceSol= referenceImplem.getValidTrans.map { case ((Nat.Nata(c), (Nat.Nata(m), Nat.Nata(t))), Nat.Nata(a)) => (c.toInt, m.toInt, t.toInt, a.toInt) }
	    
	    // for all tps (prePending the reference implem itself)
	    // wwe have to keep the reference implem as the first, otherwise I have a
	    // bug: the first button (of the first tp) is always clicked!!
			for (s <- (reference+:ss).map(_.apply())) yield {
				try{
					Try {
						ms.foreach(s.process)
						val name= s.getClass().getPackage().getName().split("validator.")(1)
						val res= s.getValidTrans.map { case ((Nat.Nata(c), (Nat.Nata(m), Nat.Nata(t))), Nat.Nata(a)) => (c.toInt, m.toInt, t.toInt, a.toInt) }
						(new Tpassoc(name, res.toString,(tp3.egal(res, referenceSol))))
					} getOrElse ({
						val name= s.getClass().getPackage().getName().split("validator.")(1)
						val res= List()
								(new Tpassoc (name, res.toString,(tp3.egal(res, referenceSol))))
					})
					//ajouté ce try catch car pas capturé par Try!!
					// pas mis dans la version de test pour détecter ces comportements
					// vraiment bizarres mais ce try catch a été ajouté sur le site!
				} catch {case e:java.lang.NoSuchMethodError => (new Tpassoc(s.authors, List().toString,false))}    
			}
  }
}

object Solutions {
  def init = Solutions(() => new validator.genetProved.ConcreteValidator, // the reference implem
      // student implementations
    () => new validator.Adili.ConcreteValidator,                 
    () => new validator.CHENAA.ConcreteValidator
  )
}

case class Tpassoc(name:String,resultat:String,correct:Boolean)


