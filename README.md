# DAW Host Services (_Marketplace_)

## Logo

![alt-text](https://i.imgur.com/GZR3STD.png "DAW Host Services Logo")

## Description

The main objective of this application is to be a hosting provider, which offers _Shared_, _VPS_, 
_Dedicated Servers_ and other services. This website will allow you to rent servers for different
purposes as blog, datacenter and any purpose you can imagine from small to large business companies.

## Team Members

| Full Name | Email | Github Profile |
| ------------- | ------------- | ------------- |
| Serghei Sergheev  | s.sergheev.2018@alumnos.urjc.es  | [sergheevdev](https://github.com/sergheevdev)  |
| Allan Robert Cobb Bellido | ar.cobb.2018@alumnos.urjc.es  | [Allanmaster](https://github.com/Allanmaster)  |
| Álvaro Noguerales Ramos  | a.noguerales.2016@alumnos.urjc.es  | [Anogue](https://github.com/Anogue)  |
| Alberto Mautone | a.mautone.2020@alumnos.urjc.es  | [albehma](https://github.com/albehma)  |
| Alejandro José Rodriguez Montero | aj.rodriguez.2018@alumnos.urjc.es  | [Alexrguez9](https://github.com/Alexrguez9)  |

All the organisation will be done using a Trello board.

## Phase 0

### Theme

- We decided that we wanted to build a _Marketplace_ and finally decided to design a server rental application.

### Entities

- **User**: a user will be the entity in charge of storing all kind of information related to authentication.
- **Role**: a role which represents the authority related to a user and contains the pertinent permissions.
- **Client**: a client will contain domain related (business) and private information about our client.
- **Product**: a product will all its configurations and specifications.
- **Category**: represents a category which will allow us to group related products.

### Roles and permissions

The previously mentioned _Role_ entity will be responsible of holding all the permissions.

- **Unregistered User**: can read generic information (i.e. homepage, about us, pricing).
- **Registered User**: can perform client tasks (i.e. can order or cancel products, change profile information, check their hired services).
- **Administrator**: can effectuate moderation and administrative actions (i.e. disabling client account, checking statistics, modify accounts info).

### Images

- The application **Administrator** can check all user profile pictures (avatars) and other information.
- We'll be using **Icons** in different sections to improve accesibility.

### Charts

- The **Administrator** control panel will contain a section where you can visualize different statistics (i.e. daily purchases, accumulated earnings).

### Third-party Additional Technology

- [Gmail API](https://developers.google.com/gmail/api): for sending the client the rented server information to be able to connect to it confirming its order.
- [Apache PDF Box](https://pdfbox.apache.org/): for generating the receipt when finished processing the order.

### Advanced Algorithm

- If it was your **first purchase**, give a 75% non-recurrent discount on inauguration (or on special events) (_will be configurable/toggleable on panel_).
- **Incremental purchases:** every X services you rent in an interval of time give an additional 10% discount (_also configurable_).


