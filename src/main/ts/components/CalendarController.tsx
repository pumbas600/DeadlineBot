import {Box, Fab, Tooltip} from "@mui/material";
import Calendar from "./Calendar";
import React from "react";
import User from "../models/User";

interface Props {
    user: User
}

const CalendarController: React.FC<Props> = ({user}) => {
    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'center',
                mt: 2
            }}
        >
            <Box
                sx={{
                    width: '850px',
                    borderRadius: 2,
                    border: (theme) => `1px solid ${theme.palette.primary.light}`,
                    padding: 1
                }}
            >
                <Calendar calendarId={user.calendar_id ?? ''} courses={user.courses}/>
                <Box sx={{display: 'flex', justifyContent: 'flex-end'}}>
                    <Tooltip title="Track another calendar" placement="left" arrow>
                        <Fab color="primary">
                            <svg xmlns="http://www.w3.org/2000/svg" width="26" height="26" fill="currentColor"
                                 className="bi bi-calendar-plus" viewBox="0 0 16 16">
                                <path
                                    d="M8 7a.5.5 0 0 1 .5.5V9H10a.5.5 0 0 1 0 1H8.5v1.5a.5.5 0 0 1-1 0V10H6a.5.5 0 0 1 0-1h1.5V7.5A.5.5 0 0 1 8 7z"/>
                                <path
                                    d="M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z"/>
                            </svg>
                        </Fab>
                    </Tooltip>
                </Box>
            </Box>
        </Box>
    );
}

export default CalendarController;