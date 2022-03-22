import React from "react";
import {Box, Card, Checkbox, Typography} from "@mui/material";
import TrackedCalendarData from "../data/TrackedCalendarData";
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import TrackedCourse from "./TrackedCourse";

const TrackedCalendar: React.FC<TrackedCalendarData> = (props) => {

    const [courses, setCourses] = React.useState<string[]>(props.courses);

    function removeCourse(course: string) {
        setCourses((courses) => courses.filter((c) => c !== course));
    }

    return (
        <Card>
            <Box>
                <CalendarMonthIcon/>
                <Typography variant="h2">
                    {props.summary}
                </Typography>
            </Box>
            <Box>
                <Typography variant="h4">
                    Public
                </Typography>
                <Checkbox checked={props.isPublic}/>
            </Box>
            <Box>
                {courses.map((course: string) => {
                    return (
                        <TrackedCourse course={course} removeCourse={removeCourse}/>
                    );
                })}
            </Box>
        </Card>
    );
}

export default TrackedCalendar;