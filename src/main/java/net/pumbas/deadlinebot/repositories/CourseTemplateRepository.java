package net.pumbas.deadlinebot.repositories;

public interface CourseTemplateRepository
{
    void deleteCourseWithIdAndOwnedBy(String courseId, String discordId);
}
