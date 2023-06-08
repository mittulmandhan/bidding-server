# bidding-server

## Overview

Bidding Server is a custom, high performance, auction server for this online car auction company's web based auctions.
Biding Server manages all the aspect of the whole auction process, from creating an auction to announcing a winner.

The server allows following capabilities to their web-based auction clients:

- [x] Only admin must be able to create auction <br />
    - [x] If there already exists a running auction for the given item then auction will not be created<br />
- [x] user can bid on a running auction<br />
    - [x] Only logged in users are allowed to place bid<br />
    - [x] Accepted or rejected all user bids must be recorded <br />
    - [x] User must not be able to bid on an item which has no running auction or the auction is expired<br />
- [x] user must be able to get the list of running auction<br />
    - [x] If user gave an invalid auction status system must return null response<br />
- [x] Bid Accept Scenarios<br />
    - [x] If there exists no bid against the item and bid amount greater than or equal to base price<br />
    - [x] If there exists a bid against the item and upcoming bid must be greater than or equal to sum of highest bid and step rate<br />
- [x] Bid Rejected Scenarios<br />
    - [x] If there exists no bid against the item and bid amount is less than base price<br />
    - [x] If there exists a bid against the item and upcoming bid is less than the sum of highest bid and step rate<br />
- [x] Auction not found scenarios<br />
    - [x] If auction is expired<br />
    - [x] If auction is null<br />
    - [x] If item does not have running auction<br />
- [x] As soon as the auction is over winner receives email<br />
- [x] Auction automatically gets over once duration completes<br />

#### Teck Stack
* Java
* Spring Boot
    * Web
    * Lombok
    * Netflix eureka
    * Data JPA
    * Config server
    * AMQP
    * MySQL Connector
* Tomcat
* MySQL
* RabbitMQ
* Graddle

## Setup Guide
yet to be added

## Architecture
#### Microservices
![alt text](https://github.com/mittulmandhan/bidding-server/blob/main/Diagrams/modular_diagram.jpeg?raw=true)

#### Workflow
![alt text](https://github.com/mittulmandhan/bidding-server/blob/main/Diagrams/Workflow.jpg?raw=true)

## REST APIs
### POST /auction/
__URL__: http://localhost:9002/auction/ <br /><br />
__Request Parameters__ <br />
Request Body: itemCode, basePrice, stepRate, duration <br /><br />
__Response__ <br />
Response Body: auctionId <br />


### GET /auction/?status=RUNNING
__URL__: http://localhost:9191/auction/?status={RUNNING/OVER} <br /><br />
__Request Parameters__ <br />
Query Parameters: status(running/over) <br /><br />
__Response__ <br />
Response Body: list of auctions <br />

### POST /auction/{itemCode}/bid
__URL__: http://localhost:9191/auction/{itemCode}/bid <br /><br />
__Request Parameters__ <br />
Path Variable: itemCode <br />
Request Body: bidAmount <br /><br />
__Response__ <br />
Response Status: <br />
201 - Bid is accepted <br />
404 - Auction not found <br />
406 - Bid is rejected <br />


## ER Diagram
![alt text](https://github.com/mittulmandhan/bidding-server/blob/main/Diagrams/ERD.jpg?raw=true)

## Problems Resolutions
#### ⭐ Auction Closing
__Problem:__ Close auction as soon as duration ends and send email to winner using a messaging queue since only the security service has the capability to send email to users

__Possible Solutions__
##### 1. MongoDB + Kafka
Using Mongo db and kafka together and set ttl to so that when auction duration ends it will be deleted from database and send message to kafka to send email message to email service

Reason to Reject: This approach will delete the auction from database once the duration ends

##### 2. RabbitMQ
Passing messages to RabbitMQ with delayed execution parameter so that it initiates when auction is over

Reason to Reject: Auctions can last for hours and storing messages in message broker is not a good practice

##### 3. AWS Lambda + RabbitMQ
Using lambda delayed execution and RabbitMQ together so that when ends lambda will start its operation make necessary changes in the database and send message to RabbitMQ to send winner email.

Reason to Reject: 

##### 4. Async + RabbitMQ
Executing asynchronous threads that will wait till auction duration ends and then close the auction and send message to RabbitMQ to send winner email.

Reason to reject: Closing the program will terminate all the asynchronous threads that are waiting to close their corresponding auctions.

##### 5. Spring Scheduler + RabbitMQ
Scheduling a job every 20 sec to close all the running auctions that are expired and send message to rabbitmq to send winner email. In this approach, situation might occur where auction duration has ended but still the status is Running so to cop up with that we can add an additional condition in bidding service to check if the auction has expired or not.If the auction is expired no bid can be placed against this auction.

Reason to Accept: Every auction will be closed sooner or later even if one job fails next job will compensate. Additional condition will prevent users to bid on an expired auction even if it is not closed.
