package net.pumbas.deadlinebot.canvascalendar;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TrackedCalendar
{
    private final static Pattern SUBJECT_PATTERN = Pattern.compile("([a-zA-Z]+) ?(\\w*[\\d]+\\w*)");

    @Getter
    private final String ownerId;

    @Getter
    private final String id;

    @Getter
    private final String summary;

    private final Set<String> courses;

    public TrackedCalendar(String ownerId, CalendarData calendarData) {
        this(ownerId, calendarData.getId(), calendarData.getSummary());
    }

    public TrackedCalendar(String ownerId, String id, String summary) {
        this.ownerId = ownerId;
        this.id = id;
        this.summary = summary;
        this.courses = new HashSet<>();
    }

    public boolean isTracked(String eventSummary) {
        return courses.stream()
            .anyMatch(subject -> eventSummary.endsWith(subject + "]"));
    }

    /**
     * Adds and formats courses to be tracked. For a course to be tracked they have to be in the format:
     * 'SOFTENG281' or 'SOFTENG 281'. The text doesn't need to be capitalised, and it can have characters before and
     * after the course number, e.g: 'ACCTG 151G' or 'ACADINT A01'. Do note that in the second case, where there are
     * characters before the course number, for them to be properly detected there must be a space between 'ACADINT'
     * and 'A01'. 'ACADINTA01' will be reformatted to 'ACADINTA 01', which will lead to the course not being
     * properly filtered. If a course is not of a valid format, e.g: just the course number: '281', then it will be
     * ignored.
     *
     * @param course
     *      The course to track
     */
    public void addCourse(String course) {
        Matcher matcher = SUBJECT_PATTERN.matcher(course.trim());
        if (matcher.matches()) {
            String blacklistedSubject = "%s %s".formatted(matcher.group(1), matcher.group(2)).toUpperCase();
            this.courses.add(blacklistedSubject);
        }
    }

    public void addCourses(Set<String> subjects) {
        for (String subject : subjects) {
            this.addCourse(subject);
        }
    }

    public void clearCourses() {
        this.courses.clear();
    }

    /**
     * @return The tracked courses in an unmodifiable set
     * @see TrackedCalendar#addCourse(String)
     */
    public Set<String> getCourses() {
        return Collections.unmodifiableSet(courses);
    }
}
