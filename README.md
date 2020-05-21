## Вариант №5
 Система Подачи Отчетов в Налоговую. Физ./Юр.лицо (далее
Пользователь) регистрируется. Подает отчет (XML/JSON/Форма).
Налоговый Инспектор принимает/отклоняет отчет (указывая причину
отказа). Пользователь может просмотреть все поданные отчеты, причины
отказа и изменять их если того потребовал Инспектор. Пользователь может
отправлять запрос на замену Инспектора в случае неудовлетворения.

### Установка
Debian-based systems
<pre><code>
$ sudo apt update
$ sudo apt install openjdk-8-jdk
$ sudo apt install maven
$ sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat
$ wget http://www-eu.apache.org/dist/tomcat/tomcat-9/v9.0.27/bin/apache-tomcat-9.0.27.tar.gz -P /tmp
$ sudo tar xf /tmp/apache-tomcat-9*.tar.gz -C /opt/tomcat
$ sudo apt-get install mysql-server
</code></pre>
Создайте пользователя в mysql
<pre><code>
$ sudo mysql
$ CREATE USER '{username}'@'localhost' IDENTIFIED BY '{password}';
$ GRANT ALL PRIVILEGES ON * . * TO '{username}'@'localhost';
$ FLUSH PRIVILEGES;
</code></pre>
Создайте базу данных
<pre><code>
$ CREATE DATABASE {db_name};
</code></pre>
Запишите ваши данные в testingServlet/src/main/resources/application.properties

Установите зависимости Maven
<pre><code>
$ mvn dependency:resolve
</code></pre>
### Запуск
<pre><code>
$ mvn spring-boot:run
</code></pre>