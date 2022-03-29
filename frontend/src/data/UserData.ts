import CalendarData from "./TrackedCalendarData";

export default interface UserData {
    linkedDiscordId: string;
    trackedCalendars: CalendarData[];
}