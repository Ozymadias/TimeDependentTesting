package helpdesk;

import issue.Issue;

import java.util.Calendar;

class HelpDesk {
    private static final int EOB_HOR = 17;
    private Calendar calendar;

    HelpDesk(Calendar calendar) {
        this.calendar = calendar;
    }

    boolean willHandleIssue(Issue issue) {
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
