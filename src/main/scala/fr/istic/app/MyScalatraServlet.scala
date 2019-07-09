package fr.istic.app

import org.scalatra._
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

import bank._
import bank.Solutions

class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport  {
  
  protected implicit def int2Nat(x:Int)= Nat.Nata(BigInt(x))
  protected def assoc[T1,T2](x:T1, l:List[(T1,T2)]):T2=
    l match {
    case (y,z)::r => if (x==y) z else assoc(x,r)
  }
  
  protected implicit val jsonFormats: Formats = DefaultFormats

  // converts external messages (from javascript) to internal bank message
  protected def ext2msg(l:List[Msg]):List[message]=
    l match {
    case Nil => Nil
    case Msg("Pay",c,m,t,am)::r => (new Pay( (c,(m,t)),am))::ext2msg(r)
    case Msg("Ack",c,m,t,am)::r => (new Ack( (c,(m,t)),am))::ext2msg(r)
    case Msg("Cancel",c,m,t,_)::r => (new Cancel( (c,(m,t))))::ext2msg(r)
    case Msg(s,_,_,_,_)::_ => println("ext2msg cannot convert : "+s); List()
  }
//  get("toserver"){
//    println(multiParams("msg"))   //http://localhost:8080/index2.html?msg=1&msg=2 
//    println(params("msg"))   //http://localhost:8080/index2.html?msg=1&msg=2 
//  }

  post("/toserver") {    
    println("toto")
    val tab = parsedBody.extract[Array[Msg]]
    println(tab.toList)
    val msg= ext2msg(tab.toList)
    val res= Solutions.init.process(msg).toArray
    println(res.toList)
    res.toList
  }
}
//   post("/toserverAck") {
//     val p = parsedBody.extract[Ack]
//     println(p);
//     //returns
//     new Ack(p.a,p.b,p.c,p.d);
//   }  

//   post("/toserverCab") {
//     val p = parsedBody.extract[Ack]
//     println(p);
//     //returns
//     new Ack(p.a,p.b,p.c,p.d);
//   }  

case class Msg(ofType:String, a: Int, b: Int, c: Int, d:Int){}
case class Tp(tp1:String,tp2:String, tp3:String)

// For the demo
case class Payd( a: Int,  b: Int,  c: Int,  d:Int){}

//case class Pay(a: (Int, (Int, Int)), m: Int){}
//case class Ack(a: (Int, (Int, Int)), m: Int){}
//case class Cancel(a: (Int, (Int, Int))){}
