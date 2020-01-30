data class Action(val description: String)


sealed class State {
    object Start : State()
    object Green : State()
    object Red : State()
}

sealed class Event {
    object Success : Event()
    object Fail : Event()
}


data class Transition(val toState: State, val transitionActions: List<Action>) {
    fun actions(vararg additionalActions: Action) = this.copy(transitionActions = transitionActions + additionalActions)

    companion object {
        fun to(state: State): Transition = Transition(toState = state, transitionActions = emptyList())
    }
}


object StateMachine {
    fun transition(currentState: State, event: Event): Transition {
        return when (currentState) {
            is State.Start -> {
                when (event) {
                    is Event.Success -> Transition.to(State.Green).actions(
                            Action("Turn on lamp"),
                            Action("Set colour to green"))
                    is Event.Fail -> Transition.to(State.Red).actions(
                            Action("Turn on lamp"),
                            Action("Set colour to red")
                    )
                }
            }
            is State.Green -> {
                when (event) {
                    is Event.Success -> Transition.to(State.Green)
                    is Event.Fail -> Transition.to(State.Red).actions(
                            Action("Set colour to Red")
                    )
                }
            }
            is State.Red -> {
                when (event) {
                    is Event.Success -> Transition.to(State.Green).actions(Action("Set colour to Red"))
                    is Event.Fail -> Transition.to(State.Red)
                }
            }
        }
    }
}


fun main() {
    val t = StateMachine.transition(State.Start, event = Event.Success)
    println(StateMachine.transition(t.toState, event = Event.Fail))
}



