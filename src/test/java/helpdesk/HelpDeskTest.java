package helpdesk;

import issue.Issue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class HelpDeskTest {
    private Calendar calendar;
    private HelpDesk helpDesk;

    @BeforeMethod
    private void setUp() {
        calendar = mock(Calendar.class);
        helpDesk = new HelpDesk(calendar);
    }

    private static int[] allHours() {
        return IntStream.rangeClosed(0, 23).toArray();
    }

    private static int[] fridaysServiceHours() {
        return IntStream.rangeClosed(0, 17).toArray();
    }

    private static int[] fridaysNotServiceHours() {
        return IntStream.rangeClosed(18, 23).toArray();
    }

    private static int[] daysWith24HourService() {
        return new int[]{
                Calendar.MONDAY,
                Calendar.TUESDAY,
                Calendar.WEDNESDAY,
                Calendar.TUESDAY
        };
    }

    private static int[] weekend() {
        return new int[]{
                Calendar.SATURDAY,
                Calendar.SUNDAY
        };
    }

    private List<Object[]> cartesianProduct(int day, int[] hours) {
        return Arrays.stream(hours)
                .mapToObj(hour -> new Object[]{day, hour})
                .collect(Collectors.toList());
    }

    private List<Object[]> cartesianProduct(int[] days, int[] hours) {
        return Arrays.stream(days)
                .mapToObj(day -> cartesianProduct(day, hours))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @DataProvider
    private Iterator<Object[]> serviceTime() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(cartesianProduct(daysWith24HourService(), allHours()));
        list.addAll(cartesianProduct(Calendar.FRIDAY, fridaysServiceHours()));
        return list.iterator();
    }

    @Test(dataProvider = "serviceTime")
    public void issuesShouldBeHandledIfComeInProperTime(int day, int hour) {
        when(calendar.get(Calendar.DAY_OF_WEEK)).thenReturn(day);
        when(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(hour);

        assertTrue(helpDesk.willHandleIssue(mock(Issue.class)));
    }

    @DataProvider
    private Iterator<Object[]> timeWithoutService() {
        List<Object[]> list = new ArrayList<>();
        list.addAll(cartesianProduct(weekend(), allHours()));
        list.addAll(cartesianProduct(Calendar.FRIDAY, fridaysNotServiceHours()));
        return list.iterator();
    }

    @Test(dataProvider = "timeWithoutService")
    public void issuesShouldNotBeHandledIfComeInImproperTime(int day, int hour) {
        when(calendar.get(Calendar.DAY_OF_WEEK)).thenReturn(day);
        when(calendar.get(Calendar.HOUR_OF_DAY)).thenReturn(hour);

        assertFalse(helpDesk.willHandleIssue(mock(Issue.class)));
    }
}