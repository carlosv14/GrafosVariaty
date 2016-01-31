/**
  * Created by Carlos Varela on 1/25/2016.
  */

import javafx.scene.control.{Menu, MenuBar}
import javafx.scene.text.Text

import scala.collection.mutable.ArrayBuffer
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.geometry.{Pos, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Circle, Rectangle}



object MainPage extends JFXApp {
  def checkCollision(circle:Circle,  posx:Double, posy:Double): Boolean ={
    val collided:Double=Math.pow(circle.centerX.value-posx,2) + Math.pow(circle.centerY.value-posy,2)
    println(collided)
    return 0 <= collided && collided <= Math.pow(2*circle.radius.value, 2)
  }



  stage = new JFXApp.PrimaryStage {
    title.value = "Graph Graficator"


    var graphicsManager = new GraphicManager()

    var circles = ArrayBuffer.empty[Circle]
    var labels = ArrayBuffer.empty[Label]

    scene = new Scene(800,600) {
      val menuBar = new MenuBar
      val chkbox = new CheckBox("Edit")
      val addTransition = new Button("Add Transition")
      val deleteTrans= new Button("Delete Transition")
      val removeState = new Button("Delete State")
      val fileMenu = new Menu("File")
      val openItem = new MenuItem("Open")
      val saveItem = new MenuItem("Save")
      val clearScreen = new MenuItem("Clear")
      fileMenu.items = List(openItem, saveItem, new SeparatorMenuItem, clearScreen)
      val optionsMenu = new Menu("Options")
      val run = new MenuItem("Run Autamaton")
      optionsMenu.items = List(run)
      val radioMenu = new Menu("Automatons")
      val dfaOption = new RadioMenuItem("DFA")
      val nfaOption = new RadioMenuItem("NFA")
      val group = new ToggleGroup()
      group.toggles =  List(dfaOption,nfaOption)
      radioMenu.items = List(dfaOption,nfaOption)
      menuBar.menus = List(fileMenu,optionsMenu,radioMenu)
      menuBar.setPrefWidth(800)

      val setInitial = new MenuItem("Set Initial")
      val setAccepted = new MenuItem("Set/Unset Accepted")
      val contextMenu = new ContextMenu(setInitial,setAccepted )

      val toolbar = new ToolBar()
      toolbar.setPrefWidth(800)
      toolbar.layoutY = 25
      toolbar.items = List(addTransition,deleteTrans,removeState, chkbox)
      content.add(menuBar)
      content.add(toolbar)

      def deleteEverything(dfa:DFA){
        content.remove(2, content.size())
        dfa.states.remove(0,dfa.states.size)
        dfa.transitions.remove(0,dfa.transitions.size)
        circles.remove(0,circles.size)
        labels.remove(0,labels.size)
        println(content.size() + " " + dfa.states.size + " " + dfa.transitions.size + " " + circles.size+ " " + labels.size)
      }

      def programLogic(dfa:DFA,posx:Double,posy:Double) {
        if (!chkbox.selected.value) {
          val dialog = new TextInputDialog(defaultValue = "") {
            initOwner(stage)
            title = "State Name"
            contentText = "Please enter the name for this state:"
          }
          var collisioned = false
          val result = dialog.showAndWait()
          result match {
            case Some(name) => println(name)

              var i = 0
              for (i <- 0 to dfa.states.size - 1) {
                if (dfa.states(i).StateName.toString() == name.toString() || checkCollision(circles(i), posx, posy)) {
                  collisioned = true
                }
              }
              if ((collisioned && dfa.states.size > 1) || posy <= 70) {
                val alert = new Alert(AlertType.Error) {
                  initOwner(stage)
                  title = "Error Dialog"
                  headerText = "Error!"
                  contentText = "State already exists or state is misplaced on screen"
                }.showAndWait()
              } else {
                dfa.states += (new State(name.toString()))
                circles += (graphicsManager.DrawCircle(posx, posy))
                content.add(circles.last)

                labels += (new Label(name.toString()))
                labels.last.layoutX = posx - 4
                labels.last.layoutY = posy - 10
                labels.last.textFill = Color.White
                labels.last.alignmentInParent = Pos.TopCenter
                labels.last.contextMenu = contextMenu
                content.add(labels.last)
                // var l = content.add(graphicsManager.WriteName(posx, posy, name.toString(), Color.White))
              }
            case None => println("Cancel")
          }
        }

          setInitial.onAction = (e: ActionEvent) => {
            if (chkbox.selected.value) {

              var initialstate = dfa.getInitialState()
              if (initialstate != null) {
                initialstate.startingstate = false
                circles(dfa.states.indexOf(initialstate)).fill = Color.Black

              }
              var i = 0
              for (i <- 0 to dfa.states.size - 1) {
                if (dfa.states(i).aceptedstate) {
                  circles(i).fill = Color.Blue
                  circles(i).stroke = Color.Green
                }
                if (circles(i).fill.value.toString() == "0xa52a2aff" ) {
                  dfa.states(i).startingstate = true
                  circles(i).fill = Color.Blue
                }
              }

            }
          }

          setAccepted.onAction = (e: ActionEvent) => {
            if(chkbox.selected.value) {
              var i = 0
              for(i<-0 to dfa.states.size-1) {
                if(circles(i).fill.value.toString() == "0xa52a2aff" && dfa.states(i).startingstate) {
                  circles(i).fill =Color.Blue
                  circles(i).stroke = Color.Green
                  dfa.states(i).aceptedstate = true
                }
                if (circles(i).fill.value.toString() == "0xa52a2aff" && !dfa.states(i).aceptedstate) {
                  dfa.states(i).aceptedstate = true
                  circles(i).fill = Color.Green
                }else if(circles(i).fill.value.toString() == "0xa52a2aff" && dfa.states(i).aceptedstate){
                  dfa.states(i).aceptedstate = false
                  circles(i).fill = Color.Black
                  println(Color.Blue)
                }
              }
            }


          }

          addTransition.onAction = (e: ActionEvent) => {
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
                  if(fromcircle!=tocircle) {
                    content.add(graphicsManager.DrawTransition(fromcircle, tocircle))
                    content.add(graphicsManager.WriteName((fromcircle.centerX.value + tocircle.centerX.value) / 2, (fromcircle.centerY.value + tocircle.centerY.value) / 2, v, Color.Blue))
                  }else{
                    var circle = new Circle() {
                      radius = 20
                      centerX = fromcircle.centerX.value
                      centerY = fromcircle.centerY.value-35
                      fill = Color.White
                      stroke = Color.Black
                      strokeWidth=1.0
                    }
                    content.add(circle)
                    content.add(graphicsManager.WriteName(fromcircle.centerX.value, fromcircle.centerY.value-35, v, Color.Blue))
                  }

                  dfa.transitions += (new Transition(v, fromstate, tostate))
                }


              case None => println("Cancel")
            }

            println(dfa.transitions.size)


          }

          deleteTrans.onAction = (e: ActionEvent) => {
            if (chkbox.selected.value) {
              var to = ""
              var from = ""
              val fromquestion = new TextInputDialog(defaultValue = "") {
                initOwner(stage)
                title = "Destination"
                contentText = "Please enter the name of the source state"
              }
              val fromresult = fromquestion.showAndWait()
              fromresult match {
                case Some(name) => from = name
                case None => println("Cancel")
              }


              val dialog = new TextInputDialog(defaultValue = "") {
                initOwner(stage)
                title = "Source"
                contentText = "Please enter the name of the destionation state"
              }

              val result = dialog.showAndWait()
              result match {
                case Some(name) => to = name
                case None => println("Cancel")
              }

              var v = ""
              val value = new TextInputDialog(defaultValue = "") {
                initOwner(stage)
                title = "Value"
                contentText = "Please enter the value for the transition"
              }

              val r = value.showAndWait()
              r match {
                case Some(name) => v = name

                  var tostate: State = null
                  var fromstate: State = null
                  var i = 0
                  for (i <- 0 to dfa.states.size - 1) {
                    if (dfa.states(i).StateName == from) {
                      fromstate = dfa.states(i)
                    }
                    if (dfa.states(i).StateName == to) {
                      tostate = dfa.states(i)
                    }
                  }
                  i = 0
                  while (i < dfa.transitions.size) {
                    if (dfa.transitions(i).fromstate == fromstate && dfa.transitions(i).tostate == tostate && dfa.transitions(i).transvalue == v) {
                      dfa.transitions.remove(i)
                      i -= 1
                    }
                    i += 1
                  }



                  println("sizeeee " + dfa.transitions.size)
                  content.remove(2, content.size())

                  i = 0

                  var fromcircle: Circle = null
                  var tocircle: Circle = null
                  for (i <- 0 to dfa.transitions.size - 1) {
                    fromcircle = circles(dfa.states.indexOf(dfa.transitions(i).fromstate))
                    tocircle = circles(dfa.states.indexOf(dfa.transitions(i).tostate))
                    var f = content.add(graphicsManager.DrawTransition(fromcircle, tocircle))
                    var t = content.add(graphicsManager.WriteName((fromcircle.centerX.value + tocircle.centerX.value) / 2, (fromcircle.centerY.value + tocircle.centerY.value) / 2, dfa.transitions(i).transvalue, Color.Blue))
                  }

                  i = 0
                  for (i <- 0 to circles.size - 1) {
                    var h = content.add(circles(i))
                    var p = content.add(graphicsManager.WriteName(circles(i).centerX.value, circles(i).centerY.value, dfa.states(i).StateName, Color.White))
                    var l = content.add(labels(i))

                  }


                case None => println("Cancel")
              }
              println(dfa.transitions.size)
            }
          }

        removeState.onAction = (e: ActionEvent) => {
          if (chkbox.selected.value) {
            var i = 0
            var erasedstatename = ""
            var erase = -1
            for(i<-0 to dfa.states.size-1){
              if(circles(i).fill.value.toString()=="0xa52a2aff") {
                erase = i
              }
            }


            erasedstatename = dfa.states(erase).StateName
            var p = dfa.states.remove(erase)
            var c = circles.remove(erase)
            var j = labels.remove(erase)
            i= 0

            while(i<dfa.transitions.size){
              if(dfa.transitions(i).fromstate.StateName == erasedstatename || dfa.transitions(i).tostate.StateName == erasedstatename ){
                dfa.transitions.remove(i)
                i-=1
              }
              i+=1
            }
            println("sizeeee " + dfa.transitions.size)
            content.remove(2,content.size())

            i=0

            var fromcircle:Circle = null
            var tocircle:Circle = null
            for(i<-0 to dfa.transitions.size-1){
              fromcircle = circles(dfa.states.indexOf(dfa.transitions(i).fromstate))
              tocircle = circles(dfa.states.indexOf(dfa.transitions(i).tostate))
              var f = content.add(graphicsManager.DrawTransition(fromcircle, tocircle))
              var t = content.add(graphicsManager.WriteName((fromcircle.centerX.value + tocircle.centerX.value) / 2, (fromcircle.centerY.value + tocircle.centerY.value) / 2, dfa.transitions(i).transvalue, Color.Blue))
            }

            i =0
            for(i<-0 to circles.size-1){
              content.add(circles(i))
              content.add(graphicsManager.WriteName(circles(i).centerX.value,circles(i).centerY.value,dfa.states(i).StateName,Color.White))
              content.add(labels(i))
            }
          }
        }

        run.onAction = (e: ActionEvent) => {
          if (!chkbox.selected.value) {
            var eval = ""
            val dialog = new TextInputDialog(defaultValue = "") {
              initOwner(stage)
              title = "Enter String to Evaluate"
              contentText = "Please enter string to evaluate"
            }

            val result = dialog.showAndWait()
            result match {
              case Some(name) => eval = name

                var l = dfa.Evaluate(eval, dfa.getInitialState(), 0)
                if (l == false) {
                  val alert = new Alert(AlertType.Error) {
                    initOwner(stage)
                    title = "Error"
                    headerText = "Error!"
                    contentText = "The String is not accepted"
                  }.showAndWait()
                } else {
                  val alert = new Alert(AlertType.Information) {
                    initOwner(stage)
                    title = "Success"
                    headerText = "Success!"
                    contentText = "The String is accepted"
                  }.showAndWait()
                }

              case None => println("Cancel")
            }
          }
        }

        clearScreen.onAction= (e: ActionEvent) => {
          deleteEverything(dfa)

          }

        }



      dfaOption.onAction = (e:ActionEvent) => {
        var dfa: DFA = new DFA()
        handleEvent(MouseEvent.MouseClicked) {
          a: MouseEvent => {
            programLogic(dfa,a.sceneX,a.sceneY)

          }
        }
      }






    }
  }
}
