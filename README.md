# DAW Host Services

## Context

This project is an assignment for the _Web Application Development_ subject in the
Software Engineering Degree, at _University King Juan Carlos_, Spain (2020/2021).

## Logo

![alt text](artifacts/header.png)

## Table of Contents

**Phases 0 & 1**.
* [Introduction](#introduction)
  * [Team Members](#members)  
* [Starting Decisions](#start)
  * [Theme](#theme)   
  * [Entities](#entities)  
  * [Roles and Permissions](#roles-and-perms)  
  * [Images](#images)  
  * [Charts](#charts)   
  * [Third Party Technology](#third-tech)
  * [Advanced algorithm](#algorithm)
* [Visuals](#visuals)
  * [Screenshots](#screenshots)
  * [Navigation Diagram](#navigation)   

**Phase 2**    
* [Installation](#phase-2)   
  * [Linux](#linux-install-linux-2)   
  * [Windows](#linux-install-win-2)   
* [Diagrams](#diagrams-2)
  * [Domain Entities](#diagrams-2-ed)
  * [Database EER](#diagrams-2-eer)
  * [Classes and Templates](#diagrams-2-ctd)
* [Members Participation](#members-part-2)

**Phase 3**
* [Installation](#phase-3)
* [API Documentation](#api-docs-3)
* [Classes and Templates Updated Diagram](#diagrams-3-ed)
* [Members Participation](#members-part-3)

**Phase 4**
* [Installation](#phase-4)
* [Angular Components Diagram](#diagrams-4-ed)
* [Demo Video](#demo-video)
* [Members Participation](#members-part-4)

# Introduction <a name="introduction"></a>

The main objective of this application is to simulate, as closely as possible, a hosting provider.
A lot of different kinds of products will be available for rental, the products will be based on
multiple elegible configurations (_hardware and network specifications_). Each package rental will
have associated an expiration time (_before package usage time expires you need to renew it_).

## Team Members <a name="members"></a>

| Full Name | Email | GitHub Profile |
| ------------- | ------------- | ------------- |
| Serghei Sergheev | s.sergheev.2018@alumnos.urjc.es | [sergheevdev](https://github.com/sergheevdev) |
| Allan Robert Cobb Bellido | ar.cobb.2018@alumnos.urjc.es | [Allanmaster](https://github.com/Allanmaster) |
| Álvaro Noguerales Ramos | a.noguerales.2016@alumnos.urjc.es | [Anogue](https://github.com/Anogue) |
| Alberto Mautone | a.mautone.2020@alumnos.urjc.es | [albehma](https://github.com/albehma) |
| Alejandro José Rodriguez Montero | aj.rodriguez.2018@alumnos.urjc.es | [Alexrguez9](https://github.com/Alexrguez9) |

All the organisation will be done using a _Trello_ board.

## Phases 0 & 1

# Starting Decisions <a name="start"></a>

### Theme <a name="theme"></a>

- The theme of the application a _marketplace_, to be more concise a server reseller.

### Entities <a name="entities"></a>

- **User**: a user will be the entity in charge of storing all kinds of information related to authentication.
- **Role**: a role or authority, holds a set of permissions that will allow a user perform certain actions.
- **Product**: is an entity that contains all the information related to the product (i.e. price, category and specs)
- **Order**: represents a product rental action and contains order attributes (i.e. rental date, expiration
  date, etc)
- **OneTimeDiscount**: will represent a one time discount sale, this is non-recurrent one time applied discount.
- **AccumulativeDiscount**: it will represent a discount as a reward for subsequent purchases in a concrete
  timespan and will contain a configuration (i.e. after 5 puchasing packages of a category, the next packages
  of the same kind, will be given an X% amount of discount)

### Roles and permissions <a name="roles-and-perms"></a>

The previously mentioned _Role_ entity, will be responsible of holding all the permissions, a permission will
represent an additional action which the user can perform (i.e. see a page, perform special action, etc).

- **Visitor**: can read generic information about the business (i.e. homepage, about us, pricing).
- **Client**: can perform client tasks (i.e. order or cancel products, change profile information, check hired services).
- **Administrator**: can effectuate moderative and administrative actions (i.e. disable accounts, check statistics, modify clients).

### Images <a name="images"></a>

- Each client will have its own _profile_ with an _avatar_.
- The website will be plenty of _icons_ to improve accesibility.
- The _administrator_ will be able to see client profile pictures when managing clients from panel.

### Charts <a name="charts"></a>

- The **administrator** control panel, will contain the weekly purchased services _pie chart_,
  weekly sales _time series chart_, and the total accumulated capital statistic.

### Third-party Additional Technology <a name="third-tech"></a>

- [Gmail API](https://developers.google.com/gmail/api): we will be using the Gmail API with Spring Email
  to send registration welcome messages and order receipts and server information to the clients.
- [Open PDF](https://github.com/LibrePDF/OpenPDF): we will also be using OpenPDF to generate PDF's for
  the product receipts in the client rented services area.

### Advanced Algorithm <a name="algorithm"></a>

We will be using two kinds of algorithms based on discounts for products:
1. Giving XX% non-recurrent discount on inauguration (or special events), this algorithm will be configurable.
2. Acummulative purchases, every X services you rent in a concrete timespan, given an additional XX% discount
   on subsequent purchases, all percentages also configurable.

# Visuals <a name="visuals"></a>

### Screenshots <a name="screenshots"></a>

#### Visitor View

- **Homepage**: main page when you enter the application, it contains basic information.

![alt text](artifacts/screenshots/0001%20-%20homepage.jpeg)

- **Pricing**: specifies the price for each package configuration available to purchase.

![alt text](artifacts/screenshots/0002%20-%20pricing.jpeg)

- **About us**: describes the enterprise, its origins, present, and future.

![alt text](artifacts/screenshots/0003%20-%20about.jpeg)

- **Legal**: contains all legal, privacy & security, terms of use and trademarks.

![alt text](artifacts/screenshots/0004%20-%20legal.jpeg)

- **FAQ**: stands for frequently asked questions and contains the most asked questions about our services.

![alt text](artifacts/screenshots/0005%20-%20faq.jpeg)

- **Screenshots**: contains an overview of the user services panel and other interface stuff.

![alt text](artifacts/screenshots/0006%20-%20screenshots.jpeg)

- **Login**: page to perform the authentication and log-in into our account.

![alt text](artifacts/screenshots/0007%20-%20login.jpeg)

- **Register**: here we register providing our personal data and creating an account to become clients.

![alt text](artifacts/screenshots/0008%20-%20register.jpeg)

- **Error**: the page which is shown when the provided page does not exist or you're unauthorized:

![alt text](artifacts/screenshots/0019%20-%20error.jpeg)

#### Client View

- **My Services**: a page where you can see a list of your recently purchased services.

![alt text](artifacts/screenshots/0009%20-%20services.jpeg)

- **Profile**: allows you to modify your personal information details.

![alt text](artifacts/screenshots/0010%20-%20profile.jpeg)

- **Service**: where you manage your concretely selected service from the previous page.

![alt text](artifacts/screenshots/0011%20-%20active%20service.jpeg)

![alt text](artifacts/screenshots/0012%20-%20cancelled%20service.jpeg)

#### Admin View

- **Panel > Statistics**: chart section of the panel where you can easily track your sales.

![alt text](artifacts/screenshots/0013%20-%20panel%20statistics.jpeg)

- **Panel > Manage Clients**: client management section of the panel where you can disable,
  enable and check client profile information.

![alt text](artifacts/screenshots/0014%20-%20panel%20clients.jpeg)

- **Panel > One Time Discount**: one time discount sale management.

![alt text](artifacts/screenshots/0015%20-%20sale%20otd%20active.jpeg)
![alt text](artifacts/screenshots/0016%20-%20sale%20otd%20inactive.jpeg)

- **Panel > Accumulative Discount**: accumulative or recurrent sale management.

![alt text](artifacts/screenshots/0017%20-%20sale%20ad%20active.jpeg)
![alt text](artifacts/screenshots/0018%20-%20sale%20ad%20inactive.jpeg)

### Navigation Diagram <a name="navigation"></a>

![alt text](artifacts/navigation_diagram.png)

## Phase 2 <a name="phase-2"></a>

### Installation

Fully working traditional MVC application with all the backend implemented.

### Installation instructions for GNU/Linux (Ubuntu/Debian based systems) <a name="linux-install-linux-2"></a>

**Implications of the scripts**:
1. Clones the repository to home directory (_removing the old cloned repo if present_).
2. Installs _Docker_, _Docker-Compose_ and _Maven_.
3. Builds the application packaging it to a _JAR_ file.
4. Builds a docker image of the currently build Spring application
5. Creates and runs two containers (application + mysql instance, in the same network).

**Installation steps**:

1. **Clone script**:

Create a new file, copy this script and give it execution permissions. This script just
clones the current code to your home directory.

```bash
#!/bin/bash
cd $HOME
rm -rf webapp11
git clone https://github.com/CodeURJC-DAW-2020-21/webapp11
```

**WARNING**: Before building and executing make sure you've set up the neccesary credentials
for all the .properties files in "webapp11/backend/src/main/resources/*".

2. **Build and execution script**:

Create a new file, copy this script and give it execution permissions. This script must
be executed with **sudo**.

```bash
#!/bin/bash
cd $HOME
cd webapp11/backend
chmod +x install_and_run.sh
./install_and_run.sh
```

### Installation instructions for Windows <a name="linux-install-win-2"></a>

**NOTE**: The first three steps can be easily automated by using a windows package manager like [Chocolatey](https://chocolatey.org/install).

1. Download and install [Docker Desktop](https://desktop.docker.com/win/stable/Docker%20Desktop%20Installer.exe)
2. Download and install [Maven](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
3. Download and install [Git](https://git-scm.com/download/win)
4. Clone the application repository with Git:

```cmd
git clone https://github.com/CodeURJC-DAW-2020-21/webapp11
```

5. Inside the cloned repository folder, go inside the "backend" folder.
6. Execute the maven command to compile the code:

```cmd
mvn clean install
```

7. Inside the "backend" folder go inside the "target" folder.
8. Copy the "marketplace-0.0.1-SNAPSHOT.jar" file to the "docker" folder.
9. Inside the "docker" folder, change the "_marketplace-0.0.1-SNAPSHOT.jar_" file name to "_marketplace.jar_"
10. Execute the docker image creation command:

```cmd
docker build -t daw-team-11/marketplace .
```

11. Run the docker compose file:

```cmd
docker-compose up -d
```

### Diagrams <a name="diagrams-2"></a>

#### Domain entities diagram <a name="diagrams-2-ed"></a>

This diagram represents our domain entities and their associative
relationships (This diagram is just a quick overview so _methods
are ommited for sake of shortness_).

![alt text](artifacts/entity-diagram.png)

#### Database EER diagram <a name="diagrams-2-eer"></a>

This diagram is an enhanced entity-relationship diagram (EER),
and this is basically an expanded version of ER diagrams, this
high-level model represents the relationship between the database
entities.

![alt text](artifacts/database-diagram.png)

#### Classes and templates diagram <a name="diagrams-2-ctd"></a>

This diagram is a classes and templates diagram of our application,
represents a high-level view of the entire application without going
too much into details, basically the flow of the requests thru the
controllers, services, repositories to the domain entities and the
response view associated to the request.

**Legend**:
- Yellow (_Views_) represent the view that is loaded upon a given request.
- Green (_Controllers_) represent the controllers that are our web adapters.
- Red (_Services_) self-contained unit of software that performs a specific task.
- Blue (_Repositories_) acts like a list of data, basically abstracts data access.
- Light Pink (_Domain_) represent our domain entities and their relationships.

![alt text](artifacts/classes-templates-diagram.png)

### Members Participation <a name="members-part-2"></a>

#### Serghei Sergheev

This team member established the base application, security, worked on the algorithm (discounts), image uploading, and helped
team members with everything that was necessary.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added security, draft entities and configuration | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/5175ef97cf93165e2a420507432f6c553bf1ac4d) | [UserServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/UserServiceImpl.java)
| #2 | Added templates and static resources | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/44bd5c2b558dd319619c44c9ed5b4ad555dddba4) | [ProductServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/ProductServiceImpl.java) |
| #3 | Fixed pricing and made it work | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/feb0b1af86083f7a07a62ea4986a50caa512fb30) | [ProductController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/product/controller/ProductController.java) |
| #4 | Added working profile pictures | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/9511b3e09a8ae888048898694c12be13804b8b0f) | [PictureServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/PictureServiceImpl.java) |
| #5 | Added working sales | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/fce2e41f3f8b87266eeec5233692186b0f9b9d1e) | [OrderRepository.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/repository/OrderRepository.java) |

#### Allan Robert Cobb

This team member mainly worked on the profile, pdf exportation, https, form functionality, cleaned code, fixed bugs and helped team members.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added profile functionality | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/911176fb4427d532582fe3d06277331377f6a08e) | [UserController.class]() |
| #2 | Added HTTPS | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7acdfe3fd6701778b48f5ac3f5f6c196754a63dd) | [ServerConfig.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/7acdfe3fd6701778b48f5ac3f5f6c196754a63dd/backend/src/main/java/es/urjc/code/daw/marketplace/config/ServerConfig.java) |
| #3 | Added PDF export functionality | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/6a27f3873a8d328bb96222db6541f64ef19d1b86) | [PdfExportServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/PdfExporterServiceImpl.java) |
| #4 | Added accumulative discount form and fixes | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/f9069bd9c3e66ad59174baae6fa03790ce443ff7) | [ProductController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/product/controller/ProductController.java) |
| #5 | Cleaned code | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/d65f292669c155f33acbe1624c330e002706a40a) | [SaleServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/SaleServiceImpl.java) |

#### Alberto Mautone

This team member mainly worked on the mailing service, the html generation, and register functionality, also
created the database diagram.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added the mail service | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/6e0da3174d3d1f77e9804b99b84b470f61e82d5d) | [EmailServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/EmailServiceImpl.java) |
| #2 | Added database diagram | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/9dbc1e20e0d5b52cf5a00383ecf58a6ef62d5b32) | [UserController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/user/controller/UserController.java) |
| #3 | Improved html generation for emails | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/83e54b12459833d2e8a61d5399d83966a4699e33) | [EmailContent.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/util/EmailContent.java) |
| #4 | Added register functionality | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/91446619e9ee6c109d49082e0872ecbeb632fe20) | [OrderController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/order/controller/OrderController.java) |
| #5 | Fixed not displaying name when logged in error | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/3462c59b0ded0d2f47240c47e179067d16c64556) | [UserController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/user/controller/UserController.java) |

#### Alejandro José Rodriguez Montero

This team member mainly worked on the service overview, classes and templates diagram, pdf exportation and multiple bug fixes.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added working service overview | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/4695410461b7f15d1717a6e337d83a675b583390) | [OrderController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/order/controller/OrderController.java) |
| #2 | Multiple bug fixes | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/892b078b420fce4756ee16122375ffb74d8521fd) | [SaleServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/SaleServiceImpl.java) |
| #3 | Added classes and templates diagram | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/a9d63f5f99450cb79dfb238dfd853a986d8d5361) | [PdfExporterServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/PdfExporterServiceImpl.java) |
| #4 | Beautified generated PDF format | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/17b1027ce63fbd161f17a0f87b20106a83b6d8b5) | [OrderServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/OrderServiceImpl.java) |
| #5 | Fixed address and email restrictions | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/5f47970d2906e84ac354b33837bdcbb598e2267e) | [Order.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/domain/Order.java) |

#### Álvaro Noguerales Ramos

This team member mainly worked on the services listing, one time discount, oders, and bug fixes, also created the entity diagram.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added services listing | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/51b043bc02207646bcd2faba8ac0e56793a1ca28) | [OrderController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/order/controller/OrderController.java) |
| #2 | Added working one time discount form | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/33b6c0ddeabafc23e65ba237cef9fdc297f66ec1) | [ProductController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/product/controller/ProductController.java) |
| #3 | Fixed multiple bugs | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7e5e4131603d34db69297ec7d02288f33e952de3) | [UserController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/user/controller/UserController.java) |
| #4 | Added entity diagram | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/bab79b7722ac715524d545a0dfbd13f1fe05f4cf) | [UserServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/UserServiceImpl.java)
| #5 | Improved discount applicability | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/1bafa39d43949420d651e14282fe30c50261c96a) | [SaleServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blame/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/SaleServiceImpl.java) |

## Phase 3 <a name="phase-3"></a>

## Installation

### Dependencies (or Prerequisites)

Make sure you have installed:
- Docker Engine
- Docker Desktop (Windows only)
- Docker Compose
- Maven
- OpenJDK 11 (Necessary for Maven to build the JAR before packaging it)

Fully working traditional MVC and REST API application.

### Installation instructions<a name="linux-install-linux-2"></a>

**Installation steps**:

1. **Clone repository**:

Clone this repository using Git Command Line (Linux or Windows)

Sample script for Linux:
```bash
#!/bin/bash
cd $HOME
rm -rf webapp11
git clone https://github.com/CodeURJC-DAW-2020-21/webapp11
```

**WARNING**: Before building and executing make sure you've set up the neccesary credentials
for all the .properties files (for email messages, https certificates, etc)
in "webapp11/backend/src/main/resources/*".

2. **Build and execution script**:

Go inside the "webapp11/docker" directory, and open the console, then execute one of the
following scripts: **_create_image.sh_** (linux) or **_create_image.bat_** (windows).

You may also check [our published docker image here](https://hub.docker.com/r/dawhost/marketplace).

## API Documentation <a name="api-docs-3"></a>

The documentation that represents all the REST API endpoints can be viewed in 3 different ways:
- Rendered HTML Static Page [(Click to view the page)](https://rawcdn.githack.com/CodeURJC-DAW-2020-21/webapp11/89222b4850b75515fb1302abe5b497bd85267418/api-docs/api-docs.html)
- Plain Text HTML [(Click to view the page)](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/api-docs/api-docs.html)
- YAML Document Format [(Click to view the page)](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/api-docs/api-docs.yaml)

If you want to test the API make sure to check our postman collections:
- Admin collection [(Click to view the collection)](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/artifacts/phase%203/postman/admin_postman_collection.json)
- Client collection [(Click to view the collection)](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/artifacts/phase%203/postman/client_postman_collection.json)
- Visitor collection [(Click to view the collection)](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/artifacts/phase%203/postman/visitor_postman_collection.json)

Note: those collections have two defined variables:
- _**{{token}}**_: represents the current token value with the bearer included, i.e. "Token rj88jf348jf438jf48..."
- _**{{address}}**_: represents the url of the current server, i.e. "https://localhost:8443"

## Classes and Templates Updated Diagram <a name="diagrams-3-ed"></a>

This diagram is a classes and templates diagram of our application,
represents a high-level view of the entire application without going
too much into details, basically the flow of the requests thru the
controllers, rest controllers, services, repositories to the domain
entities and the response views or DTO's associated to the request/response.

**Legend**:
- Yellow (_Views_) represent the view that is loaded upon a given request.
- Green (_Web Controllers_) represent the controllers that are our web adapters.
- Light Gray (_Data Transfer Objects_) represent the interface layer representation of the domain model.
- Purple (_Rest Controllers_) represent the rest controllers that are our web adapters.
- Red (_Services_) self-contained unit of software that performs a specific task.
- Blue (_Repositories_) acts like a list of data, basically abstracts data access.
- Light Pink (_Domain_) represent our domain entities and their relationships.

![alt text](artifacts/clasesytemplates.png)

### Members Participation <a name="members-part-3"></a>

#### Serghei Sergheev

This team member added the skeleton structure of the rest application, security (jwts), error handling, postman collections, bugfixes, and helped
team members with everything that was necessary.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added JWT (Json Web Tokens) auth | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/f2fceb1201f5fee6d6ca6532691c60e3dc8c92cb) | [TokenRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/jwt/controller/TokenRestController.java) |
| #2 | Added Postman Collections | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/fad5000847df15a82296dec2bec76e56cf87007a) | [OrderRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/controller/OrderRestController.java) |
| #3 | Refactored REST API for best practices | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/3acb49075913c56a8c9fa7590d9a5d498c1f8dd1) | [ProductRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/product/controller/ProductRestController.java) |
| #4 | Integrated image retrieval in find user | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/039e42e05617eee8a62a7970cc950776ca74219f) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #5 | Performed general improvements  | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/fac231e27e154d3742383219cec9ab9e5a465b71) | [UserRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/user/controller/UserRestController.java) |

#### Allan Robert Cobb

This team member mainly worked on the order and statisctics controllers, the creation of docker batch scripts (and other docker windows related stuff), helped testing the docker containers and helped team members.
| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Implemented Order Mapper and FinderOrderResponseDto | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7b5ee8cbc7e9c64130dfdde9db86435f1284ff3b) | [OrderRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/controller/OrderRestController.java) |
| #2 | Added findServices method (from OrderRestController) | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/232ee3b7f3d1966ac61d91fc8f62a5cfe06d2c07) | [RestOrderMapper.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/mapper/RestOrderMapper.java) |
| #3 | Added exportOrderToPdf method (from OrderRestController) | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/40493fae6f253c2f6971d3bbeff9dd36e3e93f3a) | [StatisticsRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/statistics/controller/StatisticsRestController.java) |
| #4 | Created StatisticsResponseDto & StatisticsRestController | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/3856f1c4fb40ddea2b1ccc46ae0b99459ac267fc) | [FindOrderResponseDto.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/dto/FindOrderResponseDto.java) |
| #5 | Created windows batch docker script | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/eaa64fd3a68ca59d2f221dac4994b2256da58df7) | [create_image.bat](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/docker/create_image.bat) |

#### Alberto Mautone

This team member mainly worked on the sales REST API section, the creation, disabling of the sales, also the adaptation of the existent services to work for both rest and web controllers.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added "updates current accumulative discount" | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/eda3ce02ad5a66e6b5d9e70c0dce4fef8f9cb555) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #2 | Added "disables current one time discount" | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/cafa1b3ccb3191dd237036cbec442fc24de0ad82) | [RestSaleMapper.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/mapper/RestSaleMapper.java) |
| #3 | Documented SalesRestController | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/9619041900d1481b36b26c796cf61e7e824024ab) | [UserRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/user/controller/UserRestController.java) |
| #4 | Added "disables current accumulative discount" | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/bf83ab0982b789f92832c9c301846bf543530124) | [RestUserMapper.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/user/mapper/RestUserMapper.java) |
| #5 | Updated UserRestController | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7f31d4a0034798cec41e5753137cd56496466123) | [UserServiceImpl.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/service/UserServiceImpl.java) |

#### Alejandro José Rodriguez Montero

This team member participated on several controlers, mostly focused on product and order rest controllers.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added the obtaining products method | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/b8ebf4f42b78a47cdf460f72cd6bd99ae2835b48) | [ProductRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/product/controller/ProductRestController.java) |
| #2 | Added purchase products method | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7fd6968edd768667d3658fa2df224ddce1203dfe) | [OrderRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/controller/OrderRestController.java) |
| #3 | Documented with open api | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/9cc88df2f7e9866a7562361b0bf0019e32bccd30) | [UserServiceImpl.class](aaaaaaaaaaaaaaa) |
| #4 | Added PlaceOrderResponseDto | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/24cfa7df0c1bd6f28a0cf92cf0a12ea1fd42d9f8) | [PlaceOrderResponseDto.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/dto/PlaceOrderResponseDto.java) |
| #5 | Update OrderRestController and ProductRestController | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/1a7631f8e93930ab34439b478c5ad87690207dca) | [UpdateOrderRequestDto.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/order/dto/UpdateOrderRequestDto.java) |

#### Álvaro Noguerales Ramos

This team member mainly focused on the sale rest controllers and the different discounts.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added "Find otd" in rest controller | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/ad244160a96c72c18b43a1640f8469e7095dc020) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #2 | Added "Find ad" in rest controller | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/4ba85577f211f3ed24e7d4f1edfcf7dae334b959) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #3 | Added "Update otd" in rest controller | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/76f8a1e067bcd08b3a6c45e00ed517b28eb5b4f2) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #4 | Added documented api error  | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/4a169cb29b4967b7e329f49d925a547386a32d6d) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |
| #5 | Modified routes for REST API best practices  | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/5d93d7b37e5e7c9ed7e2bcf9674c51b739eb86ba) | [SaleRestController.class](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/api/sale/controller/SaleRestController.java) |

## Phase 4 <a name="phase-4"></a>

## Installation

### Dependencies (or Prerequisites)

Make sure you have installed:
- Docker Engine
- Docker Desktop (Windows only)
- Docker Compose

Fully working angular, traditional MVC and REST API application.

### Installation instructions<a name="linux-install-linux-3"></a>

**Installation steps**:

1. **Clone repository**:

Clone this repository using Git Command Line (Linux or Windows)

Sample script for Linux:
```bash
#!/bin/bash
cd $HOME
rm -rf webapp11
git clone https://github.com/CodeURJC-DAW-2020-21/webapp11
```

**WARNING**: Before building and executing make sure you've set up the neccesary credentials
for all the .properties files (for email messages, https certificates, etc)
in "webapp11/backend/src/main/resources/*".

2. **Build and execution script**:

Go inside the "webapp11" directory, and open the console, then run the following command:

```
docker build -t dawhost/marketplace -f ./docker/Dockerfile .
```

**WARNING**: the build can take even half an hour for the first time (without having the cache) so
feel free to go and get a coffee.

When the build finishes you can start your application by going inside of the "docker" directory
and running the following command:

```
docker-compose up -d
```

You may also check [our published docker image here](https://hub.docker.com/r/dawhost/marketplace).

## Angular Components Diagram <a name="diagrams-4-ed"></a>

This diagram is a components diagram of our angular frontend application,
represents a high-level view of the entire frontend structure without going
too much into detail, basically the flow of the requests thru the services,
mappers, models and components.

**Legend**:
- Blue (_Components_) are the components of our application (different pieces of html, css, ts).
- Green (_Services_) are the boundary that communicates with our REST API.
- Red (_Mappers_) map the received data to the desired display domain model.
- Yellow (_Models_) represent the display entities with on the expected form to be displayed in the interface.

![alt text](artifacts/angular.png)

## Demo video <a name="demo-video"></a>

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/WOHzH2nY8YU/0.jpg)](https://www.youtube.com/watch?v=WOHzH2nY8YU)

### Members Participation <a name="members-part-4"></a>

#### Serghei Sergheev

This member stablished the base separation of components, made the control panel and
helped with the whole refactorization and services part, also a huge part of the
dockerization and made the diagram. He helped all team members and collaborated in
the video realisation.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Finished fully working panel | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/ab74c068b3063b24eab390328e7de7d2516dfb17) | [Login Service](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/services/login.service.ts) |
| #2 | Huge refactoring (charts, stats, etc) | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/86b756aa6f65b959f424e714920eb735e632fe07) | [Order Service](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/services/order.service.ts) |
| #3 | Refactored login component | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/cbadfc0af07505f2e4184718bc840f86e71ff4f4) | [User Service](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/services/user.service.ts) |
| #4 | Service overview (pdf, renew, cancel) | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/5700389a83a47ce39570c051fa9f8e84bbaf523b) | [Control Panel](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/panel/panel.component.ts) |
| #5 | Dockerfile refactoring | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/16b3fb0648243efc1bd91b30e6b28c9e3320a1e8) | [Charts](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/panel/category-purchases-chart/category-purchases-chart.component.ts) |

#### Allan Robert Cobb

This member has mostly worked on the about and services components, a part from
fixing certain bugs, adding the SPA controller for angular in spring and tried
to start the dockerization (added batch compilation) and also colaborated in a
huge part in the realization of the video.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added about and services components | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/0c24eed51b19bae7aed91c7bedb982eb9099bac4) | [About Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/about/about.component.ts) |
| #2 | Fixed broken htmls | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/4ae04f14bf1138ee7fdb7583a2e34dfba4cbadb7) | [Services Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/services/services.component.ts) |
| #3 | Added load orders for my services | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/8db9eddf2849542670525b2eda6337f173dab355) | [Broken Htmls](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/services/services.component.html) |
| #4 | Added SPAController and draft batch | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/67e1d02a77ee5b91fee957b2fc806f045de3f624) | [SPA Controller](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/backend/src/main/java/es/urjc/code/daw/marketplace/web/SPAController.java) |
| #5 | Added draft dockerfile | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/a81550bd9e267c3fe12af0b4fe1fde6861440b96) | [Batch draft Angular build](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/docker/angular.bat) |

#### Alberto Mautone

This member participated mostly on the authentication components such as the
login and registration of users, a part from fixing bugs in the rest api and
in the broken htmls.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Updated register component | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/3ace73853a811f6256451430eaf2c2f05d71b6c3) | [Login Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/login/login.component.ts) |
| #2 | Updated login component | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/a8a8163237dc6bbcf07e98d067a4010eac7601c7) | [Login broken html](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/login/login.component.html) |
| #3 | Initial login and register page | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/22d58ef97877c186df19111794e31d39af0c43c4) | [Register Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/register/register.component.ts) |
| #4 | Added base login components | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/97cf6bbab8a180065acbfb0134d1f9e8b755972f) | [Register broken html](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/register/register.component.html) |
| #5 | Fixed user rest controller bug | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/7f31d4a0034798cec41e5753137cd56496466123) | [User Rest Controller](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/7f31d4a0034798cec41e5753137cd56496466123/backend/src/main/java/es/urjc/code/daw/marketplace/api/user/controller/UserRestController.java) |

#### Alejandro José Rodriguez Montero

This member has worked on several components such as the service overview, the
screenshots and legal components, fixed broken htmls, and several merges and
also helped to add the service overview animation.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added service, screenshots and legal components | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/da6dadc05c5d677661b0f43f5cbf77e25ad91a62) | [Service Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/service/service.component.ts) |
| #2 | Fixed broken htmls | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/61b5092994cb2f4af4da345ed2540d53621a8ae4) | [Screenshots Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/screenshots/screenshots.component.html) |
| #3 | Merge modifications 1 | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/efc95b12a5912105e93a4d793403c1465e3f0ca5) | [Legal Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/legal/legal.component.html) |
| #4 | Merge modifications 2 | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/d418912e489dc4888362b5a9261a6654b9625112) | [Service broken html](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/service/service.component.html) |
| #5 | Added service ts animation draft | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/1f9d7bbb13af802c483cffa6b06058ae0486d271) | [Legal broken html](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/legal/legal.component.html) |

#### Álvaro Noguerales Ramos

This member has worked on a lot of components such as the error, the faq, the
homepage and the profile components, has fixed broken htmls, updated profile,
cleanuped and refactored several parts of the components.

| Number | Description | Commit | Focused on Files |
| ------------- | ------------- | ------------- | ------------- |
| #1 | Added error, faq, home and profile components | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/8230568cae5a76d63e48c9438d6fada0ab68b3ea) | [Error Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/error/error.component.html) |
| #2 | Fixed broken htmls | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/4edc401c30701b820a9f6b3af913fbe681b82797) | [Faq Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/faq/faq.component.html) |
| #3 | Updated profile component | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/064afcadfbb492d9eff4bf578e605336271c8338) | [Home Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/home/home.component.html) |
| #4 | Cleanup unused imports | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/42f22629f3d41fa12b6ebf9840bddc142f679528) | [Profile Component](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/profile/profile.component.ts) |
| #5 | Fixed old error code | [> See commit](https://github.com/CodeURJC-DAW-2020-21/webapp11/commit/40bffac43c14aa76b8fef15601011afa996a25ce) | [Profile broken html](https://github.com/CodeURJC-DAW-2020-21/webapp11/blob/main/frontend/angular/marketplace/src/app/components/profile/profile.component.html) |
