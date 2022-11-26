# Account Proxy App

## Usage

### Prerequisites

In order to Build and Run the application you need:

- Java version 17
- Maven
- API_KEY environment variable defined
- Docker (optional)

### Build and Deploy

To build and run the application you can simply execute the bash script _build_and_run.sh_ (Tested on a unix machine
running
an ubuntu like distribution).
The application will be executed in a docker container if a docker installation is found, or by using java itself
otherwise.

### Invoke the Services

You can invoke the service directly by using the [swagger ui](http://localhost:8080/swagger-ui.html) exposed by the
application itself.

## Proposed solution

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── clt
│   │   │           ├── accounts
│   │   │           │   ├── client
│   │   │           │   ├── component
│   │   │           │   └── router
│   │   │           ├── common
│   │   │           │   ├── client
│   │   │           │   ├── error
│   │   │           │   └── router
│   │   │           ├── payments
│   │   │           │   ├── client
│   │   │           │   ├── component
│   │   │           │   └── router
│   │   │           ├── AccountsApplication.java
│   │   │           └── AppConfig.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── clt
│       │           ├── accounts
│       │           │   ├── client
│       │           │   ├── component
│       │           │   ├── routers
│       │           │   └── AccountsApplicationTests.java
│       │           ├── common
│       │           └── payments
│       │               ├── client
│       │               ├── component
│       │               └── router
│       └── resources
│           └── application.properties
├── build_and_run.sh
├── docker-compose.yml
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
├── README.md
└── tree.txt
```

The code is organized by features, where two features have been identified in the requirements:

- **payment**: execute a payment
- **account information**: retrieve account balance and transactions

The business logic for each feature is implemented in its relative component package while the communication with the
downstream service is implemented in the client package of each feature and exposed by an interface.
This structure allows to reduce the effort for future refactoring if, for example, we want to extract each functionality
in a dedicated service as to optimize resource optimization and enforce the system reliability by keeping each core
feature (retrieve account data and issue payment request) behind its own bulkhead.

Where possible, it has been avoided to use framework specific classes, interfaces or annotations while implementing the
business logic to reduce the lock in with the framework. Moreover, by keeping all the bean declaration in specific
configuration files helps in understanding the application configuration and troubleshooting. 

