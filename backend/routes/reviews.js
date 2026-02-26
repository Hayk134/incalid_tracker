import { Router } from 'express';
import db from '../db.js';

const router = Router();

// GET /reviews/place/:placeId
router.get('/place/:placeId', (req, res) => {
  try {
    const reviews = db.prepare(`
      SELECT r.*, u.name as user_name, u.login as user_login 
      FROM reviews r 
      LEFT JOIN users u ON r.user_id = u.id 
      WHERE r.place_id = ? 
      ORDER BY r.created_at DESC
    `).all(req.params.placeId);
    res.json(reviews);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /reviews/marker/:markerId
router.get('/marker/:markerId', (req, res) => {
  try {
    const reviews = db.prepare(`
      SELECT r.*, u.name as user_name, u.login as user_login 
      FROM reviews r 
      LEFT JOIN users u ON r.user_id = u.id 
      WHERE r.marker_id = ? 
      ORDER BY r.created_at DESC
    `).all(req.params.markerId);
    res.json(reviews);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /reviews
router.post('/', (req, res) => {
  try {
    const { place_id, marker_id, text, rating, photos } = req.body;
    if (!text || !rating) {
      return res.status(400).json({ error: 'Text and rating required' });
    }
    if (!place_id && !marker_id) {
      return res.status(400).json({ error: 'Either place_id or marker_id required' });
    }

    const result = db.prepare(`
      INSERT INTO reviews (user_id, place_id, marker_id, text, rating, photos)
      VALUES (?, ?, ?, ?, ?, ?)
    `).run(req.userId, place_id || null, marker_id || null, text, rating, photos || '[]');

    // Update place rating if place_id
    if (place_id) {
      const avgResult = db.prepare('SELECT AVG(rating) as avg_rating, COUNT(*) as cnt FROM reviews WHERE place_id = ?').get(place_id);
      db.prepare('UPDATE places SET rating = ?, reviews_count = ? WHERE id = ?').run(
        Math.round(avgResult.avg_rating * 10) / 10,
        avgResult.cnt,
        place_id
      );
    }

    const review = db.prepare(`
      SELECT r.*, u.name as user_name, u.login as user_login 
      FROM reviews r 
      LEFT JOIN users u ON r.user_id = u.id 
      WHERE r.id = ?
    `).get(result.lastInsertRowid);
    res.status(201).json(review);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
