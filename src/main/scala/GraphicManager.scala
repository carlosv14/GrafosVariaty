import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.control.Label
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Line, Circle}
import scalafx.scene.text.Text
/**
  * Created by Carlos Varela on 1/26/2016.
  */
class GraphicManager {

  def DrawCircle(posx:Double, posy:Double): Circle ={


    var circle = new Circle() {
      radius = 20
      centerX = posx
      centerY = posy
      handleEvent(MouseEvent.MouseClicked) {
        a: MouseEvent => {
        if(fill.value.toString()!= "0xa52a2aff")
        fill = Color.Brown
        else
          fill = Color.Black
        }
      }
    }
    return circle
  }

  def WriteName(posx:Double, posy:Double, name:String, color:Color): Text ={
      var lbl = new Text(posx-4,posy+2,name.toString()){
      fill= color
      alignmentInParent=Pos.TopCenter
    }
    return lbl
  }


  def DrawTransition(fromcircle:Circle,tocircle:Circle):Line={

    var line= new Line(){
      stroke=Color.Black
      strokeWidth=1.0
      startX.set(fromcircle.centerX.value)
      startY.set(fromcircle.centerY.value)
      endX.set(tocircle.centerX.value)
      endY.set(tocircle.centerY.value)
    }


    return line
  }

  def DrawArrowHead(posx:Double,posy:Double): Circle ={
    var arrowHead = new Circle(){
      centerX = posx
      centerY = posy
      radius=3
      fill=Color.Gray
    }
    println("here"+posy)
    return arrowHead
  }


}
