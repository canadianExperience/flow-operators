package com.me.flow_operators

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class FlowViewModel: ViewModel() {

    private val _flow1 = (1..10).asFlow().onEach { delay(3000) }
    private val _flow2 = (1..10).asFlow().onEach { delay(5000) }
    private val _flow3 = (1..10).asFlow().onEach { delay(7000) }

    private val _numbers1 = mutableListOf<String>()
    val numbers1 get() = _numbers1

    private val _numbers2 = mutableListOf<String>()
    val numbers2 get() = _numbers2

    private val _numbers3 = mutableListOf<String>()
    val numbers3 get() = _numbers3

    init {
        _flow1.onEach { number1 ->
           _numbers1.add("$number1")
        }.launchIn(viewModelScope)

        _flow2.onEach { number2 ->
            _numbers2.add("$number2")
        }.launchIn(viewModelScope)

        _flow3.onEach { number3 ->
            _numbers3.add("$number3")
        }.launchIn(viewModelScope)
    }

    //Combine
    private val _combineResultFlow = combine(
        _flow1,
        _flow2,
        _flow3
    ){ number1, number2, number3 ->
        "($number1, $number2, $number3)"
    }

    //Zip
    private val _zipResultFlow = _flow1.zip(_flow2){ number1, number2 ->
        "$number1, $number2"
    }.zip(_flow3){ numbers, number3 ->
        "($numbers, $number3)"
    }

    //FlatMapLatest
    private val _flatMapResult = _flow1.flatMapLatest { number ->
        flow{
            emit( "${ number + 10 }")
        }
    }

    //Merge
    private val _mergeResult = merge(
        _flow1,
        _flow2,
        _flow3
    )

    private val _state = combine(
        _zipResultFlow,
        _combineResultFlow,
        _flatMapResult,
        _mergeResult
    ){ zipRes, combineRes, flatMapRes, mergeRes ->
        LaunchViewModelData(
            combine = combineRes,
            zip = zipRes,
            flatMap = flatMapRes,
            merge = "$mergeRes"
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LaunchViewModelData()
    )

    val state get() = _state

    data class LaunchViewModelData(
        val combine: String = "(0, 0, 0)",
        val zip: String = "(0, 0, 0)",
        val flatMap: String = "0",
        val merge: String = "0"
    )
}