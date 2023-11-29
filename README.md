# Antenka
## REST API written in Java. 

Main goal of this project was to learn and improve:
1) ORM framework - Hibernate,
2) Spring.

It is REST API for a mobile app (AntenkaMobile), which will be created as next step of the project Antenka development.

Antenka is a project where users can 
1) add a volleyball match and find its players
2) find a voleyball match to play.


### Registration
Every user needs to registers with email, password, first name, last name and birthday.

auth/registration

```http
POST /auth/register
```

```javascript
{
  "message" : string,
  "success" : bool,
  "data"    : string
}
```
{"email": "gosiag@wp.pl",
"password": "kotkotkot1!",
"firstName": "Gosia",
"lastName": "Galat",
"birthday":"1993-03-13"
}


	email requirements:

	1) be unique
	2) contains "@" and "."
	3) min. 8 characters
	password requirements:
	1) min. 8 characters
	2) min. 1 special character
	3) min. 1 digit
	first name:
	1) only letters
	last name:
	1) only letters
	birthday:
	1) age over 16
	2) age under 150

*Logging in
	-logging in by email and password
	-REST API is stateless, so after sucessful login application didn't start any session
	-client gets a valid JWT token, which is requirement for every request except "/", "auth/login" and "auth/registration"

*Adding Player Profile
	-Player Profile is requirement to find matches and slots
	-positions - one or many; required enum format: OUTSIDE_HITTER, MIDDLE_BLOCKER, RIGHT_SIDE_HITTER, SETTER, LIBERO
	-gender - one; required enum format: MALE, FEMALE
	-level - one; required enum format: BEGINNER, MEDIUM, ADVANCED
	-benefit card number - not requirement

*Adding match
	- User adds match with general information about it and with its slots. Number of players wanted = number of slots.
	- Every slot enables to add its requirements about a player. For example, a match organiser needs 11 players incuding:
	  3 outside hitters, 2 middle blockers, 1 libero, 2 setters, 3 right side hitters (the match organizer is 12. player).
	Other requirements (age, gender, level) can be the same or different.


*Searching Match
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
*Signing Up
	-Client sends eventID and slot number. eventID = unique identifier of a match, slot number = order number of slot in a particular
	match. Every slot gets unique ID and its order number, starting from 1. 
