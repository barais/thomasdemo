package fr.istic.app



import bank._
@SerialVersionUID(123L)
class SerializedTable extends Serializable{
  /** resultTable: mapping from tp names to maps of broken properties (a set of List of Msg) */
  private var resultsNok= Map[String,Map[Int,Set[List[message]]]]()
 
  /** update of resultTable */
  def update(tpname:String,propnumber:Int,msg:List[message]):Unit={
    var attackForTPname= resultsNok.getOrElse(tpname,Map[Int,Set[List[message]]]())
    attackForTPname= attackForTPname + (propnumber -> (attackForTPname.getOrElse(propnumber, Set[List[message]]()) + msg))
    resultsNok= resultsNok + (tpname -> attackForTPname)
  }
  
  def getTable= resultsNok
}