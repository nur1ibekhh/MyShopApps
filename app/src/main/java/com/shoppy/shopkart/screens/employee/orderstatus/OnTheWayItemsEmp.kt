package com.shoppy.shopkart.screens.employee.orderstatus

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.shoppy.shopkart.R
import com.shoppy.shopkart.ShopKartUtils
import com.shoppy.shopkart.components.BackButton
import com.shoppy.shopkart.components.DeliveryStatusCard
import com.shoppy.shopkart.models.MOrder
import com.shoppy.shopkart.navigation.BottomNavScreens
import com.shoppy.shopkart.screens.search.SearchBox

@Composable
fun OnTheWayItemsEmp(navHostController: NavHostController, viewModel: OrderStatusEmpViewModel = hiltViewModel()){

    val onTheWayItemsList = remember { mutableStateOf(emptyList<MOrder>()) }
//    Log.d("OrderedList", "OrderedItems: $orderedItemsList")

    val searchByOrderId = remember { mutableStateOf("") }

    val context = LocalContext.current

    onTheWayItemsList.value = viewModel.fireStatus.value.data?.toList()?.filter { mOrder ->

        mOrder.delivery_status == "On The Way"

    }!!

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { BackButton(navController = navHostController, topBarTitle = "On The Way Items", spacing = 35.dp) }, backgroundColor = ShopKartUtils.offWhite) { innerPadding ->

        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize(), verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally) {

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {

                SearchBox(value = searchByOrderId.value, onChange = searchByOrderId, leadingIcon = R.drawable.ic_search, placeHolder = "Search by Order Id", customAutoFocus = false)

                //Search Button
                IconButton(modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                    onClick = {
                        onTheWayItemsList.value = viewModel.fireStatus.value.data?.toList()?.filter { mOrder ->
                            mOrder.order_id == searchByOrderId.value
                        }!!
                    }){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }

            LazyColumn{
                items(items = onTheWayItemsList.value){ ordered ->
                    DeliveryStatusCard(ordered = ordered, buttonTitle = "Mark Delivered", navHostController = navHostController){
                        viewModel.markDelivered(
                            userId = ordered.user_id!!,
                            product_title = ordered.product_title!!
                        ) {
                            navHostController.popBackStack()
                            navHostController.navigate(BottomNavScreens.OnTheWayItemsEmp.route)
                            Toast.makeText(context, "Item marked as Delivered", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }
}