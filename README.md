# Person-manager project

Manage persons as part of professional network to assess how to help or the context of work together.

## Requirements

* Present a list of the active persons in a table format, sortable by name
* Present a button to enter meeting information like customer, purpose, attendees and track to do items.
* Search on meeting context, customer name, person name
* Update meeting / project to add more info

## Techno stack

* Cloudant: local or as a service on IBM Cloud
* Quarkus Panache, Resteasy
* UI in Vue.js


## Running the application in dev mode

Run the application in dev mode which enables live coding using:

```
# Start compose from parent directory
docker-compose up
# start app in development mode
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package -DskipTests`.
It produces the `person-manager-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

The application is now runnable using `java -jar target/person-manager-1.0.0-SNAPSHOT-runner.jar`.

## Build the image locally

```

```

## Deploy via knative

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/person-manager-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/building-native-image.