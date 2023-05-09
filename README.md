
# [WeatherService]()

---
This is a weather service application used to know the weather in different cities to which user is subscribed. Here, there are admin and user roles.
---

---

### [Admin]()

1. **Can create / add new city with weather:**
    * In the same API admin must create City and its Weather.
2. **Can update / edit / delete city and its weather:**
    * When admin is editing the city they can only edit the deleted field of it. Actually after setting true the deleted
      field the city is not deleted from table, but its status becomes as a deleted ⋙ ```deleted = true```
    * Weather updated by entering the `cityName` and `countryName`. Then In order to update Weather admin must fill in all information related to it.
3. **Can edit user:**
   * Change the user role.
   * Can block user by setting `active` field false
4. **Can get all information about existing users**

 * * *

### [User]()

1. **Can see all active / non deleted cities list.**
2. **Can Subscribe to available cities for their weather information.** 
3. **Can Unsubscribe from subscribed cities one by one.** 


***
* after being registered and logged in can see the list of cities and subscribe to them and get the weather information
  of all them (all cities).

* In order to create user with admin role, firstly register new user after that change the role in database from "USER"
to "ADMIN" manually.

