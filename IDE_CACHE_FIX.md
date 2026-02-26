# Исправление ошибок компиляции - Очистка кеша IDE

## Проблема
После удаления файлов отелей IDE все еще показывает ошибки вроде:
- "Too many arguments for 'constructor()'"
- "Unresolved reference 'HotelResponse'"
- "Cannot infer type for type parameter 'T'"

Это происходит потому, что IDE кеширует старые информации о проекте.

## Решение для Android Studio

### Вариант 1: Быстрая очистка (рекомендуется)

1. **Закройте проект**
   - Нажмите `File` → `Close Project`

2. **Очистите кеш AndroidStudio**
   - На Windows: Удалите папку `C:\Users\[YourUsername]\.android\gradle`
   - На Mac: Удалите папку `~/.android/gradle`
   - На Linux: Удалите папку `~/.android/gradle`

3. **Откройте проект заново**
   - Нажмите `File` → `Open` → выберите папку проекта
   - IDE автоматически пересинхронизирует Gradle

### Вариант 2: Через меню IDE

1. **File** → **Invalidate Caches** → **Invalidate and Restart**
2. Дождитесь, пока IDE перезагрузится и переиндексирует проект

### Вариант 3: Из командной строки

```bash
cd /path/to/project

# На Windows
gradlew clean build --refresh-dependencies

# На Mac/Linux
./gradlew clean build --refresh-dependencies
```

## Что делать, если ошибки остаются

1. **Убедитесь, что вы закрыли файлы hotel**
   - Все файлы с расширением `.kt` из папки `features/hotel/` должны быть удалены
   - Проверьте через Explorer/Finder

2. **Синхронизируйте Gradle еще раз**
   - `File` → `Sync Now` или нажмите `Ctrl+Shift+A` и введите "Sync Now"

3. **Проверьте build.gradle.kts**
   - Убедитесь, что в нем нет ссылок на модули отелей
   - Если есть, удалите их

4. **Проверьте AndroidManifest.xml**
   - Убедитесь, что там нет активностей из папки hotel
   - Если есть, удалите их

## После очистки

Проект должен компилироваться без ошибок. Если остаются проблемы:

1. Откройте `Logcat` и посмотрите подробное сообщение об ошибке
2. Используйте `Ctrl+Shift+F` для поиска "Hotel" или "hotel" по всему коду
3. Удалите все оставшиеся ссылки

## Ошибки для поиска и удаления

Поиск (Ctrl+H) и замена на ничего (удаление):
- `import.*[Hh]otel` - импорты отелей
- `[Hh]otel[A-Z]` - классы отелей
- `HotelsService` - сервис отелей
- `HotelController` - контроллер отелей
