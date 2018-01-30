package helpdesk;

import issue.Issue;

import java.util.Calendar;

public class HelpDesk {
    public static final int EOB_HOR = 17;

    public boolean willHandleIssue(Issue issue) {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek)
            return false;
        if (Calendar.FRIDAY == dayOfWeek) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour > EOB_HOR)
                return false;
        }
        return true;
    }
}
