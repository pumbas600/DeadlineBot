import React from "react";
import {
    Box,
    Card,
    CardHeader,
    Checkbox,
    FormControlLabel,
    FormGroup,
    List,
    ListItem,
    Stack,
    Typography
} from "@mui/material";
import TrackedCalendarData from "../data/TrackedCalendarData";
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth';
import TrackedCourse from "./TrackedCourse";
import {blue} from "@mui/material/colors";
import LabelledControl from "./LabelledControl";

interface Props {
    trackedCalendar: TrackedCalendarData;
}

const TrackedCalendar: React.FC<Props> = (props) => {

    const [courses, setCourses] = React.useState<string[]>(props.trackedCalendar.courses);

    function removeCourse(course: string) {
        setCourses((courses) => courses.filter((c) => c !== course));
    }

    return (
        <Card>
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
                    <Typography variant="h5">
                        {props.trackedCalendar.summary}
                    </Typography>
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
                </List>
            </Box>

        </Card>
    );
}

export default TrackedCalendar;