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

| Controller                   | Description                                                      | Notes                                                                        |
|------------------------------|------------------------------------------------------------------|------------------------------------------------------------------------------|
| /player/create/{editor}      | Sending create request with GET method and many query parameters | Most likely needs fix, but maybe just very strange design                    |
| /player/create/{editor}      | Positive create request has status code 200 instead of 201       | Most likely needs fix, but 200 is standard code for GET so depends on design |
| /player/create/{editor}      | Create response body has fields with null values                 | Bug, needs fix                                                               |
| /player/create/{editor}      | Editor "Admin" can't create player                               | Normally admin role should have such rights                                  |
| /player/create/{editor}      | Create with already existing login updates old player            | Shouldn't work this way. If login already exists - 403 should be returned.   |
| /player/create/{editor}      | Age is limited to range 17-60                                    | Depends on requirements, but upper value looks too low                       |
| /player/get/all              | Role field in response is null for all players                   | Bug, needs fix                                                               |
| /player/get                  | If player not found - response code is 200                       | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | Response contains field Id                                       | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | Response missed field password                                   | Bug, needs fix                                                               |
| /player/update/{editor}/{id} | Editor "Admin" can't update player                               | Normally admin role should have such rights                                  |

