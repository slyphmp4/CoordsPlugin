# CoordsPlugin

![java21](https://img.shields.io/badge/Java-21-blue) ![paperapi](https://img.shields.io/badge/API-Paper%201.20%2B-orange)

> **Paper / Purpur 1.16 +** | Java 17

Плагин постоянно показывает игроку их координаты **X Y Z** в Action Bar.  
Все строки и цвета настраиваются через YAML-файлы.

---

## Возможности

* Координаты обновляются каждую секунду (настраивается `update-interval-ticks`).
* Цвета — стандартные `&a` и HEX `&FFAA00`.
* Включение/выключение координат для себя: `/cord on`, `/cord off`.
* Перезагрузка конфигурации без рестарта сервера — `/cord reload`.
* Все сообщения вынесены в `messages.yml`; поддерживаются списки строк.
* Готовые права доступа.

---

## Установка

1. **Сборка**  
   ```bash
   ./gradlew build
   ```
   В build/libs/ появится CoordsPlugin-<version>.jar
   
2. **Размещение**
Скопируйте JAR в plugins/, перезапустите сервер.
