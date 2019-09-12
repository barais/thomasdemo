package fr.istic.app

import org.scalatra._
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._
import bank._

import java.nio.file.{Paths, Files}
import java.io._
class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport  {
  
  var resultsNok= new SerializedTable()
  
  /** We try to read last (serialized) result state in export/resultTable1.obj and (if it fails) into export/resultTable2.obj.
   *  Otherwise the resultsNok is kept empty */
  if (!initSerialized("export/resultTable1.obj")) 
    initSerialized("export/resultTable2.obj")
  
  private def initSerialized(filename:String):Boolean={
    var readSerialized= false
    // If the serialized version of the table exists
    // we load it (we have two serialization files in case of a crash!)
    if (Files.exists(Paths.get(filename))){
      val ois = new ObjectInputStream(new FileInputStream(filename))
      try{
        resultsNok= ois.readObject.asInstanceOf[SerializedTable]
        readSerialized=true
      } catch {
        case _:Throwable => ()     
      } finally ois.close
      readSerialized
    } else false
  }

  protected implicit def int2Nat(x:Int)= Nat.Nata(BigInt(x))

  protected implicit lazy val jsonFormats: Formats = DefaultFormats
			
  // converts external messages (from javascript) to internal bank message
  protected def ext2msg(l:List[Msg]):List[message]=
    l match {
    case Nil => Nil
    case Msg("Pay",c,m,t,am)::r => (new Pay( (c,(m,t)),am))::ext2msg(r)
    case Msg("Ack",c,m,t,am)::r => (new Ack( (c,(m,t)),am))::ext2msg(r)
    case Msg("Cancel",c,m,t,_)::r => (new Cancel( (c,(m,t))))::ext2msg(r)
    case Msg(s,_,_,_,_)::_ => println("ext2msg cannot convert : "+s); List()
  }

  post("/toserver") {    
    val tab = parsedBody.extract[Array[Msg]]
    val msg= ext2msg(tab.toList)
    val res= Solutions.init.process(msg).toList
    res
  }
  
  post("/toserverProp") {    
    val tab = parsedBody.extract[Array[Msg]]
    val (prop_name,l2) = Export.extractProp(tab)
    val msg= ext2msg(l2)
    val split= prop_name.split("_prop_")
    try{
      val tpname=split(0)
      val propnumber= split(1).toInt
      // When we update the global variable of the serveur, we do this in a synchronized fashion by taking
      // a lock on the server object itself.
      this.synchronized({
          // This part will be executed in mutual exclusion
         resultsNok.update(tpname,propnumber,msg)
         if (Export.isTimeToSave) Export.exportResults(resultsNok)
      })
      ResProp(tpname,"prop_"+propnumber)
    } catch {
      case _:java.lang.ArrayIndexOutOfBoundsException => println("Split has failed!")
      ResProp("","")
    }
  }
  
  // Before every action runs, set the content type to be in JSON format.
  // Useful for the answer of the get request below
  before() {
    contentType = formats("json")
  }
  
  get("/toserver"){
    val res= Solutions.init.process(List()).toList
    res
  }
  
  post("/todemo") {
    val p = parsedBody.extract[Payd]
    println(p);
    //returns
    new Payd(p.a,p.b,p.c,p.d);
  }
}

case class Msg(ofType:String, a: Int, b: Int, c: Int, d:Int){}

case class ResProp(tpname:String,prop:String)

// For the demo
case class Payd( a: Int,  b: Int,  c: Int,  d:Int){}

