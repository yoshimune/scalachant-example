package com.geishatokyo.scalajs.enchant
import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import com.geishatokyo.scalajs.enchant._

object ScalaJSExample extends JSApp {
  def main(): Unit = {
    enchant()

    val game = new Core(400, 400)

    game.preload(js.Array("images/space0.png", "images/space1.png"))
    game.fps = 20
    game.onload = {
      () =>
        var counter = 0
        var lifeCount = 5
        var player = new Sprite(32, 64)
        var enemy = new Sprite(64, 64)
        var life = new Label()
        life.x = 400 - 64
        life.y = 20
        life.color = "black"
        life.font = "32px Meiryo"
        life.text = lifeCount.toString
        player.image = game.assets.asInstanceOf[js.Dictionary[Surface]]("images/space0.png")
        enemy.image = game.assets.asInstanceOf[js.Dictionary[Surface]]("images/space1.png")
        player.x = 200 - 32
        player.y = 300
        enemy.x = 200 - 32
        enemy.y = 0
        game.rootScene.addChild(player)
        game.rootScene.addChild(enemy)
        game.rootScene.addChild(life)
        player.frame = js.Array(0)
        enemy.frame = js.Array(0)

        // player.addEventListener("enterframe", {
        //   () => return ()
            // if (counter > 0) {
            //     counter -= 1
            // } else {
            //     player.visible = true
            //     enemy.visible = true
            // }
            // if (game.input.left){
            //     player.x -= 10
            // }
            // if (game.input.right){
            //     player.x += 10
            // }
            // if (game.input.up){
            //     player.y -= 10
            // }
            // if (game.input.down){
            //     player.y += 10
            // }
        //     if (player.visible){
        //         enemy.y += 4
        //         if (enemy.y > 400){
        //             enemy.y = 0
        //         }
        //     }
        //     if (player.intersect(enemy)) {
        //         player.visible = false
        //         enemy.visible = false
        //         counter += 10
        //         player.x = 200 - 32
        //         player.y = 300
        //         enemy.x = 200
        //         enemy.y = 50
        //         lifeCount -= 1
        //         life.text = lifeCount.toString
        //     }
            // return ()
        // })

        return ()

    }

    game.start()
  }
}
