import { Router } from 'express';
import db from '../db.js';

const router = Router();

// GET /markers - List markers (optionally near a location)
router.get('/', (req, res) => {
  try {
    const { lat, lon, radius, page = 1, size = 50 } = req.query;
    let query = 'SELECT m.*, u.name as user_name, u.login as user_login FROM markers m LEFT JOIN users u ON m.user_id = u.id';
    const params = [];

    // Simple bounding box filter if lat/lon provided
    if (lat && lon && radius) {
      const latF = parseFloat(lat);
      const lonF = parseFloat(lon);
      const r = parseFloat(radius) / 111.0; // rough km to degrees
      query += ' WHERE m.latitude BETWEEN ? AND ? AND m.longitude BETWEEN ? AND ?';
      params.push(latF - r, latF + r, lonF - r, lonF + r);
    }

    const countQuery = query.replace('SELECT m.*, u.name as user_name, u.login as user_login FROM markers m LEFT JOIN users u ON m.user_id = u.id', 'SELECT COUNT(*) as total FROM markers m');
    const totalResult = params.length > 0 ? db.prepare(countQuery).get(...params) : db.prepare(countQuery).get();
    const total = totalResult.total;

    const offset = (parseInt(page) - 1) * parseInt(size);
    query += ' ORDER BY m.created_at DESC LIMIT ? OFFSET ?';
    params.push(parseInt(size), offset);

    const markersResult = db.prepare(query).all(...params);
    res.json({ markers: markersResult, total, page: parseInt(page), size: parseInt(size) });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /markers/:id
router.get('/:id', (req, res) => {
  try {
    const marker = db.prepare(`
      SELECT m.*, u.name as user_name, u.login as user_login 
      FROM markers m LEFT JOIN users u ON m.user_id = u.id 
      WHERE m.id = ?
    `).get(req.params.id);
    if (!marker) return res.status(404).json({ error: 'Marker not found' });

    const markerReviews = db.prepare(`
      SELECT r.*, u.name as reviewer_name 
      FROM reviews r 
      LEFT JOIN users u ON r.user_id = u.id 
      WHERE r.marker_id = ? 
      ORDER BY r.created_at DESC
    `).all(req.params.id);

    res.json({ ...marker, reviews: markerReviews });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /markers
router.post('/', (req, res) => {
  try {
    const { title, description, latitude, longitude, marker_type, accessibility_tags, photos } = req.body;
    if (!title || !latitude || !longitude) {
      return res.status(400).json({ error: 'Title, latitude, and longitude required' });
    }

    const result = db.prepare(`
      INSERT INTO markers (user_id, title, description, latitude, longitude, marker_type, accessibility_tags, photos)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    `).run(req.userId, title, description || '', latitude, longitude, marker_type || 'general', accessibility_tags || '', photos || '[]');

    const marker = db.prepare('SELECT * FROM markers WHERE id = ?').get(result.lastInsertRowid);
    res.status(201).json(marker);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// PUT /markers/:id/vote
router.put('/:id/vote', (req, res) => {
  try {
    const { vote } = req.body; // 'up' or 'down'
    const marker = db.prepare('SELECT * FROM markers WHERE id = ?').get(req.params.id);
    if (!marker) return res.status(404).json({ error: 'Marker not found' });

    if (vote === 'up') {
      db.prepare('UPDATE markers SET votes_up = votes_up + 1 WHERE id = ?').run(req.params.id);
    } else if (vote === 'down') {
      db.prepare('UPDATE markers SET votes_down = votes_down + 1 WHERE id = ?').run(req.params.id);
    } else {
      return res.status(400).json({ error: 'Vote must be "up" or "down"' });
    }

    const updated = db.prepare('SELECT * FROM markers WHERE id = ?').get(req.params.id);
    res.json(updated);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
