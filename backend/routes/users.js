import { Router } from 'express';
import db from '../db.js';

const router = Router();

router.get('/me', (req, res) => {
  try {
    const user = db.prepare('SELECT id, login, email, name, role, disability_types, created_at FROM users WHERE id = ?').get(req.userId);
    if (!user) return res.status(404).json({ error: 'User not found' });
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

router.put('/me', (req, res) => {
  try {
    const { name, email, disability_types } = req.body;
    db.prepare(`
      UPDATE users SET name = COALESCE(?, name), email = COALESCE(?, email), disability_types = COALESCE(?, disability_types) WHERE id = ?
    `).run(name, email, disability_types, req.userId);

    const user = db.prepare('SELECT id, login, email, name, role, disability_types, created_at FROM users WHERE id = ?').get(req.userId);
    res.json(user);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
