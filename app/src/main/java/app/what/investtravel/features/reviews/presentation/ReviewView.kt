package app.what.investtravel.features.reviews.presentation

import androidx.compose.foundation.background
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
import app.what.investtravel.data.remote.AccessibilityFeedback
import app.what.investtravel.data.remote.ReviewResponse

@Composable
fun ReviewListView(
    placeId: Int,
    reviews: List<ReviewResponse> = emptyList(),
    onBackClick: () -> Unit = {},
    onWriteReviewClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reviews & Accessibility Feedback") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onWriteReviewClick) {
                Icon(Icons.Filled.Add, contentDescription = "Write Review")
            }
        }
    ) { paddingValues ->
        if (reviews.isEmpty()) {
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
                        Icons.Filled.RateReview,
                        contentDescription = "Reviews",
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Text(
                        "No reviews yet",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        "Be the first to share your accessibility feedback",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = onWriteReviewClick) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Write",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Write First Review")
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
                items(reviews) { review ->
                    ReviewCard(review)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ReviewCard(review: ReviewResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: User name and rating
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    review.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Star",
                            modifier = Modifier.size(16.dp),
                            tint = if (index < review.rating.toInt())
                                Color(0xFFFFC107)
                            else
                                Color(0xFFE0E0E0)
                        )
                    }
                    Text(
                        "${review.rating}/5",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Review text
            Text(
                review.text,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Accessibility feedback section
            AccessibilityFeedbackCard(review.accessibilityFeedback)
        }
    }
}

@Composable
fun AccessibilityFeedbackCard(feedback: AccessibilityFeedback) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        color = Color(0xFFE3F2FD)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                "Accessibility Feedback",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Difficulty Level
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.SignalCellularAlt,
                    contentDescription = "Difficulty",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1976D2)
                )
                Text(
                    "Difficulty: ${feedback.difficultyLevel}",
                    fontSize = 12.sp
                )
            }

            // Staff Helpfulness
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Filled.Groups,
                    contentDescription = "Staff",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF1976D2)
                )
                Text(
                    "Staff Helpfulness: ${feedback.staffHelpfulness}/5",
                    fontSize = 12.sp
                )
            }

            // Issues Encountered
            if (!feedback.issuesEncountered.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        "Issues Encountered:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    feedback.issuesEncountered.forEach { issue ->
                        Text(
                            "• $issue",
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 2.dp)
                        )
                    }
                }
            }

            // Recommendations
            if (!feedback.recommendations.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        "Recommendations:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        feedback.recommendations!!,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun WriteReviewView(
    placeId: Int,
    placeName: String = "Place",
    onBackClick: () -> Unit = {},
    onSubmitReview: (String, Float, AccessibilityFeedback) -> Unit = { _, _, _ -> }
) {
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(4f) }
    var difficultyLevel by remember { mutableStateOf("easy") }
    var staffHelpfulness by remember { mutableStateOf(3) }
    var issues by remember { mutableStateOf(emptyList<String>()) }
    var recommendations by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Write Review - $placeName") },
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
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            // Rating
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Rate This Place",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            IconButton(
                                onClick = { rating = (index + 1).toFloat() }
                            ) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Star ${index + 1}",
                                    modifier = Modifier.size(32.dp),
                                    tint = if (index < rating.toInt())
                                        Color(0xFFFFC107)
                                    else
                                        Color(0xFFE0E0E0)
                                )
                            }
                        }
                    }

                    Text(
                        "Rating: $rating/5",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Review Text
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Your Review",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    TextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = { Text("Share your experience...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Accessibility Feedback
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Accessibility Feedback",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Difficulty Level
                    Text(
                        "Difficulty Level",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("easy", "moderate", "hard").forEach { level ->
                            FilterChip(
                                selected = difficultyLevel == level,
                                onClick = { difficultyLevel = level },
                                label = { Text(level.capitalize()) }
                            )
                        }
                    }

                    // Staff Helpfulness
                    Text(
                        "Staff Helpfulness: $staffHelpfulness/5",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Slider(
                        value = staffHelpfulness.toFloat(),
                        onValueChange = { staffHelpfulness = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    // Recommendations
                    Text(
                        "Recommendations for Improvement",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextField(
                        value = recommendations,
                        onValueChange = { recommendations = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        placeholder = { Text("Suggest improvements...") },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF5F5F5)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    val feedback = AccessibilityFeedback(
                        difficultyLevel = difficultyLevel,
                        issuesEncountered = issues.takeIf { it.isNotEmpty() },
                        recommendations = recommendations.takeIf { it.isNotEmpty() },
                        staffHelpfulness = staffHelpfulness
                    )
                    onSubmitReview(reviewText, rating, feedback)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = reviewText.isNotEmpty()
            ) {
                Text("Submit Review", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
