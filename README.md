# Yandex Academy Backend School
Вступительное задание в летнюю школу backend-разработки Академии Яндекса 2022.

## Создание и настройка базы данных

Первым делом следует настроить PostgreSQL базу данных.

Если у вас установлен Debian, Ubuntu или любой другой дистрибутив Linux, 
использующий пакетный менеджер apt, введите следующую команду для установки postgres

```sh
sudo apt-get install postgresql-12
```

Если установка успешна, можно переходить к созданию БД.

Переходим в пользователя postgres

```sh
sudo -i -u postgres
```

Теперь запустим консоль PostgreSQL

```sh
psql
```

Создаём базу основную базу данных и базу данных для тестирования.

```sql
CREATE DATABASE yandex;
CREATE DATABASE test;
```

Создаём нового пользователя с правами админа

```sql
CREATE USER boris WITH PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE yandex TO boris;
GRANT ALL PRIVILEGES ON DATABASE test TO boris;
```

База данных готова. Обращаю внимание, что никакие таблицы создавать не требуется, этим занимается приложение.

*Допускается указать иное имя для базы данных, другое имя пользователя и пароль. В таком случае, потребуется поправить 
строчки `spring.datasource.url`, `spring.datasource.username` и `spring.datasource.password` в файлах ``application.properties`` 
и ``test.properties`` для корректной работы*

## Тестирование, сборка и запуск
Сборка осуществляется при помощи Apache Maven 3.8.6 и под Java 17.
Все зависимости можно посмотреть в файле `pom.xml`

Для того, чтобы собрать jar с приложением, откройте терминал в папке проекта и введите

```sh
mvn package
```

В случае успехе Maven сначала подгрузит все зависимости с центрального репозитория, затем скомпилирует приложение, затем запустит модульные тесты и соберет проект
в `yandex-backend-school-entry-task-0.0.1-SNAPSHOT.jar`, который будет лежать в папке target

***ВНИМАНИЕ!!!***

**Запуск модульных тестов очищает таблицы в базе данных для корректного тестирования**

Если Maven падает с ошибкой до компиляции и запуска тестов, проверьте, что установлена указанная выше версия.

Получившийся jar можно запустить следующей командой:

```shell
sudo java -jar yandex-backend-school-entry-task-0.0.1-SNAPSHOT.jar
```

Обращаю внимание на необходимость запускать приложение с правами суперюзера (sudo)

По умолчанию веб-сервер внутри приложения разворачивается на адрес 0.0.0.0:80 (тестируется на localhost:8080).
Использование портов <1024 в Linux разрешено только супер-пользователю.
Для личного пользования допускается изменить адрес и порт развертывания, внеся изменения в файл 
`application.properties`

## Автозапуск сервера на старте машины

При желании можно настроить автозапуск сервера при старте машины, используя Linux под подсистемой systemd.

Для этого переходим в `/etc/systemd/system` и создаём файл autolaunch.service со следующим содержимым:

```shell
[Unit]
Description=YandexAcademyEntryTask
After=syslog.target
After=network.target
After=postgresql.service
Requires=postgresql.service

[Service]
Type=simple

User=ubuntu
Restart=always
RestartSec=30

OOMScoreAdjust=-100

ExecStart=sudo java -jar /home/ubuntu/rest/target/yandex-backend-school-entry-task-1.0.0.jar

TimeoutSec=100

[Install]
WantedBy=multi-user.target
```

Убедитесь, что директория, пользователь в свойстве ExecStart указана верно.

После этого сохраняем файл и включаем наш сервис:

```shell
sudo systemctl enable autolaunch
sudo systemctl start autolaunch
```

Перезагрузим демон systemctl:

```shell
sudo systemctl daemon-reload
```

Проверяем, что всё работает:

```shell
systemctl -l status autolaunch
```

При необходимости можно наблюдать за stdout сервера в режиме реального времени при помощи команды:

```shell
sudo journalctl -f -u autolaunch
```









