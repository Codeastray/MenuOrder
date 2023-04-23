package com.example.menuorder.ui.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.menuorder.Navigation.NavigationDestination
import com.example.menuorder.R
import com.example.menuorder.data.Dish
import com.example.menuorder.data.Drink
import com.example.menuorder.ui.AppViewModelProvider
import com.example.menuorder.ui.theme.*
import kotlinx.coroutines.launch

object MenuTotalDestination : NavigationDestination {
    override val route = "item_details"
    override val titleRes = R.string.app_name
    const val toppingName = "toppingName"
    val routeWithArgs = "$route/{$toppingName}"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MenuTotalScreen(
    modifier: Modifier = Modifier,
    viewModel: MenuTotalViewModel = viewModel(factory = AppViewModelProvider.Factory),
    insertViewModel: MenuInsertViewModel,
    navController: NavHostController = rememberNavController(),
    navBackToMenuOrder: () -> Unit
) {


    val totalDishUiState by viewModel.totalDishUiState.collectAsState()
    val totalDrinkUiState by viewModel.totalDrinkUiState.collectAsState()
    val toppingName = viewModel.toppingName
    var showDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var total = 0
    val coroutineScope = rememberCoroutineScope()

    totalDishUiState.dishList.forEach { dishItem ->
        total += dishItem.price * dishItem.quantity
    }
    totalDrinkUiState.drinkList.forEach { drinkItem ->
        total += drinkItem.price * drinkItem.quantity
    }
    Scaffold(
        topBar = {
            MenuAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navController = navController
            )
        }
    ) { innerPadding ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(innerPadding),
            colors = CardDefaults.cardColors(TotalBackgroundCard),
            shape = RoundedCornerShape(0.dp)
        ) {
            LazyColumn {
                item {
                    Column(
                        horizontalAlignment = CenterHorizontally,
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            fontSize = 40.sp,
                            text = "已選餐點"
                        )
                        totalDishUiState.dishList.forEach { dishItem ->
                            DishTotalItem(dishItem = dishItem)
                        }
                        Divider()
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            fontSize = 40.sp,
                            text = "已選飲料"
                        )
                        totalDrinkUiState.drinkList.forEach { drinkItem ->
                            DrinkTotalItem(drinkItem = drinkItem)
                        }
                        Divider()
                        Text(
                            modifier = Modifier.padding(top = 5.dp),
                            fontSize = 40.sp,
                            text = when (toppingName) {
                                0, 1, 2 -> "已選醬料"
                                else -> "尚未選擇醬料"
                            }
                        )
                        ToppingTotalItem(toppingName = toppingName)
                        Button(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = 10.dp, bottom = 10.dp)
                                .width(250.dp)
                                .border(width = 2.dp, color = TopBar, shape = CircleShape),
                            colors = ButtonDefaults.buttonColors(Color.White),
                            shape = ButtonDefaults.outlinedShape,
                            elevation = ButtonDefaults.buttonElevation(6.dp),
                            onClick = { showDialog = true }
                        ) {
                            Text(
                                color = TopBar,
                                fontSize = 35.sp,
                                text = "結帳"
                            )
                        }
                    }
                }
            }
        }
        if (showDialog) {
            AlertDialog(
                modifier = Modifier
                    .border(
                        width = 5.dp,
                        color = TopBar,
                        shape = RoundedCornerShape(corner = CornerSize(24.dp))
                    ),
                containerColor = Color.White,
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        fontSize = 40.sp,
                        text = "確認結帳"
                    )
                },
                text = {
                    Text(
                        fontSize = 25.sp,
                        text = buildAnnotatedString {
                            append("共計")
                            withStyle(
                                style = SpanStyle(
                                    fontSize = 30.sp,
                                    color = Color.Red
                                )
                            ) {
                                append(total.toString())
                            }
                            append("元\n\n您確定要結帳嗎？")
                        }
                    )
                },
                confirmButton = {
                    Button(modifier = Modifier
                        .border(width = 2.dp, color = TopBar, shape = CircleShape),
                        colors = ButtonDefaults.buttonColors(TopBar),
                        shape = ButtonDefaults.outlinedShape,
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = {
                            // 在這裡實現結帳邏輯
                            showSuccessDialog = true
                        }
                    ) {
                        Text(
                            fontSize = 30.sp,
                            text = "確定"
                        )
                    }
                },
                dismissButton = {
                    Button(
                        modifier = Modifier
                            .border(width = 2.dp, color = TopBar, shape = CircleShape),
                        colors = ButtonDefaults.buttonColors(TopBar),
                        shape = ButtonDefaults.outlinedShape,
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(
                            fontSize = 30.sp,
                            text = "取消"
                        )
                    }
                }
            )
        }
        if (showSuccessDialog) {
            AlertDialog(
                modifier = Modifier
                    .border(
                        width = 5.dp,
                        color = TopBar,
                        shape = RoundedCornerShape(corner = CornerSize(24.dp))
                    ),
                containerColor = Color.White,
                onDismissRequest = { showSuccessDialog = false },
                title = {
                    Text(
                        modifier = Modifier.wrapContentWidth(align = CenterHorizontally),
                        fontSize = 50.sp,
                        color = PaySuccess,
                        text = "付款成功"
                    )
                },
                text = {
                    Text(
                        fontSize = 40.sp,
                        text = "用餐愉快!!"
                    )
                },
                confirmButton = {
                    Button(modifier = Modifier
                        .padding(end = 30.dp),
                        colors = ButtonDefaults.buttonColors(TopBar),
                        shape = ButtonDefaults.outlinedShape,
                        elevation = ButtonDefaults.buttonElevation(6.dp),
                        onClick = {
                            coroutineScope.launch {
                                insertViewModel.deleteAllItem()
                                insertViewModel.setResetUI(true)
                            }
                            navBackToMenuOrder()
                            insertViewModel.clearAllList()
                            insertViewModel.clearBadgeNumber()
                        }
                    ) {
                        Text(
                            fontSize = 30.sp,
                            text = "返回點餐"
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun DishTotalItem(
    dishItem: Dish
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 40.dp)
    ) {
        Surface(
            modifier = Modifier.padding(4.dp), shadowElevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            if (dishItem.quantity !== 0) {
                Column(
                    modifier = Modifier
                        .border(2.dp, TotalMealCardBoard, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(45.dp))
                        .background(TotalMealCard.copy(alpha = 0.9f))
                        .padding(vertical = 9.dp, horizontal = 9.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 5.dp,
                                bottom = 2.dp,
                                start = 10.dp,
                                end = 8.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(66.dp)
                                    .height(66.dp),
                                colors = CardDefaults.cardColors(TotalMealCard),
                                elevation = CardDefaults.cardElevation(8.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                var imageResource by remember { mutableStateOf(R.drawable.cake) }
                                imageResource = when (dishItem.name) {
                                    "巧克力蛋糕" -> R.drawable.cake
                                    "雞排咖哩飯" -> R.drawable.chicken_carry
                                    "小籠包" -> R.drawable.little_bum
                                    "宮保雞丁" -> R.drawable.palace
                                    "豬肉火鍋" -> R.drawable.pork
                                    else -> R.drawable.tiramisu
                                }
                                Image(
                                    contentScale = ContentScale.Crop,
                                    painter = painterResource(imageResource),
                                    contentDescription = "little bum",
                                )
                            }
                            Text(
                                fontSize = 20.sp,
                                text = dishItem.name
                            )
                        }
                        Icon(
                            modifier = Modifier.size(width = 40.dp, height = 40.dp),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                        Text(
                            fontSize = 26.sp,
                            text = dishItem.quantity.toString()
                        )
                        Text(
                            fontSize = 30.sp,
                            text = "份"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkTotalItem(
    drinkItem: Drink
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 40.dp)
    ) {
        Surface(
            modifier = Modifier.padding(4.dp),
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            if (drinkItem.quantity !== 0) {
                Column(
                    modifier = Modifier
                        .border(2.dp, TotalMealCardBoard, RoundedCornerShape(20.dp))
                        .clip(RoundedCornerShape(45.dp))
                        .background(TotalMealCard.copy(alpha = 0.9f))
                        .padding(vertical = 9.dp, horizontal = 9.dp)
                ) {
                    Row(
                        modifier = if (drinkItem.quantity !== 0) Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 5.dp,
                                bottom = 2.dp,
                                start = 10.dp,
                                end = 8.dp
                            ) else Modifier.size(0.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Card(
                                modifier = Modifier
                                    .width(66.dp)
                                    .height(66.dp),
                                colors = CardDefaults.cardColors(TotalMealCard),
                                elevation = CardDefaults.cardElevation(8.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                var imageResource by remember { mutableStateOf(R.drawable.cake) }
                                imageResource = when (drinkItem.name) {
                                    "柳橙汁" -> R.drawable.orange
                                    "可樂" -> R.drawable.coke
                                    "可爾必思" -> R.drawable.karu
                                    "紅茶" -> R.drawable.black_tea
                                    "水果茶" -> R.drawable.fruit
                                    else -> R.drawable.milk_tea
                                }
                                Image(
                                    contentScale = ContentScale.Crop,
                                    painter = painterResource(imageResource),
                                    contentDescription = "little bum",
                                )
                            }
                            Text(
                                fontSize = 20.sp,
                                text = drinkItem.name
                            )
                        }
                        Icon(
                            modifier = Modifier.size(width = 40.dp, height = 40.dp),
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close"
                        )
                        Text(
                            fontSize = 26.sp,
                            text = drinkItem.quantity.toString()
                        )
                        Text(
                            fontSize = 30.sp,
                            text = "杯"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ToppingTotalItem(
    toppingName: Int
) {
    Box(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 40.dp)
    ) {
        Surface(
            modifier = Modifier.padding(4.dp),
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .border(2.dp, TotalMealCardBoard, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(45.dp))
                    .background(TotalMealCard.copy(alpha = 0.9f))
                    .padding(vertical = 9.dp, horizontal = 9.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = when (toppingName) {
                                0, 1, 2 -> 5.dp
                                else -> 10.dp
                            },
                            bottom = 2.dp,
                            start = 10.dp,
                            end = 8.dp
                        ),
                    horizontalAlignment = CenterHorizontally,
                ) {
                    Card(
                        modifier = when (toppingName) {
                            0, 1, 2 -> Modifier
                                .width(66.dp)
                                .height(66.dp)
                            else -> Modifier
                                .width(160.dp)
                                .height(70.dp)
                        },
                        colors = CardDefaults.cardColors(TotalMealCard.copy(alpha = 0.1f)),
                        elevation = when (toppingName) {
                            0, 1, 2 -> CardDefaults.cardElevation(8.dp)
                            else -> CardDefaults.cardElevation(0.dp)
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        var imageResource by remember { mutableStateOf(R.drawable.small_dish) }
                        imageResource = when (toppingName) {
                            0 -> R.drawable.thousand
                            1 -> R.drawable.tea_souce
                            2 -> R.drawable.soy_paste
                            else -> R.drawable.small_dish
                        }
                        Image(
                            contentScale = ContentScale.Crop,
                            painter = painterResource(imageResource),
                            contentDescription = "little bum",
                        )
                    }
                    Text(
                        fontSize = when (toppingName) {
                            0, 1, 2 -> 20.sp
                            else -> 0.sp
                        },
                        text = when (toppingName) {
                            0 -> "千島醬"
                            1 -> "沙茶醬"
                            2 -> "醬油膏"
                            else -> ""
                        },
                        textAlign = TextAlign.Center
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MenuOrderAppPreview() {
}