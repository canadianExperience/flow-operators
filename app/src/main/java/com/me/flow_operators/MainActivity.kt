package com.me.flow_operators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.flow_operators.ui.theme.FlowoperatorsTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<FlowViewModel>()

        setContent {
            FlowoperatorsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //val lifecycleOwner = LocalLifecycleOwner.current
                    val state = viewModel.state
                        //.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                        .collectAsState(initial = FlowViewModel.LaunchViewModelData())

                    Greeting(
                        combineResult = state.value.combine,
                        zipResult = state.value.zip,
                        flatMapResult = state.value.flatMap,
                        merge = state.value.merge,
                        numbers1 = viewModel.numbers1.joinToString (", "),
                        numbers2 = viewModel.numbers2.joinToString (", "),
                        numbers3 = viewModel.numbers3.joinToString (", ")
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    combineResult: String,
    zipResult: String,
    flatMapResult: String,
    merge: String,
    numbers1: String,
    numbers2: String,
    numbers3: String
) {
    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxHeight()
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "FLOW OPERATORS",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)) {
            Text(
                text = "Flow1 = $numbers1",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Flow2 = $numbers2",
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Flow3 = $numbers3",
                fontWeight = FontWeight.SemiBold
            )
        }
        Column(modifier = Modifier.fillMaxHeight(),) {
            Box(modifier = Modifier.weight(1f)) {
                TableRow(
                    title = "Combine",
                    description = "Result emits when at least one flow changed",
                    values = combineResult
                )
            }

            MyHorizontalDiv()

            Box(modifier = Modifier.weight(1f)) {
                TableRow(
                    title = "Zip",
                    description = "Await all flows",
                    values = zipResult
                )
            }

            MyHorizontalDiv()

            Box(modifier = Modifier.weight(1f)) {
                TableRow(
                    title = "FlatMapLatest",
                    description = "+ 10 to last flow1 emitted number",
                    values = flatMapResult
                )
            }

            MyHorizontalDiv()

            Box(modifier = Modifier.weight(1f)) {
                TableRow(
                    title = "Merge",
                    description = "Merge flows",
                    values = merge
                )
            }
        }
    }
}

@Composable
private fun TableRow(
    title: String,
    description: String,
    values: String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ){
        Column(
            modifier = Modifier
                .padding(all = 5.dp)
                .weight(1f),
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold
            )
        }

        MyVerticalDiv()

        Column(
            modifier = Modifier
                .padding(all = 5.dp)
                .weight(1f),
        ) {
            Text(text = description)
        }

        MyVerticalDiv()

        Column(
            modifier = Modifier
                .padding(all = 5.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = values)
        }
    }
}

@Composable
private fun MyVerticalDiv(){
    Divider(
        color = Gray,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
}

@Composable
private fun MyHorizontalDiv(){
    Divider(
        color = Gray,
        modifier = Modifier
            .fillMaxWidth()
            .width(1.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FlowoperatorsTheme {
        Greeting(
            combineResult = "(0, 0, 0)",
            zipResult = "(0, 0, 0)",
            flatMapResult = "0",
            merge = "0",
            numbers1 = "",
            numbers2 = "",
            numbers3 = ""
        )
    }
}