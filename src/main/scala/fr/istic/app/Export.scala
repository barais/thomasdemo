package fr.istic.app

import bank._
import java.util.Calendar
import java.io._

/** Functions for results export (to string and to file
 *  after a "maxTime" delay. e.g. we record after every 30 minutes 
 *  delay. 
 */

object Export {
  // time stamp in minutes  
  var lastSave:Long= System.currentTimeMillis / 1000 / 60
  // maximal time (in minutes) between two result recordings into an export/serialization file
  val maxTime=5
  val timeStamp= Calendar.getInstance.getTime
  
  def isTimeToSave:Boolean=
    ((System.currentTimeMillis /1000 /60)-lastSave) > maxTime
    
  /** Export of tp name + list of message from the dirty encoding coming
   *  from javascript, i.e. the first message contains the tp name 
   */
  def extractProp(tab:Array[Msg])={
    try{
      tab(0) match {
        case Msg(pn,_,_,_,_) => (pn,tab.toList.tail)
      }
    } catch {
      case _:java.lang.IndexOutOfBoundsException => println("For props: first message with tp name does not exists!")
      ("failure_prop_1",List())
    }
  }
  
  /** We produce a string version of the result table and output it into a file.
   *  To protect the file (in case of server crash) we use two distinct file names.
   */
  def exportResults(results:SerializedTable):Unit={
    val stringRes= resultsToString(results.getTable)
    save(stringRes)
    serialize(results)
    lastSave= System.currentTimeMillis / 1000 / 60
  }
  
  /** saving a string into a file  */
  private def save(s:String):Unit={
    // We save it two times to have a copy in case of a crash of the server
    val file= new FileWriter("export/export1_"+timeStamp)
    try{
       file.write(s)
     } finally file.close()
    val file2= new FileWriter("export/export2_"+timeStamp)
    try{
       file2.write(s)
     } finally file2.close()
  }
  
  /** serializing the resultTable and writing it to 2 files */
  private def serialize(t:SerializedTable):Unit={
    // We save it two times to have a copy in case of a crash of the server
    // (2) write the instance out to a file
    val oos1 = new ObjectOutputStream(new FileOutputStream("export/resultTable1.obj"))
    try {
      oos1.writeObject(t)
    } finally oos1.close

    val oos2 = new ObjectOutputStream(new FileOutputStream("export/resultTable2.obj"))
    try {
      oos2.writeObject(t)
    } finally oos2.close
  }
  
  // Export a csv string from a result map
  private def resultsToString(results:Map[String,Map[Int,Set[List[message]]]]):String={
    var res="TPname;prop1_nb;prop1_val;prop2_nb;prop2_val;prop3_nb;prop3_val;prop4_nb;prop4_val;prop5_nb;prop5_val;prop6_nb;prop6_val;prop7_nb;prop7_val;prop8_nb;prop8_val;prop9_nb;prop9_val;\n"
    for ((tpname,mapForname) <- results){
      res=res+tpname+";"
      for (i <- 1 to 9){
        val propiRes= mapForname.getOrElse(i,Set())
        res=res+propiRes.size+";"+resSetToString(propiRes)+";"
      }
      res=res+"\n"
    }
    res
  }

  private def messageToString(m:message):String=
    m match {
    case Pay((bank.Nat.Nata(a),(bank.Nat.Nata(b),bank.Nat.Nata(c))),bank.Nat.Nata(d)) => "Pay("+a+","+b+","+c+","+d+")"
    case Ack((bank.Nat.Nata(a),(bank.Nat.Nata(b),bank.Nat.Nata(c))),bank.Nat.Nata(d)) => "Ack("+a+","+b+","+c+","+d+")"
    case Cancel((bank.Nat.Nata(a),(bank.Nat.Nata(b),bank.Nat.Nata(c)))) => "Cancel("+a+","+b+","+c+")"
  }
  
  private def listToString(l:List[message]):String=
    "["+l.map(messageToString(_)).mkString(",")+"]"
    
  private def resSetToString(s:Set[List[message]]):String=
    "{"+s.map(listToString(_)).mkString(",")+"}"
  
}