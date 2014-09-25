package com.geishatokyo.scalajs.enchant
import scala.scalajs.js
import js.JSApp
import js.annotation.JSExport
import js.Dynamic.{ global => g }
import com.geishatokyo.scalajs.enchant._


object ScalaJSExample extends JSApp {
  def lifeLabel(): Label = {
    var life = new Label()
    life.x = 400 - 64
    life.y = 20
    life.color = "black"
    life.font = "32px Meiryo"
    return life
  }

  def setupPlayer(image: Surface): Sprite = {
    var player = new Sprite(32, 64)
    player.image = image
    player.x = 200 - 32
    player.y = 300
    player.frame = js.Array(0)
    return player
  }

  def setupEnemy(image: Surface): Sprite = {
    var enemy = new Sprite(64, 64)
    enemy.image = image
    enemy.x = 200 - 32
    enemy.y = 0
    enemy.frame = js.Array(0)
    return enemy
  }

  def surface(game: Core, image: String): Surface = {
    return game.assets.asInstanceOf[js.Dictionary[Surface]](image)
  }

  def main(): Unit = {
    enchant()

    val playerImage = "images/space0.png"
    val enemyImage = "images/space1.png"

    val game = new Core(400, 400)
    game.preload(js.Array(playerImage, enemyImage))
    game.fps = 20

    game.onload = {
      () =>
        var life = this.lifeLabel
        var timeCounter = new Counter(0)
        var lifeCounter = new Counter(5)
        life.text = lifeCounter.count.toString
        var player = this.setupPlayer(this.surface(game, playerImage))
        var enemy = this.setupEnemy(this.surface(game, enemyImage))
        game.rootScene.addChild(player)
        game.rootScene.addChild(enemy)
        game.rootScene.addChild(life)

        player.addEventListener("enterframe", {
          e:Event =>
            if (timeCounter.count > 0) {
              timeCounter.decrement()
            } else {
              player.visible = true
              enemy.visible = true
            }
            if (game.input.left){
              player.x -= 10
            }
            if (game.input.right){
              player.x += 10
            }
            if (game.input.up){
                player.y -= 10
            }
            if (game.input.down){
                player.y += 10
            }
            if (player.visible){
                enemy.y += 4
                if (enemy.y > 400){
                    enemy.y = 0
                }
            }
            if (player.intersect(enemy)) {
                player.visible = false
                enemy.visible = false
                timeCounter.increment(10)
                player.x = 200 - 32
                player.y = 300
                enemy.x = 200
                enemy.y = 50
                lifeCounter.decrement()
                life.text = lifeCounter.count.toString
            }

            js.Object
        })

    }

    game.start()
  }
}
