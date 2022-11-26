# Account Proxy App

### Prerequisites

In order to Build and Run the application you need:

- Java version 17
- Maven
- API_KEY environment variable defined
- Docker (optional)

To build run the application you can simply execute the bash script _build_and_run.sh_ (Tested on a unix machine running
an ubuntu lke distribution). 
The application will be executed in a docker container if a docker installation is found, by using java itself otherwise.

## Repo Structure

```
├── build_and_run.sh
├── docker-compose.yml
├── Dockerfile
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── clt
│   │   │           ├── accounts
│   │   │           │   ├── client
│   │   │           │   ├── component
│   │   │           │   └── router
│   │   │           ├── AccountsApplication.java
│   │   │           ├── AppConfig.java
│   │   │           ├── common
│   │   │           │   ├── client
│   │   │           │   └── error
│   │   │           └── payments
│   │   │               ├── client
│   │   │               ├── component
│   │   │               └── router
│   │   └── resources
│   │       └── application.properties
│   └── test ...
```

The code is organized by features, where two features have been identified in the requirements:

- **payment**: execute a payment
- **account information**: retrieve account balance and transactions

The business logic for each feature is implemented in its relative component package while the communication with the
downstream service is implemented in the client package of each feature and exposed by an interface
