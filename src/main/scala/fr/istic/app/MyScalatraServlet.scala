package fr.istic.app

import org.scalatra._
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

import bank._

class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport  {
  
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
    println("toto")
    val tab = parsedBody.extract[Array[Msg]]
    val msg= ext2msg(tab.toList)
    val res= Solutions.init.process(msg).toList
    res
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
case class Tp(tp1:String,tp2:String, tp3:String)

// For the demo
case class Payd( a: Int,  b: Int,  c: Int,  d:Int){}

