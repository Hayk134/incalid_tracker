# Сборка APK для приложения навигации доступности

Это руководство содержит пошаговые инструкции по сборке и выпуску Android APK для платформы навигации доступности.

## Предварительные требования

- Android SDK (API уровня 26+)
- Android Studio (последняя версия)
- Java Development Kit (JDK 11+)
- Gradle 8.0+

## Структура проекта

```
app/
├── src/main/
│   ├── java/app/what/investtravel/
│   │   ├── data/          # Слой данных (сервисы API, модели)
│   │   ├── domain/        # Слой домена (контроллеры, управление состоянием)
│   │   ├── features/      # Модули функций
│   │   │   ├── main/      # Основная функция/карты
│   │   │   ├── places/    # Браузер мест
│   │   │   ├── reviews/   # Система отзывов
│   │   │   └── routes/    # Планирование маршрутов
│   │   └── MainActivity.kt
│   └── AndroidManifest.xml
├── keystore/             # Конфигурация подписи
└── build.gradle.kts      # Конфигурация сборки

core/
├── foundation/           # Основные утилиты и DI
└── navigation/           # Компоненты навигации

backend/
├── main.py              # Бэкенд FastAPI
└── requirements.txt     # Зависимости бэкенда
```

## Конфигурация API

Приложение использует следующую точку API для сервисов бэкенда:

```
Base URL: http://45.155.207.232:1478
Maps API Key: 576b91a0-ac5c-421a-a932-38cbe1d4c633
```

Обновите URL бэкенда в `app/src/main/java/app/what/investtravel/data/remote/ApiClient.kt` если развертываете на другом сервере.

## Конфигурация подписи

### Вариант 1: Использование файла свойств хранилища (Рекомендуется)

1. Создайте `app/keystore/keystore_config.properties`:

```properties
storeFile=./what_apps_keystore.keystore
storePassword=your_keystore_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

2. Поместите файл хранилища в `app/keystore/what_apps_keystore.keystore`

3. Соберите релизный APK:
```bash
./gradlew assembleRelease
```

### Вариант 2: Использование переменных окружения

Установите переменные окружения перед сборкой:

```bash
export KEYSTORE_PASSWORD="your_keystore_password"
export RELEASE_SIGN_KEY_ALIAS="your_key_alias"
export RELEASE_SIGN_KEY_PASSWORD="your_key_password"

./gradlew assembleRelease
```

### Вариант 3: Создание новго хранилища

Если у вас нет хранилища, создайте его:

```bash
keytool -genkey -v -keystore app/keystore/what_apps_keystore.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias investtravel -storepass keystore_password \
  -keypass key_password
```

Затем используйте Вариант 1 с паролями, которые вы указали.

## Сборка отладочного APK

```bash
# Соберите отладочный APK
./gradlew assembleDebug

# Выход: app/build/outputs/apk/debug/app-debug.apk
```

## Сборка релизного APK (Подписанный)

### Шаг 1: Убедитесь в конфигурации подписи

Создайте `app/keystore/keystore_config.properties` или установите переменные окружения (см. раздел Конфигурация подписи выше).

### Шаг 2: Соберите релизный APK

```bash
# Очистка и сборка
./gradlew clean assembleRelease

# Или просто соберите
./gradlew assembleRelease

# Выход: app/build/outputs/apk/release/app-release.apk
```

### Шаг 3: Проверьте подпись APK

```bash
jarsigner -verify -verbose app/build/outputs/apk/release/app-release.apk
```

## Сборка Bundle (AAB) для Play Store

Android App Bundle требуется для загрузки в Google Play Store:

```bash
./gradlew bundleRelease

# Выход: app/build/outputs/bundle/release/app-release.aab
```

## Конфигурация ProGuard

ProGuard включен для релизной сборки для:
- Минимизации размера APK путем удаления неиспользуемого кода
- Обфускации кода для безопасности
- Оптимизации производительности

Файлы конфигурации:
- `proguard-rules.pro` - Пользовательские правила ProGuard
- `getDefaultProguardFile("proguard-android-optimize.txt")` - Стандартные правила Android

### Важно: Сохранение классов сериализации

Следующие классы НЕ должны быть обфусцированы (уже настроено в proguard-rules.pro):

```
-keep class app.what.investtravel.data.remote.** { *; }
-keep class kotlinx.serialization.** { *; }
```

## Установка APK

### Установка отладочного APK

```bash
./gradlew installDebug
```

### Установка релизного APK

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

### Установка из Android Studio

1. Подключите устройство Android через USB (или используйте эмулятор)
2. Нажмите "Run" или "Debug" в Android Studio
3. Выберите целевое устройство

## Оптимизация производительности

Релизная сборка включает:
- **Минимизация кода**: ProGuard удаляет неиспользуемый код
- **Сжатие ресурсов**: неиспользуемые ресурсы удаляются
- **Оптимизация кода**: ProGuard оптимизирует байт-код

Для проверки оптимизации:

```bash
# Проверьте размер APK
du -h app/build/outputs/apk/release/app-release.apk

# Извлеките и проверьте
unzip -l app/build/outputs/apk/release/app-release.apk | wc -l
```

## Развертывание бэкенда

Перед выпуском убедитесь, что API бэкенда развернут:

```bash
# Установите зависимости бэкенда
pip install -r backend/requirements.txt

# Запустите бэкенд локально
python backend/main.py

# Для продакшена разверните на облачный сервис:
# - AWS EC2
# - Google Cloud Run
# - Azure App Service
# - DigitalOcean
# - Heroku
```

Обновите `ApiClient.kt` с URL бэкенда продакшена.

## Публикация в Google Play Store

1. Создайте аккаунт Google Play Developer
2. Создайте новое приложение в Google Play Console
3. Заполните детали приложения, скриншоты, описание
4. Загрузите подписанный APK/AAB (`app-release.aab`)
5. Настройте цену и распределение
6. Отправьте на рассмотрение

## Решение проблем

### Ошибка сборки с "Missing Keystore"

Решение: создайте файл хранилища или установите переменные окружения (см. Конфигурация подписи).

### APK не устанавливается

- Проверьте, что `minSdk` соответствует уровню ОС устройства
- Убедитесь, что APK правильно подписан: `jarsigner -verify app-release.apk`

### Большой размер APK

- Проверьте наличие больших активов в `src/main/assets/`
- Убедитесь, что ProGuard включен для релизной сборки
- Используйте bundle (AAB) вместо APK

### Проблемы подключения к API

- Проверьте, что сервер бэкенда работает
- Проверьте подключение к сети
- Обновите `BASE_URL` в `ApiClient.kt`
- Проверьте правила брандмауэра если находитесь в корпоративной сети

## Контрольный список релиза

- [ ] Обновите номер версии/имя в `build.gradle.kts`
- [ ] Обновите `versionCode` и `versionName`
- [ ] Настройте подпись (пароль хранилища и т.д.)
- [ ] Тестирование на физическом устройстве (несколько уровней API если возможно)
- [ ] Проверка подключения к API
- [ ] Сборка релизного APK/AAB
- [ ] Проверка подписи APK
- [ ] Тестирование установки из файла APK
- [ ] Создание заметок о выпуске
- [ ] Загрузка в Play Store

## Управление версией

Обновите в `app/build.gradle.kts`:

```kotlin
versionCode = 2        // Увеличивайте для каждого выпуска
versionName = "1.1"    // Семантическое версионирование
```

## Поддержка

При возникновении проблем во время сборки:
1. Проверьте синхронизацию Gradle (File → Sync Now)
2. Очистите проект: `./gradlew clean`
3. Инвалидировать кэши: File → Invalidate Caches
4. Проверьте, что версии SDK соответствуют `build.gradle.kts`

## Дополнительные ресурсы

- [Документация системы сборки Android](https://developer.android.com/studio/build)
- [Подпись вашего приложения](https://developer.android.com/studio/publish/app-signing)
- [Подготовка к выпуску](https://developer.android.com/studio/publish/preparing)
