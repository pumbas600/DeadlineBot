package net.pumbas.deadlinebot.repositories;

public interface CourseTemplateRepository
{
    boolean deleteCourseWithIdAndOwnedBy(String courseId, String discordId);
}
