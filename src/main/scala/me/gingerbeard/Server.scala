package me.gingerbeard

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import me.gingerbeard.providers.Providers
import sangria.ast.Document
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.parser.QueryParser
import spray.json._
import sangria.marshalling.sprayJson._
import model.RootSchema

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

// todo: switch to circe?
object Server extends App {

  import ExecutionContext.Implicits._

  implicit val system = ActorSystem("sangria-server")

  implicit val materializer = ActorMaterializer()

  private def executeGraphQLQuery(query: Document, op: Option[String], vars: JsObject) =
    Executor.execute(RootSchema, query, new Providers, variables = vars, operationName = op)
      .map(OK → _)
      .recover {
        case error: QueryAnalysisError ⇒ BadRequest → error.resolveError
        case error: ErrorWithResolver ⇒ InternalServerError → error.resolveError
      }

  private def graphQLEndpoint(requestJson: JsValue) = {
    val JsObject(fields) = requestJson

    val JsString(query) = fields("query")

    val operation = fields.get("operationName") collect {
      case JsString(op) ⇒ op
    }

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) ⇒ obj
      case _ ⇒ JsObject.empty
    }

    QueryParser.parse(query) match {

      // query parsed successfully, time to execute it!
      case Success(queryAst) ⇒
        complete(executeGraphQLQuery(queryAst, operation, vars))

      // can't parse GraphQL query, return error
      case Failure(error) ⇒
        complete(BadRequest, JsObject("error" → JsString(error.getMessage)))
    }
  }

  val route: Route =
    (post & path("graphql")) {
      entity(as[JsValue]) { (request: JsValue) ⇒
        graphQLEndpoint(request)
      }
    } ~
      get {
        getFromResource("graphiql.html")
      }

  Http().bindAndHandle(route, "0.0.0.0", 8080)
}