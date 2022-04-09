package net.pumbas.deadlinebot.repositories.user;

public interface UserTemplateRepository
{
    void deletePublicCourseReferences(String discordId, String courseId);

    void deleteAllCourseReferences(String courseId);
}
