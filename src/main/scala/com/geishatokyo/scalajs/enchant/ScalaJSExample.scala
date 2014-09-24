package com.geishatokyo.scalajs.enchant
import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import com.geishatokyo.scalajs.enchant._

object ScalaJSExample extends JSApp {
  def main(): Unit = {
    enchant()

    val game = new Core(320, 320)

    game.preload(js.Array("chara1.png"))
    game.fps = 20
    game.onload = {
      () =>
        val bear = new Sprite(32, 32)

        bear.image = game.assets.asInstanceOf[js.Dictionary[Surface]]("chara1.png")
        game.rootScene.addChild(bear)
        bear.frame = js.Array(6, 6, 7, 7) // select sprite frame

        bear.tl.moveBy(288, 0, 90, null) // move right
          .scaleTo(-1, 1, 10, null) // turn left
          .moveBy(-288, 0, 90, null) // move left
          .scaleTo(1, 1, 10, null) // turn right
          .loop()
    }

    game.start()
  }
}
