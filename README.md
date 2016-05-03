# transaction-api

[![Build Status](https://api.travis-ci.org/jbspeakr/transaction-api.svg?branch=master)](https://travis-ci.org/jbspeakr/transaction-api)

This demo application provides a RESTful web service that stores some transactions (in memory) and returns information about those transactions.

## Used technologies:
* Java8
* SpringBoot
* Spring Data Key-Value
* Project Lombok
* ...

## Some notes:
This application is just a private demo. it does some weird stuff, you probably don't want in your applications:
* (a) instead of using a proper (e.g. SQL) database this app performs query operations in code, using Java8 streams
* (b) instead of using a more suitable data structure (something similar to a Linked-List) this app uses recursion to solve child-parent-associations
* (c) it does not provide an state-of-the-art HATEOAS API, but defines less-mature endpoints
* (d) there is no built-in API versioning
* ...

## Example usage:
```
PUT /transactionservice/transaction/10
{ "amount": 5000, "type": "cars" }
=> { "status": "ok" }

PUT /transactionservice/transaction/11 
{ "amount": 10000, "type": "shopping", "parent_id": 10 }
=> { "status": "ok" }

GET /transactionservice/types/cars
=> [10]

GET /transactionservice/sum/10
=> {"sum":15000}

GET /transactionservice/sum/11
=> {"sum":10000} 
```