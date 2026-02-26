import Database from 'better-sqlite3';
import bcrypt from 'bcryptjs';
import { places, markers, reviews, routes } from './seed-data.js';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

const db = new Database(join(__dirname, 'accessible_rostov.db'));

db.pragma('journal_mode = WAL');
db.pragma('foreign_keys = ON');

export function initDatabase() {
  db.exec(`
    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      login TEXT UNIQUE NOT NULL,
      email TEXT UNIQUE NOT NULL,
      password_hash TEXT NOT NULL,
      name TEXT DEFAULT '',
      role TEXT DEFAULT 'user',
      disability_types TEXT DEFAULT '',
      created_at TEXT DEFAULT (datetime('now'))
    );

    CREATE TABLE IF NOT EXISTS places (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      name TEXT NOT NULL,
      description TEXT DEFAULT '',
      category TEXT NOT NULL,
      address TEXT DEFAULT '',
      latitude REAL NOT NULL,
      longitude REAL NOT NULL,
      phone TEXT DEFAULT '',
      website TEXT DEFAULT '',
      images TEXT DEFAULT '[]',
      accessibility_tags TEXT DEFAULT '',
      disability_types TEXT DEFAULT '',
      rating REAL DEFAULT 0,
      reviews_count INTEGER DEFAULT 0,
      status TEXT DEFAULT 'active',
      created_at TEXT DEFAULT (datetime('now'))
    );

    CREATE TABLE IF NOT EXISTS markers (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER NOT NULL,
      title TEXT NOT NULL,
      description TEXT DEFAULT '',
      latitude REAL NOT NULL,
      longitude REAL NOT NULL,
      marker_type TEXT DEFAULT 'general',
      accessibility_tags TEXT DEFAULT '',
      photos TEXT DEFAULT '[]',
      votes_up INTEGER DEFAULT 0,
      votes_down INTEGER DEFAULT 0,
      created_at TEXT DEFAULT (datetime('now')),
      FOREIGN KEY (user_id) REFERENCES users(id)
    );

    CREATE TABLE IF NOT EXISTS reviews (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER NOT NULL,
      place_id INTEGER,
      marker_id INTEGER,
      text TEXT DEFAULT '',
      rating INTEGER DEFAULT 0,
      photos TEXT DEFAULT '[]',
      created_at TEXT DEFAULT (datetime('now')),
      FOREIGN KEY (user_id) REFERENCES users(id),
      FOREIGN KEY (place_id) REFERENCES places(id),
      FOREIGN KEY (marker_id) REFERENCES markers(id)
    );

    CREATE TABLE IF NOT EXISTS routes (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER NOT NULL,
      name TEXT NOT NULL,
      description TEXT DEFAULT '',
      disability_types TEXT DEFAULT '',
      total_distance_km REAL DEFAULT 0,
      total_duration_hours REAL DEFAULT 0,
      points TEXT DEFAULT '[]',
      created_at TEXT DEFAULT (datetime('now')),
      FOREIGN KEY (user_id) REFERENCES users(id)
    );
  `);

  // Seed default user if not exists
  const existingUser = db.prepare('SELECT id FROM users WHERE login = ?').get('demo');
  if (!existingUser) {
    const hash = bcrypt.hashSync('demo123', 10);
    db.prepare(`
      INSERT INTO users (login, email, password_hash, name, role, disability_types)
      VALUES (?, ?, ?, ?, ?, ?)
    `).run('demo', 'demo@example.com', hash, 'Demo User', 'user', 'wheelchair,limited_mobility');

    // Seed places
    const insertPlace = db.prepare(`
      INSERT INTO places (name, description, category, address, latitude, longitude, phone, website, images, accessibility_tags, disability_types, rating, reviews_count)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    `);
    for (const p of places) {
      insertPlace.run(p.name, p.description, p.category, p.address, p.latitude, p.longitude, p.phone, p.website, p.images, p.accessibility_tags, p.disability_types, p.rating, p.reviews_count);
    }

    // Seed markers
    const insertMarker = db.prepare(`
      INSERT INTO markers (user_id, title, description, latitude, longitude, marker_type, accessibility_tags, photos, votes_up, votes_down)
      VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    `);
    for (const m of markers) {
      insertMarker.run(m.user_id, m.title, m.description, m.latitude, m.longitude, m.marker_type, m.accessibility_tags, m.photos, m.votes_up, m.votes_down);
    }

    // Seed reviews
    const insertReview = db.prepare(`
      INSERT INTO reviews (user_id, place_id, marker_id, text, rating, photos)
      VALUES (?, ?, ?, ?, ?, ?)
    `);
    for (const r of reviews) {
      insertReview.run(r.user_id, r.place_id, r.marker_id, r.text, r.rating, r.photos);
    }

    // Seed routes
    const insertRoute = db.prepare(`
      INSERT INTO routes (user_id, name, description, disability_types, total_distance_km, total_duration_hours, points)
      VALUES (?, ?, ?, ?, ?, ?, ?)
    `);
    for (const rt of routes) {
      insertRoute.run(rt.user_id, rt.name, rt.description, rt.disability_types, rt.total_distance_km, rt.total_duration_hours, rt.points);
    }

    console.log('Database seeded with demo data.');
  }
}

export default db;
