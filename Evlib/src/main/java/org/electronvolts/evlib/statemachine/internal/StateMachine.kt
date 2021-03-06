package org.electronvolts.evlib.statemachine.internal

private typealias StateMap<T> = Map<T, State<T>>

class StateNotFoundError(s: StateName) : RuntimeException("Could not find state ${s.name}")

class StateMachine<T : StateName>(
    private val stateMap: StateMap<T>,
    firstStateName: T,
    states: Array<out T>,
) {

    var currName: StateName = firstStateName
        private set
    private var curr: State<T>

    init {
        // load the first state
        when (val state = stateMap[firstStateName]) {
            null -> throw StateNotFoundError(firstStateName)
            else -> curr = state
        }

        // check for null state on all names pre-emptively
        for (name in states) {
            stateMap[name] ?: throw StateNotFoundError(name)
        }
    }

    fun act() {
        when (val next = curr.act()) {
            currName -> return
            null -> return
            else -> {
                currName = next
                curr = stateMap[next]!!
            }
        }
    }

}