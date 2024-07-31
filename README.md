# 🎉 Event-Ease API

<p align="center">
  <img src="https://i.ibb.co/Zm4hVmB/Purple-Orange-Pink-Modern-Gradient-Finance-Platform-Animated-Logo-2.png" width="80%"/>
</p>


Welcome to Event-Ease API! Our SaaS platform helps you seamlessly manage any event quickly and easily. Perfect for any occasion, from corporate meetings to weddings and everything in between. Our versatile SaaS platform provides comprehensive functionality to create, update, view, and delete events, transactions, and reservations, ensuring a smooth and personalized experience for any occasion. 🎊


This project uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.


## 📖 Overview

Event-Ease API provides a powerful set of tools to handle event bookings, event details, and operations. Below you'll find everything you need to get started and integrate our API into your applications.

### Version

This documentation covers version `1.0.0` of the Event-Ease API.

## 📚 Functionalities

### Booking Resource

#### Easy and Flexible Booking
Make your bookings effortlessly! You can book an event as long as it is in the "PUBLISHED" state and within the defined reservation date range, if applicable. Please note that you cannot book events that are "IN PROGRESS", "COMPLETED", "CLOSED", or "CANCELLED".

Additionally, bookings cannot be made if the event has reached its maximum capacity. Enjoy planning and experiencing your favorite events with peace of mind!

| Title                          | Description                                                                                                                        |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| Create a booking               | Creates a new booking for a specified event and consumer.                                                                          |
| Retrieve Bookings paginated    | Retrieves a paginated list of bookings based on specified filters (see below).                                                     |
| Retrieve Bookings              | Retrieves a list of bookings based on specified filters (events, limit, offset, statuses).                                         |
| Retrieve a Booking by ID       | Retrieves details of a specific booking by its ID.                                                                                 |
| Delete a Booking by ID         | Deletes a specific booking by its ID.                                                                                              |
| Update a Booking by ID         | Updates the consumer information of a specific booking by its ID.                                                                  |  
## 
### Event Resource

#### Create and Manage Events with Ease
Design and manage a wide range of events effortlessly! You can create events by specifying essential details such as availability, location ID, activity ID, and organizer ID. Set the start and end dates, include an optional booking date, and specify the number of available spots if needed.

Once an event is created and marked as "PUBLISHED", it will be managed automatically by our system. The system will handle the transition of the event status to "IN PROGRESS" when the time comes. Enjoy a seamless experience in setting up and overseeing your events with minimal hassle!

| Title                          | Description                                                                                                                        |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------|
| Create an Event                | Creates a new event with specified details such as owner, activity id, bookable time range and event time range.                   |
| Retrieve Events paginated      | Retrieves a paginated list of events based on specified filters (see below).                                                       |
| Retrieve Events                | Retrieves a list of events based on specified filters (activities, codes, endFrom, limit, offset, startFrom, statuses, suppliers). |
| Retrieve Event by Code         | Retrieves an event by its code. Each event has an unique human readable code.                                                      |
| Retrieve Event by ID           | Retrieves details of a specific event by its ID.                                                                                   |
| Delete an Event by ID          | Deletes a specific event by its ID.                                                                                                |
| Update an Event by ID          | Updates details of a specific event by its ID.                                                                                     |
| Close an event in progress     | Closes a specific event by its ID with a given reason.                                                                             |
| Complete an event in progress  | Completes a specific event by its ID.                                                                                              |
| Publish an event created       | Publishes a specific event by its ID. Internally the API EventEase will know when it will be In Progress based on event infos.     |
## 
### Operation Resource

#### Manage Operations Seamlessly
Handle operations with the same ease as events! Operations are the atomic activities that fall under a group of operations within an event. These operations are optional but crucial for tracking the progress of activities throughout the event.

You can create and manage these operations, specifying details just like you would for events. This includes managing their status and ensuring they fit within the broader context of the event. Operations allow for meticulous tracking and management of each activity's progress during the event, ensuring everything runs smoothly and efficiently.

| Title                          | Description                                                                                                                              |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| Create an Operation            | Creates a new operation with specified details.                                                                                          |
| Retrieve Operations paginated  | Retrieves a paginated list of operations based on specified filters (see below).                                                         |
| Retrieve Operations            | Retrieves a list of operations based on specified filters (activities, endFrom, events, limit, offset, operators, startFrom, statuses).  |
| Retrieve Operation by ID       | Retrieves details of a specific operation by its ID.                                                                                     |
| Delete an Operation by ID      | Deletes a specific operation by its ID.                                                                                                  |
| Update an Operation by ID      | Updates details of a specific operation by its ID.                                                                                       |
| Close an operation in progress | Closes a specific operation by its ID with a given reason.                                                                               |
| Complete an operation in progress | Completes a specific operation by its ID.                                                                                             |
| Publish an operation created   | Publishes a specific operation by its ID.                                                                                                |


For more details, please consult the [OpenAPI document](https://github.com/servament/eventease/blob/master/src/main/resources/apis/openapi.yaml) 📄🔍. This comprehensive guide provides all the information you need to effectively use and integrate the functionality.
## 
## 🚀 Getting Started

### Base URLs

- Local Development: `http://localhost:8080`
- Production: `http://0.0.0.0:8080`

#### (DEV) Starting the services with Docker Compose
To start the service in development mode, run the following command:

```bash
docker compose -f docker-compose.dev.yml up -d
```

📄 This command starts all services defined in the `docker-compose.dev.yml` file in `detached` mode, meaning in the background.

---

#### Retrieving the Database Container IP Address
To retrieve the IP address of the database container, use the following command:

```bash
docker inspect eventease_db | grep IPAddress
```

🔍 **Note**: Use the obtained IP address to connect to the database via pgAdmin.

---

#### Connecting to the Database with pgAdmin
1. **Open pgAdmin**.
2. **Create a new server** and use the IP address obtained in the previous step as the server address.

#### Running the in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

#### Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

#### Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/eventease-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

#### Related Guides

- Mutiny ([guide](https://quarkus.io/guides/mutiny-primer)): Write reactive applications with the modern Reactive Programming library Mutiny
- Hibernate Validator ([guide](https://quarkus.io/guides/validation)): Validate object properties (field, getter) and method parameters for your beans (REST, CDI, Jakarta Persistence)
- REST resources for Hibernate Reactive with Panache ([guide](https://quarkus.io/guides/rest-data-panache)): Generate Jakarta REST resources for your Hibernate Reactive Panache entities and repositories
- Reactive Routes ([guide](https://quarkus.io/guides/reactive-routes)): REST framework offering the route model to define non blocking endpoints


## 📬 Support

Need help or have questions? Reach out to our support team!

- **Name:** Example API Support
- **Website:** [Contact Us](http://exampleurl.com/contact)
- **Email:** [coralo.luca@outlook.com](mailto:coralo.luca@outlook.com)

## 📄 License

Event-Ease API is licensed under the MIT License. See the full license [here](https://opensource.org/license/mit).

---

Happy event managing! 🎉

