# Credit-processing
 Credit-processing - это сервис, отвечающий за обработку кредитных заявок.

## Тесты

1) isNewRequestAdded - Попытка добавить новую заявку.
   1) Проверяем, успешно ли получилось отправить запрос на добавление заявки.
2) isStatusCodeApproved - Проверка, подтверждена ли заявка на кредит.
   1) Получаем информацию о статусе заявки.
   2) Проверяем, подтверждена ли заявка.
3) areThereThreeTariffs - Проверка количества доступных тарифов.
   1) Получаем информацию обо всех тарифах.
   2) Проверяем, равно количество тарифов трём.
4) IsFirstTariffInterestRateEqualsValue - Проверка на то, равен ли interestRate первого тарифа переданному значению.
   1) Получаем информацию о первом тарифе.
   2) Сравниваем interestRate первого тарифа с переданным значением.

## Установка
Для развертки бд я использовал PostgreSQL. Поэтому перед тестированием необходимо:
- Зайти на официальный сайт PostgreSQL и загрузить установщик в соответсивии с вашей системой: https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
- После установки, создайте пользователя с логином "user" и паролем: "1234". Далее для этого пользователя необходимо создать базу данных с названием "credit_processing_db"

## Как запустить проект

Необходимо написать следующие команды, находясь в директории проекта

```bash
mvn clean install

./mvnw package
java -jar target/credit-processing-0.0.1-SNAPSHOT.jar
```

## API
1. Получение тарифов
```
localhost:8080/loan-service/getTariffs
```
2. Подача заявки на кредит
```
localhost:8080/loan-service/order
```
http body (пример)
```
{
    "userId": 123456,
    "tariffId": 3
}
```
3. Получение статуса заявки
```
localhost:8080/loan-service/getStatusOrder?orderId=24f35741-df87-47fb-8bd6-b927a720018
```
4. Удаление заявки
```
localhost:8080/loan-service/deleteOrder
```
http body (пример)
```
{
    "userId": 123456,
    "orderId": "b12b25e9-f6be-4c28-9140-ed137bd1f630"
}
```
