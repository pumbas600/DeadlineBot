import React, {useState} from "react";
import {SpacedRow} from "./StyledComponents";
import {IconButton, TextField} from "@mui/material";
import AddIcon from '@mui/icons-material/Add';

interface Props {
    addCourse: (course: string) => boolean;
}

interface State {
    course: string;
    error: string;
}

const AddTrackedCourse: React.FC<Props> = (props) => {

    const coursePattern = /([a-zA-Z]+) ?(\w*[\d]+\w*)/g
    const [state, setState] = useState<State>({ course: '', error: '' });

    function handleInput(e: React.ChangeEvent<HTMLTextAreaElement | HTMLInputElement>) {
        setState({ course: e.target.value, error: '' });
    }

    function handleSubmit() {
        if (state.course.length !== 0) {
            const match = coursePattern.exec(state.course);
            if (match) {
                const formattedCourseName = `${match[1].toUpperCase()} ${match[2]}`;
                if (!props.addCourse(formattedCourseName)) {
                    setState({ ...state, error: 'That course is already tracked' });
                }
                else setState({ course: '', error: '' });
            }
            else setState({ ...state, error: 'Should be formatted like: SOFTENG 281' });
        }
    }

    return (
        <SpacedRow sx={{ marginTop: 0.5 }}>
            <TextField
                onChange={handleInput}
                size="small"
                label="Course Name"
                variant="outlined"
                value={state.course}
                error={state.error.length !== 0}
                helperText={state.error}
                sx={{
                    width: '100%'
                }}
            />
            <IconButton
                disabled={state.course.length === 0}
                onClick={e => handleSubmit()}
            >
                <AddIcon />
            </IconButton>
        </SpacedRow>
    );
}

export default AddTrackedCourse;