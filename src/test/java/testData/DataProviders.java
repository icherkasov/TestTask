package testData;

import org.testTask.ENUM.ROLE;
import org.testng.annotations.DataProvider;
import org.testng.internal.collections.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DataProviders {

    @DataProvider(name = "missingFieldCreateParams")
    public Object[][] missingFieldCreateParams() {
        List<Pair<Map<String, Object>, String>> params = CreatePlayerData.getMissingRequiredFieldsData();
        Object[][] testData = new Object[params.size()][2];
        for (int i = 0; i < params.size(); i++) {
            testData[i][0] = params.get(i).first();
            testData[i][1] = params.get(i).second();
        }
        return testData;
    }


    @DataProvider(name = "provideRoles")
    public Object[][] provideRoles() {
        Object[][] testData = new Object[ROLE.values().length][1];
        for (int i = 0; i < ROLE.values().length; i++) {
            testData[i][0] = ROLE.values()[i];
        }
        return testData;
    }

    @DataProvider(name = "provideRolesForCRUD")
    public Object[][] provideRolesForCRUD() {
        List<ROLE> CrudRoles = Arrays.asList(ROLE.ADMIN, ROLE.USER);
        Object[][] testData = new Object[CrudRoles.size()][1];
        for (int i = 0; i < CrudRoles.size(); i++) {
            testData[i][0] = CrudRoles.get(i);
        }
        return testData;
    }

    @DataProvider(name = "provideIncorrectPasswords")
    public Object[][] provideIncorrectPasswords() {
        List<String> passwords = Arrays.asList("five", "1a", "123", "nonumber", "123456789", "toomanysymbolspassword1", "vwerylongpassword", "01234567891234567890");
        Object[][] testData = new Object[passwords.size()][1];
        for (int i = 0; i < passwords.size(); i++) {
            testData[i][0] = passwords.get(i);
        }
        return testData;
    }
}
