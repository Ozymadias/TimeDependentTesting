package helpdesk;

import issue.Issue;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertTrue;

public class HelpDeskTest {
    private static int[] allHours() {
        return IntStream.rangeClosed(0, 23).toArray();
    }

    private static int[] fridaysServiceHours() {
        return IntStream.rangeClosed(0, 17).toArray();
    }

    private static int[] daysWith24HourService() {
        return new int[]{
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.TUESDAY
        };
    }

    private List<Object[]> pairsDayHourWithWholeDayServiceDays() {
        List<Object[]> list = new ArrayList<>();
        for (Object day: daysWith24HourService())
            for (Object hour: allHours())
                list.add(new Object[]{day, hour});
        return list;
    }

    private List<Object[]> fridaysProperHours() {
        List<Object[]> list = new ArrayList<>();
        for (Object hour: fridaysServiceHours())
            list.add(new Object[]{Calendar.FRIDAY, hour});
        return list;
    }

    @DataProvider
    private Iterator<Object[]> serviceTime() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(pairsDayHourWithWholeDayServiceDays());
        list.addAll(fridaysProperHours());
        return list.iterator();
    }

    @Test(dataProvider = "serviceTime")
    public void issuesShouldBeHandledIfComeInProperTime(int day, int hour) {
        Calendar calendar = mock(Calendar.class);
        when(calendar.get(Calendar.DAY_OF_WEEK)).thenReturn(day);
        when(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(hour);
        HelpDesk helpDesk = new HelpDesk(calendar);

        assertTrue(helpDesk.willHandleIssue(mock(Issue.class)));
    }
}