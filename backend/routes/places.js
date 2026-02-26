import { Router } from 'express';
import db from '../db.js';

const router = Router();

// GET /places - List with filters
router.get('/', (req, res) => {
  try {
    const { category, disability_types, tags, search, page = 1, size = 20, lat, lon, radius } = req.query;
    let query = 'SELECT * FROM places WHERE status = ?';
    const params = ['active'];

    if (category) {
      query += ' AND category = ?';
      params.push(category);
    }

    if (disability_types) {
      const types = disability_types.split(',');
      const clauses = types.map(() => 'disability_types LIKE ?');
      query += ' AND (' + clauses.join(' OR ') + ')';
      types.forEach(t => params.push(`%${t.trim()}%`));
    }

    if (tags) {
      const tagList = tags.split(',');
      const clauses = tagList.map(() => 'accessibility_tags LIKE ?');
      query += ' AND (' + clauses.join(' OR ') + ')';
      tagList.forEach(t => params.push(`%${t.trim()}%`));
    }

    if (search) {
      query += ' AND (name LIKE ? OR description LIKE ? OR address LIKE ?)';
      params.push(`%${search}%`, `%${search}%`, `%${search}%`);
    }

    // Count total
    const countQuery = query.replace('SELECT *', 'SELECT COUNT(*) as total');
    const totalResult = db.prepare(countQuery).get(...params);
    const total = totalResult.total;

    // Pagination
    const offset = (parseInt(page) - 1) * parseInt(size);
    query += ' ORDER BY rating DESC LIMIT ? OFFSET ?';
    params.push(parseInt(size), offset);

    const placesResult = db.prepare(query).all(...params);

    res.json({
      places: placesResult,
      total,
      page: parseInt(page),
      size: parseInt(size)
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /places/categories
router.get('/categories', (req, res) => {
  const categories = [
    { id: 'cafe', name: 'Кафе', icon: 'coffee' },
    { id: 'restaurant', name: 'Рестораны', icon: 'utensils' },
    { id: 'shop', name: 'Магазины', icon: 'shopping-cart' },
    { id: 'clinic', name: 'Поликлиники', icon: 'hospital' },
    { id: 'theater', name: 'Театры', icon: 'theater-masks' },
    { id: 'cinema', name: 'Кинотеатры', icon: 'film' },
    { id: 'transport_stop', name: 'Остановки', icon: 'bus' },
    { id: 'park', name: 'Парки', icon: 'tree' },
    { id: 'museum', name: 'Музеи', icon: 'landmark' },
    { id: 'pharmacy', name: 'Аптеки', icon: 'pills' },
    { id: 'bank', name: 'Банки', icon: 'credit-card' },
    { id: 'government', name: 'Госуслуги', icon: 'building' },
    { id: 'sport', name: 'Спорт', icon: 'dumbbell' },
    { id: 'library', name: 'Библиотеки', icon: 'book' }
  ];
  res.json(categories);
});

// GET /places/:id
router.get('/:id', (req, res) => {
  try {
    const place = db.prepare('SELECT * FROM places WHERE id = ?').get(req.params.id);
    if (!place) return res.status(404).json({ error: 'Place not found' });

    const placeReviews = db.prepare(`
      SELECT r.*, u.name as user_name, u.login as user_login 
      FROM reviews r 
      LEFT JOIN users u ON r.user_id = u.id 
      WHERE r.place_id = ? 
      ORDER BY r.created_at DESC
    `).all(req.params.id);

    res.json({ ...place, reviews: placeReviews });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /places
router.post('/', (req, res) => {
  try {
    const { name, description, category, address, latitude, longitude, phone, website, images, accessibility_tags, disability_types } = req.body;
    if (!name || !category || !latitude || !longitude) {
      return res.status(400).json({ error: 'Name, category, latitude, and longitude required' });
    }

    const result = db.prepare(`
      INSERT INTO places (name, description, category, address, latitude, longitude, phone, website, images, accessibility_tags, disability_types)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    `).run(name, description || '', category, address || '', latitude, longitude, phone || '', website || '', images || '[]', accessibility_tags || '', disability_types || '');

    const place = db.prepare('SELECT * FROM places WHERE id = ?').get(result.lastInsertRowid);
    res.status(201).json(place);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
