package com.mohammad.hazem.smart_software.pom_game

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohammad.hazem.smart_software.pom_game.ui.theme.PomGameTheme
import androidx.compose.runtime.MutableState as MutableState1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    GameNavController()

                }//end Surface
            }//end PomGameTheme
        }//end setContent
    }//end onCreate
}//end MainActivity

@Composable
fun SplashScreen(navCtrl : NavController){

    Column(modifier = Modifier.fillMaxSize()) {

        Image(painter = painterResource(id = R.drawable.banana_game_logo), contentDescription ="banana logo" , modifier = Modifier.fillMaxSize())

    }

    val count = object : CountDownTimer(1000 ,100){
        override fun onTick(p0: Long) {

        }

        override fun onFinish() {
            navCtrl.navigate("main")
        }
    }

    count.start()

}

@Composable
fun MainScreen(navCtrl : NavController) {

    val con = LocalContext.current
    val dialogOpining = remember {
        mutableStateOf(false)
    }

    navCtrl.enableOnBackPressed(false)

    Column(modifier = Modifier.fillMaxSize()) {

        val defVStr: MutableState1<String> = remember {

            mutableStateOf("${100}")

        }

        Image(painter = painterResource(id = R.drawable.ic_icon_foreground),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
                    .padding(2.dp))

        Spacer(modifier = Modifier.height(90.dp))

        Row {

            val isEnabled: MutableState1<Boolean> = remember {

                mutableStateOf(false)

            }

            FloatingActionButton(
                onClick = {

                    isEnabled.value = isEnabled.value != true

                },
                shape = Shapes.Full
            ) {

                Text(text = "Limit")

            }//end FloatingActionButton


            OutlinedTextField(
                value = defVStr.value,
                onValueChange = {v -> defVStr.value = v.trim()},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number , imeAction = ImeAction.Done) , enabled = isEnabled.value)
        }//end Row

        Spacer(modifier = Modifier.height(20.dp))

        FloatingActionButton(
            onClick = {

                  dialogOpining.value= true

            },
            shape = Shapes.Full
        ) {

            Text(text = "?")

        }//end FloatingActionButton

        Spacer(modifier = Modifier.height(20.dp))

        FloatingActionButton(onClick = {

            if(defVStr.value!="" && defVStr.value.trim().toInt()>=10 && defVStr.value.trim().toInt()<=2000000000) {

                navCtrl.navigate("game")

                navCtrl.currentBackStackEntry!!.savedStateHandle["limit"] = defVStr.value.trim().toInt()
            }else{

                navCtrl.navigate("game")

                navCtrl.currentBackStackEntry!!.savedStateHandle["limit"] = 100

                Toast.makeText(con ,"you entered invalid limit" ,Toast.LENGTH_SHORT).show()

            }
        },
                                shape = Shapes.Full) {

            Icon(painter = painterResource(id = R.drawable.ic_baseline_play_arrow_24), contentDescription = "Play")

        }//end FloatingActionButton

    }//end Column

    if(dialogOpining.value) {

        AlertDialog(
            onDismissRequest = { dialogOpining.value = false },
            properties = DialogProperties(dismissOnBackPress = true ,dismissOnClickOutside = true),
            title = { Text(text = "How To Play") },
            shape = RoundedCornerShape(20.dp),
            confirmButton = { Button(onClick = {dialogOpining.value = false }) {
                Text(text = "Ok")
            }},
            text = {
                Text(
                    text = "If number witch next to showed is divisible by 5 you must click \"pom\" button \n else you must click \"+\" button"
                    , textAlign = TextAlign.Justify)
            })
    }

}//end MainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navCtrl : NavController ,gLimit: Int) {

    val limit = remember {

        mutableStateOf(gLimit)

    }

    val con = LocalContext.current

    val counter : MutableState1<Int> = remember {
        mutableStateOf(1)
    }

    Column(modifier = Modifier.fillMaxSize() , horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.height(20.dp))

        Card(shape = RoundedCornerShape(1000.dp)) {


            Text(
                text = "${counter.value}",
                modifier = Modifier
                    .padding(60.dp),
                textAlign = TextAlign.Center,
                fontSize = 25.sp
            )
        }
        Spacer(modifier = Modifier.height(230.dp))

        Row {

            Spacer(modifier = Modifier.width(2.dp))

            ElevatedButton(onClick = {

                val next = counter.value+1

                if(next%5==0) {

                    Toast.makeText(con ,"You loosed in number ${counter.value}" ,Toast.LENGTH_SHORT).show()
                    navCtrl.navigate("main")

                }else {
                    counter.value += 1

                    if(counter.value==limit.value){

                        Toast.makeText(con ,"You won" ,Toast.LENGTH_LONG).show()
                        navCtrl.navigate("main"){

                            popUpTo("game")

                        }

                    }//end if
                }//end else

            } ,
                Modifier
                    .width(90.dp)
                    .height(90.dp)) {

                Text(text = "+")

            }//end ElevatedButton

            Spacer(modifier = Modifier.width(155.dp))


            ElevatedButton(onClick = {

                val next = counter.value+1

                if(next%5!=0) {

                    Toast.makeText(con ,"You loosed in number ${counter.value}" ,Toast.LENGTH_SHORT).show()
                    navCtrl.navigate("main"){

                        popUpTo("game")

                    }

                }else{
                    counter.value += 1

                    if(counter.value==limit.value){

                        Toast.makeText(con ,"You won" ,Toast.LENGTH_LONG).show()
                        navCtrl.navigate("main"){

                            popUpTo("game")

                        }

                    }//end if
                }//end else

            } ,
                Modifier
                    .width(90.dp)
                    .height(90.dp)) {

                Text(text = "pom")

            }//end ElevatedButton

            Spacer(modifier = Modifier.width(2.dp))

        }//end Row

        ElevatedButton(onClick ={navCtrl.navigate("main")} ,
            Modifier
                .width(90.dp)
                .height(90.dp)) {

            Text(text = "end game" , fontSize = 12.sp , textAlign = TextAlign.Justify)

        }//end ElevatedButton

    }//end Column

}//end GameScreen

@Composable
fun GameNavController(){

    val navCtrl = rememberNavController()

    NavHost(navController = navCtrl, startDestination = "splash"){

        composable("splash"){

            SplashScreen(navCtrl)

        }//end composable

        composable("main"){

            MainScreen(navCtrl)

        }//end composable

        composable("game"){


            navCtrl.currentBackStackEntry!!.savedStateHandle.get<Int>("limit")
                ?.let { limit -> GameScreen(navCtrl , limit) }

        }//end composable

    }//end NavHost

}//end GameNavController