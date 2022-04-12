import React from "react";
import Course from "../models/Course";
import {Box, Card, CardHeader, Checkbox, IconButton, List, ListItem, Typography} from "@mui/material";
import {blue} from "@mui/material/colors";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import {SpacedRow} from "./StyledComponents";
import CloseIcon from "@mui/icons-material/Close";
import LabelledControl from "./LabelledControl";
import TrackedCourse from "./TrackedCourse";
import AddTrackedCourse from "./AddTrackedCourse";

interface Props {
    calendarId: string;
    calendarName?: string;
    courses: Course[]
}

const Calendar: React.FC<Props> = (props) => {
    const [courses, setCourses] = React.useState<Course[]>(sortCourses(props.courses));

    function sortCourses(courses: Course[]): Course[] {
        return courses.sort((courseA, courseB) => courseA.name.localeCompare(courseB.name));
    }

    function removeCourse(courseName: string) {
        setCourses((courses) => courses.filter((c) => c.name !== courseName));
    }

    function addCourse(course: Course): boolean {
        if (courses.filter((c) => c.name === course.name).length !== 0)
            return false;

        setCourses(sortCourses([ ...courses, course ]));
        return true;
    }

    return (
        <Card sx={{
            width: '350px'
        }}>
            <CardHeader
                sx={{
                    paddingY: 1.5,
                    backgroundColor: blue[900],
                    color: 'white',
                }}
                avatar={
                    <CalendarMonthIcon/>
                }
                title={
                    <SpacedRow>
                        <Typography variant="h5">
                            {props.calendarName ?? 'Unnamed Calendar'}
                        </Typography>
                        <IconButton>
                            <CloseIcon sx={{ color: 'white' }} />
                        </IconButton>
                    </SpacedRow>

                }
            />
            <Box sx={{ paddingY: 1, paddingX: 2 }}>
                <LabelledControl label="ID">
                    <Typography variant="subtitle1" sx={{ marginRight: '9px' }}>
                        {props.calendarId}
                    </Typography>
                </LabelledControl>
                <Typography variant="h6">
                    Courses
                </Typography>
                <List>
                    {courses.map((course: Course) => {
                        return (
                            <TrackedCourse key={course.name} course={course} removeCourse={removeCourse}/>
                        );
                    })}
                    <ListItem disablePadding>
                        <AddTrackedCourse addCourse={addCourse} />
                    </ListItem>
                </List>
            </Box>
        </Card>
    );
}

export default Calendar;