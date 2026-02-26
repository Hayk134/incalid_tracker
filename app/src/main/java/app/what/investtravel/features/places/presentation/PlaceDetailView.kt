package app.what.investtravel.features.places.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import app.what.investtravel.data.remote.AccessiblePlaceResponse
import app.what.investtravel.data.remote.AccessibilityInfo

@Composable
fun PlaceDetailView(
    place: AccessiblePlaceResponse,
    onBackClick: () -> Unit = {},
    onSaveClick: (Int) -> Unit = {},
    onWriteReviewClick: (Int) -> Unit = {}
) {
    var isSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place.name) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        isSaved = !isSaved
                        onSaveClick(place.id)
                    }) {
                        Icon(
                            if (isSaved) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                            contentDescription = "Save"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onWriteReviewClick(place.id) }
            ) {
                Icon(Icons.Filled.RateReview, contentDescription = "Write Review")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            item {
                // Header with place image placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Filled.Image,
                            contentDescription = "Place Image",
                            modifier = Modifier.size(48.dp),
                            tint = Color.Gray
                        )
                        Text("Place Image", color = Color.Gray)
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    // Category and Rating
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.wrapContentSize(),
                            shape = MaterialTheme.shapes.small,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Text(
                                place.category.uppercase(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 11.sp,
                                color = Color(0xFF1976D2),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Rating",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFFFFC107)
                            )
                            Text(
                                "${place.rating}",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "(${place.reviewCount})",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    // Address and Contact
                    Text(
                        place.address,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (!place.phone.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Phone,
                                contentDescription = "Phone",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF1976D2)
                            )
                            Text(place.phone!!, fontSize = 13.sp)
                        }
                    }

                    if (!place.email.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Email,
                                contentDescription = "Email",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF1976D2)
                            )
                            Text(place.email!!, fontSize = 13.sp)
                        }
                    }

                    if (!place.website.isNullOrEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Filled.Language,
                                contentDescription = "Website",
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF1976D2)
                            )
                            Text(place.website!!, fontSize = 13.sp)
                        }
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        "Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        place.description ?: "No description available",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                AccessibilityFeaturesSection(place.accessibilityInfo)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun AccessibilityFeaturesSection(accessibilityInfo: AccessibilityInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            "Accessibility Features",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AccessibilityFeatureRow(
                "Wheelchair Accessible",
                accessibilityInfo.wheelchairAccessible,
                Icons.Filled.AccessibilityNew
            )
            AccessibilityFeatureRow(
                "Elevator",
                accessibilityInfo.elevator,
                Icons.Filled.ElevatorOutlined
            )
            AccessibilityFeatureRow(
                "Accessible Toilet",
                accessibilityInfo.accessibleToilet,
                Icons.Filled.RestRoom
            )
            AccessibilityFeatureRow(
                "Hearing Loop",
                accessibilityInfo.hearingLoop,
                Icons.Filled.Hearing
            )
            AccessibilityFeatureRow(
                "Visual Guides",
                accessibilityInfo.visualGuides,
                Icons.Filled.Visibility
            )
            AccessibilityFeatureRow(
                "Accessible Parking",
                accessibilityInfo.parkingAccessible,
                Icons.Filled.LocalParking
            )
            AccessibilityFeatureRow(
                "Pet Friendly",
                accessibilityInfo.petFriendly,
                Icons.Filled.Pets
            )
            AccessibilityFeatureRow(
                "Service Animals Allowed",
                accessibilityInfo.serviceAnimalsAllowed,
                Icons.Filled.Dogs
            )
            AccessibilityFeatureRow(
                "Staff Trained",
                accessibilityInfo.staffTrained,
                Icons.Filled.SchoolOutlined
            )
        }

        if (!accessibilityInfo.notes.isNullOrEmpty()) {
            Divider(modifier = Modifier.padding(vertical = 12.dp))
            Text(
                "Additional Notes",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                accessibilityInfo.notes!!,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AccessibilityFeatureRow(
    label: String,
    isAvailable: Boolean,
    icon: androidx.compose.material.icons.materialIcon
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = if (isAvailable) Color(0xFF4CAF50) else Color(0xFFBDBDBD)
        )
        Text(label, fontSize = 13.sp, modifier = Modifier.weight(1f))
        if (isAvailable) {
            Surface(
                modifier = Modifier.size(24.dp),
                shape = MaterialTheme.shapes.small,
                color = Color(0xFF4CAF50)
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = "Available",
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.Center),
                    tint = Color.White
                )
            }
        } else {
            Text(
                "N/A",
                fontSize = 11.sp,
                color = Color(0xFFBDBDBD),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
