# role-based-authorization-server
role-based-authorization-server

    Requirements 
    Java 8
    mysql
    spring boot
    maven


Setup steps 

1) clone the repository https://github.com/seetharamugn/role-based-authorization-server

2) create a schema in ur local called database called user 

3) application.properties as the db details make it changes according to the steps 
![img.png](img.png)

4) insert into role elements directly into db 
 ![img_1.png](img_1.png)
5) **Admin** as the full access

    **Moderator** as edition only    

    **User** only view option
   
     
 **SignUp API**

     for admin specify the role as 'admin' and moderator as 'editor' for 'user' u can specify anything  not emplty set 
      ex: "role":["editor"]

     curl --location --request POST 'http://localhost:8081/users/add' \
     --header 'Content-Type: application/json' \
     --data-raw '{
     "username":"seetharama",
     "password":"redhat",
    "email":"seetha@gmail.com",
    "role":["editor"]
    }'

 **SignIn API**

    curl --location --request POST 'http://localhost:8081/oauth/token' \
    --header 'Authorization: Basic bW9iaWxlOnNlY3JldA==' \
    --form 'grant_type="password"' \
    --form 'username="seetharama"' \
    --form 'password="redhat"'

**Private API**       ------ROLE_ADMIN-------------

    curl --location --request GET 'http://localhost:8081/users/user' \
    --header 'Authorization: bearer 5020301b-f43d-47ce-9311-8c207974b189' \
    --header 'Cookie: JSESSIONID=4BE66C743409E75BDA59C5596D96B6BB'
    
**Public API**

    curl --location --request GET 'http://localhost:8081/users/getPublic' \
    --header 'Authorization: bearer a9dfc336-6dd4-4131-a021-7db393b4701a' \
    --header 'Cookie: JSESSIONID=5A4500B0F9B5D62856E0BA7976C4BC13'

     



