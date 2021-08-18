# Dispatch Simulator
## 1. Submit file strucutre
There are several files under the folder **homework**.
- **DispatchSimulator**: A folder which contains the source code;
- **DispatchSimulator Design.md**: Design document for DispatchSimulator;
- **DispatchSimulator.jar**: .jar file of Dispatch Simulator, you can use java command to run it. Please go to section **2. Usage** check the detail;
- **README.md**: User guide of Dispatch Simulator;
- **json**: This is one folder which contains the json files for local testing.

## 2. Usage
### 2.1 Local Usage
Make sure you have installed java environment before you run it locally.

Since the folder **homework/json** already contains some .json files for local testing, you can directly use following commands to run the simulator under *Matched* or *First-in-first-out*.
```
java -jar DispatchSimulator.jar json/request_fifo.json
java -jar DispatchSimulator.jar json/request_matched.json
```

### 2.2 Request schema
Below is one example of DispatchSimulator equest.
```
{
  "mode": "FIFO",
  "orderJsonPath": "json/dispatch_orders.json"
}
```
- **mode**: To specify the simulator will work under which mode. (**Matched** or **FIFO**)
- **orderJsonPath**: Json file path which contains the data of orders, and below is one example of dispatch orders.
```
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
    },
    {
        "id": "2ec069e3-576f-48eb-869f-74a540ef840c",
        "name": "Acai Bowl",
        "prepTime": 12
    }
]
```

### 2.3 Output description
When you are running DispatchSimulator, you will encounter some kinds of output.
- Order received: the simulator generated a new order;
- Courier dispatched: a courier received request to dispatch order;
- Order prepared: a chef prepared any order.
- Order picked up: a courier arrived and picked up any order.

According to the requirement, simulator needs print the statistics each time an order is picked up, and below is one example when one order is picked up.
```
=================Strategy Statistics=================
Amount of Picked up Orders = 132
Food Wait Time = 8003 ms
Courier Wait Time = 1 ms
=====================================================
```

And after the simulator has finished processing all orders, you can find the **Average Food Wait Time** and **Average Courier Wait Time** for current process.

Such as below is the output for Matched strategy.
```
=================Strategy Statistics=================
Amount of Picked up Orders = 132
Average Food Wait Time = 2289 ms
Average Courier Wait Time = 2303 ms
=====================================================
```

And below is the output for First-in-first-out strategy.
```
=================Strategy Statistics=================
Amount of Picked up Orders = 132
Average Food Wait Time = 730 ms
Average Courier Wait Time = 403 ms
=====================================================
```

## 3. Design
**Design.md** is the design document of DispatchSimulator, please check that document to understand more design decisions.
