# Trading Card Game Web App

## Overview

This is a web application for a trading card game. Users can buy and sell cards in the market, view their own cards, and perform other actions related to managing their collection.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) version 8 or higher
- Maven

### Installation and Setup

1. Compile and run the project using Maven:

2. Once the application is running, open your web browser and go to [http://localhost:8080/h2-console](http://localhost:8080/h2-console).

3. In the H2 Database Console, enter the following JDBC URL:

   ```
   jdbc:h2:mem:testdb
   ```

4. Click **Connect** to access the H2 in-memory database.

5. In the SQL query editor, paste the contents of the SQL file provided in the project (e.g., `data.sql`). This file contains queries to populate the database with initial data, including 10 cards.

6. After running the SQL queries, you can create two distinct users, each with five basic cards, by signing up through the application's registration page.

7. You can then explore the application by buying and selling cards in the market, viewing your own cards, and trying out other features.
