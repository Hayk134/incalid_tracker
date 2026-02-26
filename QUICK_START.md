# Руководство быстрого старта

## 1. Клонирование и настройка (5 минут)

```bash
# Клонируйте репозиторий
git clone <repository-url>
cd incalid_tracker

# Переключитесь на ветку
git checkout navigation-app-for-disabled

# Откройте в Android Studio
# File → Open → Выберите папку проекта
```

## 2. Запуск сервера бэкенда (5 минут)

```bash
cd backend

# Установите зависимости Python
pip install -r requirements.txt

# Запустите сервер
python main.py

# Бэкенд запущен на http://localhost:8000
# Документация API на http://localhost:8000/docs
```

## 3. Сборка и запуск приложения Android (10 минут)

### Вариант A: Использование Android Studio (Самый простой)

1. Откройте проект в Android Studio
2. Дождитесь завершения синхронизации Gradle
3. Подключите устройство Android через USB или используйте эмулятор
4. Нажмите "Run" (зеленая кнопка)
5. Выберите целевое устройство

### Вариант B: Использование командной строки

```bash
# Соберите и запустите версию для отладки
./gradlew installDebug

# Или только соберите
./gradlew assembleDebug

# Установите на подключенное устройство
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## 4. Тестирование приложения

После запуска приложения:

1. **Экран карт** - Показывает доступные места рядом с Ростовом-на-Дону
2. **Вкладка Места** - Просмотрите и ищите доступные места
3. **Вкладка Маршруты** - Создавайте и просматривайте доступные маршруты
4. **Вкладка Сохраненные** - Просмотрите ваши сохраненные избранные места

### Примеры данных

Бэкенд включает макетные данные:
- 1 образец доступного кафе по координатам (47.2314, 39.7258)
- Вы можете создать дополнительные места/отзывы через API

### Тестирование точки API

```bash
# Получить все места
curl "http://localhost:8000/places/search?latitude=47.2314&longitude=39.7258&radius_km=5"

# Получить документацию API
curl http://localhost:8000/docs
```

## 5. Сборка релизного APK (10 минут)

Для создания готового к выпуску APK, который можно распространять или публиковать:

### Создание хранилища подписей

```bash
# Создайте хранилище (один раз)
keytool -genkey -v -keystore app/keystore/what_apps_keystore.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias investtravel -storepass keystore_pass -keypass key_pass
```

### Создание файла конфигурации хранилища

Создайте `app/keystore/keystore_config.properties`:

```properties
storeFile=./what_apps_keystore.keystore
storePassword=keystore_pass
keyAlias=investtravel
keyPassword=key_pass
```

### Сборка релизного APK

```bash
# Очистка и сборка
./gradlew clean assembleRelease

# Расположение APK: app/build/outputs/apk/release/app-release.apk
```

### Установка релизного APK

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Обзор структуры проекта

```
incalid_tracker/
├── app/                    # Основное приложение Android
│   ├── src/main/java/     # Исходный код Kotlin
│   │   └── app/what/investtravel/
│   │       ├── data/      # Сервисы API, модели
│   │       ├── features/  # Функции приложения (места, отзывы, маршруты)
│   │       └── MainActivity.kt
│   ├── keystore/          # Ключи подписи для релиза
│   └── build.gradle.kts   # Конфигурация сборки приложения
│
├── backend/               # Сервер бэкенда FastAPI
│   ├── main.py           # Реализация API
│   └── requirements.txt   # Зависимости Python
│
├── core/                 # Общие модули
│   ├── foundation/       # DI, утилиты
│   └── navigation/       # Компоненты навигации
│
├── PROJECT_README.md     # Полная документация
├── APK_BUILD_GUIDE.md    # Руководство по сборке релиза
└── QUICK_START.md        # Этот файл
```

## Ключевые файлы

**Приложение Android**:
- `app/src/main/java/app/what/investtravel/data/remote/Models.kt` - Модели данных
- `app/src/main/java/app/what/investtravel/data/remote/Services.kt` - Сервисы API
- `app/src/main/java/app/what/investtravel/features/main/presentation/MainView.kt` - UI карт
- `app/build.gradle.kts` - Зависимости и конфигурация сборки

**Бэкенд**:
- `backend/main.py` - Реализация точек API
- `backend/requirements.txt` - Зависимости

## Конфигурация

### URL бэкенда

Отредактируйте `app/src/main/java/app/what/investtravel/data/remote/ApiClient.kt`:

```kotlin
companion object {
    const val BASE_URL = "http://your-server:8000"  // Измените это
}
```

### Ключ API карт

Приложение настроено для использования:
```
576b91a0-ac5c-421a-a932-38cbe1d4c633
```

## Решение проблем

### "Module initialization failed"
```bash
# Очистите кэш Gradle
./gradlew clean

# Синхронизируйте проект
./gradlew sync
```

### Ошибка подключения бэкенда
1. Убедитесь, что бэкенд работает: `python backend/main.py`
2. Проверьте URL в `ApiClient.kt` соответствует адресу бэкенда
3. Убедитесь, что брандмауэр разрешает порт 8000

### Не удается найти SDK
```bash
# Обновите расположение SDK в Android Studio
Tools → SDK Manager → Android SDK Location
```

### Ошибки сборки
```bash
# Полная чистая сборка
./gradlew clean
./gradlew build
```

## Контрольный список функций

- [x] Карты с маркерами доступных мест
- [x] Браузер мест и подробный просмотр
- [x] Отображение информации о доступности
- [x] Отзывы с обратной связью по доступности
- [x] Планирование маршрутов с тегами доступности
- [x] Управление сохраненными местами
- [x] Аутентификация пользователя (макет)
- [x] REST API бэкенда
- [x] Конфигурация релизной сборки
- [x] Правильная подпись для APK

## Следующие шаги

1. **Настройте бэкенд**: обновите базу данных и модель данных при необходимости
2. **Подключите реальные карты**: интегрируйте Google Maps или SDK Yandex Maps
3. **Разверните бэкенд**: разверните на AWS, Google Cloud, Azure или Heroku
4. **Добавьте аутентификацию**: подключитесь к реальной системе аутентификации
5. **Опубликуйте в Play Store**: загрузите релизный APK в Google Play

## Полезные команды

```bash
# Сборка отладки
./gradlew assembleDebug

# Сборка релиза
./gradlew assembleRelease

# Запуск на устройстве
./gradlew installDebug

# Просмотр журналов
adb logcat

# Список устройств
adb devices

# Очистка данных приложения
adb shell pm clear app.what.investtravel

# Сборка APK + Bundle
./gradlew build

# Проверка зависимостей
./gradlew dependencies
```

## Документация

- **Полный гайд**: смотрите [PROJECT_README.md](PROJECT_README.md)
- **Сборка релиза**: смотрите [APK_BUILD_GUIDE.md](APK_BUILD_GUIDE.md)
- **Настройка бэкенда**: смотрите [backend/README.md](backend/README.md)

## Получить справку

1. Проверьте журналы: `adb logcat | grep investtravel`
2. Просмотрите документацию API: http://localhost:8000/docs
3. Проверьте выход консоли бэкенда
4. Просмотрите комментарии кода и документацию

## Контакт поддержки

По вопросам доступности или запросам функций:
- Алексей Олегович Серов (@LeksGray)
- Ростовский государственный экономический университет (РИНХ)

---

**Готовы к сборке!** Начните с шага 1 выше, и у вас будет приложение, работающее локально примерно за 30 минут.
