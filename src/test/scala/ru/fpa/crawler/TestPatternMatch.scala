package ru.fpa.crawler

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import Crawler._

class TestPatternMatch extends AnyFreeSpec with Matchers {

  "TitlePatternMatch should match" - {
    "<title>Title</title>" in {
      val str = "<title>Title</title>"
      titlePatternMatch.matches(str) shouldBe true
      val title = str match {
        case titlePatternMatch(title) => title
        case _                        => ""
      }
      title shouldBe "Title"
    }

    "with spaces  <title>Title</title> " in {
      val str = " <title>Title</title> "
      titlePatternMatch.matches(str) shouldBe true
      val title = str match {
        case titlePatternMatch(title) => title
        case _                        => ""
      }
      title shouldBe "Title"
    }
  }
}
