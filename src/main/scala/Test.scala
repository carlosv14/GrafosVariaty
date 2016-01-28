/**
  * Created by Carlos Varela on 1/25/2016.
  */

import java.awt.Checkbox



import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.{TextInputDialog, CheckBox, Button}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}




object HelloStageDemo extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600




    var graphicsManager = new GraphicManager()
    var states= ArrayBuffer.empty[State]
    var circles = ArrayBuffer.empty[Circle]

    height = 450

    scene = new Scene {
      fill = Color.White
      content = Nil
      val chkbox = new CheckBox("Edit")
      chkbox.layoutX=520
      chkbox.layoutY = 25
      val button = new Button("Delete")
      button.layoutX = 25
      button.layoutY = 25
      content.add(chkbox)
      content.add(button)
      button.  handleEvent(MouseEvent.MouseClicked) {
        a: MouseEvent => {
          if(chkbox.selected.value) {
            var i = 0
            var erase = -1
            for(i<-0 to states.size-1){
              if(circles(i).fill.value.toString()=="0xa52a2aff") {
                  erase = i
              }
            }
            var p = states.remove(erase)
            var l = content.remove(erase+2)
            var c = circles.remove(erase)
          }
        }
      }

      handleEvent(MouseEvent.MouseClicked){
        a: MouseEvent => {
          println("Mouse pos:" + a.sceneX+ "  " + a.sceneY)

          if(!chkbox.selected.value) {
            val dialog = new TextInputDialog(defaultValue = "") {
              initOwner(stage)
              title = "State Name"
              contentText = "Please enter State name:"
            }
            val result = dialog.showAndWait()
            result match {
              case Some(name) =>println(name)
                var d = states += (new State(name.toString()))
                var p = circles += (graphicsManager.DrawCircle(a.sceneX, a.sceneY))
                var _ = content.add(circles.last)
              case None => println("Cancel")
            }
          }

          }
        }

      }

    }

}
