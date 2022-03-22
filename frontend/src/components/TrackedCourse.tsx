import React from "react";
import {Box, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';

interface Props {
    course: string;
    removeCourse: (course: string) => void;
}

const TrackedCourse: React.FC<Props> = (props) => {
    return (
        <Box>
            <Typography variant="h6">{props.course}</Typography>
            <CloseIcon onClick={e => props.removeCourse(props.course)}/>
        </Box>
    );
}

export default TrackedCourse;