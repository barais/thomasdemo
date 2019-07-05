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

//  get("toserver"){
//    println(multiParams("msg"))   //http://localhost:8080/index2.html?msg=1&msg=2 
//    println(params("msg"))   //http://localhost:8080/index2.html?msg=1&msg=2 
//  }

  post("/toserver") {    
    val p = parsedBody.extract[Msg]
    println(p);
    val v= new validator.genetProved.ConcreteValidator()
    println(v.authors)
    println(v.getClass().getPackage().getName())
    p.ofType match{
      case "Pay" =>{
        val p2= new Pay( (p.a,(p.b,p.c)),p.d);
        println(p2)
        val res= Solutions.init.process(p2).toList
        println(res)
        new Tp("genetProved: "+assoc("genetProved",res).toString,"tp1: nok", "tp3: ok/nok");
      }
      case "Ack" =>{
        val p2= new Ack( (p.a,(p.b,p.c)),p.d);
        println(p2)
        val res= Solutions.init.process(p2).toList
        new Tp("genetProved: "+assoc("genetProved",res).toString,"tp2: nok", "tp3: ok/nok");
      }
      case "Cancel" =>{
        val p2= new Cancel( (p.a,(p.b,p.c)));
        println(p2)
        val res= Solutions.init.process(p2).toList
        new Tp("genetProved: "+assoc("genetProved",res).toString,"tp3: nok", "tp3: ok/nok");
      }
      case s =>{
        println("Type de message indéfini: "+s)
        new Tp("error","error", "error")   
      }
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

}

case class Msg(ofType:String, a: Int, b: Int, c: Int, d:Int){}
case class Tp(tp1:String,tp2:String, tp3:String)

// For the demo
case class Payd( a: Int,  b: Int,  c: Int,  d:Int){}

//case class Pay(a: (Int, (Int, Int)), m: Int){}
//case class Ack(a: (Int, (Int, Int)), m: Int){}
//case class Cancel(a: (Int, (Int, Int))){}
