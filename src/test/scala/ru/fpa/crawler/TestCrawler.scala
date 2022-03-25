package ru.fpa.crawler

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxOptionId
import org.http4s.blaze.client.BlazeClientBuilder
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import fs2.Stream
import ru.fpa.crawler.Crawler.Title

import scala.concurrent.ExecutionContext

class TestCrawler extends AnyFreeSpec with Matchers {

  private def test(urls: List[String]) = (for {
    client <- BlazeClientBuilder[IO](ExecutionContext.global).stream
    crawler = Crawler(client)
    result <- Stream.eval(crawler.getTitles(urls))
  } yield result).compile.last

  "Should read title from google" in {
    val urls = List("http://www.google.com")
    test(urls).unsafeRunSync() shouldBe List(Title("http://www.google.com", "Google")).some
  }

  "Should read title from github" in {
    val urls = List("https://github.com")
    test(urls).unsafeRunSync() shouldBe List(
      Title("https://github.com", "GitHub: Where the world builds software · GitHub")
    ).some
  }

  "Should read title from fs2, google, github" in {
    val urls = List("https://fs2.io", "http://www.google.com", "https://github.com")
    test(urls).unsafeRunSync() shouldBe List(
        Title("https://fs2.io", "Fs2")
      , Title("http://www.google.com", "Google")
      , Title("https://github.com", "GitHub: Where the world builds software · GitHub")
    ).some
  }

}
