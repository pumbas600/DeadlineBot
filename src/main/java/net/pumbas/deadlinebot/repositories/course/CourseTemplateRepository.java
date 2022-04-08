package net.pumbas.deadlinebot.repositories.course;

public interface CourseTemplateRepository
{
    boolean deleteCourseWithIdAndOwnedBy(String courseId, String discordId);
}
