# Weather Forecast Telegram Bot with Microservice

<img width="492" alt="Screenshot 2023-11-17 at 18 58 21" src="https://github.com/frog-da/WeatherBotProject/assets/84839431/6c1ef3b5-867f-4bc1-901e-9b27d6d271af">

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
  - [Telegram Bot Commands](#telegram-bot-commands)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Configuration](#configuration)
- [Logging](#logging)
- [Deployment](#deployment)

## Overview

A Telegram bot that provides users with real-time weather information for their chosen locations. The bot interacts with a microservice, which queries external weather APIs and returns the weather data. 

## Features

- Interactive Telegram bot for weather queries.
- Real-time weather data retrieval from external sources.
- Logging for tracking user interactions and errors.

## Getting Started

### Prerequisites

- Scala
- SBT (Scala Build Tool)
- Telegram Bot Token (obtained from [BotFather on Telegram](https://core.telegram.org/bots#botfather))
- API Key for OpenWeatherMap Weather Service

### Installation

1. Clone the repository:
   ```shell
   git clone https://github.com/frog-da/WeatherBotProject.git
   cd WeatherBotProject
   ```

2. Build the project using SBT:
   ```shell
   sbt compile
   ```

## Usage

To use the Weather Forecast Telegram Bot, follow these steps:

1. Start a conversation with the bot by searching for its username or by clicking the provided link.

2. Send a message or command to the bot. For example:
   - `/weather CityName` to get the weather for a specific city.

### Telegram Bot Commands

- `/start` - Start a conversation with the bot.
- `/weather CityName` - Get the weather for a specific city.

## Project Structure

The project is structured as follows:

- [x] `src/` - Contains the source code for the bot and microservice.
- [ ] `test/` - Contains test code.
- [ ] `docs/` - Documentation and API reference.
  
## Testing

Testing for this project has not been implemented yet. 

## Configuration

Configuration settings, such as API keys, can be managed in the `application.conf` file.

## Logging

Logging is implemented using the log4cats library. Logs can be found in the `logs/` directory.

## Deployment

Deployment for this Scala Telegram bot has not been implemented yet.

---

### **Step 1: Project Setup**

- [x]  1.1. Create a new directory for your MVP project.
- [x]  1.2. Initialize a new Scala project using your preferred build tool (e.g., SBT).
- [x]  1.3. Set up a Git repository for version control and make an initial commit.

### **Step 2: Telegram Bot Development**

- [x]  2.1. Create a new Telegram bot by talking to the BotFather on Telegram to obtain a bot token.
- [x]  2.2. Implement the Telegram bot using the chosen library (e.g., Telegramium or Bot4s Telegram). Set up the bot token and basic configuration.
- [x]  2.3. Develop the bot to respond to user messages and commands, focusing on the "/weather" command.

### **Step 3: Weather Microservice**

- [x]  3.1. Define data models for weather information (e.g., **`WeatherResponse`**) for the predefined city (e.g., "London").
- [x]  3.2. Implement a microservice to interact with a single external weather API (e.g., OpenWeatherMap) using STTP.
- [x]  3.3. Ensure the microservice can retrieve weather data for the predefined city and convert it into the data models.
- [x]  3.4. Implement basic error handling in the microservice to handle potential issues during data retrieval.

### **Step 4: Bot-Microservice Integration**

- [x]  4.1. Connect your Telegram bot with the microservice.
- [x]  4.2. Implement logic to allow users to send a "/weather" command to the bot to request weather information for the predefined city.
- [x]  4.3. Ensure the bot communicates with the microservice to retrieve weather data.
- [x]  4.4. Send weather information back to users as a response message.

### **Step 5: Testing**

- [ ]  5.1. Write unit tests for the core functionality of the Telegram bot, including command processing and interaction with the microservice.
- [ ]  5.2. Develop basic integration tests to verify that the bot can retrieve weather data from the microservice.
- [ ]  5.3. Implement data retrieval tests for the microservice to validate its interaction with the external weather API.

### **Step 6: Documentation**

- [ ]  6.1. Provide minimal documentation for users on how to initiate a conversation with the bot and request weather information using the "/weather" command.

### **Step 7: Deployment**

- [ ]  7.1. Deploy the Telegram bot and microservice to a server or cloud platform for demonstration.