# Dispatch Simulator

`dispatch-simulator` simulates kitchen order fulfillment under two courier dispatch strategies:

- `Matched`
- `FIFO`

The simulator reads a request JSON file, loads an order list, runs the dispatch flow, prints order lifecycle events, and continuously reports food wait time and courier wait time statistics.

## Project Layout

```text
.
├── pom.xml
├── README.md
├── DispatchSimulator
│   ├── resources
│   │   ├── dispatch_orders.json
│   │   ├── dispatch_orders_min.json
│   │   ├── request.json
│   │   ├── request_fifo.json
│   │   ├── request_matched.json
│   │   ├── test_invalid_orders.json
│   │   └── test_invalid_request.json
│   └── src
├── DispatchSimulator Design.md
└── design
```

## Prerequisites

You need:

- Java 8 or later
- Maven 3.9+

## Build

From the repository root:

```bash
mvn clean package
```

To run the legacy unit tests only:

```bash
mvn test
```

This builds an executable jar under:

```text
target/dispatch-simulator-1.0-SNAPSHOT.jar
```

Because the project uses the Maven Shade plugin, the output jar includes the required runtime dependencies.

## Run

Run the simulator by passing a request JSON file path as the first argument:

```bash
java -jar target/dispatch-simulator-1.0-SNAPSHOT.jar DispatchSimulator/resources/request_fifo.json
```

Or:

```bash
java -jar target/dispatch-simulator-1.0-SNAPSHOT.jar DispatchSimulator/resources/request_matched.json
```

## Request File Format

Example:

```json
{
  "mode": "FIFO",
  "orderJsonPath": "DispatchSimulator/resources/dispatch_orders.json"
}
```

Fields:

- `mode`
  Dispatch strategy. Valid values are `Matched` and `FIFO`.
- `orderJsonPath`
  Path to the order list JSON file.

## Order File Format

Example:

```json
[
  {
    "id": "a8cfcb76-7f24-4420-a5ba-d46dd77bdffd",
    "name": "Banana Split",
    "prepTime": 4
  },
  {
    "id": "58e9b5fe-3fde-4a27-8e98-682e58a4a65d",
    "name": "McFlury",
    "prepTime": 14
  }
]
```

Fields:

- `id`
  Order identifier.
- `name`
  Order name.
- `prepTime`
  Preparation time in seconds.

## Sample Commands

### FIFO

```bash
java -jar target/dispatch-simulator-1.0-SNAPSHOT.jar DispatchSimulator/resources/request_fifo.json
```

### Matched

```bash
java -jar target/dispatch-simulator-1.0-SNAPSHOT.jar DispatchSimulator/resources/request_matched.json
```

## Output

During execution, the simulator prints events such as:

- order received
- courier dispatched
- order prepared
- order picked up

When an order is picked up, the simulator prints rolling statistics:

```text
=================Strategy Statistics=================
Amount of Picked up Orders = 132
Food Wait Time = 8003 ms
Courier Wait Time = 1 ms
=====================================================
```

When processing is complete, it prints summary averages for the selected strategy:

```text
=================Strategy Statistics=================
Amount of Picked up Orders = 132
Average Food Wait Time = 2289 ms
Average Courier Wait Time = 2303 ms
=====================================================
```

## Notes

- The sample request files in `DispatchSimulator/resources` are already updated to use the current repository-relative order file paths.
- If you run the jar from a different working directory, make sure the request file path and the `orderJsonPath` inside it still resolve correctly.

## Design

See:

- [DispatchSimulator Design.md](/Users/yangruiguo/Documents/dispatch-simulator/DispatchSimulator%20Design.md)
