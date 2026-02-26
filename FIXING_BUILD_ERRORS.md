# Исправление ошибок сборки

## Если вы видите ошибки компиляции после очистки

### Проблема 1: "Unresolved reference" для Hotel классов

Все файлы отелей были удалены. Если IDE ещё показывает ошибки:

1. **Синхронизируйте Gradle**:
   ```bash
   ./gradlew clean
   ./gradlew build --refresh-dependencies
   ```

2. **В Android Studio**:
   - File → Invalidate Caches → Invalidate and Restart
   - После перезагрузки IDE нажмите Ctrl+Shift+O (или Cmd+Shift+O на Mac) для переиндексирования

3. **Убедитесь, что удалены все файлы**:
   ```bash
   # Должны быть удалены:
   - app/src/main/java/app/what/investtravel/features/hotel/ (вся папка)
   - app/src/main/java/app/what/investtravel/data/local/entity/HotelEntity.kt
   - app/src/main/java/app/what/investtravel/data/local/mappers/HotelMapper.kt
   - app/src/main/java/app/what/investtravel/data/local/database/HotelsDao.kt
   ```

### Проблема 2: Ошибки Koin DI

Если вы видите ошибки типа "Too many arguments for constructor()":

Это должно быть исправлено в InvestTravelApp.kt. Проверьте, что регистрация выглядит так:

```kotlin
single { PlacesService(get(), get()) }
single { ReviewsService(get(), get()) }
single { AccessibleRoutesService(get(), get()) }
single { AiService(get(), get()) }
```

### Проблема 3: Type inference errors

Если вы видите "Cannot infer type for type parameter 'T'":

Это может быть кешем IDE. Попробуйте:
1. Закройте проект
2. Удалите папку `.gradle` и `.idea`
3. Откройте проект снова

### Проблема 4: ContentType ошибки

Убедитесь, что в Services.kt импортирован правильный ContentType:

```kotlin
import io.ktor.http.ContentType
import io.ktor.http.contentType
```

## Команды для полной очистки:

```bash
# Полная очистка сборки
./gradlew clean

# Очистка кеша Gradle
rm -rf ~/.gradle/caches

# Перестроение проекта
./gradlew build

# Запуск отладочной сборки
./gradlew assembleDebug
```

## После исправления

Если ошибки персистируют:
1. Убедитесь, что все удаленные файлы действительно удалены из файловой системы
2. Проверьте git статус: `git status`
3. Если файлы ещё в git, удалите их: `git rm -r app/src/main/java/app/what/investtravel/features/hotel/`

## Версия Kotlin и Gradle

Убедитесь, что используете совместимые версии:
- Kotlin: 1.9.0+
- Gradle: 8.0+
- Android Gradle Plugin: 8.0+

Все ошибки должны быть исправлены после выполнения этих шагов!
