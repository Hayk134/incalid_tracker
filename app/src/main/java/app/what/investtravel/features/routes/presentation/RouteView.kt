package app.what.investtravel.features.routes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import app.what.investtravel.data.remote.AccessibleRouteResponse
import app.what.investtravel.data.remote.RoutePoint

@Composable
fun RoutePlannerView(
    onBackClick: () -> Unit = {},
    onRouteCreated: (String, List<RoutePoint>) -> Unit = { _, _ -> }
) {
    var routeName by remember { mutableStateOf("") }
    var routeDescription by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf("easy") }
    var wheelchairFriendly by remember { mutableStateOf(true) }
    var accessibilityTags by remember { mutableStateOf(emptyList<String>()) }
    var startPoint by remember { mutableStateOf(RoutePoint(47.2314, 39.7258, "Start Location")) }
    var endPoint by remember { mutableStateOf(RoutePoint(47.2400, 39.7350, "End Location")) }
    var waypoints by remember { mutableStateOf(emptyList<RoutePoint>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan Accessible Route") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Route Name and Description
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Route Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = routeName,
                        onValueChange = { routeName = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Route Name") },
                        placeholder = { Text("e.g., Downtown Accessible Tour") },
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = routeDescription,
                        onValueChange = { routeDescription = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        label = { Text("Description") },
                        placeholder = { Text("Describe the route and its highlights...") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Difficulty Level
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Difficulty Level",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("easy", "moderate", "hard").forEach { level ->
                            FilterChip(
                                selected = selectedDifficulty == level,
                                onClick = { selectedDifficulty = level },
                                label = { Text(level.replaceFirstChar { it.uppercase() }) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accessibility Options
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Accessibility Requirements",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = wheelchairFriendly,
                            onCheckedChange = { wheelchairFriendly = it }
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Wheelchair Friendly",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp
                            )
                            Text(
                                "All locations wheelchair accessible",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Accessibility Tags",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val availableTags = listOf(
                        "no-stairs",
                        "elevator-access",
                        "accessible-restrooms",
                        "parking",
                        "pet-friendly",
                        "low-stress",
                        "air-conditioned",
                        "rest-areas"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        availableTags.chunked(2).forEach { row ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { tag ->
                                    FilterChip(
                                        selected = tag in accessibilityTags,
                                        onClick = {
                                            accessibilityTags = if (tag in accessibilityTags)
                                                accessibilityTags - tag
                                            else
                                                accessibilityTags + tag
                                        },
                                        label = { Text(tag.replace("-", " ").capitalize()) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (row.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Route Points
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Route Points",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    RoutePointCard(
                        "Start Location",
                        startPoint,
                        Icons.Filled.GpsFixed,
                        Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (waypoints.isNotEmpty()) {
                        waypoints.forEachIndexed { index, point ->
                            RoutePointCard(
                                "Waypoint ${index + 1}",
                                point,
                                Icons.Filled.LocationOn,
                                Color(0xFF2196F3)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    RoutePointCard(
                        "End Location",
                        endPoint,
                        Icons.Filled.LocationOn,
                        Color(0xFFFF5252)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* Add waypoint */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add Waypoint")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Create Route Button
            Button(
                onClick = {
                    onRouteCreated(routeName, listOf(startPoint) + waypoints + listOf(endPoint))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = routeName.isNotEmpty()
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Create",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Route", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RoutePointCard(
    label: String,
    point: RoutePoint,
    icon: androidx.compose.material.icons.materialIcon,
    iconColor: Color
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = MaterialTheme.shapes.small,
        color = Color(0xFFFAFAFA)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(28.dp),
                tint = iconColor
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp
                )
                Text(
                    point.name ?: "Unnamed Location",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    "${point.latitude.toString().take(7)}°N, ${point.longitude.toString().take(7)}°E",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Edit",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun RouteListView(
    routes: List<AccessibleRouteResponse> = emptyList(),
    onRouteSelected: (AccessibleRouteResponse) -> Unit = {},
    onBackClick: () -> Unit = {},
    onCreateNewRoute: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Accessible Routes") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNewRoute) {
                Icon(Icons.Filled.Add, contentDescription = "Create Route")
            }
        }
    ) { paddingValues ->
        if (routes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
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
                        "No routes yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "Create your first accessible route",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = onCreateNewRoute) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Create",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Create Route")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5))
            ) {
                items(routes) { route ->
                    RouteListCard(route, onRouteSelected)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun RouteListCard(
    route: AccessibleRouteResponse,
    onRouteSelected: (AccessibleRouteResponse) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onRouteSelected(route) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    route.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    modifier = Modifier.wrapContentSize(),
                    shape = MaterialTheme.shapes.small,
                    color = when (route.difficultyLevel) {
                        "easy" -> Color(0xFFC8E6C9)
                        "moderate" -> Color(0xFFFFE0B2)
                        else -> Color(0xFFFFCDD2)
                    }
                ) {
                    Text(
                        route.difficultyLevel.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (route.difficultyLevel) {
                            "easy" -> Color(0xFF2E7D32)
                            "moderate" -> Color(0xFFE65100)
                            else -> Color(0xFFC62828)
                        }
                    )
                }
            }

            if (!route.description.isNullOrEmpty()) {
                Text(
                    route.description!!,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // Route Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                RouteStatItem(
                    icon = Icons.Filled.Straighten,
                    label = "${route.totalDistanceKm} km"
                )
                RouteStatItem(
                    icon = Icons.Filled.Schedule,
                    label = "${route.estimatedDurationMinutes} min"
                )
                if (route.wheelchairFriendly) {
                    RouteStatItem(
                        icon = Icons.Filled.AccessibilityNew,
                        label = "Wheelchair"
                    )
                }
            }

            // Accessibility Tags
            if (route.accessibilityTags.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    route.accessibilityTags.take(3).forEach { tag ->
                        Surface(
                            modifier = Modifier.wrapContentSize(),
                            shape = MaterialTheme.shapes.extraSmall,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Text(
                                tag.take(10),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = Color(0xFF1976D2)
                            )
                        }
                    }
                    if (route.accessibilityTags.size > 3) {
                        Text(
                            "+${route.accessibilityTags.size - 3}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RouteStatItem(
    icon: androidx.compose.material.icons.materialIcon,
    label: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF1976D2)
        )
        Text(
            label,
            fontSize = 11.sp,
            color = Color.Gray
        )
    }
}

private fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
