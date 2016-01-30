/**
  * Created by Carlos Varela on 1/25/2016.
  */


import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Alert, TextInputDialog, CheckBox, Button}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}



object HelloStageDemo extends JFXApp {
  def checkCollision(circle:Circle,  posx:Double, posy:Double): Boolean ={
    var collided:Double=Math.pow(circle.centerX.value-posx,2) + Math.pow(circle.centerY.value-posy,2)
    println(collided)
    return 0 <= collided && collided <= Math.pow(2*circle.radius.value, 2)
  }

  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600

    var graphicsManager = new GraphicManager()

    var circles = ArrayBuffer.empty[Circle]

    var dfa:DFA = new DFA()

    height = 450

    scene = new Scene {
      fill = Color.White
      content = Nil
      val chkbox = new CheckBox("Edit")
      chkbox.layoutX=520
      chkbox.layoutY = 25
      val Evaluate = new Button("Evaluate")
      Evaluate.layoutX = 180
      Evaluate.layoutY = 25
      content.add(Evaluate)
      val addTransition = new Button("Add Transition")
      addTransition.layoutX = 80
      addTransition.layoutY = 25
      content.add(addTransition)
      val SetInitial = new Button("Set Initial")
      SetInitial.layoutX = 250
      SetInitial.layoutY = 25
      content.add(SetInitial)

      SetInitial.handleEvent(MouseEvent.MouseClicked){
        a:MouseEvent=>{
          if(chkbox.selected.value) {
            var initialstate = dfa.getInitialState()
             if(initialstate!=null) {
               initialstate.startingstate = false
               circles(dfa.states.indexOf(initialstate)).fill= Color.Black

             }
            var i = 0
            for(i<-0 to dfa.states.size-1) {
              if (circles(i).fill.value.toString() == "0xa52a2aff" ) {
                    dfa.states(i).startingstate = true
                    circles(i).fill = Color.Blue
              }
            }
          }
        }
      }


      Evaluate.handleEvent(MouseEvent.MouseClicked) {
        a: MouseEvent => {
          var eval =""
          val dialog = new TextInputDialog(defaultValue = "") {
            initOwner(stage)
            title = "Enter String to Evaluate"
            contentText = "Please enter string to evaluate"
          }

          val result = dialog.showAndWait()
          result match {
            case Some(name) => eval  = name
            case None => println("Cancel")
          }
              dfa.states.last.aceptedstate = true
              var l =  dfa.Evaluate(eval,dfa.getInitialState(),0)
              println(l)
          }
      }

      addTransition.handleEvent(MouseEvent.MouseClicked) {
        a: MouseEvent => {
          var to =""
          var from =""
          val fromquestion = new TextInputDialog(defaultValue = "") {
            initOwner(stage)
            title = "Destination"
            contentText = "Please enter the name of the source state"
          }
          val fromresult = fromquestion.showAndWait()
          fromresult match {
            case Some(name) => from  = name
            case None => println("Cancel")
            }


          val dialog = new TextInputDialog(defaultValue = "") {
            initOwner(stage)
            title = "Source"
            contentText = "Please enter the name of the destionation state"
          }

          val result = dialog.showAndWait()
          result match {
            case Some(name) => to  = name
            case None => println("Cancel")
          }

          var v =""
          val value = new TextInputDialog(defaultValue = "") {
            initOwner(stage)
            title = "Value"
            contentText = "Please enter the value for the transition"
          }

          val r= value.showAndWait()
          r match {
            case Some(name) => v  =  name
            case None => println("Cancel")
          }

          var tostate:State=null
          var fromstate:State=null
          var i = 0
         var fromcircle:Circle =null
         var tocircle:Circle=null

          for(i<-0 to dfa.states.size-1){
            if(dfa.states(i).StateName == from ){
              fromcircle = circles(i)
              fromstate = dfa.states(i)
            }
            if(dfa.states(i).StateName == to){
             tocircle= circles(i)
             tostate = dfa.states(i)
            }
          }
          if(tocircle!= null && fromcircle != null ) {
            var f = content.add(graphicsManager.DrawTransition(fromcircle, tocircle))
            var t = content.add(graphicsManager.WriteName((fromcircle.centerX.value + tocircle.centerX.value) / 2, (fromcircle.centerY.value + tocircle.centerY.value) / 2, v, Color.Blue))
            var g = dfa.transitions += (new Transition(v, fromstate, tostate))
          }
        }
      }
      val removeState = new Button("Delete")
      removeState.layoutX = 25
      removeState.layoutY = 25
      content.add(chkbox)
      content.add(removeState)
      removeState.  handleEvent(MouseEvent.MouseClicked) {
        a: MouseEvent => {
          if(chkbox.selected.value) {
            var i = 0
            var erase = -1
            for(i<-0 to dfa.states.size-1){
              if(circles(i).fill.value.toString()=="0xa52a2aff") {
                  erase = i
              }
            }
            println("tucu"+erase)

            var p = dfa.states.remove(erase)
            var c = circles.remove(erase)


            content.remove(4,content.size())
            i =0
            for(i<-0 to circles.size-1){
              var h = content.add(circles(i))
              var p = content.add(graphicsManager.WriteName(circles(i).centerX.value,circles(i).centerY.value,dfa.states(i).StateName,Color.White))
            }
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
              contentText = "Please enter the name for this state:"
            }
            var collisioned = false
            val result = dialog.showAndWait()
            result match {
              case Some(name) =>println(name)

                var i = 0
                for(i<-0 to dfa.states.size-1 ){
                  if(dfa.states(i).StateName.toString() == name.toString() || checkCollision(circles(i),a.sceneX,a.sceneY)){
                    collisioned = true
                  }
                }
                if(collisioned && dfa.states.size>1){
                 val alert =  new Alert(AlertType.Error) {
                    initOwner(stage)
                    title = "Error Dialog"
                    headerText = "Error!"
                    contentText = "State already exists or state is misplaced on screen"
                  }.showAndWait()
                }else{
                  var d = dfa.states += (new State(name.toString()))
                  var p = circles += (graphicsManager.DrawCircle(a.sceneX, a.sceneY))
                  var _ = content.add(circles.last)
                  var l = content.add( graphicsManager.WriteName(a.sceneX,a.sceneY,name.toString(),Color.White  ))
                }
              case None => println("Cancel")
            }
          }

          }
        }

      }

    }

}
