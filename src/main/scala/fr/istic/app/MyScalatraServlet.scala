package fr.istic.app

import org.scalatra._
// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}

// JSON handling support from Scalatra
import org.scalatra.json._

class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport  {
  protected implicit val jsonFormats: Formats = DefaultFormats

  post("/toserver") {
    val p = parsedBody.extract[Pay]
    println(p);
    new Pay(p.a,p.b,p.c,p.d);
  }  

  post("/todemo") {
    val p = parsedBody.extract[Pay]
    println(p);
    //returns
    new Pay(p.a,p.b,p.c,p.d);
  }  

}

case class Msg(ofType:String, a: Int, b: Int, c: Int, d:Int){}

// For the demo
case class Pay( a: Int,  b: Int,  c: Int,  d:Int){}
