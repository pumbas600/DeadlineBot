import React from "react";
import {Box, IconButton, ListItem, Stack, Typography} from "@mui/material";
import CloseIcon from '@mui/icons-material/Close';

interface Props {
    course: string;
    removeCourse: (course: string) => void;
}

const TrackedCourse: React.FC<Props> = (props) => {
    return (
        <ListItem disablePadding>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    width: '100%'
                }}
            >
                <Typography variant="subtitle1">
                    {props.course}
                </Typography>
                <IconButton>
                    <CloseIcon onClick={e => props.removeCourse(props.course)}/>
                </IconButton>
            </Box>

        </ListItem>
    );
}

export default TrackedCourse;