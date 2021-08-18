# Dispatch Simulator Design

## 1. Product Description

Create one system to simulate the the fulfillment of delivery orders for a kitchen, the system should support two courier dispatch strategies: Matched and First-in-first-out. As a result, the system can be used to evaluate the performance of the two courier dispatch strategies.

## 2. Terminology

* **Matched**:  a courier is dispatched for a specific order and may only pick up that order.
* **First-in-first-out (FIFO)**: a courier picks up the next available order upon arrival. If there are multiple orders available, pick up an arbitrary order. If there are no available orders, couriers wait for the next available one. When there are multiple couriers waiting, the next available order is assigned to the earliest arrived courier.
* **Average food wait time**: the time between order ready and pickup.
* **Average courier wait time**: the time between arrival and order pickup.

## 3. Function Requirements

* The simulator should generate 2 delivery orders per second.
* If the simulator is working under Matched strategy, a courier needs to wait and pick up specific order.
* If the simulator is working under FIFO strategy, if there are no available orders, couriers wait for the next available one. When there are multiple couriers waiting, the next available order is assigned to the earliest arrived courier.
* The simulator will print food wait time and courier wait time each time when an order is picked up.
* The simulator will calculate and print average food wait time and average courier wait time when simulator has finished processing all orders.

## 4. Design Overview

### 4.1 Use case analysis

There are four kinds of objects in this simulator: Order Generator, Chef, Courier and Analyst.

* Order Generator: only one instance in this simulator and generates 2 orders per second;
* Chef: multiple instances in this simulator, Chef will receive orders and prepare them;
* Courier: multiple instances in this simulator, Courier will receive orders and pick up them after Chef has prepared the order;
* Analyst: only one instance in this simulator, it will calculate the average food wait time and average courier wait time.

### 4.2 Components Interactive Analysis

For the 4 components in Dispatch Simulator, Image 1 shows their interactive relationships between them. And the difference between Matched and FIFO strategies are step 6 and 7, if we apply Matched strategy, Courier needs to wait and pick up specific order; if we apply FIFO strategy, Courier can pick up any prepared order(if there are multiple prepared orders).

![Image 1: Sequence diagram for components of Dispatch Simulator](https://github.com/jiabailie/homeworks/blob/master/dispatch-simulator/design/strategy.jpeg)

Image 1: Sequence diagram for components of Dispatch Simulator

## 5. Detail Design

### 5.1 Order

The raw data schema of Order from .json file is composed by 3 fields { id, name, prepTime }, when the simulator have read the raw data from .json, it needs to expand the data schema of Order for Matched and First-in-first-out strategies.

|name	|data type	|description	|
|---	|---	|---	|
|id	|string	|Order id, get it from .json.	|
|name	|string	|Order name, get it from .json.	|
|prepTime	|integer	|Current order needs prepTime seconds to be prepared, get it from .json.	|
|orderCreateTime	|datetime	|Order create time, it is the time when simulator received this order.	|
|orderPreparedTime	|datetime	|The time when Chef has prepared this order	|
|courierArrivalTime	|datetime	|The arrival time of the Courier who will pick up this order	|
|courierPickupTime	|datetime	|The time when this order is picked up by som Courier	|

So according to the schema of Order, we have below formula to calculate food wait time and courier wait time for each order.

> food wait time = courierPickupTime - orderPreparedTime

> courier wait time = courierPickupTime - courierReadyTime

### 5.2 Courier Arrival Time

According to the requirement, Couriers will arrive 3-15 seconds after they’ve been dispatched. For this, since the method Math.random() in Java will return a number greater than or equal to 0.0 and less than 1.0 with approximately uniform distribution, so we can use below formula to get the courier arrival time.

```
COURIER_ARRIVAL_MAX = 15
COURIER_ARRIVAL_MIN = 3
Arrival_Time = Math.random() * (COURIER_ARRIVAL_MAX - COURIER_ARRIVAL_MIN + 1) + COURIER_ARRIVAL_MIN
```
