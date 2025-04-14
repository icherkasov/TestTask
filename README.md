# Content

1. **Run**
2. **Report**
3. **Bugs**

## 1. RUN

``
./gradlew clean test
``

### Configuration Parameters

| Parameter Name | Possible Values                         | Default Value       | Description                                  |
|----------------|-----------------------------------------|---------------------|----------------------------------------------|
| uri            | any http(s)                             | http://3.68.165.45/ | Base URL for API requests                    |
| parallel       | methods, classes, tests etc from testNG | methods             | Sets parallelization to defined testNG value |
| threadCount    | any number                              | 1                   | sets amount of threads                       
 suiteXml       | name of any testNG.xml                  | All.xml             | Runs corresponding xml file with tests       |

### Run command with all optional parameters

``
./gradlew clean test -Puri=http://3.68.165.45/ -Pparallel=methods -PthreadCount=3 -PsuiteXml=All.xml
``

## 2. REPORT

After tests execution this command will generate and open in browser Allure report

``
./gradlew showReport
``

## 3. Bugs

| Controller                   | Description                                                                | Notes                                                                        |
|------------------------------|----------------------------------------------------------------------------|------------------------------------------------------------------------------|
| /player/create/{editor}      | Sending create request with GET method and many query parameters           | Most likely needs fix, but maybe just very strange design                    |
| /player/create/{editor}      | Positive create request has status code 200 instead of 201                 | Most likely needs fix, but 200 is standard code for GET so depends on design |
| /player/create/{editor}      | Create response body has fields with null values                           | Bug, needs fix                                                               |
| /player/create/{editor}      | Create supervisor response code 400                                        | Looks like 403 is more correct for such case                                 |
| /player/create/{editor}      | Request to create with incorrect role status 400                           | Looks like 403 is more correct for such case                                 |
| /player/create/{editor}      | Password field in request is not mandatory                                 | Swagger says it's optional so not sure what is correct behaviour             |
| /player/create/{editor}      | Password can have any value                                                | Bug, needs fix                                                               |
| /player/create/{editor}      | Gender can have any value                                                  | Bug, needs fix                                                               |
| /player/get/all              | Role field in response is null for all players                             | Bug, needs fix                                                               |
| /player/get                  | If player not found - response code is 200                                 | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | Response contains field Id                                                 | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | Response misses field password                                             | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | User can update other users and admins                                     | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | screenName can be changed to already existing one                          | Bug, needs fix                                                               |
| /player/delete/{editor}}     | User editor can delete  players with user and admin roles, including himself | Bug, needs fix                                                               |
| /player/delete/{editor}}     | Admin editor can delete  other  players with admin roles                   | Not clear from requirements if admin can delete other admin. Guess shouldn't |

