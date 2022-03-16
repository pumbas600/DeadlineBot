package net.pumbas.deadlinebot.canvascalendar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
    private final String id;

    @Getter
    private final String summary;

    private final Set<String> blacklistedSubjects;

    public TrackedCalendar(CalendarData calendarData) {
        this(calendarData.getId(), calendarData.getSummary());
    }

    public TrackedCalendar(String id, String summary) {
        this.id = id;
        this.summary = summary;
        this.blacklistedSubjects = new HashSet<>();
    }

    public boolean isBlacklisted(String eventSummary) {
        return blacklistedSubjects.stream()
            .anyMatch(subject -> eventSummary.endsWith(subject + "]"));
    }

    /**
     * Adds and formats subjects to be blacklisted. For a subject to be blacklisted they have to be in the format:
     * 'SOFTENG281' or 'SOFTENG 281'. The text doesn't need to be capitalised, and it can have characters before and
     * after the subject number, e.g: 'ACCTG 151G' or 'ACADINT A01'. Do note that in the second case, where there are
     * characters before the subject number, for them to be properly detected there must be a space between 'ACADINT'
     * and 'A01'. 'ACADINTA01' will be reformatted to 'ACADINTA 01', which will lead to the subject not being
     * properly filtered. If a subject is not of a valid format, e.g: just the course number: '281', then it will be
     * ignored.
     *
     * @param subject
     *      The subject to blacklist
     */
    public void addBlacklistedSubject(String subject) {
        Matcher matcher = SUBJECT_PATTERN.matcher(subject.trim());
        if (matcher.matches()) {
            String blacklistedSubject = "%s %s".formatted(matcher.group(1), matcher.group(2)).toUpperCase();
            this.blacklistedSubjects.add(blacklistedSubject);
        }
    }

    public void addBlackListedSubjects(Set<String> subjects) {
        for (String subject : subjects) {
            this.addBlacklistedSubject(subject);
        }
    }

    public void clearBlacklistedSubjects() {
        this.blacklistedSubjects.clear();
    }

    /**
     * @return The blacklisted subjects in an unmodifiable set
     * @see TrackedCalendar#addBlacklistedSubject(String)
     */
    public Set<String> getBlacklistedSubjects() {
        return Collections.unmodifiableSet(blacklistedSubjects);
    }
}
