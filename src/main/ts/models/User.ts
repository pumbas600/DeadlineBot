import Course from "./Course";

export default interface User {
    discord_id?: string;
    calendar_id?: string;
    courses: Course[];
}