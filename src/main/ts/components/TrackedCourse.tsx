import React, {useState} from "react";
import {IconButton, ListItem, Stack, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import {SpacedRow} from "./StyledComponents";
import Course from "../models/Course";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';

interface Props {
    course: Course;
    removeCourse: (course: string) => void;
}

const TrackedCourse: React.FC<Props> = (props) => {
    const [isPublic, setIsPublic] = useState(props.course.is_public);

    return (
        <ListItem disablePadding>
            <SpacedRow>
                <Typography variant="subtitle1">
                    {props.course.name}
                </Typography>
                <Stack direction="row">
                    <IconButton onClick={e => props.removeCourse(props.course.name)}>
                        <CloseIcon/>
                    </IconButton>
                    <IconButton onClick={e => setIsPublic(!isPublic)}>
                        {isPublic ? (
                            <VisibilityIcon/>
                        ) : (
                            <VisibilityOffIcon/>
                        )}
                    </IconButton>
                </Stack>
            </SpacedRow>

        </ListItem>
    );

}

export default TrackedCourse;