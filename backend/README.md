# Accessibility Navigation Platform - Backend API

REST API backend for the accessibility navigation Android app. Provides endpoints for managing accessible places, user reviews with accessibility feedback, and accessible routes.

## Setup

### Prerequisites
- Python 3.8+
- pip

### Installation

1. Install dependencies:
```bash
pip install -r requirements.txt
```

2. Run the API server:
```bash
python main.py
```

The API will be available at `http://localhost:8000`

## API Endpoints

### Places
- `GET /places/search` - Search accessible places by coordinates and filters
- `GET /places/{place_id}` - Get place details
- `POST /places/create` - Create a new place
- `POST /places/filter` - Filter places by accessibility criteria
- `POST /places/save` - Save place to favorites
- `GET /places/saved` - Get saved places

### Reviews
- `POST /reviews/create` - Create a review with accessibility feedback
- `GET /reviews/place/{place_id}` - Get reviews for a place
- `GET /reviews/my` - Get user's reviews

### Routes
- `POST /routes/create` - Create an accessible route
- `GET /routes/accessible` - Get accessible routes
- `GET /routes/{route_id}` - Get route details
- `POST /routes/{route_id}/add-place` - Add a place to a route

### Auth
- `POST /auth/login/` - Login (mock implementation)

### Health
- `GET /health` - Health check

## Testing with cURL

```bash
# Search places
curl "http://localhost:8000/places/search?latitude=47.2314&longitude=39.7258&radius_km=5"

# Get place details
curl "http://localhost:8000/places/1"

# Create a review
curl -X POST "http://localhost:8000/reviews/create" \
  -H "Content-Type: application/json" \
  -d '{
    "place_id": 1,
    "rating": 4.5,
    "text": "Great accessible cafe!",
    "accessibility_feedback": {
      "difficulty_level": "easy",
      "staff_helpfulness": 5
    }
  }'
```

## Database

Currently uses in-memory mock data storage. For production, integrate with a database like PostgreSQL or MongoDB.

Mock data includes:
- 1 sample accessible place (Accessible Cafe Downtown in Rostov-on-Don)
- Sample routes and reviews can be created through API

## Deployment

To deploy to production:
1. Set up a proper database (PostgreSQL recommended)
2. Configure authentication/JWT tokens
3. Deploy to cloud service (AWS, Google Cloud, Azure, etc.)
4. Update the `BASE_URL` in the Android app to point to the deployed API

## License

MIT
