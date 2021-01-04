# PoGo Stats
[![Build Status](https://travis-ci.org/adheli/pogoStatsApp.svg?branch=main)](https://travis-ci.org/adheli/pogoStatsApp)

This is a simple web app to match your PoGo pokemon with current released Pokemons and their version

  - Check for legendary and shiny releases
  - Add your PoGo Pokemon to check for Legendary and Leagues
  - Register all your shiny Pokemons!

# Features

  - PoGo stats use an open API to get information about the latest released Pokemon for PokemonGo!
  - You can add the ones you have on your game for checking Shiny Pokemon and for creating teams for Leagues according to CP

### Tech

PoGo Stats uses a bunch of tech super easy to learn

* Java SDK 8
* Spring Boot 2.2.4
* Spring Data JPA
* MySQL (sorry, didn't had time to add to a docker)
* Thymeleaf
* Bootstrap (so it's at least good in the eyes)

### Installation

Need to install JDK 8+, MySQL 8 and some IDE.
Maven can be used as standalone from project or installed with the IDE.

From project folder
```sh
$ mvn clean install
$ ./mvnw clean install
$ cd target
$ java -jar pogo-stats-0.0.1-SNAPSHOT.jar
```

Open the brower and navigate to http://localhost:8080/pogo

### Using the app
To start, when loading All Pokemons page, no data is available.
User "Load Pokemon Data" to get data from [PokemonGO API](https://rapidapi.com/Chewett/api/pokemon-go1/details)
