# WPS Customer Service
Demo for hiring process of WPS Company
## Starting of localdb for development
```
docker run --name wps-mysql -p 3306:3306 -e MYSQL_DATABASE=wps_customer -e MYSQL_USER=wps -e MYSQL_PASSWORD=admin -e MYSQL_ROOT_PASSWORD=admin -v $(pwd)/../wps-db-data:/var/lib/mysql --rm mysql:5.7
```
The files will be stored in the wps-db-data project's sibling folder(parent folder).

## Running the appplication
```
./gradlew bootRun 
```

## Swagger documentation
Swagger is used in this application and swagger-ui for customers' endpoint is at http://localhost:8080/swagger-ui.html#/customer-controller
Please access this page to try the available operations.

# Demo Url
http://wps.supercloud.com.br:8080/customer

# Authentication
We are using inMemoryAuthentication for testing. It supports session using JSESSIONID cookie and also Basic Auth. 
If using the browser you will be redirected to the login page, if using postman or curl you must add basic auth otherwise you will receive a 401 Unauthorized.

The username is 'user' and the password is 'password'.
