import express from 'express';
import cors from 'cors';
import jwt from 'jsonwebtoken';
import { initDatabase } from './db.js';
import authRoutes, { JWT_SECRET } from './routes/auth.js';
import usersRoutes from './routes/users.js';
import placesRoutes from './routes/places.js';
import markersRoutes from './routes/markers.js';
import reviewsRoutes from './routes/reviews.js';
import routesRoutes from './routes/routes.js';

const app = express();
const PORT = process.env.PORT || 3001;

// Middleware
app.use(cors());
app.use(express.json({ limit: '10mb' }));

// Auth middleware
function authMiddleware(req, res, next) {
  const authHeader = req.headers.authorization;
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Authorization required' });
  }
  try {
    const token = authHeader.split(' ')[1];
    const decoded = jwt.verify(token, JWT_SECRET);
    req.userId = decoded.id;
    req.userRole = decoded.role;
    next();
  } catch {
    return res.status(401).json({ error: 'Invalid token' });
  }
}

// Optional auth - sets userId if token present, but doesn't block
function optionalAuth(req, res, next) {
  const authHeader = req.headers.authorization;
  if (authHeader && authHeader.startsWith('Bearer ')) {
    try {
      const token = authHeader.split(' ')[1];
      const decoded = jwt.verify(token, JWT_SECRET);
      req.userId = decoded.id;
      req.userRole = decoded.role;
    } catch {
      // ignore invalid token
    }
  }
  next();
}

// Initialize database
initDatabase();

// Public routes
app.use('/auth', authRoutes);

// Places and markers are publicly readable
app.use('/places', optionalAuth, placesRoutes);
app.use('/markers', optionalAuth, markersRoutes);
app.use('/reviews', optionalAuth, reviewsRoutes);

// Protected routes
app.use('/users', authMiddleware, usersRoutes);
app.use('/routes', authMiddleware, routesRoutes);

// Markers POST and Reviews POST need auth
// (already handled since they use req.userId)

// Health check
app.get('/', (req, res) => {
  res.json({
    name: 'Accessible Rostov API',
    version: '1.0.0',
    endpoints: ['/auth', '/users', '/places', '/markers', '/reviews', '/routes']
  });
});

// Disability types reference
app.get('/reference/disability-types', (req, res) => {
  res.json([
    { id: 'wheelchair', name: 'Колясочники', description: 'Пользователи инвалидных колясок' },
    { id: 'visually_impaired', name: 'Слабовидящие', description: 'Люди с нарушениями зрения' },
    { id: 'hearing_impaired', name: 'Слабослышащие', description: 'Люди с нарушениями слуха' },
    { id: 'limited_mobility', name: 'Ограниченная подвижность', description: 'Люди с ограниченной подвижностью' },
    { id: 'cognitive', name: 'Когнитивные нарушения', description: 'Люди с когнитивными особенностями' },
    { id: 'elderly', name: 'Пожилые', description: 'Пожилые люди' },
    { id: 'stroller', name: 'С колясками', description: 'Родители с детскими колясками' }
  ]);
});

// Accessibility tags reference
app.get('/reference/accessibility-tags', (req, res) => {
  res.json([
    { id: 'ramp', name: 'Пандус', icon: 'ramp' },
    { id: 'wide_door', name: 'Широкие двери', icon: 'door' },
    { id: 'elevator', name: 'Лифт', icon: 'elevator' },
    { id: 'tactile_tiles', name: 'Тактильная плитка', icon: 'braille' },
    { id: 'audio_guide', name: 'Аудиогид', icon: 'headphones' },
    { id: 'vibro_signal', name: 'Вибросигнал', icon: 'vibration' },
    { id: 'subtitles', name: 'Субтитры', icon: 'subtitles' },
    { id: 'handrails', name: 'Поручни', icon: 'handrail' },
    { id: 'parking_disabled', name: 'Парковка для инвалидов', icon: 'parking' },
    { id: 'low_floor_transport', name: 'Низкопольный транспорт', icon: 'bus' },
    { id: 'bench', name: 'Скамейки', icon: 'bench' },
    { id: 'rest_area', name: 'Зона отдыха', icon: 'rest' },
    { id: 'simple_navigation', name: 'Простая навигация', icon: 'navigation' },
    { id: 'braille', name: 'Шрифт Брайля', icon: 'braille' }
  ]);
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Accessible Rostov API running on port ${PORT}`);
});
