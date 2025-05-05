Card Management System — Инструкция по локальному запуску
📋 Предусловия
Убедитесь, что у вас установлены:

Java 21+

Maven 3.6+

PostgreSQL

Postman (или другой API-клиент для тестирования)

⚙️ Настройка базы данных:

Создайте базу данных:
sql
CREATE DATABASE bank_card_db;

Создайте схему:
sql
CREATE SCHEMA banksystem;

Убедитесь, что в application.properties указаны корректные параметры подключения к базе данных:
properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bank_card_db
spring.datasource.username=postgres
spring.datasource.password=1493041

🚀 Запуск приложения
Склонируйте репозиторий:
bash
git clone https://github.com/your-username/card-management-system.git
cd card-management-system

Запустите приложение с помощью Maven:
bash
./mvnw spring-boot:run
Либо через IDE (например, IntelliJ IDEA) — запустите CardManagementSystemApplication.java.

Приложение будет доступно по адресу:
http://localhost:8080

🛠 Liquibase
Liquibase автоматически применит миграции при запуске, если они находятся в classpath:/db/changelog/generated-changelog.xml.

Убедитесь, что файл changelog содержит таблицы для пользователей, ролей, карт и их связей.

👥 Создание аккаунтов
После запуска приложения необходимо создать пользователя USER.

Пример запросов (через Postman или curl):

1. Создать пользователя с ролью USER

POST /register
Content-Type: application/json

{
    "username": "user1",
    "password": "password123",
    "confirmPassword": "password123",
    "email": "user1@example.com"
}

🔐 Аутентификация
После регистрации вы можете получить JWT токен через эндпоинт логина, например:

POST /auth
Content-Type: application/json

{
  "username": "user1",
  "password": "password123"
}
Ответ будет содержать токен доступа, который нужно использовать для авторизации в дальнейших запросах.

2. Чтобы зайти под админа ADMIN

POST /auth
Content-Type: application/json

{
  "username": "admin",
  "password": "admin"
}

## API Документация

Чтобы просмотреть все доступные методы API:

1. Перейдите на сайт [Swagger Editor](https://editor.swagger.io).
2. Загрузите файл `openapi.yaml` (можно перетащить файл в окно редактора или использовать пункт **File → Import File**).
3. После загрузки вы сможете просмотреть документацию и протестировать доступные методы API.
4. Или использовать внутренний инструмент ide.