# Antenka
## REST API written in Java. 

Main goal of this project was to learn and improve:
1) ORM framework - Hibernate,
2) Spring.

It is REST API for a mobile app (AntenkaMobile), which will be created as next step of the project Antenka development.

Antenka is a project where users can 
1) add a volleyball match and find its players
2) find a voleyball match to play.


## Registration
Every user needs to registers with email, password, first name, last name and birthday.


```http
POST /auth/register
```

```json
{
  "email" : "m.galat@rocketmail.com",
  "password" : "ILoveCatsAndDogs1!",
  "firstName" : "Malgorzata",
  "lastName" : "Galat",
  "birthday" : "1993-03-13"
}
```

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `email` | `string` | **Required**. Unique for every user |
| `password` | `string` | **Required**. Min. 8 char., min. 1 special char., min. 1 digit. Password is stored encrypted.|
| `firstName` | `string` | **Required**. Only letters |
| `lastName` | `string` | **Required**. Only letters |
| `birthdat` | `date` | **Required**. Age over 16 and under 150 |


## Authentication
REST API is stateless, so after sucessful login application didn't start any session. Every request except 'auth/login' and 'auth/registration' is secured and requires from a client a valid JWT.
Registered user can get a token after logging in.

```http
POST /auth/login
```

```json
{
    "email": "m.galat@rocketmail.com",
    "password": "ILoveCatsAndDogs1!"
}
```
### Response
After successful login API returns JWT - `accessToken`. 
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI3NSIsImV4cCI6MTcwMGOSIAc1MSwiZW1haWwiOiJnb3NpYWdhbGF0QHdwLnBsIiwicm9sZXMiOlsiUk9MRV9VU0VSIl19.KvGOSIAptjF7xau6uWvU6RDgnovT-jf0KEq7PuwXs_g"
}
```

## Adding a match
User adds a match with:
1) general informations about a match,
2) slots. Number of players wanted = number of slots. Every slot enables to add its requirements about a player. For example, a match organiser needs 11 players incuding:
3 outside hitters, 2 middle blockers, 1 libero, 2 setters, 3 right side hitters (the match organizer is 12. player).
Other requirements (age, gender, level) can be the same or different.


```http
POST /add
```

```json
{
    "name": "Warszawski Mecz Charytatywny",
    "dateTime": "2024-04-23T18:00",
    "price":{"regularPrice": 20, "benefitPrice":10},
    "address": {"street": "Adolfa Pawińskiego", "number": 2, "zipCode": "02106", "locality": "Warsaw", "description": "Hala"},
    "playersNum": 2,
    "playersWanted":[
    {"playerWanted":{"position": "SETTER", "level": "BEGINNER", "gender": "FEMALE", "ageRange":{"ageMin":20, "ageMax":35}}},
    {"playerWanted":{"position": "LIBERO", "level": "BEGINNER", "gender": "FEMALE", "ageRange":{"ageMin":20, "ageMax":35}}}
    ] 
}
```


| Parameter | Type | Description |
| :--- | :--- | :--- |
| `name` | `string` | **Required**. Match name |
| `dateTime` | `dateTime` | **Required**. Not in the past or more than 6 months from now.|
| `price` | `number` | **Required**. Price per 1 player. If benefit system is unavailable, then regularPrice rgument should be equal benefitPrice argument |
| `address` | all `string` | **Required**. Every parameter of an address is string. Zip code can only be digits. Locality can be only letters. |
| `playersNum` | `integer` | **Required**. Number of players to find. Must be equal playersWanted size.|
| `playersWanted` | `collection` | **Required**. Collection of playerWanted.|
| `playerWanted` | `playerWanted` | **Required**. Requirements about player wanted to sign in match. `Position`: one of `OUTSIDE_HITTER, MIDDLE_BLOCKER, RIGHT_SIDE_HITTER, SETTER, LIBERO`. `Gender`: one of `MALE, FEMALE`. `Level`: one of `BEGINNER, MEDIUM, ADVANCED`. `AgeRange`: 'ageMin': minimal age of a player, 'ageMax': maximal age of a player.


## Adding Player Profile
Player Profile is requirement to find matches and them slots.

```json
{
    "positions": ["RIGHT_SIDE_HITTER"],
    "level": "MEDIUM",
    "gender": "FEMALE",
    "benefitCardNumber": "12345"
}
```

| Parameter | Type | Description |
| :--- | :--- | :--- |
| `positions` | `collection` | **Required**. Collection of enum Position. At least one element required. Duplicates will be ignored. One of 'OUTSIDE_HITTER, MIDDLE_BLOCKER, RIGHT_SIDE_HITTER, SETTER, LIBERO' |
| `level` | `string` | **Required**. One of 'BEGINNER, MEDIUM, ADVANCED'|
| `gender` | `string` | **Required**.  One of `MALE, FEMALE` |
| `benefitCardNumber` | `string` | Not required |
Age is calculated based on user's birthday.

## Searching Match
	-Two endpoints, two structure of result (bidirectional relationalship between Match and Slot
	-Results are based on a player's profile of user - only 
			1) matches with slots where user meets requirements (and other these matches' slots)
			or 
			2) only slots where user meets the requirements.
	-User without player's profile can't find and sign up to a match
	-Client sends only a maximal price
	-If user has an active benefit card, then only benefit prices are checked. If player doesn't have active benefit card, then only
	regular price are checked.
	-If match doesn't have benefit price, then for simplicity "regular price = benefit price" 

