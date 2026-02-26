import { Router } from 'express';
import db from '../db.js';

const router = Router();

// POST /routes/generate - Generate accessible route
router.post('/generate', (req, res) => {
  try {
    const { name, description, start_lat, start_lon, end_lat, end_lon, disability_types, avoid_stairs, prefer_ramps, max_distance_km } = req.body;

    // Find places along a rough corridor between start and end
    const minLat = Math.min(start_lat || 47.22, end_lat || 47.24) - 0.005;
    const maxLat = Math.max(start_lat || 47.22, end_lat || 47.24) + 0.005;
    const minLon = Math.min(start_lon || 39.70, end_lon || 39.75) - 0.005;
    const maxLon = Math.max(start_lon || 39.70, end_lon || 39.75) + 0.005;

    let placesQuery = `
      SELECT * FROM places 
      WHERE status = 'active' 
      AND latitude BETWEEN ? AND ? 
      AND longitude BETWEEN ? AND ?
    `;
    const params = [minLat, maxLat, minLon, maxLon];

    if (disability_types) {
      const types = disability_types.split(',');
      const clauses = types.map(() => 'disability_types LIKE ?');
      placesQuery += ' AND (' + clauses.join(' OR ') + ')';
      types.forEach(t => params.push(`%${t.trim()}%`));
    }

    if (prefer_ramps) {
      placesQuery += ' AND accessibility_tags LIKE ?';
      params.push('%ramp%');
    }

    placesQuery += ' ORDER BY rating DESC LIMIT 10';
    const routePlaces = db.prepare(placesQuery).all(...params);

    // Build route points from found places
    const points = routePlaces.map((p, idx) => ({
      name: p.name,
      latitude: p.latitude,
      longitude: p.longitude,
      order: idx,
      accessibility_tags: p.accessibility_tags,
      category: p.category,
      address: p.address
    }));

    // Calculate approximate distance
    let totalDistance = 0;
    for (let i = 1; i < points.length; i++) {
      const dlat = points[i].latitude - points[i - 1].latitude;
      const dlon = points[i].longitude - points[i - 1].longitude;
      totalDistance += Math.sqrt(dlat * dlat + dlon * dlon) * 111;
    }

    const routeName = name || `Доступный маршрут (${new Date().toLocaleDateString('ru-RU')})`;
    const routeDesc = description || `Маршрут с учётом доступности для: ${(disability_types || 'все').split(',').join(', ')}`;

    const result = db.prepare(`
      INSERT INTO routes (user_id, name, description, disability_types, total_distance_km, total_duration_hours, points)
      VALUES (?, ?, ?, ?, ?, ?, ?)
    `).run(
      req.userId,
      routeName,
      routeDesc,
      disability_types || '',
      Math.round(totalDistance * 10) / 10,
      Math.round(totalDistance / 4 * 10) / 10, // ~4km/h walking
      JSON.stringify(points)
    );

    const route = db.prepare('SELECT * FROM routes WHERE id = ?').get(result.lastInsertRowid);
    res.status(201).json({
      ...route,
      points: JSON.parse(route.points)
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /routes - List user routes
router.get('/', (req, res) => {
  try {
    const routes = db.prepare('SELECT * FROM routes WHERE user_id = ? ORDER BY created_at DESC').all(req.userId);
    res.json(routes.map(r => ({ ...r, points: JSON.parse(r.points) })));
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// GET /routes/:id
router.get('/:id', (req, res) => {
  try {
    const route = db.prepare('SELECT * FROM routes WHERE id = ?').get(req.params.id);
    if (!route) return res.status(404).json({ error: 'Route not found' });
    res.json({ ...route, points: JSON.parse(route.points) });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// DELETE /routes/:id
router.delete('/:id', (req, res) => {
  try {
    const route = db.prepare('SELECT * FROM routes WHERE id = ? AND user_id = ?').get(req.params.id, req.userId);
    if (!route) return res.status(404).json({ error: 'Route not found' });
    db.prepare('DELETE FROM routes WHERE id = ?').run(req.params.id);
    res.json({ success: true });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
