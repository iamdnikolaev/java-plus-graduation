# Проект Explore With Me (англ. «исследуй со мной»)
Он позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них.

Проект разработан на микросервисной архитектуре. Состоит из следующих модулей и сервисов:

## Модуль `core`
Содержит основные сервисы, отвечающие за логику приложения:

1. **event-service** — управление событиями, категориями и подборками событий.
2. **user-service** — управление пользователями.
3. **request-service** — управление заявками на участие в событиях.
4. **comment-service** — управление комментариями к событиям.
5. **interaction-api** — общие для сервисов классы DTO, типы ошибок, API-клиентов для межсервисного взаимодействия.

## Модуль `infra`
Содержит инфраструктурные сервисы, обеспечивающие работу приложения:

1. **config-server** — централизованное управление настройками, упрощающее конфигурацию микросервисов. Настройки - в разделе `resources` этого сервиса.
2. **gateway-server** — API-шлюз, обеспечивает единую точку входа для пользователей. Перенаправляет запросы в соответствующий микросервис.
3. **discovery-server** — сервис Discovery (Eureka), который регистрирует все микросервисы и обеспечивает их обнаружение динамически.

## Модуль `stats`
Ведет учет просмотров и позволяет делать различные выборки для анализа работы приложения:

1. **stats-client** — http-клиент для доступа к серверу статистики из сервиса событий модуля `core`.
2. **stats-dto** — подмодуль DTO для работы.
3. **stats-server** — сервер статистики, ведущий учет.  

## Спецификации внешнего API
Спецификации внешнего API можно найти по следующим ссылкам:
1. [Спецификация основного сервиса](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json)
2. [Спецификация сервиса статистики](https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json)

## Спецификация внутреннего API
Взаимосвязь сервисов:

| Сервис          | Используемые сервисы                                          |
|-----------------|---------------------------------------------------------------|
| event-service   | user-service, stats-server, request-service, comment-service |
| comment-service | user-service, event-service                                   |
| request-service | user-service, event-service                                   |
| user-service    | -                                                             |
| stats-service   | -                                                             |

Описание внутренних API:

| Event API                                                   | Описание                                                               |
|-------------------------------------------------------------|------------------------------------------------------------------------|
| GET event-service/admin/events/{eventId}                    | получение события по ID                                                |
| POST event-service/admin/events/confirm?eventId=123&count=0 | сохранение количества подтвержденных заявок на участие в событии по ID |

| Request API                                                                  | Описание                                                                  |
|------------------------------------------------------------------------------|---------------------------------------------------------------------------|
| GET request-service/users/requests/count?requestStatus=CONFIRMED&eventId=123 | Подсчет количества заявок на участие в указанном статусе по событию из ID |

| User API                                  | Описание                         |
|-------------------------------------------|----------------------------------|
| GET user-service//admin/users/{userId}    | Получение пользователя по ID     |

## Тестирование
Для проверки работы сервисов разработаны Postman-тесты. Они находятся по следующим ссылкам:
1. [Проверка работоспособности основого сервиса](https://github.com/yandex-praktikum/java-plus-graduation/blob/ci/.github/workflows/stuff/postman/microservices/ewm-main-service.json)
2. [Проверка работоспособности сервиса статистики](https://github.com/yandex-praktikum/java-plus-graduation/blob/ci/.github/workflows/stuff/postman/microservices/ewm-stat-service.json)
3. [Проверка работоспособности сервиса комментариев](https://github.com/iamdnikolaev/java-explore-with-me-plus462/blob/main/postman/feature.json)

## Стек технологий
- Java
- Spring Boot
- Spring Data
- Spring Cloud (Config, Eureka, Gateway)
- Hibernate
- REST API
- PostgreSQL
- Lombok
- Maven
- Postman
- Feign Client
