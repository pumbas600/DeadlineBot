import React from "react";
import {IconButton, ListItem, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';
import {SpacedRow} from "./StyledComponents";

interface Props {
    course: string;
    removeCourse: (course: string) => void;
}

const TrackedCourse: React.FC<Props> = (props) => {
    return (
        <ListItem disablePadding>
            <SpacedRow>
                <Typography variant="subtitle1">
                    {props.course}
                </Typography>
                <IconButton onClick={e => props.removeCourse(props.course)}>
                    <CloseIcon/>
                </IconButton>
            </SpacedRow>

        </ListItem>
    );
}

export default TrackedCourse;