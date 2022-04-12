import React from "react";
import {
    Box,
    Card,
    CardHeader,
    Checkbox, IconButton,
    List,
    ListItem,
    Typography
} from "@mui/material";
import TrackedCalendarData from "../models/TrackedCalendarData";
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import TrackedCourse from "./TrackedCourse";
import {blue} from "@mui/material/colors";
import LabelledControl from "./LabelledControl";
import AddPersonalCourse from "./AddPersonalCourse";
import CloseIcon from '@mui/icons-material/Close';
import {SpacedRow} from "./StyledComponents";

interface Props {
    trackedCalendar: TrackedCalendarData;
}

const TrackedCalendar: React.FC<Props> = (props) => {

    const [courses, setCourses] = React.useState<string[]>(props.trackedCalendar.courses.sort());

    function removeCourse(course: string) {
        setCourses((courses) => courses.filter((c) => c !== course));
    }

    function addCourse(course: string): boolean {
        if (courses.filter((c) => c === course).length !== 0)
            return false;

        setCourses([ ...courses, course ].sort());
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
                            {props.trackedCalendar.summary}
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
                        {props.trackedCalendar.id}
                    </Typography>
                </LabelledControl>
                <LabelledControl label="Public">
                    <Checkbox
                        checked={props.trackedCalendar.isPublic}
                    />
                </LabelledControl>
                <Typography variant="h6">
                    Tracked Courses
                </Typography>
                <List>
                    {courses.map((course: string) => {
                        return (
                            <TrackedCourse key={course} course={course} removeCourse={removeCourse}/>
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

export default TrackedCalendar;