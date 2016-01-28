import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.Circle

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


}
