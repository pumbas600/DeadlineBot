import React, {useState} from "react";
import {IconButton, ListItem, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import {SpacedRow} from "./StyledComponents";
import Course from "../models/Course";
import VisibilityIcon from '@mui/icons-material/Visibility';
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff';

interface Props {
    course: Course;
    removeCourse: (course: string) => void;
}

const Course: React.FC<Props> = (props) => {

    const [isPublic, setIsPublic] = useState<boolean>(props.course.is_public);

    return (
        <ListItem disablePadding>
            <SpacedRow>
                <Typography variant="subtitle1">
                    {props.course.name}
                </Typography>
                <SpacedRow>
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
                </SpacedRow>
            </SpacedRow>

        </ListItem>
    );
}

export default Course;