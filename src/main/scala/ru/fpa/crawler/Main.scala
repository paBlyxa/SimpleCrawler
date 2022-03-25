package ru.fpa.crawler

import cats.effect.{ExitCode, IO, IOApp}
import io.circe.syntax.EncoderOps
import org.http4s.HttpRoutes
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.jsonEncoder
import org.http4s.dsl.io._

import scala.concurrent.ExecutionContext.global

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO](global).resource.use { client =>
      val crawler = Crawler(client)
      BlazeServerBuilder[IO]
        .bindHttp(8080)
        .withHttpApp(httpApp(crawler).orNotFound)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    }

  def httpApp(crawler: Crawler): HttpRoutes[IO] = HttpRoutes.of[IO] { case req @ POST -> Root / "titles" =>
    for {
      uris     <- req.as[List[String]]
      body     <- crawler.getTitles(uris)
      response <- Ok(body.asJson)
    } yield response
  }

}
