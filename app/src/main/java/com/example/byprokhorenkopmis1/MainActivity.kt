package com.example.byprokhorenkopmis1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.byprokhorenkopmis1.data.remote.responses.Icons
import com.example.byprokhorenkopmis1.ui.theme.Byprokhorenkopmis1Theme
import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import org.koin.androidx.scope.scope
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val pokemonList = remember {
                mutableStateOf<List<PokemonCard>>(
                    listOf(
                        PokemonCard(name = "Leafeon", hp = 15, attack = 11),
                        PokemonCard(name = "Le", hp = 13, attack = 12),
                        PokemonCard(name = "Leaf", hp = 10, attack = 14),
                        PokemonCard(name = "Feon", hp = 6, attack = 19),
                    )
                )
            }

            Byprokhorenkopmis1Theme {
                val scopee = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(

                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    }

                ) { contentPadding ->
                    NavHost(navController = navController, startDestination = "screen_1") {
                        composable("screen_1") {
                            ScreenCardList({
                                navController.navigate("screen_2/{pokemonName}") {

                                }
                            }, pokemonList, snackbarHostState)
                        }
                        composable(
                            "screen_2/{pokemonName}",
                            arguments = listOf(navArgument("pokemonName") {
                                type = NavType.StringType
                            })
                        ) {
                            val pokemonName = remember {
                                it.arguments?.getString("pokemonName")
                            }
                            ScreenInfo {
                                navController.navigate("screen_1") {
                                    popUpTo("screen_1") {
                                        inclusive = true
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
    }
}


data class PokemonCard(
    val name: String, val hp: Int, val attack: Int, val id: Int = Random.nextInt()
)

@Composable
fun onSearchAndEdit(
    searchName: String,
    updatedHp: Int,
    updatedAttack: Int,
    id: Int,
    pokemonList: MutableState<List<PokemonCard>>
) {
    val list = pokemonList.value.toMutableList()
    val editPokemon = pokemonList.value.find { it.id == id }
    val id = list.indexOf(editPokemon)
    list[id] = editPokemon!!.copy(name = searchName, hp = updatedHp, attack = updatedAttack)
    pokemonList.value = list
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismiss: () -> Unit,
    attack: MutableState<Int>,
    hp: MutableState<Int>,
    name: MutableState<String>,
    onEditState: MutableState<Boolean>
) {
    val modalBottomSheetState = androidx.compose.material3.rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Коллекция карточек", fontSize = 20.sp
            )
            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name") }
            )
            TextField(
                value = hp.value.toString(),
                onValueChange = { hp.value = it.toInt() },
                label = { Text("Updated HP") }
            )
            TextField(
                value = attack.value.toString(),
                onValueChange = { attack.value = it.toInt() },
                label = { Text("Updated Attack") }
            )
            Button(
                modifier = Modifier
                    .padding(bottom = 30.dp),
                onClick = {

                    onEditState.value = true
                    onDismiss()
                },
                enabled = name.value.isNotBlank() && hp.value != 0 && attack.value != 0
            ) {
                Text(text = "Отправить")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenCardList(
    onClick: () -> Unit,
    pokemonList: MutableState<List<PokemonCard>>,
    snackbarHostState: SnackbarHostState
) {
    val showDialog = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val hp = remember { mutableStateOf("") }
    val attack = remember { mutableStateOf("") }
    val scopee = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Коллекция карточек", fontSize = 20.sp
            )
            Spacer(modifier = Modifier.width((70.dp)))
            Button(onClick = { onClick() }) {
                Text(text = "Info")
            }

        }
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn() {
                items(pokemonList.value) { pokemon ->
                    ListRow(
                        nameP = pokemon.name,
                        hpP = pokemon.hp,
                        attackP = pokemon.attack,
                        id = pokemon.id,
                        pokemonList = pokemonList
                    ) { idd ->
                        val list = pokemonList.value.toMutableList()

                        val editPokemon = pokemonList.value.find { it.id == idd }
                        val id = list.indexOf(editPokemon)
                        list.removeAt(id)
                        pokemonList.value = list
                        scopee.launch { snackbarHostState.showSnackbar("Запись удалена") }
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(Color.White)
        ) {
            Button(
                onClick = { showDialog.value = true },
                modifier = Modifier.align(Alignment.BottomCenter)

            ) {
                Text(text = "Добавить")
            }
        }
    }
    if (showDialog.value) {
        AlertDialog(onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Добавить покемона") },
            text = {
                Column {
                    TextField(value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text("Имя") })
                    TextField(value = hp.value,
                        onValueChange = { hp.value = it },
                        label = { Text("HP") })
                    TextField(value = attack.value,
                        onValueChange = { attack.value = it },
                        label = { Text("Атака") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.value.isNotBlank() && hp.value.isNotBlank() && attack.value.isNotBlank()) {
                        val newPokemon = PokemonCard(
                            name = name.value,
                            hp = hp.value.toIntOrNull() ?: 0,
                            attack = attack.value.toIntOrNull() ?: 0
                        )

                        val list = pokemonList.value.toMutableList()
                        list.add(newPokemon)
                        pokemonList.value = list

                        showDialog.value = false
                        name.value = ""
                        hp.value = ""
                        attack.value = ""
                    }
                }) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog.value = false }) {
                    Text("Отмена")
                }
            })
    }

}

@Composable
fun ListRow(
    nameP: String,
    hpP: Int,
    attackP: Int,
    id: Int,
    pokemonList: MutableState<List<PokemonCard>>,
    delite: (Int) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var onEditState = remember { mutableStateOf(false) }
    val name = remember { mutableStateOf("") }
    val hp = remember { mutableStateOf(0) }
    val attack = remember { mutableStateOf(0) }

    if (onEditState.value) {
        onSearchAndEdit(name.value, hp.value, attack.value, id, pokemonList = pokemonList)
    }


    if (showSheet) {
        BottomSheet(
            onDismiss = { showSheet = false },
            attack,
            hp,
            name,
            onEditState
        )
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable { Log.d("MyLog", "Clicked") },
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = Brush.run {
                    horizontalGradient(
                        colors = listOf(Color(246, 246, 246), Color(160, 202, 192)),

                        )
                })
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.riifia),
                    contentDescription = "TV",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(100.dp)
                        .clip(CircleShape)

                )
                Box(
                    modifier = Modifier.width(160.dp).fillMaxWidth()){
                Column(
                    modifier = Modifier.padding(5.dp)

                ) {
                    Text(text = nameP)
                    Row() {

                        Image(
                            painter = painterResource(id = R.drawable.heart),
                            contentDescription = "hp",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(text = hpP.toString())

                    }
                    Row() {

                        Image(
                            painter = painterResource(id = R.drawable.attack),
                            contentDescription = "attack",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(text = attackP.toString())
                    }


                }}
                Column {
                    Button(
                        onClick = {
                            name.value = nameP
                            hp.value = hpP
                            attack.value = attackP
                            showSheet = true
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .size(width = 80.dp, height = 32.dp)
                    ) {
                        Text(text = "edit")
                    }
                    Button(
                        onClick = {
                            delite(id)
                        },
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .size(width = 80.dp, height = 32.dp)
                    ) {
                        Text(text = "del")
                    }
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Photo() {


    val image = painterResource(id = R.drawable.riifia)

    Image(
        painter = image,
        contentDescription = null,
        modifier = Modifier
            .height(200.dp)
            .width(160.dp), // Измените размеры по желанию
        contentScale = ContentScale.Crop
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Byprokhorenkopmis1Theme {
        Greeting("Android")
    }
}