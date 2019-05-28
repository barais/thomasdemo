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
    println(p.a);
    new Pay(2,3,4,5);
  }  

}

case class Pay( a: Int,  b: Int,  c: Int,  d:Int){}
