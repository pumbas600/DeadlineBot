import React from "react";
import Course from "../models/Course";
import {Box, Card, CardHeader, IconButton, List, ListItem, Typography} from "@mui/material";
import {blue} from "@mui/material/colors";
import CalendarMonthIcon from "@mui/icons-material/CalendarMonth";
import {SpacedRow} from "./StyledComponents";
import CloseIcon from "@mui/icons-material/Close";
import LabelledControl from "./LabelledControl";
import TrackedCourse from "./TrackedCourse";
import AddPersonalCourse from "./AddPersonalCourse";

interface Props {
    calendarId: string;
    calendarName?: string;
    courses: Course[]
}

const Calendar: React.FC<Props> = (props) => {
    var childrenHaveBeenUpdated = false;
    const [courses, setCourses] = React.useState<Course[]>(sortCourses(props.courses));

    function sortCourses(courses: Course[]): Course[] {
        return courses.sort((courseA, courseB) => courseA.name.localeCompare(courseB.name));
    }

    function removeCourse(courseName: string) {
        updateCourses((courses) => courses.filter((c) => c.name !== courseName));
    }

    function setChildrenHaveBeenUpdated() {
        childrenHaveBeenUpdated = true;
    }

    function updateCourses(updateCourses: (current: Course[]) => Course[]) {
        var currentCourses = courses;
        if (childrenHaveBeenUpdated) {
            // Fetch courses from children
        }
        setCourses(updateCourses(currentCourses));
        childrenHaveBeenUpdated = false; // TODO: Check if this automatically occurs during re-rendering of component
    }

    function addCourse(course: Course): boolean {
        if (courses.filter((c) => c.name === course.name).length !== 0)
            return false;

        updateCourses(courses => sortCourses([ ...courses, course ]));
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
                        <AddPersonalCourse addCourse={addCourse} />
                    </ListItem>
                </List>
            </Box>
        </Card>
    );
}

export default Calendar;