import Course from "./Course";

export default interface User {
    discord_id: string | undefined;
    calendar_id: string | undefined;
    courses: Course[];
}