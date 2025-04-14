package testData;

import org.testng.annotations.DataProvider;
import org.testng.internal.collections.Pair;

import java.util.List;
import java.util.Map;

public class DataProviders {

    @DataProvider(name = "missingFieldCreateParams")
    public Object[][] provideMultipleArgs() {
        List<Pair<Map<String, Object>, String>> params = CreatePlayerData.getMissingRequiredFieldsData();
        Object[][] testData = new Object[params.size()][2];
        for (int i = 0; i < params.size(); i++) {
            testData[i][0] = params.get(i).first();
            testData[i][1] = params.get(i).second();
        }
        return testData;
    }
}
