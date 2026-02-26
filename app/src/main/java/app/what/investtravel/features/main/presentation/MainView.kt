package app.what.investtravel.features.main.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.what.investtravel.data.remote.AccessiblePlaceResponse
import app.what.investtravel.data.remote.AccessibilityInfo

@Composable
fun MainView(
    onPlaceSelected: (AccessiblePlaceResponse) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showFilters by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf(Pair(47.2314, 39.7258)) } // Rostov-on-Don
    var searchRadius by remember { mutableStateOf(5f) }
    
    // Mock places data
    val mockPlaces = listOf(
        AccessiblePlaceResponse(
            id = 1,
            name = "Accessible Cafe Downtown",
            description = "Modern cafe with full wheelchair access",
            address = "Ul. Pushkina 15",
            city = "Rostov-on-Don",
            latitude = 47.2314,
            longitude = 39.7258,
            category = "cafe",
            accessibilityInfo = AccessibilityInfo(
                wheelchairAccessible = true,
                elevator = false,
                accessibleToilet = true,
                hearingLoop = false,
                visualGuides = false,
                parkingAccessible = true,
                petFriendly = true,
                serviceAnimalsAllowed = true,
                staffTrained = true,
                notes = "Entrance has ramp, accessible parking nearby"
            ),
            phone = "+7-123-456-7890",
            email = "info@cafe.com",
            website = "https://cafe.example.com",
            rating = 4.5f,
            reviewCount = 23,
            images = null,
            status = "active",
            createdAt = "2024-01-15",
            updatedAt = "2024-02-20"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Accessibility Navigator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filters")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Map, contentDescription = "Map") },
                    label = { Text("Map") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Place, contentDescription = "Places") },
                    label = { Text("Places") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Route, contentDescription = "Routes") },
                    label = { Text("Routes") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.BookmarkBorder, contentDescription = "Saved") },
                    label = { Text("Saved") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> MapTabContent(mockPlaces, userLocation, searchRadius)
                1 -> PlacesTabContent(mockPlaces, onPlaceSelected)
                2 -> RoutesTabContent()
                3 -> SavedPlacesTabContent()
            }
        }
    }
}

@Composable
fun MapTabContent(
    places: List<AccessiblePlaceResponse>,
    userLocation: Pair<Double, Double>,
    searchRadius: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Map placeholder - in production use Google Maps SDK
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color(0xFFE0E0E0)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Filled.Map,
                    contentDescription = "Map",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Gray
                )
                Text(
                    "Map view with accessible place markers",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    "Location: ${userLocation.first.toFloat().toInt()}°N, ${userLocation.second.toFloat().toInt()}°E",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }

        // Places near location
        Text(
            "Accessible Places Nearby (${places.size})",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(places) { place ->
                PlaceMarkerCard(place)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PlacesTabContent(
    places: List<AccessiblePlaceResponse>,
    onPlaceSelected: (AccessiblePlaceResponse) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Search bar
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search places...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            singleLine = true
        )

        Text(
            "All Accessible Places",
            modifier = Modifier.padding(horizontal = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(places) { place ->
                PlaceDetailsCard(place, onPlaceSelected)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun PlaceMarkerCard(place: AccessiblePlaceResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon
            Icon(
                Icons.Filled.Place,
                contentDescription = place.category,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF1976D2)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(place.name, fontWeight = FontWeight.Bold)
                Text(place.address, fontSize = 12.sp, color = Color.Gray)
                Text(
                    "Rating: ${place.rating}/5 (${place.reviewCount} reviews)",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            // Accessibility indicator
            if (place.accessibilityInfo.wheelchairAccessible) {
                Icon(
                    Icons.Filled.AccessibilityNew,
                    contentDescription = "Wheelchair accessible",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun PlaceDetailsCard(
    place: AccessiblePlaceResponse,
    onPlaceSelected: (AccessiblePlaceResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlaceSelected(place) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(place.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(place.description ?: "No description", fontSize = 13.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            // Accessibility features
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (place.accessibilityInfo.wheelchairAccessible) {
                    AccessibilityFeatureChip("Wheelchair")
                }
                if (place.accessibilityInfo.accessibleToilet) {
                    AccessibilityFeatureChip("Restroom")
                }
                if (place.accessibilityInfo.petFriendly) {
                    AccessibilityFeatureChip("Pet Friendly")
                }
                if (place.accessibilityInfo.parkingAccessible) {
                    AccessibilityFeatureChip("Parking")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "Rating: ${place.rating}/5 (${place.reviewCount} reviews)",
                fontSize = 12.sp,
                color = Color(0xFF1976D2),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AccessibilityFeatureChip(label: String) {
    Surface(
        modifier = Modifier
            .wrapContentSize()
            .padding(2.dp),
        shape = MaterialTheme.shapes.small,
        color = Color(0xFFE3F2FD)
    ) {
        Text(
            label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            color = Color(0xFF1976D2),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun RoutesTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.Route,
            contentDescription = "Routes",
            modifier = Modifier.size(48.dp),
            tint = Color.Gray
        )
        Text(
            "Accessible Routes",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            "Plan your journey with accessible waypoints",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.Add,
                contentDescription = "Create",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Create New Route")
        }
    }
}

@Composable
fun SavedPlacesTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Filled.BookmarkBorder,
            contentDescription = "Saved",
            modifier = Modifier.size(48.dp),
            tint = Color.Gray
        )
        Text(
            "Saved Places",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            "Your favorite accessible locations",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}
