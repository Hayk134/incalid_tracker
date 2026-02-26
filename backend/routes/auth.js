import { Router } from 'express';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import db from '../db.js';

const router = Router();
const JWT_SECRET = process.env.JWT_SECRET || 'accessible-rostov-secret-key-2026';

router.post('/login', (req, res) => {
  try {
    const { login, password } = req.body;
    if (!login || !password) {
      return res.status(400).json({ error: 'Login and password required' });
    }

    const user = db.prepare('SELECT * FROM users WHERE login = ? OR email = ?').get(login, login);
    if (!user) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    if (!bcrypt.compareSync(password, user.password_hash)) {
      return res.status(401).json({ error: 'Invalid credentials' });
    }

    const token = jwt.sign({ id: user.id, login: user.login, role: user.role }, JWT_SECRET, { expiresIn: '30d' });
    res.json({ token, user: { id: user.id, login: user.login, name: user.name, email: user.email, role: user.role, disability_types: user.disability_types } });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

router.post('/register', (req, res) => {
  try {
    const { login, email, password, name, disability_types } = req.body;
    if (!login || !email || !password) {
      return res.status(400).json({ error: 'Login, email, and password required' });
    }

    const existing = db.prepare('SELECT id FROM users WHERE login = ? OR email = ?').get(login, email);
    if (existing) {
      return res.status(409).json({ error: 'User already exists' });
    }

    const hash = bcrypt.hashSync(password, 10);
    const result = db.prepare(`
      INSERT INTO users (login, email, password_hash, name, disability_types)
      VALUES (?, ?, ?, ?, ?)
    `).run(login, email, hash, name || '', disability_types || '');

    const token = jwt.sign({ id: result.lastInsertRowid, login, role: 'user' }, JWT_SECRET, { expiresIn: '30d' });
    res.status(201).json({ token, user: { id: result.lastInsertRowid, login, name: name || '', email, role: 'user', disability_types: disability_types || '' } });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

export default router;
export { JWT_SECRET };
