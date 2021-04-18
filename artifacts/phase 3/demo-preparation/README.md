# Demo Preparation

- ✔ Verified whole functionality list more than twice

### Questionnaire requirements

- ✔ The API REST provides the same funcionality as the web traditional app.
- ✔ Both Rest and Web work together.
- ✔ Rest Controllers have custom not found and error routes different from web ones.
- ✔ The API REST routes have no verbs, correct status code usage, used headers on creation.
- ✔ You can perform same searches and functionality as in the traditional web.
- ✔ You can easily obtain the chart statistics (custom controller dedicated to that).
- ✔ Upload and download images using API REST (we used base64 to encode the image and by that allow all kinds of devices to read the image and the client will have the responsability of decoding the image to the bytes stream and displaying it).
- ✔ Credentials are not send "raw or hashed in the url", the credentials are put in the request body so the HTTPS encrypts them when sending the login (generating token) request.
- ✔ Permissions handling, we take into account the different roles and were cautious not to allow clients perform admin stuff, or visitors to access the panel.
- ✔ We made sure to rehuse the traditional web controllers services to encapsulate all the domain logic inside of them (because of our anemic domain model) and so we use the same services in both types of controllers (REST and Web).
- ✔ Script de construccion de la app.
- ✔ Dockerfile and Docker compose.
- ✔ Images of DockerHub in the docker-compose.
- ✔ Updated class diagram with the new rest controllers.
- ✔ Readme contains all the members participation.

### Functionality by user type (REST API)

#### Visitor (✔ complete)

- ✔ Register new account
- ✔ Generate token using the new account
- ✔ Validate token using the new account
- ✔ Check pricing

#### Client

- ✔ Generate token using client account
- ✔ Validate token using client account
- ✔ Find all my services (paginated)
- ✔ Find service by id
- ✔ Renew service by id
- ✔ Cancel service by id
- ✔ Export service pdf receipt
- ✔ Find user profile
- ✔ Update user profile

#### Admin

- ✔ Generate token using admin account
- ✔ Validate token using admin account
- ✔ Check statistics
- ✔ Find all users (paginated)
- ✔ Find user by id
- ✔ Find picture by user id
- ✔ Disable user
- ✔ Enable user
- ✔ Disable one time discount
- ✔ Update one time discount
- ✔ Get current one time discount
- ✔ Disable accumulative discount
- ✔ Update accumulative discount
- ✔ Get current accumulative discount
