package ru.fpa.crawler

import cats.effect.IO
import fs2.{Stream, text}
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.http4s.client.Client
import org.http4s.{Method, Request, Uri}
import org.slf4j.{Logger, LoggerFactory}
import ru.fpa.crawler.Crawler._

import scala.language.postfixOps
import scala.util.matching.Regex

case class Crawler(client: Client[IO]) {

  private val logger: Logger = LoggerFactory.getLogger(Crawler.getClass)

  def getTitles(urls: List[String]): IO[List[Title]] = {
    val uris = urls.map(Uri.fromString).collect { case Right(uri) => uri }
    Stream
      .emits(uris)
      .covary[IO]
      .parEvalMap(5)(getTitle)
      .compile
      .toList
  }

  def getTitle(uri: Uri): IO[Title] = {
    val request = Request[IO](method = Method.GET, uri = uri)
    client
      .stream(request)
      .evalTap(response => IO(logger.info(s"Received status=${response.status}")))
      .flatMap { response =>
        if (response.status.isSuccess) {
          response.body
            .through(text.utf8.decode)
            .through(text.lines)
            .collectFirst { case titlePatternMatch(title) => title }
        } else {
          Stream.empty
        }
      }
      .compile
      .toList
      .map(_.headOption.fold(Title(uri.toString(), "Title not found"))(Title(uri.toString(), _)))
  }
}
object Crawler {

  val titlePatternMatch: Regex = ".*<title>(.+)</title>.*".r

  case class Title(url: String, title: String)

  implicit val titleEncoder: Encoder[Title] = deriveEncoder[Title]
}
