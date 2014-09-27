package com.geishatokyo.scalajs.enchant
import scala.scalajs.js
import js.JSApp
import js.annotation.JSExport
import js.Dynamic.{ global => g }
import com.geishatokyo.scalajs.enchant._
import scala.util.Random


object ScalaJSExample extends JSApp {
  val fieldWidth = 400
  val fieldHeight = 400

  def lifeLabel(): Label = {
    var life = new Label()
    life.x = fieldWidth - 64
    life.y = 20
    life.color = "green"
    life.font = "32px Meiryo"
    return life
  }

  def killLabel(): Label = {
    var life = new Label()
    life.x = fieldWidth - 64
    life.y = 52
    life.color = "red"
    life.font = "32px Meiryo"
    return life
  }

  def setupPlayer(image: Surface): Sprite = {
    var player = new Sprite(32, 64)
    player.image = image
    player.x = fieldWidth/2 - 32
    player.y = fieldHeight*3/4
    player.frame = js.Array(0)
    return player
  }

  def initEnemies(image: Surface, size: Int): List[Sprite] = {
    List.fill(size)(setupEnemy(image))
  }

  def setupEnemy(image: Surface): Sprite = {
    var enemy = new Sprite(64, 64)
    enemy.image = image
    enemy.x = Random.nextInt(fieldWidth-32)+32
    enemy.y = 0 - Random.nextInt(fieldHeight)
    enemy.frame = js.Array(0)
    return enemy
  }

  def setupLaser(image: Surface): Sprite = {
    var laser = new Sprite(1, 16)
    laser.image = image
    laser.x = 0
    laser.y = -20
    laser.frame = js.Array(0)
    return laser
  }

  def setupBackground(image: Surface): List[Sprite] = {
    var backgroundFirst = new Sprite(fieldWidth, fieldHeight+200)
    backgroundFirst.image = image
    backgroundFirst.x = 0
    backgroundFirst.y = 0
    backgroundFirst.frame = js.Array(0)
    var backgroundSecond = new Sprite(fieldWidth, fieldHeight+200)
    backgroundSecond.image = image
    backgroundSecond.x = 0
    backgroundSecond.y = 0 - (fieldHeight+200)
    backgroundSecond.frame = js.Array(0)
    return  List(backgroundFirst, backgroundSecond)
  }

  def setupGameOver(image: Surface): Sprite = {
    var gameOver = new Sprite(189, 97)
    gameOver.image = image
    gameOver.x = fieldWidth/2 - 94
    gameOver.y = fieldHeight/2 - 48
    gameOver.frame = js.Array(0)
    return gameOver
  }

  def surface(game: Core, image: String): Surface = {
    return game.assets.asInstanceOf[js.Dictionary[Surface]](image)
  }

  def main(): Unit = {
    enchant()

    val playerImage = "images/space0.png"
    val enemyImage = "images/space1.png"
    val laserImage = "images/bar.png"
    val gameOverImage = "images/end.png"
    val backgroundImage = "images/backUniverse.png"

    val game = new Core(fieldWidth, 400)
    game.preload(js.Array(playerImage, enemyImage, laserImage, gameOverImage, backgroundImage))
    game.fps = 20

    game.onload = {
      () =>
        var life = this.lifeLabel
        var kill = this.killLabel
        var timeCounter = new Counter(0)
        var lifeCounter = new Counter(500)
        var killCounter = new Counter(0)
        life.text = lifeCounter.count.toString
        kill.text = killCounter.count.toString
        var player = this.setupPlayer(this.surface(game, playerImage))
        var enemies = this.initEnemies(this.surface(game, enemyImage), 50)
        var laser = this.setupLaser(this.surface(game, laserImage))
        var gameOver = this.setupGameOver(this.surface(game, gameOverImage))
        var backgrounds = this.setupBackground(this.surface(game, backgroundImage))
        backgrounds.foreach(game.rootScene.addChild(_))
        game.rootScene.addChild(player)
        enemies.foreach(game.rootScene.addChild(_))
        game.rootScene.addChild(laser)
        game.rootScene.addChild(life)
        game.rootScene.addChild(kill)

        player.addEventListener("enterframe", {
          e:Event =>
            if(lifeCounter.count > 0) {
              if (timeCounter.count > 0) {
                timeCounter.decrement()
              } else {
                player.visible = true
                enemies.foreach(_.visible = true)
              }
              if (game.input.left) {
                player.x -= 10
              }
              if (game.input.right) {
                player.x += 10
              }
              if (game.input.up) {
                player.y -= 10
              }
              if (game.input.down) {
                player.y += 10
              }
              if (game.input.space) {
                laser.x = player.x
                laser.y = player.y
              }
              if (player.visible) {
                enemies.foreach( e => {
                  e.y += 8
                  if (e.y > fieldHeight) {
                    e.x = Random.nextInt(fieldWidth-32)+32
                    e.y = 0
                  }
                })

                backgrounds.foreach(b =>{
                  b.y += 4
                  if(b.y >= fieldHeight){
                    b.y = 0 - (fieldHeight+200)
                  }
                })
              }


            }

            //衝突判定
            enemies.foreach( e => {
              if (player.intersect(e)) {
                player.visible = false
                e.visible = false
                //timeCounter.increment(10)
                player.x = fieldWidth/2 - 32
                player.y = fieldHeight*3/4
                e.x = Random.nextInt(fieldWidth-32)+32
                e.y = 0 - Random.nextInt(fieldHeight)
                lifeCounter.decrement()
                life.text = lifeCounter.count.toString
              }

              if (laser.intersect(e)) {
                e.visible = false
                e.x = Random.nextInt(fieldWidth-32)+32
                e.y = 0 - Random.nextInt(fieldHeight)
                killCounter.decrement()
                kill.text = killCounter.count.toString
              }
            })

            //レーザーの挙動
            if(laser.y >= 0) {
              laser.y -= 10
            }

            //ゲームオーバー表示
            if (lifeCounter.count <= 0){
              game.rootScene.addChild(gameOver)
            }

            js.Object
        })

    }

    game.start()
  }
}

case class enemy(sprite: Sprite, movementX: (Int)=>Int, movementY: (Int)=>Int ){

}

