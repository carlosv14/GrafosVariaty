import java.io.{BufferedWriter, FileWriter, PrintWriter, File}

import scala.collection.mutable.ArrayBuffer
import scalafx.scene.shape.Circle

/**
  * Created by Carlos Varela on 1/31/2016.
  */
class FileParser(rfile:File) {
  var file:File = rfile
  val readInfo = scala.io.Source.fromFile(file).getLines.toBuffer

  def getStates(): Array[String] ={
    var states = readInfo(1).split(":")(1).split(",")
    return states
  }
  def getType(): String ={
    return readInfo(0).split(":")(1)
  }

  def getTransitions(): Array[String] ={
    if(readInfo.size>2) {
      var trans = readInfo(2).split(":")(1).split(",")
      return trans
    }
    return null
  }

  def writeFile(states:ArrayBuffer[State],transitions:ArrayBuffer[Transition],t:String,circles:ArrayBuffer[Circle]) {
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("type:"+t)
    bw.newLine()
    bw.write("states:")
    var i = 0
    for(i<-0 to circles.size-1 ){
      bw.write(states(i).StateName + "-" + circles(i).centerX.value + "-" + circles(i).centerY.value + "-")
      if(states(i).aceptedstate)
        bw.write("isAccepted")
      else if(states(i).startingstate)
        bw.write("isInitial")
      else
        bw.write("None")
      if(i<=circles.size-2)
      bw.write(",")
    }
    bw.newLine()
    i=0
    bw.write("transitions:")
    for(trans<-transitions){
      bw.write(trans.fromstate.StateName+"-"+trans.tostate.StateName +"-" + trans.transvalue)
      if(i<=transitions.size-2)
        bw.write(",")
      i+=1
    }

    bw.close()
  }

}
