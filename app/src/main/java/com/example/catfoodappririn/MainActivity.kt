package com.example.catfoodappririn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Data makanan kucing dan deskripsinya
val catFoodDescriptions = mapOf(
    "Whiskas" to "Makanan kucing dengan rasa ayam dan ikan, dilengkapi nutrisi untuk menjaga bulu kucing tetap sehat dan berkilau.",
    "Royal Canin" to "Produk yang dirancang khusus sesuai dengan usia dan jenis kucing, membantu dalam mendukung kesehatan pencernaan dan kekebalan tubuh.",
    "Me-O" to "Makanan kucing yang kaya akan nutrisi penting, termasuk vitamin dan mineral untuk kesehatan tulang dan gigi kucing.",
    "Friskies" to "Mengandung protein tinggi dari ayam dan ikan untuk mendukung energi dan vitalitas kucing sehari-hari.",
    "Pro Plan" to "Makanan premium dengan protein dan nutrisi untuk menjaga berat badan ideal dan menguatkan sistem kekebalan tubuh.",
    "Kit Cat" to "Dibuat dari bahan alami, bebas biji-bijian dan kedelai, cocok untuk kucing dengan alergi atau sensitivitas makanan.",
    "Equilibrio" to "Produk ini mengandung omega-3 dan omega-6 untuk kesehatan kulit dan bulu, serta prebiotik untuk kesehatan pencernaan.",
    "Hill's" to "Makanan kucing yang diformulasikan oleh dokter hewan, mengandung antioksidan untuk menjaga kesehatan jangka panjang.",
    "IAMS" to "Mengandung nutrisi lengkap dengan protein dari ayam dan ikan, cocok untuk menjaga kesehatan otot dan tulang.",
    "Blackwood" to "Makanan berbahan dasar daging dan biji-bijian yang mudah dicerna, membantu kucing mempertahankan berat badan ideal."
)

// Peta nama makanan ke resource gambar
val foodIcons = mapOf(
    "Whiskas" to R.drawable.ic_whiskas,
    "Royal Canin" to R.drawable.ic_royalcanin,
    "Me-O" to R.drawable.ic_meo,
    "Friskies" to R.drawable.ic_friskies,
    "Pro Plan" to R.drawable.ic_proplan,
    "Kit Cat" to R.drawable.ic_kitcat,
    "Equilibrio" to R.drawable.ic_equilibrio,
    "Hill's" to R.drawable.ic_hills,
    "IAMS" to R.drawable.ic_iam,
    "Blackwood" to R.drawable.ic_blackwood
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CatFoodApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatFoodApp() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            TopAppBar(
                title = {
                    Text(
                        text = when (currentRoute) {
                            "list" -> "List"
                            "grid" -> "Grid"
                            "about" -> "About"
                            else -> "Detail"
                        },
                        color = Color.DarkGray
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Cyan),
                navigationIcon = {
                    if (currentRoute == "detail/{itemId}") {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("list") { ListScreen(navController) }
            composable("grid") { GridScreen(navController) }
            composable("about") { AboutScreen() }
            composable("detail/{itemId}") { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")
                itemId?.let { DetailScreen(it, navController) }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.Transparent) {
        val items = listOf(
            "list" to R.drawable.ic_list,
            "grid" to R.drawable.ic_grid,
            "about" to R.drawable.ic_about
        )
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { (route, iconRes) ->
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = iconRes), contentDescription = route) },
                label = { Text(route.capitalize()) },
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun ListScreen(navController: NavController) {
    val catFoods = listOf("Whiskas", "Royal Canin", "Me-O", "Friskies", "Pro Plan", "Kit Cat", "Equilibrio", "Hill's", "IAMS", "Blackwood")
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(8.dp)) {
            items(catFoods) { food ->
                val icon = foodIcons[food] ?: R.drawable.ic_default // Default icon if no match
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { navController.navigate("detail/$food") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = icon), contentDescription = food, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = food)
                }
            }
        }
    }
}

@Composable
fun GridScreen(navController: NavController) {
    val catFoods = listOf("Whiskas", "Royal Canin", "Me-O", "Friskies", "Pro Plan", "Kit Cat", "Equilibrio", "Hill's", "IAMS", "Blackwood")
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(catFoods) { food ->
                val icon = foodIcons[food] ?: R.drawable.ic_default // Default icon if no match
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { navController.navigate("detail/$food") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = icon), contentDescription = food, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = food)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(itemId: String, navController: NavController) {
    val description = catFoodDescriptions[itemId] ?: "Deskripsi tidak tersedia."
    val iconResId = foodIcons[itemId] ?: R.drawable.ic_default

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = iconResId),
                            contentDescription = itemId,
                            modifier = Modifier.size(40.dp) // Ukuran ikon
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Jarak antara ikon dan teks
                        Text("Detail $itemId", color = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Menampilkan gambar ikon makanan kucing
            Image(painter = painterResource(id = iconResId), contentDescription = itemId, modifier = Modifier.size(100.dp))
            Spacer(modifier = Modifier.height(16.dp))

            // Menampilkan deskripsi makanan kucing
            Text(text = description, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Menampilkan gambar ikon profil
        Image(
            painter = painterResource(id = R.drawable.ic_profile), // Ganti dengan gambar profil Anda
            contentDescription = "Profil",
            modifier = Modifier
                .size(100.dp) // Ukuran gambar profil
                .padding(bottom = 16.dp) // Jarak antara gambar dan teks
        )

        // Nama Pengguna
        Text(
            text = "Ririn Novelia Roselah",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp)) // Jarak antara teks

        // Email atau informasi lainnya
        Text(text = "rinovelia@example.com")
        Spacer(modifier = Modifier.height(8.dp)) // Jarak antara teks

        // Institusi dan jurusan
        Text(text = "Politeknik Negeri Sriwijaya")
        Text(text = "Teknik Informatika")
    }
}
