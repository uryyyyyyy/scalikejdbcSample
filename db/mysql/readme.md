

## mysql

docker run --name mysql-5.7 -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7.19

## setup

```
CREATE DATABASE scalikejdbc
  DEFAULT CHARACTER SET utf8
  DEFAULT COLLATE utf8_general_ci;

CREATE USER my_user@'%' ;
GRANT ALL ON scalikejdbc.* to 'my_user'@'%';
SET PASSWORD FOR my_user = 'my_password'
```

## migrate

```
cd flyway
./flyway-4.2.0/flyway -user=my_user -driver=com.mysql.cj.jdbc.Driver -password=my_password -url='jdbc:mysql://localhost:3306/scalikejdbc?characterEncoding=utf8&useSSL=false' migrate
```
