package com.example.menuorder.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.flow.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.Badge
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.menuorder.data.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.menuorder.Navigation.NavigationDestination
import com.example.menuorder.R
import com.example.menuorder.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*

object MenuDestination : NavigationDestination {
    override val route = "Menu"
    override val titleRes = R.string.app_name
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOrderApp(
    navigateToTotal: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuInsertViewModel,
    navController: NavHostController = rememberNavController()
) {
//    val backStackEntry by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            MenuAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navController = navController
            )
        }
    ) { innerPadding ->
        MenuOrderList(
            menuUiState = viewModel.menuUiState,
            onSendClick = {
                coroutineScope.launch {
                    viewModel.saveItem()
                }
            },
            uiStateUpdate = viewModel::updateUiState,
            deleteUiState = viewModel::deleteUiState,
            mealList = MenuData().loadMeals(),
            drinkList = MenuData().loadDrinks(),
            toppingList = MenuData().loadToppings(),
            context = context,
            navigateToTotal = navigateToTotal,
            modifier = modifier.padding(innerPadding),
            viewModel = viewModel,
            navController = navController,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuAppBar(
    canNavigateBack: Boolean,
    navController: NavHostController
) {
    TopAppBar(
        title = {
            Text(
                if (canNavigateBack) "結帳區" else "點餐區",
                fontSize = 40.sp,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(TopBar),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        }
    )
}

@Composable
fun MenuOrderList(
    menuUiState: MenuUiState,
    onSendClick: () -> Unit,
    uiStateUpdate: (MenuUiState) -> Unit,
    deleteUiState: (String) -> Unit,
    mealList: List<Meals>,
    drinkList: List<Drinks>,
    toppingList: List<Toppings>,
    context: Context,
    navigateToTotal: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuInsertViewModel,
    navController: NavHostController,
) {
    var selectedTopping by remember { mutableStateOf(4) }
//    val badgeNumbers = remember { mutableStateMapOf<Int, Int>() }
    var noOrder by remember { mutableStateOf(false) }
    val badgeNumber = viewModel.badgeNumber.collectAsState().value
    val badgeNumberKeep by remember { mutableStateOf(badgeNumber) }
    Spacer(modifier = Modifier.height(2.dp))
    LazyVerticalGrid(
        GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier,
    ) {
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(R.string.meals),
                fontSize = 30.sp
            )
        }
        items(mealList, key = { it.id }) { meal ->
            MealOrderCard(
                meal,
                menuUiState = menuUiState,
                uiStateUpdate,
                deleteUiState = deleteUiState,
                context = context,
                viewModel = viewModel,
                navController = navController,
            )
        }
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(R.string.drinks),
                fontSize = 30.sp
            )
        }
        items(drinkList) { drink ->
            DrinkOrderCard(
                drink,
                menuUiState = menuUiState,
                uiStateUpdate,
                deleteUiState = deleteUiState,
                context = context,
                viewModel = viewModel
            )
        }
        item(
            span = {
                GridItemSpan(maxLineSpan)
            }
        ) {
            Text(
                style = MaterialTheme.typography.bodySmall,
                text = stringResource(R.string.toppings),
                fontSize = 30.sp
            )
        }
        items(toppingList) { topping ->
            ToppingOrderCard(
                topping,
                selected = selectedTopping == topping.id,
                onClick = {
                    selectedTopping = topping.id
                    viewModel.dishCard(99, selectedTopping)
                },
                viewModel = viewModel,
                selectedTopping = selectedTopping
            )
        }
        item(
            span = {
                GridItemSpan(20)
            }
        ) {
            Box(
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 10.dp, bottom = 10.dp)
                        .width(250.dp)
                        .align(Center)
                        .border(width = 2.dp, color = ButtonColor, shape = CircleShape),
                    colors = ButtonDefaults.buttonColors(Color.White),
                    shape = ButtonDefaults.outlinedShape,
                    elevation = ButtonDefaults.buttonElevation(6.dp),
                    onClick = {
                        val hasZeroQuantity =
                            viewModel.dishesList.all { it.dish_quantity == 0 } || viewModel.drinksList.all { it.drink_quantity == 0 }
                        if (hasZeroQuantity) {
                            noOrder = true
                        } else {
                            onSendClick()
                            navigateToTotal(badgeNumberKeep[99]!!)
                        }
                    },
                )
                {
                    Text(
                        color = ButtonColor,
                        fontSize = 30.sp,
                        text = "點餐送出"
                    )
                }
            }
        }
    }
    if (noOrder) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = false),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            containerColor = Color.White,
            onDismissRequest = { noOrder = false },
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 40.sp,
                    color = OrderWarning,
                    text = if (viewModel.dishesList.all { it.dish_quantity == 0 }) "尚未點餐" else "尚未點飲料"
                )
            },
            confirmButton = {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(OrderWarning),
                    shape = ButtonDefaults.outlinedShape,
                    elevation = ButtonDefaults.buttonElevation(6.dp),
                    onClick = {
                        noOrder = false
                    }
                ) {
                    Text(
                        fontSize = 30.sp,
                        text = "返回點餐"
                    )
                }
            },
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealOrderCard(
    meal: Meals,
    menuUiState: MenuUiState,
    uiStateUpdate: (MenuUiState) -> Unit,
    deleteUiState: (String) -> Unit,
    context: Context,
    viewModel: MenuInsertViewModel,
    navController: NavHostController,
) {
    val coroutineScope = rememberCoroutineScope()
    var badgeNumber by remember { mutableStateOf(0) }
    val badgeNumberKeep by viewModel.badgeNumber.collectAsState()
    badgeNumber = badgeNumberKeep.getOrPut(meal.hashCode()) { 0 }
    Box(
        modifier = Modifier
            .size(height = 180.dp, width = 180.dp)
            .zIndex(1f),
        Alignment.BottomEnd,
    ) {
        var imageResource by remember { mutableStateOf(R.drawable.cake) }
        imageResource = when (stringResource(meal.meal)) {
            "巧克力蛋糕" -> R.drawable.two_hunderd
            "雞排咖哩飯" -> R.drawable.seventy_five
            "小籠包" -> R.drawable.sixty
            "宮保雞丁" -> R.drawable.ninety_five
            "豬肉火鍋" -> R.drawable.one_hundred
            else -> R.drawable.eighty
        }
        Image(
            modifier = Modifier
                .size(82.dp)
                .offset(x = 32.dp, y = (-75).dp),
            painter = painterResource(imageResource),
            contentDescription = "80"
        )
        if (badgeNumber > 0) {
            Badge(
                modifier = Modifier.size(40.dp),
            ) {
                Text(
                    badgeNumber.toString(),
                    modifier = Modifier.semantics {
                        contentDescription = " new notifications"
                    },
                    fontSize = 27.sp,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f)
                    .size(height = 180.dp, width = 180.dp),
            ){
                IconButton(
                    onClick = {
                        badgeNumber -= 1
                        viewModel.dishCard(meal.hashCode(), badgeNumber)
                        deleteUiState(
                            context.getString(meal.meal)
                        )
                    },
                    modifier = Modifier
                        .size(75.dp)
                        .offset(x = (-15).dp, y = 112.dp),

                ) {
                    Icon(
                        modifier = Modifier.size(75.dp),

                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Close"
                    )
                    Icon(
                        modifier = Modifier.size(72.dp),
                        tint = TotalMealDelete,
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Close"
                    )
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .size(height = 180.dp, width = 180.dp)
            .padding(9.dp)
            .clickable {
                badgeNumber += 1
                viewModel.dishCard(meal.hashCode(), badgeNumber)
                val payPrice = when (context.getString(meal.meal)) {
                    "巧克力蛋糕" -> "200"
                    "雞排咖哩飯" -> "75"
                    "小籠包" -> "60"
                    "宮保雞丁" -> "95"
                    "豬肉火鍋" -> "120"
                    else -> "80"
                }
                uiStateUpdate(
                    menuUiState.copy(
                        dish_name = context.getString(meal.meal),
                        dish_price = payPrice,
                        dish_quantity = 1,
                        drink_name = "",
                        drink_price = "",
                        drink_quantity = 0
                    )
                )
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = cardColors(MealCard),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = LocalContext.current.getString(meal.meal),
            fontSize = 24.sp
        )
        var imageResource by remember { mutableStateOf(R.drawable.cake) }
        imageResource = when (stringResource(meal.meal)) {
            "巧克力蛋糕" -> R.drawable.cake
            "雞排咖哩飯" -> R.drawable.chicken_carry
            "小籠包" -> R.drawable.little_bum
            "宮保雞丁" -> R.drawable.palace
            "豬肉火鍋" -> R.drawable.pork
            else -> R.drawable.tiramisu
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(imageResource),
            contentDescription = stringResource(meal.meal),
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkOrderCard(
    drink: Drinks,
    menuUiState: MenuUiState,
    uiStateUpdate: (MenuUiState) -> Unit,
    deleteUiState: (String) -> Unit,
    context: Context,
    viewModel: MenuInsertViewModel,
) {
    var badgeNumber by remember { mutableStateOf(0) }
    val badgeNumberKeep by viewModel.badgeNumber.collectAsState()
    badgeNumber = badgeNumberKeep.getOrPut(drink.hashCode()) { 0 }
    Box(
        modifier = Modifier
            .size(height = 180.dp, width = 180.dp)
            .zIndex(1f),
        Alignment.BottomEnd
    ) {
        var imageResource by remember { mutableStateOf(R.drawable.cake) }
        imageResource = when (stringResource(drink.drink)) {
            "柳橙汁" -> R.drawable.thirty
            "可樂" -> R.drawable.thirty
            "可爾必思" -> R.drawable.thirty_five
            "紅茶" -> R.drawable.thirty
            "水果茶" -> R.drawable.sixty_five
            else -> R.drawable.fifty
        }
        Image(
            modifier = Modifier
                .size(55.dp)
                .offset(x = 7.dp, y = (-90).dp),
            painter = painterResource(imageResource),
            contentDescription = "80"
        )
        if (badgeNumber > 0) {
            Badge(
                modifier = Modifier.size(40.dp)
            ) {
                Text(
                    badgeNumber.toString(),
                    modifier = Modifier.semantics {
                        contentDescription = "$badgeNumber new notifications"
                    },
                    fontSize = 27.sp,
                )
            }
            Box(  modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
                .size(height = 180.dp, width = 180.dp), ){
                IconButton(
                    onClick = {
                        badgeNumber -= 1
                        viewModel.dishCard(drink.hashCode(), badgeNumber)
                        deleteUiState(
                            context.getString(drink.drink)
                        )
                    },
                    modifier = Modifier
                        .size(75.dp)
                        .offset(x = (-15).dp, y = 112.dp),
                ) {
                    Icon(
                        modifier = Modifier.size(75.dp),
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Close"
                    )
                    Icon(
                        modifier = Modifier.size(72.dp),
                        tint = TotalMealDelete,
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Close"
                    )
                }
            }

        }
    }
    Card(
        modifier = Modifier
            .size(height = 180.dp, width = 180.dp)
            .padding(9.dp)
            .clickable {
                val payPrice = when (context.getString(drink.drink)) {
                    "柳橙汁" -> "30"
                    "可樂" -> "30"
                    "可爾必思" -> "35"
                    "紅茶" -> "30"
                    "水果茶" -> "60"
                    else -> "50"
                }
                badgeNumber += 1
                viewModel.dishCard(drink.hashCode(), badgeNumber)
                uiStateUpdate(
                    menuUiState.copy(
                        drink_name = context.getString(drink.drink),
                        drink_price = payPrice,
                        drink_quantity = 1,
                        dish_name = "",
                        dish_price = "",
                        dish_quantity = 0,
                    )
                )
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = cardColors(DrinkCard),
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = LocalContext.current.getString(drink.drink),
            fontSize = 24.sp
        )
        var imageResource by remember { mutableStateOf(R.drawable.cake) }
        imageResource = when (stringResource(drink.drink)) {
            "柳橙汁" -> R.drawable.orange
            "可樂" -> R.drawable.coke
            "可爾必思" -> R.drawable.karu
            "紅茶" -> R.drawable.black_tea
            "水果茶" -> R.drawable.fruit
            else -> R.drawable.milk_tea
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(imageResource),
            contentDescription = stringResource(drink.drink),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ToppingOrderCard(
    topping: Toppings,
    selected: Boolean,
    onClick: () -> Unit,
    selectedTopping: Int,
    viewModel: MenuInsertViewModel,
) {
    val toppingKeep by viewModel.badgeNumber.collectAsState()
    var selectedToppingTwo = toppingKeep.getOrPut(99) { 4 }
    Log.d("console", topping.toString())
    Card(
        modifier = Modifier
            .size(height = 180.dp, width = 180.dp)
            .padding(9.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = if (topping.id == selectedToppingTwo) cardColors(ToppingSelected) else cardColors(
            ToppingCard
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (topping.id == selectedToppingTwo) BorderStroke(2.dp, ToppingSelected) else null
    ) {
        Text(
            style = MaterialTheme.typography.bodySmall,
            text = LocalContext.current.getString(topping.topping),
            fontSize = 24.sp
        )
        var imageResource by remember { mutableStateOf(R.drawable.cake) }
        imageResource = when (topping.id) {
            0 -> R.drawable.thousand
            1 -> R.drawable.tea_souce
            else -> R.drawable.soy_paste
        }
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(imageResource),
            contentDescription = stringResource(imageResource),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
private fun MenuOrderAppPreview() {
}