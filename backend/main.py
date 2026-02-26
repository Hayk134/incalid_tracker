"""
Accessibility Navigation Platform - Backend API
REST API for managing accessible places, reviews, and routes for people with disabilities
"""

from fastapi import FastAPI, HTTPException, Query, Depends
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime
import uvicorn

app = FastAPI(
    title="Accessibility Navigation API",
    description="API for finding accessible places and planning routes for people with disabilities",
    version="1.0.0"
)

# Enable CORS for Android app
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ============= MODELS =============

class AccessibilityInfo(BaseModel):
    wheelchair_accessible: bool = False
    elevator: bool = False
    accessible_toilet: bool = False
    hearing_loop: bool = False
    visual_guides: bool = False
    parking_accessible: bool = False
    pet_friendly: bool = False
    service_animals_allowed: bool = False
    staff_trained: bool = False
    notes: Optional[str] = None

class AccessiblePlace(BaseModel):
    id: int
    name: str
    description: Optional[str] = None
    address: str
    city: str
    latitude: float
    longitude: float
    category: str
    accessibility_info: AccessibilityInfo
    phone: Optional[str] = None
    email: Optional[str] = None
    website: Optional[str] = None
    rating: float = 0.0
    review_count: int = 0
    images: Optional[List[str]] = None
    status: str = "active"
    created_at: datetime
    updated_at: Optional[datetime] = None

class AccessiblePlaceCreateRequest(BaseModel):
    name: str
    description: Optional[str] = None
    address: str
    city: str
    latitude: float
    longitude: float
    category: str
    accessibility_info: AccessibilityInfo
    phone: Optional[str] = None
    email: Optional[str] = None
    website: Optional[str] = None

class AccessibilityFeedback(BaseModel):
    difficulty_level: str = "easy"
    issues_encountered: Optional[List[str]] = None
    recommendations: Optional[str] = None
    staff_helpfulness: int = 3
    visited_date: Optional[str] = None

class Review(BaseModel):
    id: int
    place_id: int
    user_id: int
    user_name: str
    rating: float
    text: str
    accessibility_feedback: AccessibilityFeedback
    photos: Optional[List[str]] = None
    created_at: datetime
    updated_at: Optional[datetime] = None

class ReviewCreateRequest(BaseModel):
    place_id: int
    rating: float
    text: str
    accessibility_feedback: AccessibilityFeedback

class RoutePoint(BaseModel):
    latitude: float
    longitude: float
    name: Optional[str] = None
    notes: Optional[str] = None

class AccessibleRoute(BaseModel):
    id: int
    name: str
    description: Optional[str] = None
    start_point: RoutePoint
    end_point: RoutePoint
    waypoints: Optional[List[RoutePoint]] = None
    total_distance_km: float
    estimated_duration_minutes: int
    accessibility_tags: List[str]
    difficulty_level: str = "easy"
    wheelchair_friendly: bool = False
    rest_points: Optional[List[AccessiblePlace]] = None
    created_by: int
    created_at: datetime
    updated_at: Optional[datetime] = None

class RouteCreateRequest(BaseModel):
    name: str
    description: Optional[str] = None
    start_point: RoutePoint
    end_point: RoutePoint
    waypoints: Optional[List[RoutePoint]] = None
    accessibility_tags: List[str]
    difficulty_level: str = "easy"
    wheelchair_friendly: bool = False

class SavedPlace(BaseModel):
    id: int
    user_id: int
    place_id: int
    place: AccessiblePlace
    notes: Optional[str] = None
    saved_at: datetime

# Mock Data Storage
mock_places: dict[int, dict] = {
    1: {
        "id": 1,
        "name": "Accessible Cafe Downtown",
        "description": "Modern cafe with full wheelchair access",
        "address": "Ul. Pushkina 15",
        "city": "Rostov-on-Don",
        "latitude": 47.2314,
        "longitude": 39.7258,
        "category": "cafe",
        "accessibility_info": {
            "wheelchair_accessible": True,
            "elevator": False,
            "accessible_toilet": True,
            "hearing_loop": False,
            "visual_guides": False,
            "parking_accessible": True,
            "pet_friendly": True,
            "service_animals_allowed": True,
            "staff_trained": True,
            "notes": "Entrance has ramp, accessible parking nearby"
        },
        "phone": "+7-123-456-7890",
        "email": "info@cafe.com",
        "website": "https://cafe.example.com",
        "rating": 4.5,
        "review_count": 23,
        "status": "active",
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
}

mock_reviews: dict[int, dict] = {}
mock_routes: dict[int, dict] = {}
mock_saved_places: dict[int, dict] = {}

# ============= PLACES ENDPOINTS =============

@app.get("/places/search", response_model=dict)
async def search_places(
    latitude: float = Query(...),
    longitude: float = Query(...),
    radius_km: float = Query(5.0),
    category: Optional[str] = None,
    wheelchair_accessible: Optional[bool] = None,
    page: int = Query(1, ge=1),
    size: int = Query(10, ge=1, le=100),
    token: Optional[str] = Query(None)
):
    """Search for accessible places near coordinates"""
    filtered_places = list(mock_places.values())
    
    if category:
        filtered_places = [p for p in filtered_places if p["category"].lower() == category.lower()]
    
    if wheelchair_accessible:
        filtered_places = [p for p in filtered_places if p["accessibility_info"].get("wheelchair_accessible", False)]
    
    total = len(filtered_places)
    places = filtered_places[(page - 1) * size : page * size]
    
    return {
        "places": places,
        "total": total,
        "page": page,
        "size": size,
        "total_pages": (total + size - 1) // size
    }

@app.get("/places/{place_id}", response_model=dict)
async def get_place(place_id: int, token: Optional[str] = Query(None)):
    """Get details of a specific place"""
    if place_id not in mock_places:
        raise HTTPException(status_code=404, detail="Place not found")
    return mock_places[place_id]

@app.post("/places/create", response_model=dict)
async def create_place(place_data: AccessiblePlaceCreateRequest, token: Optional[str] = Query(None)):
    """Create a new accessible place"""
    new_id = max(mock_places.keys()) + 1 if mock_places else 1
    place = place_data.dict()
    place["id"] = new_id
    place["created_at"] = datetime.now()
    place["updated_at"] = datetime.now()
    place["rating"] = 0.0
    place["review_count"] = 0
    mock_places[new_id] = place
    return place

@app.post("/places/filter", response_model=dict)
async def filter_places(
    filters: dict,
    page: int = Query(1),
    size: int = Query(10),
    token: Optional[str] = Query(None)
):
    """Filter places by accessibility criteria"""
    filtered = list(mock_places.values())
    
    for key, value in filters.items():
        if value is not None:
            if key in ["wheelchair_accessible", "elevator", "accessible_toilet", "parking_accessible"]:
                filtered = [p for p in filtered if p.get("accessibility_info", {}).get(key) == value]
    
    total = len(filtered)
    places = filtered[(page - 1) * size : page * size]
    
    return {
        "places": places,
        "total": total,
        "page": page,
        "size": size,
        "total_pages": (total + size - 1) // size
    }

@app.post("/places/save", response_model=dict)
async def save_place(save_request: dict, token: Optional[str] = Query(None)):
    """Save place to favorites"""
    new_id = max(mock_saved_places.keys()) + 1 if mock_saved_places else 1
    saved = {
        "id": new_id,
        "user_id": 1,  # Mock user
        "place_id": save_request.get("place_id"),
        "place": mock_places.get(save_request.get("place_id")),
        "notes": save_request.get("notes"),
        "saved_at": datetime.now()
    }
    mock_saved_places[new_id] = saved
    return saved

@app.get("/places/saved", response_model=list)
async def get_saved_places(
    page: int = Query(1),
    size: int = Query(10),
    token: Optional[str] = Query(None)
):
    """Get user's saved places"""
    saved = list(mock_saved_places.values())
    return saved[(page - 1) * size : page * size]

# ============= REVIEWS ENDPOINTS =============

@app.post("/reviews/create", response_model=dict)
async def create_review(review_data: ReviewCreateRequest, token: Optional[str] = Query(None)):
    """Create a review with accessibility feedback"""
    new_id = max(mock_reviews.keys()) + 1 if mock_reviews else 1
    review = {
        "id": new_id,
        "place_id": review_data.place_id,
        "user_id": 1,  # Mock user
        "user_name": "Anonymous User",
        "rating": review_data.rating,
        "text": review_data.text,
        "accessibility_feedback": review_data.accessibility_feedback.dict(),
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
    mock_reviews[new_id] = review
    
    # Update place rating
    if review_data.place_id in mock_places:
        place = mock_places[review_data.place_id]
        old_rating = place.get("rating", 0.0)
        count = place.get("review_count", 0)
        new_rating = (old_rating * count + review_data.rating) / (count + 1)
        place["rating"] = round(new_rating, 1)
        place["review_count"] = count + 1
    
    return review

@app.get("/reviews/place/{place_id}", response_model=dict)
async def get_place_reviews(
    place_id: int,
    page: int = Query(1),
    size: int = Query(10),
    token: Optional[str] = Query(None)
):
    """Get reviews for a place"""
    place_reviews = [r for r in mock_reviews.values() if r["place_id"] == place_id]
    total = len(place_reviews)
    reviews = place_reviews[(page - 1) * size : page * size]
    
    return {
        "reviews": reviews,
        "total": total,
        "page": page,
        "size": size
    }

@app.get("/reviews/my", response_model=dict)
async def get_user_reviews(
    page: int = Query(1),
    size: int = Query(10),
    token: Optional[str] = Query(None)
):
    """Get current user's reviews"""
    user_reviews = [r for r in mock_reviews.values() if r["user_id"] == 1]
    total = len(user_reviews)
    reviews = user_reviews[(page - 1) * size : page * size]
    
    return {
        "reviews": reviews,
        "total": total,
        "page": page,
        "size": size
    }

# ============= ROUTES ENDPOINTS =============

@app.post("/routes/create", response_model=dict)
async def create_route(route_data: RouteCreateRequest, token: Optional[str] = Query(None)):
    """Create an accessible route"""
    new_id = max(mock_routes.keys()) + 1 if mock_routes else 1
    route = {
        "id": new_id,
        "name": route_data.name,
        "description": route_data.description,
        "start_point": route_data.start_point.dict(),
        "end_point": route_data.end_point.dict(),
        "waypoints": [wp.dict() for wp in route_data.waypoints] if route_data.waypoints else [],
        "total_distance_km": 5.0,  # Mock calculation
        "estimated_duration_minutes": 30,
        "accessibility_tags": route_data.accessibility_tags,
        "difficulty_level": route_data.difficulty_level,
        "wheelchair_friendly": route_data.wheelchair_friendly,
        "created_by": 1,  # Mock user
        "created_at": datetime.now(),
        "updated_at": datetime.now()
    }
    mock_routes[new_id] = route
    return route

@app.get("/routes/accessible", response_model=dict)
async def get_routes(
    page: int = Query(1),
    size: int = Query(10),
    difficulty_level: Optional[str] = None,
    wheelchair_friendly: Optional[bool] = None,
    token: Optional[str] = Query(None)
):
    """Get accessible routes with optional filtering"""
    routes = list(mock_routes.values())
    
    if difficulty_level:
        routes = [r for r in routes if r["difficulty_level"] == difficulty_level]
    
    if wheelchair_friendly is not None:
        routes = [r for r in routes if r["wheelchair_friendly"] == wheelchair_friendly]
    
    total = len(routes)
    paginated = routes[(page - 1) * size : page * size]
    
    return {
        "routes": paginated,
        "total": total,
        "page": page,
        "size": size,
        "total_pages": (total + size - 1) // size
    }

@app.get("/routes/{route_id}", response_model=dict)
async def get_route(route_id: int, token: Optional[str] = Query(None)):
    """Get route details"""
    if route_id not in mock_routes:
        raise HTTPException(status_code=404, detail="Route not found")
    return mock_routes[route_id]

@app.post("/routes/{route_id}/add-place", response_model=dict)
async def add_place_to_route(
    route_id: int,
    place_id: int = Query(...),
    token: Optional[str] = Query(None)
):
    """Add a place to a route"""
    if route_id not in mock_routes:
        raise HTTPException(status_code=404, detail="Route not found")
    
    route = mock_routes[route_id]
    if "waypoints" not in route:
        route["waypoints"] = []
    
    if place_id in mock_places:
        place = mock_places[place_id]
        route["waypoints"].append({
            "latitude": place["latitude"],
            "longitude": place["longitude"],
            "name": place["name"]
        })
    
    return route

# ============= AUTH ENDPOINTS =============

@app.post("/auth/login/", response_model=dict)
async def login(login_request: dict):
    """Login endpoint (mock)"""
    return {
        "access_token": "mock_token_12345",
        "token_type": "bearer"
    }

# ============= HEALTH CHECK =============

@app.get("/health")
async def health_check():
    """Health check endpoint"""
    return {"status": "ok", "service": "Accessibility Navigation API"}

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
