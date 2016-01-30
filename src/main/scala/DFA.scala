import scala.collection.mutable.ArrayBuffer

/**
  * Created by Carlos Varela on 1/27/2016.
  */
class DFA {
  var states = ArrayBuffer.empty[State]
  var transitions = ArrayBuffer.empty[Transition]

  def getInitialState(): State = {
    var i = 0
    for (i <- 0 to states.size - 1) {
      if (states(i).startingstate) {
        return states(i)
      }
    }
    return null
  }

  def Evaluate(word: String, state: State, beginpos: Int): Boolean = {
   var valid:Boolean = false
    if(state.aceptedstate)
        valid = true
    if(state==null)
        valid= false
    var i = 0
    for (i <- 0 to transitions.size - 1) {
      if (transitions(i).transvalue == word.drop(beginpos).take(1) && transitions(i).fromstate.StateName == state.StateName) {
        println(word.drop(beginpos).take(1))
        println(transitions(i).transvalue)
        println(transitions(i).tostate.StateName)
        valid = true
        return Evaluate(word, transitions(i).tostate, beginpos + 1)
      }
    }
    return valid
  }


}
